package com.fourthrock.invade.game.physics;

import com.fourthrock.invade.draw.Color;

public class ColoredPoint implements ColoredCircle {
	private final Color color;
	private final Position2D position;
	
	public ColoredPoint(final Color color, final Position2D position) {
		this.color = color;
		this.position = position;
	}

	@Override
	public Position2D getPosition() {
		return position;
	}

	@Override
	public float getCollideRadius() {
		return 1e-5f;
	}

	@Override
	public Color getColor() {
		return color;
	}

}
