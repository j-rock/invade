package com.fourthrock.invade.game.scene;

import com.fourthrock.invade.draw.PixelScreen2D;

/**
 * Represents a Scene where user gestures such as
 * scrolls, pans, and flings have no effect.
 * @author Joseph
 *
 */
public abstract class FixedEyeScene implements Scene {

	@Override
	public void handlePan(final PixelScreen2D start, final PixelScreen2D end) {
		// do nothing!
	}

	@Override
	public void handleScaling(final float scaleFactor) {
		// do nothing!
	}

	@Override
	public void handleFling(final PixelScreen2D start, final PixelScreen2D velocity) {
		// do nothing
	}

	@Override
	public float[] getEye() {
		return new float[]{0f, 0f, -3f};
	}

	@Override
	public float getZoom() {
		return 1f;
	}

}
