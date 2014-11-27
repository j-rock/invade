package com.fourthrock.invade.game.scene;

import java.util.List;

import com.fourthrock.invade.draw.CanvasRenderer;
import com.fourthrock.invade.game.levels.Level;
import com.fourthrock.invade.game.levels.LevelMap;
import com.fourthrock.invade.game.physics.Position2D;
import com.fourthrock.invade.util.Index2D;

public class LevelChooserScene extends WorldEyeScene {
	private final List<Level> levels;
	private final java.util.Map<Level, Position2D> levelToPosition;
	private final List<Index2D> unlockSet;
	
	private LevelChooserScene(final LevelMap levelMap) {
		super(levelMap.getMinZoom(), levelMap.getMaxZoom(), new Position2D(0f, 0f), levelMap.getBounds());
		this.levels = levelMap.getLevels();
		this.levelToPosition = levelMap.getPositionMapping();
		this.unlockSet = levelMap.getUnlockSet();
	}
	
	public LevelChooserScene() {
		this(new LevelMap());
	}

	@Override
	public void renderScreen(final CanvasRenderer renderer) {
		// TODO - remove this.
		levels.get(0);
		unlockSet.get(0);
		levelToPosition.put(null, null);
	}
}
