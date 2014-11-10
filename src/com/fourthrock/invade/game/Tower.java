package com.fourthrock.invade.game;

import com.fourthrock.invade.draw.Color;
import com.fourthrock.invade.draw.ScaleVec;
import com.fourthrock.invade.game.physics.ColoredCircle;
import com.fourthrock.invade.game.physics.Position2D;

/**
 * Represents a tower that can be captured by a PlayerUnit.
 * @author Joseph
 *
 */
public class Tower implements ColoredCircle {
	public static final float RADIUS = 1.3f;
	public static final ScaleVec SCALE = new ScaleVec(RADIUS, RADIUS, RADIUS);
	private final Position2D position;
	private Player player;
	
	public Tower(final Position2D position) {
		this.position = position;
	}

	@Override
	public Position2D getPosition() {
		return position;
	}
	
	@Override
	public float getRadius() {
		return RADIUS;
	}

	@Override
	public Color getColor() {
		return player.getColor();
	}
	
	public void setPlayer(final Player p) {
		this.player = p;
	}
}
