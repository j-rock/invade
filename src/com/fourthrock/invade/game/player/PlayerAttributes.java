package com.fourthrock.invade.game.player;

import com.fourthrock.invade.game.tower.Tower;
import com.fourthrock.invade.game.unit.PlayerUnit;


/*
 * Represents the stats a Player has throughout the course of the game.
 * Handles upgrading logic.
 */
public class PlayerAttributes {
	private static final long MAX_LEVEL = 9;
	private static final long unitCreationWaitTime = 500L;
	
	private final float healthPercentage;
	private int maxUnitCapacity;
	
	private int speedLevel;
	private int healthLevel;
	private int attackLevel;
	private int attackRadiusLevel;
	
	private int level;
	private int progress;
	private int achievementPoints;
	
	public PlayerAttributes(final float healthPercentage) {
		 // TODO - determine properly
		this.healthPercentage = healthPercentage;
		this.maxUnitCapacity = 0;
		
		speedLevel = healthLevel = attackLevel = attackRadiusLevel = 0;
		this.level = 1;
		this.progress = 0;
		this.achievementPoints = 0;
	}
	
	public PlayerAttributes() {
		this(1f);
	}

	public long getUnitCreationWaitTime() {
		return unitCreationWaitTime;
	}
	
	public float getUnitMoveSpeed() {
		final float baseSpeed = Tower.BORDER_RADIUS / (1.5f * 1000L);
		return upgradeFunc(speedLevel, baseSpeed);
	}
	
	public float getUnitAttackSpeed() {
		final float baseAttack = Tower.REGEN_RATE / 3f;
		return upgradeFunc(attackLevel, baseAttack);
	}
	
	public float getBaseUnitHealth() {
		final float baseHealth = healthPercentage * 2000f * Tower.REGEN_RATE / 2f;
		return upgradeFunc(healthLevel, baseHealth);
	}
	
	public float getUnitAttackRadius() {
		final float baseAttackRadius = PlayerUnit.BORDER_RADIUS * 5f;
		return upgradeFunc(attackRadiusLevel, baseAttackRadius);
	}
	
	public int getAchievementPoints() {
		return achievementPoints;
	}
	
	public float getProgressRatio() {
		final int remainingPoints = remainingPointsUntilNextLevel();
		return ((float)progress) / (progress + remainingPoints);
	}
	
	public int getMaxUnitCapacity() {
		return Math.min(130, maxUnitCapacity);
	}

	public void registerKill() {
		registerPoints(5);
	}

	public void registerDeath() {
		registerPoints(1);
	}
	
	public void registerTowerCapture() {
		registerPoints(70);
	}

	public void handleNewTower(final Tower t) {
		updateStatsByTower(1, t);
	}

	public void handleLoseTower(final Tower t) {
		updateStatsByTower(-1, t);
	}

	public void improveAttack() {
		if(canImproveAttack()) {
			achievementPoints--;
			attackLevel++;
		}
	}

	public void improveHealth() {
		if(canImproveHealth()) {
			achievementPoints--;
			healthLevel++;
		}
	}

	public void improveSpeed() {
		if(canImproveSpeed()) {
			achievementPoints--;
			speedLevel++;
		}
	}
	
	public boolean canImproveAttack() {
		return canImprove(attackLevel);
	}
	
	public boolean canImproveHealth() {
		return canImprove(healthLevel);
	}
	
	public boolean canImproveSpeed() {
		return canImprove(speedLevel);
	}
	
	private boolean canImprove(final int level) {
		return achievementPoints > 0 && level < MAX_LEVEL;
	}
	
	private void updateStatsByTower(final int sign, final Tower t) {
		attackLevel     += sign * t.getAttackLevelBuff();
		healthLevel     += sign * t.getHealthLevelBuff();
		speedLevel      += sign * t.getSpeedLevelBuff();
		maxUnitCapacity += sign * t.getUnitCapacity();
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
		// O(n^2 / logn) function for level growth.
		return 100 + (int)((16 * lvl * lvl) / Math.log(lvl + 1));
	}

	private static float upgradeFunc(final int level, final float baseAmount) {
		return baseAmount * (float)(Math.pow(1.1f, Math.min(MAX_LEVEL, level)));
	}


}
