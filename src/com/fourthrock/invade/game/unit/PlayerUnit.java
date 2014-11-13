package com.fourthrock.invade.game.unit;

import com.fourthrock.invade.draw.Color;
import com.fourthrock.invade.draw.ScaleVec;
import com.fourthrock.invade.game.physics.ColoredCircle;
import com.fourthrock.invade.game.physics.Position2D;
import com.fourthrock.invade.game.player.Player;
import com.fourthrock.invade.game.tower.Tower;

/**
 * A PlayerUnit represents the little minions
 * a Player has to go and invade Towers.
 * 
 * @author Joseph
 *
 */
public class PlayerUnit implements ColoredCircle {
	public static final float RADIUS = 0.0035f;
	public static ScaleVec SCALE = new ScaleVec(RADIUS, RADIUS, RADIUS);

	private final Player player;
	private Position2D position;
	private float health;
	private UnitState state;
	private PlayerUnit targetUnit;
	
	public PlayerUnit(final Player player, final Position2D position) {
		this.player = player;
		this.position = position;
		this.health = player.getAttributes().getBaseUnitHealth();
		this.state = UnitState.MOVING;
	}

	@Override
	public Position2D getPosition() {
		return position;
	}
	
	@Override
	public Color getColor() {
		return player.getColor();
	}
	
	@Override
	public float getCollideRadius() {
		return player.getAttributes().getUnitAttackRadius();
	}
	
	/**
	 * Gets the color used by GamePlayScene to draw the PlayerUnit
	 */
	public Color getRenderColor() {
		return blendOnHealth(state.getRenderColor(getColor()));
	}

	public boolean alive() {
		return health > 0f;
	}
	
	public float getHealth() {
		return health;
	}
	
	public void takeDamage(final float damage) {
		health = Math.max(0f, health - damage);
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
		
		if(t.equals(player.getTargetTower())) {
			state = UnitState.ATTACKING_TOWER;
		}
	}
	
	/**
	 * Delegates to the current UnitState how
	 * to move towards the current Player's target.
	 */
	public void moveTowardsTarget(final long dt) {
		position = state.moveTowards(position, player.getTarget(), player.getAttributes().getUnitMoveSpeed(), dt);
	}
	
	public void moveOffTower(final Tower t) {
		position = t.positionForAttackingUnit(position);
		
	}

	/**
	 * Might be firing at at Tower or another PlayerUnit.
	 * Delegate to the UnitState how to proceed.
	 */
	public void fireAtTarget(final long dt) {
		state.fireAtTarget(player, player.getTargetTower(), targetUnit, dt);
	}
	
	/**
	 * The lower the health, the darker the PlayerUnit appears.
	 */
	private Color blendOnHealth(final Color renderColor) {
		final float healthRatio = health / player.getAttributes().getBaseUnitHealth();
		final Color healthBlack = new Color(0f, 0f, 0f, 1 - healthRatio);
		return healthBlack.blend(renderColor);
	}
}
