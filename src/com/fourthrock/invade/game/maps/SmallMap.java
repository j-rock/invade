package com.fourthrock.invade.game.maps;

import com.fourthrock.invade.game.tower.Tower;

/**
 *	Map shaped like this :
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

		final float x = 1f;
		final Tower left = addNewTower(-x, 0);
		final Tower right = addNewTower(x, 0);

		left.setAdjacentTo(right);
	}


}
