package com.fourthrock.invade.game.maps;

import com.fourthrock.invade.game.tower.Tower;

public class TwoTowerMap extends Map {
	
	public TwoTowerMap() {
		super();
		
		final float shift = 4 * Tower.BORDER_RADIUS;
		final Tower left = addNewTower(-shift,  0f);
		final Tower right = addNewTower(shift, 0f);
		
		left.setAdjacentTo(right);
	}
}
