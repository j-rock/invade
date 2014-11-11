package com.fourthrock.invade.game.player;

import com.fourthrock.invade.draw.Color;
import com.fourthrock.invade.game.physics.ColoredCircleCollider;

/**
 * Represents an artificially intelligent Player
 * @author Joseph
 *
 */
public class AI extends Player {
	
	public AI(final Color color, final ColoredCircleCollider collider) {
		super(color, collider);
	}

	@Override
	public void decideTarget() {
	}

}
