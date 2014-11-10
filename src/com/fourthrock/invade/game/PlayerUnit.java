package com.fourthrock.invade.game;

import static com.fourthrock.invade.game.PlayerUnit.UnitState.ATTACKING_UNIT;
import static com.fourthrock.invade.game.PlayerUnit.UnitState.MOVING;

import com.fourthrock.invade.draw.Color;
import com.fourthrock.invade.draw.ScaleVec;
import com.fourthrock.invade.game.physics.ColoredCircle;
import com.fourthrock.invade.game.physics.Position2D;
import com.fourthrock.invade.game.physics.Vector2D;

public class PlayerUnit implements ColoredCircle {
	public static final float RADIUS = 0.07f;
	public static ScaleVec SCALE = new ScaleVec(RADIUS, RADIUS, RADIUS);
			
	private final Color color;
	private final UnitAttributes attributes;
	private Position2D position;
	private UnitState state;
	
	public PlayerUnit(final Color color, final Position2D position, final UnitAttributes attributes) {
		this.color = color;
		this.position = position;
		this.attributes = attributes;
		this.state = MOVING;
	}
	
	public void moveTowards(final Position2D target, final Tower targetTower, final long dt) {
		if(state != ATTACKING_UNIT) {
			final float speed = attributes.getSpeed();
			final Vector2D dir = target.minus(position).unitize();
			final Vector2D translation = dir.scale(speed * dt);
			position = position.add(translation).toPosition();
		}
	}
	
	@Override
	public Position2D getPosition() {
		return position;
	}
	
	@Override
	public Color getColor() {
		return color;
	}
	
	@Override
	public float getRadius() {
		return RADIUS;
	}
	
	/**
	 * Returns an altered color based on its state.
	 */
	public Color getRenderColor() {
		switch(state) {
			case ATTACKING_TOWER:
				return color.blend(new Color(0f, 0.4f, 0.4f, 0.2f));
			case ATTACKING_UNIT:
				return color.blend(new Color(1f, 0f, 0f, 0.2f));
			case MOVING:
			default:
				return color;
		}
	}

	public static enum UnitState {
		ATTACKING_TOWER, ATTACKING_UNIT, MOVING;
	}
}
