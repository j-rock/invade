package com.fourthrock.invade.game.physics;

import com.fourthrock.invade.game.unit.PlayerUnit;

/**
 * A simple object to hold the PlayerUnit's
 * involved in a collision.
 * 
 * @author Joseph
 *
 */
public class UnitCollision {
	public final PlayerUnit u;
	public final PlayerUnit u2;
	
	public UnitCollision(final PlayerUnit u, final PlayerUnit u2) {
		this.u = u;
		this.u2 = u2;
	}
}
