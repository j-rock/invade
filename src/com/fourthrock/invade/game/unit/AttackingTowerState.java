package com.fourthrock.invade.game.unit;

import com.fourthrock.invade.draw.Color;
import com.fourthrock.invade.game.physics.Position2D;
import com.fourthrock.invade.game.player.Player;
import com.fourthrock.invade.game.tower.Tower;

/**
 * The PlayerUnit must have collided with an enemy Tower.
 * Gotta destroy that edifice.
 * 
 * @author Joseph
 *
 */
public class AttackingTowerState extends UnitState {

	@Override
	public Color getRenderColor(final Color original) {
		return original.blend(new Color(0f, 0.4f, 0.4f, 0.5f));
	}

	@Override
	public Position2D moveTowards(final Position2D startPos, final Position2D target, final float speed, final long dt) {
		return UnitState.MOVING.moveTowards(startPos, target, speed*0.5f, dt);
	}

	@Override
	public void fireAtTarget(final Player player, final Tower targetTower, final PlayerUnit targetUnit, final long dt) {
		// do not attack the unit, we're attacking the Tower this round.
		
		if(targetTower == null) { // previous unit must have killed it.
			return;
		}

		final float attackPower = player.getAttributes().getUnitAttackSpeed() * dt;
		targetTower.takeDamage(attackPower);
			
		if(targetTower.getHealth() <= 0f && targetTower.adjacentTo(player.getTowers())) {
			player.getAttributes().registerTowerCapture();
			player.updateTarget(null);
			targetTower.resetHealth();
			targetTower.adoptNewPlayer(player);
		}
	}

}
