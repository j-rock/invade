package com.fourthrock.invade.game.physics.collision;

import com.fourthrock.invade.game.unit.PlayerUnit;

public class MoveBackCollision extends Collision {

	public final PlayerUnit unit;
	public final ColoredCircle c;

	public MoveBackCollision(final PlayerUnit unit, final ColoredCircle c) {
		super(Collision.Type.MOVE_BACK);
		this.unit = unit;
		this.c = c;
	}
}
