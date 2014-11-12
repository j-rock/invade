package com.fourthrock.invade.game.unit;

import com.fourthrock.invade.draw.Color;
import com.fourthrock.invade.draw.ScaleVec;
import com.fourthrock.invade.game.physics.ColoredCircle;
import com.fourthrock.invade.game.physics.Position2D;
import com.fourthrock.invade.game.player.PlayerAttributes;
import com.fourthrock.invade.game.tower.Tower;

public class PlayerUnit implements ColoredCircle {
	public static final float RADIUS = 0.07f;
	public static ScaleVec SCALE = new ScaleVec(RADIUS, RADIUS, RADIUS);
			
	private final Color color;
	private final PlayerAttributes attributes;
	private Position2D position;
	private UnitState state;
	private Tower targetTower;
	private PlayerUnit targetUnit;
	
	public PlayerUnit(final Color color, final Position2D position, final PlayerAttributes attributes) {
		this.color = color;
		this.position = position;
		this.attributes = attributes;
		this.state = UnitState.MOVING;
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
	
	public Color getRenderColor() {
		return state.getRenderColor(color);
	}
	
	public void setMoving() {
		state = UnitState.MOVING;
		targetUnit = null;
	}
	
	public void setAttackingUnit(final PlayerUnit u) {
		this.targetUnit = u;
		state = UnitState.ATTACKING_UNIT;
	}
	
	public void setAttackingTower(final Tower t) {
		// Only attack Tower t
		// if commanded to attack it
		
		if(t.equals(targetTower)) {
			state = UnitState.ATTACKING_TOWER;
		}
	}
	
	public void moveTowards(final Position2D target, final Tower targetTower, final long dt) {
		this.targetTower = targetTower;
		position = state.moveTowards(position, target, attributes.getUnitMoveSpeed(), dt);
	}
	
	public void moveOffTower(final Tower t) {
		position = state.moveOffTower(position, t, targetTower);
	}

	/**
	 * Returns whether or not the 
	 */
	public void fireAtTarget() {
		state.fireAtTarget(targetTower, targetUnit);
	}


}
