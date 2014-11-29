package com.fourthrock.invade.game.physics.collision;

import com.fourthrock.invade.draw.Color;
import com.fourthrock.invade.game.physics.Position2D;

public class ColoredPoint implements ColoredCircle {
	private static final float EPSILON = 1e-12f;
	
	private final Position2D position;
	
	public ColoredPoint(final Position2D position) {
		this.position = position;
	}

	@Override
	public Position2D getPosition() {
		return position;
	}

	@Override
	public float getPhysicalRadius() {
		return EPSILON;
	}

	@Override
	public float getActiveRadius() {
		return EPSILON;
	}

	@Override
	public Color getColor() {
		return null;
	}

}
