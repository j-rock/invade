package com.fourthrock.invade.draw;

import com.fourthrock.invade.game.physics.Vector2D;

/**
 * Represents a 2D vector in device screen coordinates.
 * 
 * The greater the y value, the farther down on the screen it goes.
 * 
 * @author Joseph
 *
 */
public class Screen2D extends Vector2D {

	public Screen2D(final float x, final float y) {
		super(x, y);
	}

}
