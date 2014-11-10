package com.fourthrock.invade.game.scene;

import com.fourthrock.invade.draw.Color;
import com.fourthrock.invade.draw.CanvasRenderer;
import com.fourthrock.invade.game.physics.BoundingBox2D;
import com.fourthrock.invade.game.physics.Position2D;

import static com.fourthrock.invade.draw.DrawEnum.*;

/**
 * Simple class to demonstrate how easy it is
 * to subclass ZoomAndPanScene and get it going.
 * 
 * Shows two sets of rotating squares and triangles
 * with different colors and rotational velocities;
 * @author Joseph
 *
 */
public class SampleScene extends ZoomAndPanScene {
	private static final float MIN_ZOOM = 0.000001f;
	private static final float MAX_ZOOM = 100f;
	private float t;

	public SampleScene() {
		super(MIN_ZOOM, MAX_ZOOM,
				new BoundingBox2D(new Position2D(-20f, -20f),
								  new Position2D(20f, 20f)));
		t = 0f;
	}

	@Override
	public Scene step(final long dt) {
		t += (float)(dt / 16.7f);
		t %= 360;
		return this;
	}
	
	@Override
	public void handleTap(final float x, final float y) {
		// do nothing!
	}

	@Override
	public void render(CanvasRenderer renderer) {
		final float x = 0.75f;
		final Position2D left = new Position2D(-x, 0f);
		final Position2D right = new Position2D(x, 0f);
		
		final Color lightBlue = new Color(0f, 0.7f, 0.9f, 1f);
		final Color darkPurple =  new Color(0.3f, 0f, 0.6f, 0.4f);
		final Color lightPurple = new Color(0.6f, 0f, 0.9f, 1f);
		
		renderer.draw(SQUARE, left, t, lightBlue);
		renderer.draw(TRIANGLE, left, -t, Color.WHITE);
		
		renderer.draw(SQUARE, right, -t*2, darkPurple);
		renderer.draw(TRIANGLE, right, t*2, lightPurple);
	}

}
