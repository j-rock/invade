package com.fourthrock.invade.game.scene;

import static com.fourthrock.invade.draw.DrawEnum.CIRCLE;

import java.util.ArrayList;
import java.util.List;

import com.fourthrock.invade.draw.CanvasRenderer;
import com.fourthrock.invade.draw.Color;
import com.fourthrock.invade.draw.OpenGLRunner;
import com.fourthrock.invade.draw.PixelScreen2D;
import com.fourthrock.invade.draw.RenderScreen2D;
import com.fourthrock.invade.draw.ScaleVec;
import com.fourthrock.invade.game.levels.Level;
import com.fourthrock.invade.game.maps.PentagonStarMap;


/**
 * Represents the pre-game menu where a User
 * can select a Color.
 * 
 * @author Joseph
 *
 */
public class ColorChoiceScene extends FixedEyeScene {
	private final Level levelChoice;
	private final List<ColorPosition> choices;
	private final float choiceRadius;
	private Color choiceColor;
	
	private ColorChoiceScene(final Level levelChoice) {
		this.levelChoice = levelChoice;
		
		choices = new ArrayList<>();

		choices.add(new ColorPosition(Color.GREEN, -2));
		choices.add(new ColorPosition(Color.RED,   -1));
		choices.add(new ColorPosition(Color.BLUE,   0));
		choices.add(new ColorPosition(Color.PURPLE, 1));
		choices.add(new ColorPosition(Color.YELLOW, 2));
		
		choiceRadius = 0.15f;
		choiceColor = null;
	}
	
	public ColorChoiceScene() {
		this(new Level(new PentagonStarMap(), 1));
	}

	@Override
	public Scene step(final long dt) {
		if(choiceColor != null) {
			final Scene game = new GamePlayScene(levelChoice, choiceColor);
			return new FadeToBlackScene(this, game);
		} else {
			return this;
		}
	}

	@Override
	public void handleTap(final PixelScreen2D screenCoords) {
		for(final ColorPosition choice : choices) {
			if(withinSqrDist(choice.getPos(), screenCoords)) {
				choiceColor = choice.color;
				return;
			}
		}
	}

	@Override
	public void render(final CanvasRenderer renderer) {
		// do nothing yet!
	}

	@Override
	public void renderScreen(final CanvasRenderer renderer) {
		for(final ColorPosition choice : choices) {
			renderer.drawScreen(CIRCLE, choice.getPos(), new ScaleVec(choiceRadius), choice.color);
		}
	}
	
	private static class ColorPosition {
		public final Color color;
		public final float shift;
		
		public ColorPosition(final Color color, final float shift) {
			this.color = color;
			this.shift = shift;
		}
		
		public RenderScreen2D getPos() {
			return new RenderScreen2D(shift * 0.4f * OpenGLRunner.SCREEN_RATIO, 0f);
		}
	}

	private boolean withinSqrDist(final RenderScreen2D buttonCenter, final PixelScreen2D tapPos) {
		return buttonCenter.minus(tapPos.asRenderScreen2D()).sqrMagnitude() < choiceRadius * choiceRadius;
	}

}
