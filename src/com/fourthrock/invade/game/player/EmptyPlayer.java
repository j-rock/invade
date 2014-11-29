package com.fourthrock.invade.game.player;

import com.fourthrock.invade.draw.Color;
import com.fourthrock.invade.game.unit.PlayerUnit;
import com.fourthrock.invade.util.ObjectPool;

public class EmptyPlayer extends Player {

	public EmptyPlayer() {
		super(Color.SNOW);
	}

	@Override
	public void decideTarget() {
		// do nothing
	}

	@Override
	public void spendAchievementPoints() {
		// do nothing
	}
	
	@Override
	public void tryGenerateUnit(final ObjectPool<PlayerUnit> allUnits, final long dt) {
		// do nothing
	}

}
