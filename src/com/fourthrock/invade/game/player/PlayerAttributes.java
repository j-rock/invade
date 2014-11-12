package com.fourthrock.invade.game.player;


/*
 * Represents the stats a Player has throughout the course of the game.
 * Handles upgrading logic.
 */
public class PlayerAttributes {
	private int maxUnitCount;
	private long unitCreationWaitTime;
	
	private float unitMoveSpeed;
	private float unitBaseHealth;
	private float unitAttackSpeed;
	
	private int level;
	private int progress;
	private int achievementPoints;
	
	public PlayerAttributes() {
		 // TODO - determine properly
		
		final long ONE_SEC = 1000L;
		
		this.maxUnitCount = 10;
		this.unitCreationWaitTime = ONE_SEC;
		
		this.unitMoveSpeed = 0.01f / ONE_SEC;
		this.unitBaseHealth = 50f;
		this.unitAttackSpeed = unitBaseHealth / (2 * ONE_SEC);
		
		this.level = 1;
		this.progress = 0;
		this.achievementPoints = 0;
	}
	
	public int getMaxUnitCount() {
		return maxUnitCount;
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
		registerPoints(50);
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
		if(remainingPointsUntilNextLevel() <= 0) {
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
