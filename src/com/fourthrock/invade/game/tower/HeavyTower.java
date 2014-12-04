package com.fourthrock.invade.game.tower;

import com.fourthrock.invade.game.physics.Position2D;

public class HeavyTower extends Tower {
	private static final float SIZE_INC = 1.9f;

	public HeavyTower(final Position2D position) {
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
		return 4 * super.getUnitCapacity();
	}
	
	@Override
	public int getHealthLevelBuff() {
		return 4;
	}
	
	@Override
	public int getSpeedLevelBuff() {
		return -3;
	}
}
