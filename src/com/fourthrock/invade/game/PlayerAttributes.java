package com.fourthrock.invade.game;

public class PlayerAttributes {
	private final UnitAttributes unitAttrs;
	private final TowerAttributes towerAttrs;
	
	private int level;
	private int progress;
	
	public PlayerAttributes() {
		unitAttrs = new UnitAttributes();
		towerAttrs = new TowerAttributes();
		level = 1;
		progress = 0;
	}
	
	public UnitAttributes getUnitAttributes() {
		return new UnitAttributes(unitAttrs);
	}
	
	public TowerAttributes getTowerAttributes() {
		return new TowerAttributes(towerAttrs);
	}
	
	/**
	 * Registers a kill for the progress meter.
	 * Returns whether or not a new level was reached.
	 */
	public boolean registerKill() {
		return registerPoints(5);
	}
	
	/**
	 * Registers a death for the progress meter.
	 * Returns whether or not a new level was reached.
	 */
	public boolean registerDeath() {
		return registerPoints(1);
	}
	
	/**
	 * Registers a tower capture for the progress meter.
	 * Returns whether or not a new level was reached.
	 */
	public boolean registerTowerCapture() {
		return registerPoints(15);
	}
	
	private boolean registerPoints(final int points) {
		progress += points;
		final int nextLevelPoints = minPointsForLevel(level+1);
		return progress >= nextLevelPoints;
	}
	
	private static int minPointsForLevel(final int lvl) {
		// O(n^2 / logn) function for level growth.
		return 100 + (int)((lvl * lvl) / Math.log(lvl));
	}

	public int getMaxUnitCount() {
		return 10;
	}

	public long getUnitCreationWaitTime() {
		return 300 * 17; // wait 300 frames, 17 millis per frame
	}
}
