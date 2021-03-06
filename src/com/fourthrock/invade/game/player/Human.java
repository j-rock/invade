package com.fourthrock.invade.game.player;

import com.fourthrock.invade.draw.Color;

/**
 * A class to represent a human player.
 * Since a human interacts through the screen,
 * the Player methods are empty.
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

	@Override
	public void spendAchievementPoints() {
		// do nothing...
		// screen taps will dictate
		// how to spend points
	}
}