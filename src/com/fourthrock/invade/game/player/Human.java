package com.fourthrock.invade.game.player;

import com.fourthrock.invade.draw.Color;
import com.fourthrock.invade.game.physics.ColoredCircleCollider;

/**
 * A class to represent a human player.
 * 
 * @author Joseph
 *
 */
public class Human extends Player {
	
	public Human(final Color color, final ColoredCircleCollider collider) {
		super(color, collider);
	}

	@Override
	public void decideTarget() {
		// do nothing...
		// screen taps will dictate
		// where to target units.
	}
}