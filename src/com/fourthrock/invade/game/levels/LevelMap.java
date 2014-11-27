package com.fourthrock.invade.game.levels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fourthrock.invade.game.maps.Map;
import com.fourthrock.invade.game.physics.BoundingBox2D;
import com.fourthrock.invade.game.physics.Position2D;
import com.fourthrock.invade.util.Index2D;

/**
 * Holds all of the levels in one object
 * for the LevelChooserScene.
 * 
 * This has a lot of common code with the game.maps.Map class,
 * but I did not see an opportunity for parametric polymorphism,
 * given Java's type system, so the code is copied.
 * @author Joseph
 *
 */
public class LevelMap {
	private BoundingBox2D bounds;
	private final List<Level> levels;
	private final java.util.Map<Level, Position2D> levelToPosition;

	public LevelMap() {
		this.bounds = BoundingBox2D.ORIGIN_POINT;
		this.levels = new ArrayList<Level>();
		this.levelToPosition = new HashMap<>();
		addAllLevels();
	}

	public BoundingBox2D getBounds() {
		return bounds;
	}

	public List<Level> getLevels() {
		return levels;
	}
	
	public java.util.Map<Level, Position2D> getPositionMapping() {
		return levelToPosition;
	}
	
	public float getMinZoom() {
		return Map.MIN_ZOOM;
	}
	
	public float getMaxZoom() {
		return Map.MAX_ZOOM;
	}
	
	/**
	 * Computes the list of pairs of indices (i, j) such
	 * that the ith Level unlocks the jth Level.
	 */
	public List<Index2D> getUnlockSet() {
		final List<Index2D> unlocks = new ArrayList<>(levels.size());
		for(int i=0; i<levels.size(); i++) {
			for(int j=i+1; j<levels.size(); j++) {
				if(levels.get(i).getLevelID() + 1 == levels.get(j).getLevelID()) {
					unlocks.add(new Index2D(i, j));
				} else {
					break;
				}
			}
		}
		return unlocks;
	}
	
	private void addNewLevel(final Level l, final float x, final float y) {
		final Position2D p = new Position2D(x, y);
		bounds = bounds.expandWith(p);
		levels.add(l);
		levelToPosition.put(l, p);
	}
	
	/*
	 * Make sure to add levels in non-decreasing levelID order.
	 */
	private void addAllLevels() {
		addNewLevel(new LevelOne(), 0f, 0f);
	}
}
