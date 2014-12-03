package com.fourthrock.invade.game.levels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fourthrock.invade.game.maps.KothMap;
import com.fourthrock.invade.game.maps.Map;
import com.fourthrock.invade.game.maps.PentagonStarMap;
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
	private final List<Index2D> unlockPairs;

	public LevelMap() {
		this.bounds = BoundingBox2D.ORIGIN_POINT;
		this.levels = new ArrayList<Level>();
		this.levelToPosition = new HashMap<>();
		addAllLevels();
		this.unlockPairs = buildUnlockPairs();
	}

	public BoundingBox2D getBounds() {
		return bounds;
	}

	public List<Level> getLevels() {
		return levels;
	}
	
	public Position2D getLevelPosition(final int index) {
		return levelToPosition.get(levels.get(index));
	}
	
	public Position2D getPositionFor(final Level l) {
		return levelToPosition.get(l);
	}
	
	public float getMinZoom() {
		return Map.MIN_ZOOM;
	}
	
	public float getMaxZoom() {
		return Map.MAX_ZOOM;
	}
	
	public List<Index2D> getUnlockPairs() {
		return unlockPairs;
	}
	
	/**
	 * Computes the list of pairs of indices (i, j) such
	 * that the ith Level unlocks the jth Level.
	 */
	private List<Index2D> buildUnlockPairs() {
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
	
	private void addNewLevel(final float x, final float y, final Level l) {
		final Position2D p = new Position2D(x, y);
		bounds = bounds.expandWith(p);
		levels.add(l);
		levelToPosition.put(l, p);
	}
	
	/*
	 * Make sure to add levels in non-decreasing levelID order.
	 */
	private void addAllLevels() {
		final float shift = 30f;
		addNewLevel(     0f,       0f, new LevelOne());
		addNewLevel(  shift,       0f, new LevelTwo());
		addNewLevel(2*shift,  shift/2, new LevelThree());
		addNewLevel(2*shift, -shift/2, new Level(new PentagonStarMap(), 4));
		addNewLevel(  shift,   -shift, new Level(new KothMap(), 5));
	}
}
