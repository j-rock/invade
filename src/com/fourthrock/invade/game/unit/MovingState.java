package com.fourthrock.invade.game.unit;

import com.fourthrock.invade.draw.Color;
import com.fourthrock.invade.game.physics.Position2D;
import com.fourthrock.invade.game.physics.Vector2D;
import com.fourthrock.invade.game.tower.Tower;

public class MovingState extends UnitState {

	@Override
	public Color getRenderColor(final Color original) {
		return original;
	}

	@Override
	public Position2D moveTowards(final Position2D startPos, final Position2D target, final float speed, final long dt) {
		final Vector2D dir = target.minus(startPos).unitize();
		final Vector2D translation = dir.scale(speed * dt);
		return startPos.add(translation).toPosition();
	}

	@Override
	public Position2D moveOffTower(final Position2D startPos, final Tower t, final Tower targetTower) {
		return startPos;
	}

	@Override
	public void fireAtTarget(final Tower targetTower, final PlayerUnit targetUnit) {
	}


}