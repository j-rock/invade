package com.fourthrock.invade.game.physics.collision;

/**
 * Represents a collision between
 * 	   A Tower and a PlayerUnit
 *         or
 *     A PlayerUnit and another PlayerUnit
 * @author Joseph
 *
 */
public interface Collision {
	public void process(final long dt);
}
