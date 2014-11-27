package com.fourthrock.invade.game.unit;

import static com.fourthrock.invade.draw.DrawEnum.CIRCLE;
import static com.fourthrock.invade.draw.DrawEnum.SQUARE;

import com.fourthrock.invade.draw.CanvasRenderer;
import com.fourthrock.invade.draw.Color;
import com.fourthrock.invade.draw.ScaleVec;
import com.fourthrock.invade.game.physics.Position2D;
import com.fourthrock.invade.game.physics.Vector2D;
import com.fourthrock.invade.game.physics.collision.ColoredCircle;
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
	public static final float BORDER_RADIUS = 0.3f;
	public static ScaleVec SCALE = new ScaleVec(BORDER_RADIUS);// * (float)Math.sqrt(2));

	private Player player;
	private Position2D position;
	private float orientation;
	private float health;
	private UnitState state;
	private PlayerUnit targetUnit;
	private Tower targetTower;

	public void reset(final Player player, final Position2D position, final float orientation) {
		this.player = player;
		this.position = position;
		this.orientation = orientation;
		this.health = player.getAttributes().getBaseUnitHealth();
		setMoving();
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
	public float getPhysicalRadius() {
		return BORDER_RADIUS;
	}
	
	@Override
	public float getActiveRadius() {
		return player.getAttributes().getUnitAttackRadius();
	}
	
	/**
	 * Gets the color used by GamePlayScene to draw the PlayerUnit
	 */
	public Color getRenderColor() {
		final float healthRatio = health / player.getAttributes().getBaseUnitHealth();
		final Color healthBlack = Color.BLACK.withAlpha(Math.min(0.6f, 1 - healthRatio));
		return healthBlack.blend(getColor());
	}
	
	public float getOrientation() {
		return orientation;
	}

	public float getHealth() {
		return health;
	}
	
	public float getSpeed() {
		return player.getAttributes().getUnitMoveSpeed();
	}
	
	public PlayerUnit getTargetUnit() {
		return targetUnit;
	}
	
	public Tower getTargetTower() {
		return targetTower;
	}
	
	public UnitState getState() {
		return state;
	}
	
	public boolean alive() {
		return health > 0f;
	}
	
	public void takeDamage(final float damage) {
		health = Math.max(0f, health - damage);
	}
	
	public void setMoving() {
		state = UnitState.MOVING;
		targetUnit = null;
		targetTower = null;
	}
	
	public void setAttackingUnit(final PlayerUnit u) {
		targetUnit = u;
		setOrientation(u.getPosition());
		state = UnitState.ATTACKING_UNIT;
	}
	
	public void setAttackingTower(final Tower t) {
		targetTower = t;
		setOrientation(t.getPosition());
		state = UnitState.ATTACKING_TOWER;
	}
	
	/**
	 * Delegates to the current UnitState how
	 * to move towards the current Player's target.
	 */
	public void moveTowardsTarget(final long dt) {
		final Position2D nextPosition = state.moveTowards(position, player.getTarget(), player.getAttributes().getUnitMoveSpeed(), dt);
		setOrientation(nextPosition);
		moveTo(nextPosition);
	}
	
	/**
	 * Instructs this PlayerUnit to move to target.
	 */
	public void moveTo(final Position2D target) {
		position = target;
	}

	/**
	 * Might be firing at at Tower or another PlayerUnit.
	 * Delegate to the UnitState how to proceed.
	 */
	public void fireAtTarget(final long dt) {
		state.fireAtTarget(player, targetTower, targetUnit, dt);
	}
	
	private void setOrientation(final Position2D target) {
		if(!target.equals(position)) {
			orientation = target.minus(position).theta();
		}
	}

	public void preRender(final CanvasRenderer renderer) {
		if(targetUnit != null || targetTower != null) {
			final Position2D t =
				(targetUnit != null)
					? targetUnit.getPosition()
					: targetTower.getPosition();

			final Vector2D displacement = t.minus(position);
			final float length = displacement.magnitude() - 2f * getPhysicalRadius();
			final float height = 0.07f * PlayerUnit.BORDER_RADIUS;
			final Position2D midpoint = t.add(position).scale(0.5f).asPosition();
		
			renderer.draw(SQUARE, midpoint, new ScaleVec(length, height, 1f), displacement.theta(), getRenderColor());
		}
	}
	
	public void postRender(final CanvasRenderer renderer, final Color blender) {
		final Color blendColor = blender.blend(getRenderColor());
		renderer.draw(CIRCLE, position, PlayerUnit.SCALE, orientation, getRenderColor());
		renderer.draw(CIRCLE, position, PlayerUnit.SCALE.scale(0.7f), orientation, blendColor);
	}
}
