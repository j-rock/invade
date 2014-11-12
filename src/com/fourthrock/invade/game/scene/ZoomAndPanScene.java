package com.fourthrock.invade.game.scene;

import android.util.Log;

import com.fourthrock.invade.control.GameInput;
import com.fourthrock.invade.game.physics.BoundingBox2D;
import com.fourthrock.invade.game.physics.BoundingCircle2D;
import com.fourthrock.invade.game.physics.Position2D;

/**
 * Scene starter-kit to have zooming (scaling) with pinch gestures
 * and panning with scroll gestures. One can specify minimum
 * and maximum zoom factors, as well as bounds on scrolling.
 * If one leaves zoom bounds or panning bounds out, they
 * are assumed to be unbounded.
 * 
 * @author Joseph
 *
 */
public abstract class ZoomAndPanScene implements Scene {
	private final float minZoom, maxZoom;
	private float zoom;

	private final BoundingCircle2D cameraBounds;
	private float eyeX, eyeY, eyeZ;
	

	public ZoomAndPanScene(final float minZoom, final float maxZoom, final BoundingBox2D cameraBounds) {
		this.minZoom = minZoom;
		this.maxZoom = maxZoom;
		zoom = Math.max(minZoom, Math.min(1f, maxZoom));
		
		this.cameraBounds = cameraBounds.toCircleBounds();
		eyeX = eyeY = 0f;
		eyeZ = -3f;
	}

	public ZoomAndPanScene(final BoundingBox2D cameraBounds) {
		this(1e-13f, Float.MAX_VALUE, cameraBounds);
	}
	
	public ZoomAndPanScene(final float minZoom, final float maxZoom) {
		this(minZoom, maxZoom, BoundingBox2D.UNBOUNDED);
	}
	
	@Override
	public void handlePan(final float dx, final float dy) {
		final Position2D adjustedCam =
				cameraBounds.getClosestInBounds(
						new Position2D(eyeX/zoom - dx * GameInput.SCROLL_SPEED,
									   eyeY/zoom - dy * GameInput.SCROLL_SPEED));
		eyeX = adjustedCam.x;
		eyeY = adjustedCam.y;
	}

	@Override
	public void handleScaling(final float scaleFactor) {
		zoom = Math.max(minZoom, Math.min(maxZoom, zoom * scaleFactor));
	}

	@Override
	public float[] getEye() {
		return new float[] { eyeX, eyeY, eyeZ };
	}

	@Override
	public float getZoom() {
		return 1f;//zoom;
	}
}
