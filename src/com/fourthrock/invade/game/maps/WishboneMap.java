package com.fourthrock.invade.game.maps;

import com.fourthrock.invade.game.tower.Tower;

public class WishboneMap extends Map {
	public WishboneMap() {
		super();

		final float shift = 4 * Tower.BORDER_RADIUS;
		final Tower left = addNewTower(-shift*1.1f, -shift/6);
		final Tower right = addNewTower(shift, 0f);
		final Tower top = addNewTower((shift*7)/13, shift*2);

		left.setAdjacentTo(right);
		right.setAdjacentTo(top);
	}
}