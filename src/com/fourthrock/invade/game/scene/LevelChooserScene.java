package com.fourthrock.invade.game.scene;

import static com.fourthrock.invade.draw.DrawEnum.SQUARE;

import java.util.ArrayList;
import java.util.List;

import com.fourthrock.invade.draw.CanvasRenderer;
import com.fourthrock.invade.draw.Color;
import com.fourthrock.invade.draw.PixelScreen2D;
import com.fourthrock.invade.draw.ScaleVec;
import com.fourthrock.invade.game.levels.Level;
import com.fourthrock.invade.game.levels.LevelCircle;
import com.fourthrock.invade.game.levels.LevelMap;
import com.fourthrock.invade.game.physics.Position2D;
import com.fourthrock.invade.game.physics.Vector2D;
import com.fourthrock.invade.game.physics.collision.TreeCollider;
import com.fourthrock.invade.game.tower.Tower;
import com.fourthrock.invade.game.unit.PlayerUnit;
import com.fourthrock.invade.util.Index2D;

public class LevelChooserScene extends WorldEyeScene {
	private final LevelMap levelMap;
	private final TreeCollider<LevelCircle> collider;
	private float angleOfTime;
	private Level levelChosen;
	
	private LevelChooserScene(final LevelMap levelMap) {
		super(levelMap.getMinZoom(), levelMap.getMaxZoom(), new Position2D(0f, 0f), levelMap.getBounds());
		this.levelMap = levelMap;
		this.angleOfTime = 0f;
		this.levelChosen = null;
		this.collider = constructCollider();
	}

	public LevelChooserScene() {
		this(new LevelMap());
	}
	
	@Override
	public Scene step(final long dt) {
		super.step(dt);
		angleOfTime = (angleOfTime + dt/16.7f) % 360f;
		
		if(levelChosen != null) {
			final ColorChoiceScene colorScene = new ColorChoiceScene(levelChosen);
			return new FadeToBlackScene(this, colorScene, 1000L);
		} else {
			return this;
		}
	}

	@Override
	public void handleTap(final PixelScreen2D screenCoords) {
		final Position2D tapPos = getPositionFromScreen(screenCoords);
		final LevelCircle tapped = collider.findStaticCollision(tapPos);
		if(tapped != null) {
			levelChosen = tapped.getLevel();
		}
	}
	
	@Override
	public void renderScreen(final CanvasRenderer renderer) {
		// do nothing!
	};
	
	@Override
	public void render(final CanvasRenderer renderer) {
		// TODO - currently all levels are unlocked.
		//        render them transparently if not unlocked.
		
		// Draw unlock lines between Levels
		for (final Index2D ij : levelMap.getUnlockPairs()) {
			final Position2D s = levelMap.getLevelPosition(ij.x);
			final Position2D t = levelMap.getLevelPosition(ij.y);
			drawUnlockLine(renderer, s, t);
		}

		// Draw Levels as towers
		for (final Level l : levelMap.getLevels()) {
			final Position2D lPos = levelMap.getPositionFor(l);
			l.render(renderer, lPos, angleOfTime);
		}
	}

	private void drawUnlockLine(final CanvasRenderer renderer, final Position2D s, final Position2D t) {
		final Vector2D displacement = t.minus(s);
		final Position2D midpoint = s.add(t).scale(0.5f).asPosition();
		final float length = displacement.magnitude() - 2 * Tower.SPAWN_RADIUS;

		final float phase = (float) ((s.x + s.y + t.x + t.y + angleOfTime) * Math.PI / 180f);
		final float height = PlayerUnit.BORDER_RADIUS * (float) (Math.abs(2 * Math.cos(phase)));

		final float alpha = (float) Math.abs(Math.sin(phase));
		final Color color = Color.SNOW.withAlpha(alpha);

		renderer.draw(SQUARE, midpoint, new ScaleVec(length, height, 1f), displacement.theta(), color);
	}
	
	private TreeCollider<LevelCircle> constructCollider() {
		final List<Level> levels = levelMap.getLevels();
		final List<LevelCircle> levelCircles = new ArrayList<>(levels.size());
		
		for(final Level l : levelMap.getLevels()) {
			levelCircles.add(new LevelCircle(l, levelMap.getPositionFor(l)));
		}
		return new TreeCollider<>(levelCircles);
	}
}
