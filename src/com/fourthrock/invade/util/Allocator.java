package com.fourthrock.invade.util;

/**
 * An interface to encapsulate calling a constructor on an object.
 * 
 * We do this so Allocator<T> can be generic.
 * 
 * @author Joseph
 */
public interface Allocator<T> {
	
	/**
	 * Returns a newly constructed object of type T.
	 */
	public T allocate();
}
