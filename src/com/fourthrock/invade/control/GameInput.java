package com.fourthrock.invade.control;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.fourthrock.invade.game.GameState;

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
	
	private class GestureListener implements OnGestureListener {

		@Override
		public boolean onDown(final MotionEvent e) {
			return false;
		}

		@Override
		public boolean onFling(final MotionEvent e1, final MotionEvent e2,
				final float velocityX, final float velocityY) {
			return false;
		}

		@Override
		public void onLongPress(final MotionEvent e) {
		}

		@Override
		public boolean onScroll(final MotionEvent e1, final MotionEvent e2,
				float distanceX, float distanceY) {
			gameState.handlePan(distanceX, distanceY);
			return true;
		}

		@Override
		public void onShowPress(final MotionEvent e) {
		}

		@Override
		public boolean onSingleTapUp(final MotionEvent e) {
			gameState.handleTap(e.getX(), e.getY());
			return true;
		}
		
	}
}