package com.fourthrock.invade.game.player;

import java.util.ArrayList;
import java.util.List;

import com.fourthrock.invade.draw.Color;
import com.fourthrock.invade.game.tower.Tower;

/**
 * Represents an artificially intelligent Player.
 * Evenly distributes achievement points across skills.
 * 
 * Chooses random adjacent Towers to capture. If its target
 * becomes non-adjacent, it chooses again.
 * 
 * @author Joseph
 *
 */
public class AI extends Player {
	private Tower targetTower;
	private int achievementChoice;
	
	public AI(final Color color, final PlayerAttributes playerAttributes) {
		super(color, playerAttributes);
		this.targetTower = null;
		this.achievementChoice = 0;
	}
	
	public AI(final Color color) {
		super(color);
		this.targetTower = null;
		this.achievementChoice = 0;
	}

	@Override
	public void cancelTarget() {
		super.cancelTarget();
		targetTower = null;
	}

	@Override
	public void decideTarget() {
		final List<Tower> allAdjacentTowers = new ArrayList<>();
		for(final Tower t : getTowers()) {
			allAdjacentTowers.addAll(t.getAdjacents());
		}
		
		if(   targetTower == null				  	  	   // if we have no target
		   || targetTower.getColor().equals(getColor())    // or we're targeting a Tower we own
		   || !allAdjacentTowers.contains(targetTower)) {  // or the target isn't adjacent to our current towers
														   // it's time to look for a new target
			while(!allAdjacentTowers.isEmpty()) {
				final int randIdx = (int) (Math.random() * allAdjacentTowers.size());
				final Tower randTower = allAdjacentTowers.remove(randIdx);
				if (!randTower.getColor().equals(getColor())) {
					targetTower = randTower;
					updateTarget(targetTower.getPosition());
				}
			}
		}
	}

	@Override
	public void spendAchievementPoints() {
		while(getAttributes().getAchievementPoints() > 0) {
			switch(achievementChoice++) {
				case 0:
					getAttributes().improveAttack();
				case 1:
					getAttributes().improveHealth();
				case 2:
				default:
					getAttributes().improveSpeed();
			}
		}
	}

}
