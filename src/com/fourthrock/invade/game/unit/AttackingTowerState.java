package com.fourthrock.invade.game.unit;

import com.fourthrock.invade.draw.Color;
import com.fourthrock.invade.game.physics.Position2D;
import com.fourthrock.invade.game.tower.Tower;

public class AttackingTowerState extends UnitState {

	@Override
	public Color getRenderColor(final Color original) {
		return original.blend(new Color(0f, 0.4f, 0.4f, 0.2f));
	}

	@Override
	public Position2D moveTowards(final Position2D startPos, final Position2D target, final float speed, final long dt) {
		return startPos;
	}

	@Override
	public Position2D moveOffTower(final Position2D startPos, final Tower t, final Tower targetTower) {
		return (!t.equals(targetTower)) ? startPos : t.positionForAttackingUnit(startPos);
	}

	@Override
	public void fireAtTarget(final Tower targetTower, final PlayerUnit targetUnit) {
	}


}
