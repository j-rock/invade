package com.fourthrock.invade.game.scene;

import static com.fourthrock.invade.draw.DrawEnum.SQUARE;

import java.util.ArrayList;
import java.util.List;

import com.fourthrock.invade.camera.Picker2D;
import com.fourthrock.invade.camera.TelescopingEye;
import com.fourthrock.invade.draw.CanvasRenderer;
import com.fourthrock.invade.draw.Color;
import com.fourthrock.invade.draw.ScaleVec;
import com.fourthrock.invade.draw.Screen2D;
import com.fourthrock.invade.game.physics.BoundingBox2D;
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
public abstract class WorldEyeScene implements Scene {
	private final TelescopingEye eye;
	private final Picker2D picker;
	private final List<Tap> userTaps;
	

	public WorldEyeScene(final float minZoom, final float maxZoom, final Position2D startEye, final BoundingBox2D cameraBounds) {
		eye = new TelescopingEye(minZoom, maxZoom, startEye, cameraBounds.toCircleBounds());
		
		this.picker = new Picker2D(this);
		this.userTaps = new ArrayList<>();
	}
	
	@Override
	public Scene step(final long dt) {
		eye.step(dt);
		return this;
	}
	
	@Override
	public void handleTap(final Screen2D screenCoords) {
		final Position2D p = getPositionFromScreen(screenCoords);
		if (p != null) {
			userTaps.add(new Tap(p));
		}
	}
	
	@Override
	public void handlePan(final Screen2D start, final Screen2D end) {
		final Vector2D d = getWorldDisplacementFromScreen(start, end).scale(0.85f);
		final Position2D eyeP = eye.getPosition();
		final Position2D p = new Position2D(eyeP.x - d.x, eyeP.y + d.y);
		eye.setPosition(p);
		eye.stopMoving();
	}

	@Override
	public void handleScaling(final float scaleFactor) {
		eye.zoomTo(scaleFactor);
		eye.stopMoving();
	}
	
	@Override
	public void handleFling(final Screen2D start, final Screen2D screenVelocity) {
		// using 'screenVelocity' we move one millisecond in time from 'start'
		// we compute the displacement in world coordinates
		// and implicitly divide this displacement by one millisecond
		// to compute the world velocity.
		
		final Screen2D screenVelocityMillis = screenVelocity.scale(1 / 1000f).asScreen2D();
		final Screen2D oneMillisFromStart = start.add(screenVelocityMillis).asScreen2D();
		final Vector2D worldDisplacement = getWorldDisplacementFromScreen(start, oneMillisFromStart);
		final Vector2D eyeVelocity = new Vector2D(worldDisplacement.x, -worldDisplacement.y);
		eye.setMoving(eyeVelocity.scale(0.85f)); // Community recommends using a fraction of the actual fling velocity.
	}

	@Override
	public float[] getEye() {
		final Position2D p = eye.getPosition();
		return new float[] {p.x, p.y, -3};
	}

	@Override
	public float getZoom() {
		return eye.getZoom();
	}

	@Override
	public void render(final CanvasRenderer renderer) {
		for(int i=userTaps.size()-1; i>=0; i--) {
			final Tap tap = userTaps.get(i);
			if(tap.radius <= 0f) {
				userTaps.remove(i);
			} else {
				tap.radius -= 17f / 400; // magic number that looks pretty.
				renderer.draw(SQUARE, tap.pos, tap.getScale(), Tap.COLOR);
			}
		}
	}
	
	protected Position2D getPositionFromScreen(final Screen2D screenCoords) {
		return picker.convertScreenToWorld(screenCoords);
	}
	
	private Vector2D getWorldDisplacementFromScreen(final Screen2D start, final Screen2D end) {
		final Position2D worldStart = getPositionFromScreen(start);
		final Position2D worldEnd = getPositionFromScreen(end);
		return worldEnd.minus(worldStart);
	}
	
	private static class Tap {
		public static Color COLOR = Color.RED;
		public final Position2D pos;
		public float radius;
		
		public Tap(final Position2D pos) {
			this.pos = pos;
			this.radius = 0.5f;
		}
		
		public ScaleVec getScale() {
			return new ScaleVec(radius);
		}
	}
}
