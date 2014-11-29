package com.fourthrock.invade.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * So as to prevent unnecessary reallocations,
 * we hold onto all allocated objects of type T.
 * 
 * If somebody needs a new T, they come through
 * this allocator. If there is a pre-allocated T
 * that is not in use, this allocator passes it along.
 * Otherwise, the allocator uses an Allocateable
 * to construct a new value.
 * 
 * Hopefully this helps speed up the game.
 * 
 * @author Joseph
 *
 */
public class ObjectPool<T> implements Iterable<T> {
	private final Map<T, Integer> indices;
	private final List<T> allObjects;
	private final Allocator<T> tFactory;
	private int numberInUse;
	
	public ObjectPool(final Allocator<T> factory) {
		indices = new HashMap<>();
		allObjects = new ArrayList<>();
		tFactory = factory;
		numberInUse = 0;
	}
	
	public T allocate() {
		if(numberInUse == allObjects.size()) {
			final T t = tFactory.allocate();
			indices.put(t, numberInUse);
			allObjects.add(t);
			numberInUse++;
			return t;
		} else {
			final T t = allObjects.get(numberInUse);
			numberInUse++;
			return t;
		}
	}
	
	public void deallocate(final T t) {
		final int index = indices.get(t).intValue();
		if(numberInUse <= 0) {
			throw new IllegalStateException("No units to deallocate!");
		} else if (index == numberInUse - 1) {
			numberInUse--;
		} else {
			final int lastInUseIndex = numberInUse - 1;
			final T lastInUse = allObjects.get(lastInUseIndex);
			
			indices.remove(t);
			indices.remove(lastInUse);
			
			allObjects.set(index, lastInUse);
			allObjects.set(lastInUseIndex, t);
			
			indices.put(lastInUse, index);
			indices.put(t, lastInUseIndex);
			
			numberInUse--;
		}
	}
	
	public int size() {
		return numberInUse;
	}

	public T get(final int i) {
		return allObjects.get(i);
	}
	
	public void deallocateAll() {
		numberInUse = 0;
	}
	
	public void preAllocate(final int numToCreate) {
		for(int i=0; i<numToCreate; i++) {
			allocate();
		}
		deallocateAll();
	}

	@Override
	public Iterator<T> iterator() {
		return new AllocatorIterator<>(this);
	}
	
	private static class AllocatorIterator<T> implements Iterator<T> {
		private final ObjectPool<T> allocator;
		private int currIndex;
		
		public AllocatorIterator(final ObjectPool<T> allocator) {
			this.allocator = allocator;
			this.currIndex = 0;
		}

		@Override
		public boolean hasNext() {
			return currIndex < allocator.size();
		}

		@Override
		public T next() {
			return allocator.allObjects.get(currIndex++);
		}

		@Override
		public void remove() {
			// do not support.
		}
	}
}