package com.fourthrock.invade.game.scene;

import static com.fourthrock.invade.draw.DrawEnum.SQUARE;
import static com.fourthrock.invade.draw.OpenGLRunner.SCREEN_RATIO;

import com.fourthrock.invade.draw.CanvasRenderer;
import com.fourthrock.invade.draw.Color;
import com.fourthrock.invade.draw.PixelScreen2D;
import com.fourthrock.invade.draw.RenderScreen2D;
import com.fourthrock.invade.draw.ScaleVec;

/**
 * This Scene transitions from one Scene to another.
 * It does so over a set time period, making the screen
 * darker and darker as the time passes, eventually
 * becoming black, and then clearing up to reveal
 * the ending scene.
 * 
 * @author Joseph
 *
 */
public class FadeToBlackScene extends FixedEyeScene {
	private final Scene fromScene;
	private final Scene toScene;
	private final long timeForFadeMillis;
	private long timePassed;
	
	public FadeToBlackScene(final Scene fromScene, final Scene toScene, final long timeForFadeMillis) {
		this.fromScene = fromScene;
		this.toScene = toScene;
		this.timeForFadeMillis = timeForFadeMillis;
		this.timePassed = 0L;
	}
	
	/**
	 * Default constructor fades for 3.5 seconds from fromScene to toScene.
	 */
	public FadeToBlackScene(final Scene fromScene, final Scene toScene) {
		this(fromScene, toScene, 2000L);
	}

	@Override
	public Scene step(final long dt) {
		if(timePassed < timeForFadeMillis) {
			timePassed += dt;
			return this;
		} else {
			return toScene;
		}
	}

	@Override
	public void handleTap(final PixelScreen2D screenCoords) {
		// do nothing!
	}
	
	@Override
	public float getZoom() {
		return getCurrentSceneInTransition().getZoom();
	}
	
	@Override
	public float[] getEye() {
		return getCurrentSceneInTransition().getEye();
	}

	@Override
	public void render(final CanvasRenderer renderer) {
		getCurrentSceneInTransition().render(renderer);
	}

	@Override
	public void renderScreen(final CanvasRenderer renderer) {
		getCurrentSceneInTransition().renderScreen(renderer);

		final RenderScreen2D centerPos = new RenderScreen2D(0f, 0f);
		final ScaleVec coverScreenScale = new ScaleVec(2*SCREEN_RATIO, 2f, 1f);
		
		final long halfTime = timeForFadeMillis / 2;
		final float alpha;
		if(timePassed < halfTime) {
			alpha = (float)timePassed / halfTime;
		} else {
			alpha = 1f - (float)(timePassed-halfTime) / halfTime;
		}
		final Color black = Color.BLACK.withAlpha(Math.min(1f, alpha));
		renderer.drawScreen(SQUARE, centerPos, coverScreenScale, black);
	}
	
	private Scene getCurrentSceneInTransition() {
		return (2*timePassed < timeForFadeMillis) ? fromScene : toScene;
	}

}
