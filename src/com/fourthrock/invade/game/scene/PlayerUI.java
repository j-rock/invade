package com.fourthrock.invade.game.scene;

import static com.fourthrock.invade.draw.DrawEnum.BOLT;
import static com.fourthrock.invade.draw.DrawEnum.CIRCLE;
import static com.fourthrock.invade.draw.DrawEnum.SQUARE;
import static com.fourthrock.invade.draw.DrawEnum.TRIANGLE;
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
		
		final float alpha = player.getAttributes().getAchievementPoints() > 0 ? 0f : 0.5f;
		drawAttack(renderer, darken(alpha, Color.PURPLE));
		drawHealth(renderer, darken(alpha, Color.RED));
		drawSpeed(renderer,  darken(alpha, Color.ORANGE));
	}
	
	private void drawAttack(final CanvasRenderer renderer, final Color color) {
		renderer.drawScreen(CIRCLE, attackPosition, new ScaleVec(buttonRadius), color);
		drawNail(renderer, attackPosition, lighten(color));
	}
	
	private void drawNail(final CanvasRenderer renderer, final RenderScreen2D position, final Color color) {
		final float bodyLength = 1.2f * buttonRadius;
		final float bodyWidth = buttonRadius / 6;
		final float headWidth = 1.5f * bodyWidth;
		final float headHeight = buttonRadius / 8;
		
		final RenderScreen2D tipPos = position.add(new RenderScreen2D(0f, 0.002f + bodyLength / 2)).asRenderScreen2D();
		final RenderScreen2D headPos = position.minus(new RenderScreen2D(0f, bodyLength / 2)).asRenderScreen2D();
		
		renderer.drawScreen(SQUARE, position, new ScaleVec(bodyWidth, bodyLength, 1f), color);
		renderer.drawScreen(TRIANGLE, tipPos, new ScaleVec(bodyWidth), 60f, color);
		renderer.drawScreen(CIRCLE, headPos, new ScaleVec(headWidth, headHeight, 1f), color);
	}
	
	private void drawHealth(final CanvasRenderer renderer, final Color color) {
		final float fat = 1.3f * buttonRadius;
		final float skinny = buttonRadius / 3;
		renderer.drawScreen(CIRCLE, healthPosition, new ScaleVec(buttonRadius), color);
		renderer.drawScreen(SQUARE, healthPosition, new ScaleVec(fat, skinny, 1f), lighten(color));
		renderer.drawScreen(SQUARE, healthPosition, new ScaleVec(skinny, fat, 1f), lighten(color));
	}
	
	private void drawSpeed(final CanvasRenderer renderer, final Color color) {
		renderer.drawScreen(CIRCLE, speedPosition, new ScaleVec(buttonRadius), color);
		renderer.drawScreen(BOLT,   speedPosition, new ScaleVec(buttonRadius*0.6f), lighten(color));
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
	
	private static Color lighten(final Color c) {
		return new Color(1f, 1f, 1f, 0.9f).blend(c);
	}
}
