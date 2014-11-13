package com.fourthrock.invade.game.scene;

import com.fourthrock.invade.draw.CanvasRenderer;
import com.fourthrock.invade.draw.Screen2D;
import com.fourthrock.invade.game.player.Player;

/**
 * After a Player has won the game,
 * this Scene depicts their victory.
 * 
 * @author Joseph
 *
 */
public class EndGameScene implements Scene {

	public EndGameScene(final Player winner, final boolean wasWinnerHuman) {
		// TODO
	}

	@Override
	public Scene step(final long dt) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void handleTap(final Screen2D screenCoords) {
		// TODO - handle input
	}

	@Override
	public void handlePan(final Screen2D start, final Screen2D end) {
		// do nothing
	}

	@Override
	public void handleScaling(final float scaleFactor) {
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

	@Override
	public void render(final CanvasRenderer renderer) {
		// TODO Auto-generated method stub

	}

}
