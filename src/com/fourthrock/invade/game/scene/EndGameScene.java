package com.fourthrock.invade.game.scene;

import static com.fourthrock.invade.draw.DrawEnum.TRIANGLE;

import com.fourthrock.invade.draw.CanvasRenderer;
import com.fourthrock.invade.draw.Color;
import com.fourthrock.invade.draw.PixelScreen2D;
import com.fourthrock.invade.draw.RenderScreen2D;
import com.fourthrock.invade.draw.ScaleVec;

/**
 * After a Player has won the game,
 * this Scene depicts their victory.
 * 
 * @author Joseph
 *
 */
public class EndGameScene extends FixedEyeScene {
	private final Color winnerColor;
	private final Color humanColor;
	private boolean playAgain;

	public EndGameScene(final Color winnerColor, final Color humanColor) {
		this.winnerColor = winnerColor;
		this.humanColor = humanColor;
		this.playAgain = false;
	}

	@Override
	public Scene step(final long dt) {
		if(playAgain) {
			return new FadeToBlackScene(this, new MenuScene());
		} else {
			return this;
		}
	}

	@Override
	public void handleTap(final PixelScreen2D screenCoords) {
		playAgain = true;
	}

	@Override
	public void render(final CanvasRenderer renderer) {
		// no 3D drawing yet. do nothing.
	}

	@Override
	public void renderScreen(final CanvasRenderer renderer) {
		final RenderScreen2D p = new RenderScreen2D(0f, 0f);
		final ScaleVec s = new ScaleVec(0.1f);
		renderer.drawScreen(TRIANGLE, p, s, winnerColor);
	}


}
