package com.fourthrock.invade.game.player;

import com.fourthrock.invade.draw.Color;
import com.fourthrock.invade.game.physics.ColoredCircleCollider;

public class WhiteAI extends AI {

	public WhiteAI(final ColoredCircleCollider collider) {
		super(Color.WHITE, collider);
	}

	@Override
	public void decideTarget() {
		// TODO - make WhiteAI passively defend base, no more, no less.
	}
	
}
