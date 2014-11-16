package com.fourthrock.invade.game.physics.collision;

import com.fourthrock.invade.game.tower.Tower;
import com.fourthrock.invade.game.unit.PlayerUnit;

public class AttackTowerCollision extends Collision {
	public final PlayerUnit attacker;
	public final Tower victim;
	
	public AttackTowerCollision(final PlayerUnit attacker, final Tower victim) {
		super(Collision.Type.ATTACK_TOWER);
		this.attacker = attacker;
		this.victim = victim;
	}

	@Override
	public void process(final long dt) {
		attacker.setAttackingTower(victim);
	}
}
