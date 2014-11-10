package com.fourthrock.invade.game.physics;

import com.fourthrock.invade.draw.Color;

/**
 * Something that can be collided in a ColoredCircleCollider.
 * 
 * It must have position, radius, and color.
 * 
 * @author Joseph
 *
 */
public interface ColoredCircle {
	
	public Position2D getPosition();
	
	public float getRadius();
	
	public Color getColor();
	
}
