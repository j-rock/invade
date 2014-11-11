package com.fourthrock.invade.game.unit;

public class UnitAttributes {
	private float speed;

	public UnitAttributes(final UnitAttributes unitAttrs) {
		this.speed = unitAttrs.getSpeed();
	}

	public UnitAttributes() {
		this.speed = 1.0f;
	}

	public float getSpeed() {
		return speed;
	}

}
