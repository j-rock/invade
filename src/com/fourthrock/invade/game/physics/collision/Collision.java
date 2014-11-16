package com.fourthrock.invade.game.physics.collision;

/**
 * Represents a collision between
 * 	   A Tower and a PlayerUnit
 *         or
 *     A PlayerUnit and another PlayerUnit
 * @author Joseph
 *
 */
public abstract class Collision {
	public static enum Type {
		ATTACK_TOWER, ATTACK_UNIT, MOVE_BACK;
	}
	
	public final Type type;
	
	public Collision(final Type type) {
		this.type = type;
	}
	
	public abstract void process(final long dt);
}
