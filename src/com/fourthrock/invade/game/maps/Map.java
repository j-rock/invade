package com.fourthrock.invade.game.maps;

import java.util.ArrayList;
import java.util.List;

import com.fourthrock.invade.draw.Color;
import com.fourthrock.invade.game.Tower;
import com.fourthrock.invade.game.physics.BoundingBox2D;

/**
 * A class to hold the initial configuration of an Invade game.
 * 
 * 1. Where are the towers located
 * 2. The adjacency of the towers
 * 3. Which color starts in which Tower
 * 
 * @author Joseph
 *
 */
public class Map {
	private final BoundingBox2D bounds;
	private final Color[] colors;
	private final List<Tower> towers;
	
	public Map(final BoundingBox2D bounds, final List<Tower> towers) {
		this.bounds = bounds;
		this.colors = new Color[]{Color.GREEN, Color.ORANGE, Color.PURPLE, Color.RED, Color.BLUE};
		this.towers = towers;
	}

	public BoundingBox2D getBounds() {
		return bounds;
	}

	public List<Tower> getTowers() {
		return towers;
	}

	public List<Color> getColorsNotEqual(final Color u) {
		final List<Color> cs = new ArrayList<>(colors.length);
		for(final Color c : colors) {
			if(!c.equals(u)) {
				cs.add(c);
			}
		}
		return cs;
	}

}
