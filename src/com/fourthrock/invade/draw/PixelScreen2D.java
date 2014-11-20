package com.fourthrock.invade.draw;

import static com.fourthrock.invade.draw.OpenGLRunner.SCREEN_HEIGHT;
import static com.fourthrock.invade.draw.OpenGLRunner.SCREEN_RATIO;
import static com.fourthrock.invade.draw.OpenGLRunner.SCREEN_WIDTH;

import com.fourthrock.invade.game.physics.Vector2D;

/**
 * Represents a 2D vector in device screen coordinates.
 * 
 * The greater the y value, the farther down on the screen it goes.
 * 
 * @author Joseph
 *
 */
public class PixelScreen2D extends Vector2D {

	public PixelScreen2D(final float x, final float y) {
		super(x, y);
	}

	public RenderScreen2D asRenderScreen2D() {
		final float rendX = x / SCREEN_WIDTH * 2 * SCREEN_RATIO - SCREEN_RATIO;
		final float rendY = y / SCREEN_HEIGHT * 2 - 1f;
		return new RenderScreen2D(rendX, rendY);
	}

}
