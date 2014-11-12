package com.fourthrock.invade.game.unit;

import com.fourthrock.invade.draw.Color;
import com.fourthrock.invade.game.physics.Position2D;
import com.fourthrock.invade.game.player.Player;
import com.fourthrock.invade.game.tower.Tower;

/**
 * A stateless class to determine behaviors for a PlayerUnit.
 * @author Joseph
 *
 */
public abstract class UnitState {
	public static final UnitState MOVING = new MovingState();
	public static final UnitState ATTACKING_UNIT = new AttackingUnitState();
	public static final UnitState ATTACKING_TOWER = new AttackingTowerState();
	
	/**
	 * What color should the PlayerUnit be? (Attacking units should look angry)
	 */
	public abstract Color getRenderColor(final Color original);
	
	/*
	 * Move toward a target over time dt (attacking units don't budge!)
	 */
	public abstract Position2D moveTowards(final Position2D startPos, final Position2D target, final float speed, final long dt);
	
	/**
	 * Units attacking a tower can't get too close to it. If you've
	 * collided with Tower t and its your targetTower, what do you do?!
	 */
	public abstract Position2D moveOffTower(final Position2D startPos, final Tower t, final Tower targetTower);

	/**
	 * Over time dt, attack the given target.
	 */
	public abstract void fireAtTarget(final Player player, final Tower targetTower, final PlayerUnit targetUnit, final long dt);
}
