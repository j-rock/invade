package com.fourthrock.invade.control;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.fourthrock.invade.draw.Screen2D;
import com.fourthrock.invade.game.GameState;
import com.fourthrock.invade.game.physics.Vector2D;

/**
 * GameInput allows classification of taps, pans, and scales.
 * Delegates to GameState when these events occur.
 * 
 * @author Joseph
 *
 */
public class GameInput {
	public static final float SCROLL_SPEED = 1/800f; // determined experimentally

    private final GameState gameState;
    private final ScaleGestureDetector scaleDetector;
    private final GestureDetectorCompat gestureDetector;

	public GameInput(final Context context, final GameState gameState) {
		this.gameState = gameState;
		
		scaleDetector = new ScaleGestureDetector(context, new ScaleListener());
		gestureDetector = new GestureDetectorCompat(context, new GestureListener());
	}

	public void onTouchEvent(final MotionEvent e) {
		scaleDetector.onTouchEvent(e);
		gestureDetector.onTouchEvent(e);
	}

	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
		
		@Override
		public boolean onScale(final ScaleGestureDetector detector) {
			gameState.handleScaling(detector.getScaleFactor());
			return true;
		}
	}
	
	private class GestureListener extends GestureDetector.SimpleOnGestureListener {
		@Override
		public boolean onScroll(final MotionEvent e1, final MotionEvent e2, final float dx, final float dy) {
			final Screen2D end = new Screen2D(e2.getX(),  e2.getY());
			final Screen2D start = end.minus(new Vector2D(dx, dy)).asScreen2D();
			gameState.handlePan(start, end);
			return true;
		}

		@Override
		public boolean onSingleTapUp(final MotionEvent e) {
			gameState.handleTap(e.getX(), e.getY());
			return true;
		}
		
		@Override
		public boolean onFling(final MotionEvent e1, final MotionEvent e2, final float velocityX, final float velocityY) {
			final Screen2D start = new Screen2D(e2.getX(), e2.getY());
			final Screen2D velocity = new Screen2D(velocityX, velocityY);
			gameState.handleFling(start, velocity);
			return true;
		}
		
	}
}