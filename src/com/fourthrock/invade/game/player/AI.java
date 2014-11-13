package com.fourthrock.invade.game.player;

import java.util.ArrayList;
import java.util.List;

import com.fourthrock.invade.draw.Color;
import com.fourthrock.invade.game.physics.ColoredCircleCollider;
import com.fourthrock.invade.game.tower.Tower;

/**
 * Represents an artificially intelligent Player
 * @author Joseph
 *
 */
public class AI extends Player {
	private int achievementChoice;
	
	public AI(final Color color, final ColoredCircleCollider collider) {
		super(color, collider);
		this.achievementChoice = 0;
	}

	@Override
	public void decideTarget() {
		
		if(getTargetTower() == null || getTargetTower().getColor().equals(getColor())) {
			// already captured the current target, time to move on.
		
			final List<Tower> allAdjacentTowers = new ArrayList<>();
			for(final Tower t : getTowers()) {
				allAdjacentTowers.addAll(t.getAdjacents());
			}
			
			while(!allAdjacentTowers.isEmpty()) {
				final int randIdx = (int) (Math.random() * allAdjacentTowers.size());
				final Tower randTower = allAdjacentTowers.remove(randIdx);
				if (!randTower.getColor().equals(getColor())) {
					setTargetTower(randTower);
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
