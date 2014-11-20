package com.fourthrock.invade.draw;

import com.fourthrock.invade.game.physics.Vector2D;

/**
 * Represents a Vector2D in Screen rendering coordinates
 * 
 * X varies from -ratio to ratio where ratio = screen width / screen height
 * Y varies from -1 to 1
 * 
 * @author Joseph
 *
 */
public class RenderScreen2D extends Vector2D {

	public RenderScreen2D(final float x, final float y) {
		super(x, y);
	}
}
