package com.fourthrock.invade.game.maps;

import com.fourthrock.invade.game.tower.Tower;

public class SmallMap extends Map {
	
	public SmallMap() {
		super();
		
		final Tower left = addNewTower(-5f,  0f);
		final Tower right = addNewTower(5f, 0f);
		
		left.setAdjacentTo(right);
	}
}
