package com.fourthrock.invade.game.physics.collision;

import com.fourthrock.invade.game.unit.PlayerUnit;
import com.fourthrock.invade.game.unit.UnitState;

public class AttackUnitCollision implements Collision {
	public final PlayerUnit attacker;
	public final PlayerUnit victim;
	
	public AttackUnitCollision(final PlayerUnit attacker, final PlayerUnit victim) {
		this.attacker = attacker;
		this.victim = victim;
	}

	@Override
	public void process(final long dt) {
		// choose the first colliding Unit to attack
		// so we can deterministically draw attack graphics.
		if(attacker.getState() != UnitState.ATTACKING_UNIT) {
			attacker.setAttackingUnit(victim);
		}
	}
}
