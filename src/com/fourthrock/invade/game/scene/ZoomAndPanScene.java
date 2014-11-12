package com.fourthrock.invade.game.scene;

import com.fourthrock.invade.draw.Screen2D;
import com.fourthrock.invade.game.physics.BoundingBox2D;
import com.fourthrock.invade.game.physics.BoundingCircle2D;
import com.fourthrock.invade.game.physics.Position2D;
import com.fourthrock.invade.game.physics.Vector2D;

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
	private Position2D eye;
	
	private final Picker2D picker;
	

	public ZoomAndPanScene(final float minZoom, final float maxZoom, final BoundingBox2D cameraBounds) {
		this.minZoom = minZoom;
		this.maxZoom = maxZoom;
		zoom = Math.max(minZoom, Math.min(1f, maxZoom));
		
		this.cameraBounds = cameraBounds.toCircleBounds();
		eye = new Position2D(0f, 0f);
		
		this.picker = new Picker2D(this);
	}

	public ZoomAndPanScene(final BoundingBox2D cameraBounds) {
		this(1e-13f, Float.MAX_VALUE, cameraBounds);
	}
	
	public ZoomAndPanScene(final float minZoom, final float maxZoom) {
		this(minZoom, maxZoom, BoundingBox2D.UNBOUNDED);
	}
	
	@Override
	public void handlePan(final Screen2D start, final Screen2D end) {
		final Vector2D d = getPositionFromScreen(end).minus(getPositionFromScreen(start));
		final Position2D p = new Position2D(eye.x - d.x, eye.y + d.y);
		eye = cameraBounds.getClosestInBounds(p);
	}

	@Override
	public void handleScaling(final float scaleFactor) {
		zoom = Math.max(minZoom, Math.min(maxZoom, zoom * scaleFactor));
	}

	@Override
	public float[] getEye() {
		return new float[] { eye.x, eye.y, -3 };
	}

	@Override
	public float getZoom() {
		return zoom;
	}
	
	protected Position2D getPositionFromScreen(final Screen2D screenCoords) {
		return picker.convertScreenToWorld(screenCoords);
	}
}
