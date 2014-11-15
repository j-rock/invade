package com.fourthrock.invade.game.physics.collision;

import com.fourthrock.invade.draw.Color;
import com.fourthrock.invade.game.physics.Position2D;

/**
 * Something that can be collided in a ColoredCircleCollider.
 * 
 * It must have position, radii, and color.
 * 
 * @author Joseph
 *
 */
public interface ColoredCircle {
	
	/**
	 * Get the center position of the circle in world space.
	 */
	public Position2D getPosition();
	
	/**
	 * Get the radius at which objects need to be separated.
	 * Circles of the same color will trigger collisions at
	 * the physical radius.
	 */
	public float getPhysicalRadius();
	
	/**
	 * Get the radius at which to perform an action.
	 * Circles only of different colors will trigger collisions
	 * at the active radius.
	 */
	public float getActiveRadius();
	
	/**
	 * Get the color of the circle.
	 */
	public Color getColor();
	
}
