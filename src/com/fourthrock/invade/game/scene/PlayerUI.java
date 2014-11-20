package com.fourthrock.invade.game.scene;

import static com.fourthrock.invade.draw.DrawEnum.CIRCLE;
import static com.fourthrock.invade.draw.DrawEnum.SQUARE;
import static com.fourthrock.invade.draw.OpenGLRunner.SCREEN_RATIO;

import com.fourthrock.invade.draw.CanvasRenderer;
import com.fourthrock.invade.draw.Color;
import com.fourthrock.invade.draw.PixelScreen2D;
import com.fourthrock.invade.draw.RenderScreen2D;
import com.fourthrock.invade.draw.ScaleVec;
import com.fourthrock.invade.game.player.Player;

/**
 * Encapsulates the logic for rendering Player stats
 * as well as processing taps to update attributes.
 * 
 * @author Joseph
 *
 */
public class PlayerUI {
	private final Player player;
	private final float buttonRadius;

	private RenderScreen2D attackPosition;
	private RenderScreen2D healthPosition;
	private RenderScreen2D speedPosition;
	private RenderScreen2D progressBarPosition;

	public PlayerUI(final Player player) {
		this.player = player;
		this.buttonRadius = 0.15f;
	}
	
	/**
	 * If the user tapped on a button on the screen,
	 * handle the logic required and return true.
	 * Otherwise, the user was not interacting with the UI,
	 * return false.
	 */
	public boolean handleTap(final PixelScreen2D tapPos) {
		if(withinSqrDist(attackPosition, tapPos)) {
			player.getAttributes().improveAttack();
		} else if(withinSqrDist(healthPosition, tapPos)) {
			player.getAttributes().improveHealth();
		} else if(withinSqrDist(speedPosition, tapPos)) {
			player.getAttributes().improveSpeed();
		} else {
			return false;
		}
		return true;
	}
	
	/**
	 * Draw the user interface with the renderer.
	 */
	public void render(final CanvasRenderer renderer) {
		final float progress = player.getAttributes().getProgressRatio();
		calculatePositions(progress);
		
		final ScaleVec progressScale = new ScaleVec(progress*2*SCREEN_RATIO, 0.03f, 1f);
		renderer.drawScreen(SQUARE, progressBarPosition, progressScale, player.getColor());
		
		final ScaleVec s = new ScaleVec(buttonRadius);
		final float alpha = player.getAttributes().getAchievementPoints() > 0 ? 0f : 0.5f;
		renderer.drawScreen(CIRCLE, attackPosition, s, darken(alpha, Color.RED));
		renderer.drawScreen(CIRCLE, healthPosition, s, darken(alpha, Color.GREEN));
		renderer.drawScreen(CIRCLE,  speedPosition, s, darken(alpha, Color.BLUE));
	}

	private boolean withinSqrDist(final RenderScreen2D buttonCenter, final PixelScreen2D tapPos) {
		return buttonCenter.minus(tapPos.asRenderScreen2D()).sqrMagnitude() < buttonRadius * buttonRadius;
	}

	private void calculatePositions(final float progress) {
		final float centerX = 0f;
		final float shift = SCREEN_RATIO * 0.8f;
		final float liftedY = 0.8f;
		
		this.progressBarPosition = new RenderScreen2D(progress*SCREEN_RATIO - SCREEN_RATIO, -0.95f);
		this.attackPosition = new RenderScreen2D(centerX - shift, liftedY);
		this.healthPosition = new RenderScreen2D(centerX        , liftedY);
		this.speedPosition  = new RenderScreen2D(centerX + shift, liftedY);
	}
	
	private static Color darken(final float alpha, final Color c) {
		return new Color(0f, 0f, 0f, alpha).blend(c);
	}
}
