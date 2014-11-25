package com.fourthrock.invade.game.player;

import com.fourthrock.invade.game.tower.Tower;
import com.fourthrock.invade.game.unit.PlayerUnit;


/*
 * Represents the stats a Player has throughout the course of the game.
 * Handles upgrading logic.
 */
public class PlayerAttributes {
	private int maxUnitsPerTowerCount;
	private long unitCreationWaitTime;
	
	private float unitMoveSpeed;
	private float unitBaseHealth;
	private float unitAttackSpeed;
	private float unitAttackRadius;
	
	private int level;
	private int progress;
	private int achievementPoints;
	
	public PlayerAttributes(final float healthPercentage) {
		 // TODO - determine properly
		
		final long ONE_SEC = 1000L;
		
		this.maxUnitsPerTowerCount = 15;
		this.unitCreationWaitTime = ONE_SEC;
		
		this.unitMoveSpeed    = Tower.BORDER_RADIUS / (1.8f * ONE_SEC);
		this.unitBaseHealth   = healthPercentage * Tower.BASE_HEALTH / 200f;
		this.unitAttackSpeed  = Tower.REGEN_RATE / 2f;
		this.unitAttackRadius = PlayerUnit.BORDER_RADIUS * 3;
		
		this.level = 1;
		this.progress = 0;
		this.achievementPoints = 0;
	}
	
	public PlayerAttributes() {
		this(1f);
	}
	

	public int getMaxUnitsPerTowerCount() {
		return maxUnitsPerTowerCount;
	}

	public long getUnitCreationWaitTime() {
		return unitCreationWaitTime;
	}
	
	public float getUnitMoveSpeed() {
		return unitMoveSpeed;
	}
	
	public float getUnitAttackSpeed() {
		return unitAttackSpeed;
	}
	
	public float getUnitAttackRadius() {
		return unitAttackRadius;
	}
	
	public float getBaseUnitHealth() {
		return unitBaseHealth;
	}
	
	public int getAchievementPoints() {
		return achievementPoints;
	}
	
	public float getProgressRatio() {
		final int remainingPoints = remainingPointsUntilNextLevel();
		return ((float)progress) / (progress + remainingPoints);
	}

	public void registerKill() {
		registerPoints(5);
	}

	public void registerDeath() {
		registerPoints(1);
	}
	
	public void registerTowerCapture() {
		registerPoints(150);
	}

	public void improveAttack() {
		if(readyTakeAchievement()) {
			unitAttackSpeed *= 1.1;
		}
	}

	public void improveHealth() {
		if(readyTakeAchievement()) {
			unitBaseHealth *= 1.1;
		}
	}

	public void improveSpeed() {
		if(readyTakeAchievement()) {
			unitMoveSpeed *= 1.1;
		}
	}
	
	/**
	 * Augments progress by points.
	 * If a level has been surpassed, we record it
	 * and gain an achievement point.
	 */
	private void registerPoints(final int points) {
		progress += points;
		final int remPoints = remainingPointsUntilNextLevel();
		if(remPoints <= 0) {
			progress = -remPoints;
			level++;
			achievementPoints++;
		}
	}
	
	private int remainingPointsUntilNextLevel() {
		return minPointsForLevel(level+1) - progress;
	}
	
	private static int minPointsForLevel(final int lvl) {
		// O(n^3 / logn) function for level growth.
		return 100 + (int)((4 * lvl * lvl * lvl) / Math.log(lvl + 1));
	}

	private boolean readyTakeAchievement() {
		if(achievementPoints > 0) {
			achievementPoints--;
			return true;
		} else {
			return false;
		}
	}

}
