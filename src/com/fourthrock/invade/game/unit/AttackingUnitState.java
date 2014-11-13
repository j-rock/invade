package com.fourthrock.invade.game.unit;

import com.fourthrock.invade.draw.Color;
import com.fourthrock.invade.game.physics.Position2D;
import com.fourthrock.invade.game.player.Player;
import com.fourthrock.invade.game.tower.Tower;

/**
 * The PlayerUnit must have collided with an enemy PlayerUnit.
 * THEY MUST FIGHT!
 * 
 * @author Joseph
 *
 */
public class AttackingUnitState extends UnitState {

	@Override
	public Color getRenderColor(final Color original) {
		return original.blend(new Color(1f, 0f, 0f, 0.3f));
	}

	@Override
	public Position2D moveTowards(final Position2D startPos, final Position2D target, final float speed, final long dt) {
		// attackers don't move.
		return startPos;
	}

	@Override
	public void fireAtTarget(final Player player, final Tower targetTower, final PlayerUnit targetUnit, final long dt) {
		// do not attack the targetTower, we're attacking the PlayerUnit this round.
		
		if(!targetUnit.alive()) {
			// can't kill what's already dead... UNLESS ZOMBIES!!!
			return;
		}
		
		final float attackPower = player.getAttributes().getUnitAttackSpeed() * dt;
		targetUnit.takeDamage(attackPower);
		
		if(!targetUnit.alive()) {
			player.getAttributes().registerKill();
		}
	}

}