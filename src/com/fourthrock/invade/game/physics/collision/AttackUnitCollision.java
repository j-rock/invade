package com.fourthrock.invade.game.physics.collision;

import com.fourthrock.invade.game.unit.PlayerUnit;

public class AttackUnitCollision extends Collision {
	public final PlayerUnit attacker;
	public final PlayerUnit victim;
	
	public AttackUnitCollision(final PlayerUnit attacker, final PlayerUnit victim) {
		super(Collision.Type.ATTACK_UNIT);
		this.attacker = attacker;
		this.victim = victim;
	}
}
