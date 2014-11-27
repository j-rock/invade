package com.fourthrock.invade.game.player;

import com.fourthrock.invade.draw.Color;
import com.fourthrock.invade.game.tower.Tower;

/**
 * Unlike regular AI, the WhiteAI never go to capture a Tower.
 * They jealously guard their own Tower, trying to fend away
 * other invaders.
 * 
 * @author Joseph
 *
 */
public class WhiteAI extends AI {

	public WhiteAI() {
		super(Color.SNOW, new PlayerAttributes(0.15f));
	}

	@Override
	public void decideTarget() {
		// TODO - make WhiteAI passively defend base, no more, no less.
		
		if(getTowers().size() > 0) {
			final Tower onlyTower = getTowers().get(0);
			updateTarget(onlyTower.getPosition());
		}
	}
	
}
