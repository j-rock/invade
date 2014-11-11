package com.fourthrock.invade.game.physics;

import com.fourthrock.invade.game.tower.Tower;
import com.fourthrock.invade.game.unit.PlayerUnit;

public class TowerCollision {
	public final Tower t;
	public final PlayerUnit u;
	
	public TowerCollision(final Tower t, final PlayerUnit u) {
		this.t = t;
		this.u = u;
	}
}
