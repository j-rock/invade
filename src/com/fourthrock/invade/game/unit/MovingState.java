package com.fourthrock.invade.game.unit;

import com.fourthrock.invade.game.physics.Position2D;
import com.fourthrock.invade.game.physics.Vector2D;
import com.fourthrock.invade.game.player.Player;
import com.fourthrock.invade.game.tower.Tower;

/**
 * Represents the state in which a PlayerUnit
 * is neither attacking a Tower nor another PlayerUnit.
 * @author Joseph
 *
 */
public class MovingState extends UnitState {

	@Override
	public Position2D moveTowards(final Position2D startPos, final Position2D target, final float speed, final long dt) {
		if(target == null) {
			return startPos;
		} else {
			final Vector2D dir = target.minus(startPos).unitize();
			final Vector2D translation = dir.scale(speed * dt);
			return startPos.add(translation).asPosition();
		}
	}

	@Override
	public void fireAtTarget(final Player player, final Tower targetTower, final PlayerUnit targetUnit, final long dt) {
		// don't fire, you're only moving this turn.
	}
}