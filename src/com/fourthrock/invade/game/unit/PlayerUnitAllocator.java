package com.fourthrock.invade.game.unit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fourthrock.invade.game.physics.Position2D;
import com.fourthrock.invade.game.player.Player;

/**
 * So as to prevent unnecessary reallocations,
 * we hold onto all allocated PlayerUnits.
 * 
 * If somebody needs a new one, they come through
 * this class.
 * 
 * Hopefully this helps speed up the game.
 * 
 * @author Joseph
 *
 */
public class PlayerUnitAllocator implements Iterable<PlayerUnit> {
	private final Map<PlayerUnit, Integer> unitIndices;
	private final List<PlayerUnit> allUnits;
	private int nUnitsInUse;
	
	public PlayerUnitAllocator() {
		unitIndices = new HashMap<>();
		allUnits = new ArrayList<>();
		nUnitsInUse = 0;
	}
	
	public PlayerUnit allocateUnit(final Player p, final Position2D pos, final float orientation) {
		if(nUnitsInUse == allUnits.size()) { //must allocate a new PlayerUnit
			final PlayerUnit u = new PlayerUnit(p, pos, orientation);
			unitIndices.put(u, nUnitsInUse);
			allUnits.add(u);
			nUnitsInUse++;
			return u;
		} else {
			final PlayerUnit u = allUnits.get(nUnitsInUse);
			u.reset(p, pos, orientation);
			nUnitsInUse++;
			return u;
		}
	}
	
	public void deallocateUnit(final PlayerUnit u) {
		final int unitIndex = unitIndices.get(u).intValue();
		if(nUnitsInUse <= 0) {
			throw new IllegalStateException("No units to deallocate!");
		} else if (unitIndex == nUnitsInUse - 1) {
			nUnitsInUse--;
		} else {
			final int lastUnitInUseIndex = nUnitsInUse - 1;
			final PlayerUnit lastUnitInUse = allUnits.get(lastUnitInUseIndex);
			
			unitIndices.remove(u);
			unitIndices.remove(lastUnitInUse);
			
			allUnits.set(unitIndex, lastUnitInUse);
			allUnits.set(lastUnitInUseIndex, u);
			
			unitIndices.put(lastUnitInUse, unitIndex);
			unitIndices.put(u, lastUnitInUseIndex);
			
			nUnitsInUse--;
		}
	}
	
	public int size() {
		return nUnitsInUse;
	}

	@Override
	public Iterator<PlayerUnit> iterator() {
		return new AllocatorIterator(this);
	}
	
	private static class AllocatorIterator implements Iterator<PlayerUnit> {
		private final PlayerUnitAllocator allocator;
		private int currIndex;
		
		public AllocatorIterator(final PlayerUnitAllocator allocator) {
			this.allocator = allocator;
			this.currIndex = 0;
		}

		@Override
		public boolean hasNext() {
			return currIndex < allocator.size();
		}

		@Override
		public PlayerUnit next() {
			return allocator.allUnits.get(currIndex++);
		}

		@Override
		public void remove() {
			// do not support.
		}
	}
	
}
