package com.fourthrock.invade.game.unit;

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
	
	/*
	 * Move toward a target over time dt (attacking units don't budge!)
	 */
	public abstract Position2D moveTowards(final Position2D startPos, final Position2D target, final float speed, final long dt);
	
	/**
	 * Over time dt, attack the given target.
	 */
	public abstract void fireAtTarget(final Player player, final Tower targetTower, final PlayerUnit targetUnit, final long dt);
}
