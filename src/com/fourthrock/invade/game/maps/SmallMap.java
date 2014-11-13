package com.fourthrock.invade.game.maps;

import com.fourthrock.invade.game.tower.Tower;

/**
 *	Map shaped like this:
 *
 * 		 O -- O
 * 
 *  Supports up to 2 players
 *  
 *  @author Joseph
 */
public class SmallMap extends Map {
	private static final float MIN_ZOOM = 1f;
	private static final float MAX_ZOOM = 6f;
	
	public SmallMap() {
		super(MIN_ZOOM, MAX_ZOOM);

		final Tower left = addNewTower(-1f, 0);
		final Tower right = addNewTower(1f, 0);

		left.setAdjacentTo(right);
	}


}
