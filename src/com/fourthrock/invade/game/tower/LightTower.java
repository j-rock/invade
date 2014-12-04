package com.fourthrock.invade.game.tower;

import com.fourthrock.invade.game.physics.Position2D;

public class LightTower extends Tower {
	private static final float SIZE_INC = 0.7f;

	public LightTower(final Position2D position) {
		super(position);
		health *= SIZE_INC;
	}

	@Override
	public float getPhysicalRadius() {
		return SIZE_INC * super.getPhysicalRadius();
	}
	
	@Override
	public float getActiveRadius() {
		return SIZE_INC * super.getActiveRadius();
	}
	
	@Override
	public int getUnitCapacity() {
		return super.getUnitCapacity() / 2;
	}
	
	@Override
	public int getAttackLevelBuff() {
		return 1;
	}
	
	public int getHealthLevelBuff() {
		return -2;
	}
	
	public int getSpeedLevelBuff() {
		return 1;
	}
}
