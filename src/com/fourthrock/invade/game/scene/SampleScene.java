package com.fourthrock.invade.game.scene;

import static com.fourthrock.invade.draw.DrawEnum.SQUARE;
import static com.fourthrock.invade.draw.DrawEnum.TRIANGLE;

import java.util.ArrayList;
import java.util.List;

import android.util.Pair;

import com.fourthrock.invade.draw.CanvasRenderer;
import com.fourthrock.invade.draw.Color;
import com.fourthrock.invade.draw.ScaleVec;
import com.fourthrock.invade.draw.Screen2D;
import com.fourthrock.invade.game.physics.BoundingBox2D;
import com.fourthrock.invade.game.physics.Position2D;

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
	private static final float MIN_ZOOM = 1.2f;
	private static final float MAX_ZOOM = 2f;
	private float t;
	private final List<Pair<Position2D, Long>> dots;

	public SampleScene() {
		super(MIN_ZOOM, MAX_ZOOM,
				new BoundingBox2D(new Position2D(-0.4f, -0.4f),
								  new Position2D(0.4f, 0.4f)));
		t = 0f;
		dots = new ArrayList<>();
	}

	@Override
	public Scene step(final long dt) {
		t += (float)(dt / 16.7f);
		t %= 360;
		return this;
	}
	
	@Override
	public void handleTap(final Screen2D screenCoords) {
		final Position2D p = getPositionFromScreen(screenCoords);
		if (p != null) {
			dots.add(new Pair<Position2D, Long>(p, 400L));
		}
	}

	@Override
	public void render(final CanvasRenderer renderer) {
		final float x = 0.75f;
		final Position2D left = new Position2D(-x, 0f);
		final Position2D right = new Position2D(x, 0f);
		final Position2D top = new Position2D(0f, x);
		final Position2D bot = new Position2D(0f, -x);
		
		final Color lightBlue = new Color(0f, 0.7f, 0.9f, 1f);
		final Color darkPurple =  new Color(0.3f, 0f, 0.6f, 0.4f);
		final Color lightPurple = new Color(0.6f, 0f, 0.9f, 1f);
		
		renderSqrTri(renderer, left, t, lightBlue, Color.WHITE);
		renderSqrTri(renderer, right, -t*2, darkPurple, lightPurple);
		renderSqrTri(renderer, top, t*4, Color.GREEN, Color.BLUE);
		renderSqrTri(renderer, bot, -t*4, Color.RED, Color.ORANGE);
		
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

	private static void renderSqrTri(final CanvasRenderer r, final Position2D pos, final float angle, final Color sqrC, final Color triC) {
		final ScaleVec s = new ScaleVec(0.1f, 0.1f, 0.1f);
		r.draw(SQUARE,  pos, s, angle, sqrC);
		r.draw(TRIANGLE, pos, s, -angle, triC);
	}
	
	private static ScaleVec scale(final long l) {
		final float rad = l/(2f * 400);
		return new ScaleVec(rad, rad, rad);
	}
	
}
