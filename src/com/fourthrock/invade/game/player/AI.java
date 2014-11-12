package com.fourthrock.invade.game.player;

import java.util.HashSet;
import java.util.Set;

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
		final Set<Tower> allAdjacentTowers = new HashSet<>();
		for(final Tower t : getTowers()) {
			allAdjacentTowers.addAll(t.getAdjacents());
		}
		for(final Tower adjT : allAdjacentTowers) {
			if(!adjT.getColor().equals(getColor())) {
				this.target = adjT.getPosition();
				setTargetTower(adjT);
				return;
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
