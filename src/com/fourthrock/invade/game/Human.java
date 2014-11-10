package com.fourthrock.invade.game;

import com.fourthrock.invade.draw.Color;

/**
 * A class to represent a human player.
 * 
 * @author Joseph
 *
 */
public class Human extends Player {
	
	public Human(final Color color) {
		super(color);
	}

	@Override
	public void decideTarget() {
		// do nothing...
		// screen taps will dictate
		// where to target units.
	}
}