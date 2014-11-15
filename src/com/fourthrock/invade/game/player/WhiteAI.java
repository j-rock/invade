package com.fourthrock.invade.game.player;

import com.fourthrock.invade.draw.Color;
import com.fourthrock.invade.game.physics.collision.ColoredCircleCollider;

/**
 * Unlike regular AI, the WhiteAI never go to capture a Tower.
 * They jealously guard their own Tower, trying to fend away
 * other invaders.
 * 
 * @author Joseph
 *
 */
public class WhiteAI extends AI {

	public WhiteAI(final ColoredCircleCollider collider) {
		super(Color.WHITE, collider);
	}

	@Override
	public void decideTarget() {
		// TODO - make WhiteAI passively defend base, no more, no less.
	}
	
}
