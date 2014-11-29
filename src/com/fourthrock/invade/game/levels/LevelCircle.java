package com.fourthrock.invade.game.levels;

import com.fourthrock.invade.draw.Color;
import com.fourthrock.invade.game.physics.Position2D;
import com.fourthrock.invade.game.physics.collision.ColoredCircle;
import com.fourthrock.invade.game.tower.Tower;

public class LevelCircle implements ColoredCircle {
	private final Level level;
	private final Position2D position;
	
	public LevelCircle(final Level l, final Position2D p) {
		this.level = l;
		this.position = p;
	}

	@Override
	public Position2D getPosition() {
		return position;
	}

	@Override
	public float getPhysicalRadius() {
		return Tower.SPAWN_RADIUS;
	}

	@Override
	public float getActiveRadius() {
		return getPhysicalRadius();
	}

	@Override
	public Color getColor() {
		return Color.SLATE;
	}
	
	public Level getLevel() {
		return level;
	}

}
