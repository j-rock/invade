package com.fourthrock.invade.game.scene;

import static com.fourthrock.invade.draw.DrawEnum.SQUARE;

import java.util.ArrayList;
import java.util.List;

import android.util.Pair;

import com.fourthrock.invade.draw.CanvasRenderer;
import com.fourthrock.invade.draw.Color;
import com.fourthrock.invade.draw.ScaleVec;
import com.fourthrock.invade.draw.Screen2D;
import com.fourthrock.invade.game.physics.BoundingBox2D;
import com.fourthrock.invade.game.physics.BoundingCircle2D;
import com.fourthrock.invade.game.physics.Position2D;
import com.fourthrock.invade.game.physics.Vector2D;


/**
 * Scene starter-kit to have zooming (scaling) with pinch gestures
 * and panning with scroll gestures. One can specify minimum
 * and maximum zoom factors, as well as bounds on scrolling.
 * 
 * It also provides Screen -> World coordinate functions with
 * the object picking Picker2D.
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
	private final List<Pair<Position2D, Long>> dots;
	

	public ZoomAndPanScene(final float minZoom, final float maxZoom, final BoundingBox2D cameraBounds) {
		this.minZoom = minZoom;
		this.maxZoom = maxZoom;
		zoom = Math.max(minZoom, Math.min(1f, maxZoom));
		
		this.cameraBounds = cameraBounds.toCircleBounds();
		eye = new Position2D(0f, 0f);
		
		this.picker = new Picker2D(this);
		this.dots = new ArrayList<>();
	}
	
	@Override
	public void handleTap(final Screen2D screenCoords) {
		final Position2D p = getPositionFromScreen(screenCoords);
		if (p != null) {
			dots.add(new Pair<>(p, 400L));
		}
	}
	
	@Override
	public void handlePan(final Screen2D start, final Screen2D end) {
		final Position2D worldStart = getPositionFromScreen(start);
		final Position2D worldEnd = getPositionFromScreen(end);
		final Vector2D d = worldEnd.minus(worldStart);
		final Position2D p = new Position2D(eye.x - d.x, eye.y + d.y);
	
		eye = cameraBounds.getClosestInBounds(p);
	}

	@Override
	public void handleScaling(final float scaleFactor) {
		zoom = Math.max(minZoom, Math.min(maxZoom, zoom * scaleFactor));
	}

	@Override
	public float[] getEye() {
		return new float[] { eye.x, eye.y, -3};
	}

	@Override
	public float getZoom() {
		return zoom;
	}

	@Override
	public void render(final CanvasRenderer renderer) {
		for(int i=dots.size()-1; i>=0; i--) {
			final Pair<Position2D, Long> dot = dots.get(i);
			if(dot.second <= 0L) {
				dots.remove(i);
			} else {
				dots.set(i, new Pair<Position2D, Long>(dot.first, dot.second - 17));
				renderer.draw(SQUARE, dot.first, scale(dot.second), Color.RED);
			}
		}
	}
	
	protected Position2D getPositionFromScreen(final Screen2D screenCoords) {
		return picker.convertScreenToWorld(screenCoords);
	}
	
	private static ScaleVec scale(final long l) {
		final float rad = l/(2f * 400);
		return new ScaleVec(rad, rad, rad);
	}
}
