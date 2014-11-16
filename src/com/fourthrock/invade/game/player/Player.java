package com.fourthrock.invade.game.player;

import java.util.ArrayList;
import java.util.List;

import com.fourthrock.invade.draw.Color;
import com.fourthrock.invade.game.physics.Position2D;
import com.fourthrock.invade.game.physics.collision.ColoredCircleCollider;
import com.fourthrock.invade.game.tower.Tower;
import com.fourthrock.invade.game.unit.PlayerUnit;

/**
 * A class to represent one legion in the game.
 * 
 * @author Joseph
 *
 */
public abstract class Player {
	private final Color color;
	private final ColoredCircleCollider collider;
	private final List<PlayerUnit> units;
	private final List<Tower> towers;
	private final PlayerAttributes attrs;
	protected Position2D target;
	private long unitGenTime;

	protected Player(final Color color, final ColoredCircleCollider collider) {
		this.color = color;
		this.collider = collider;
		units = new ArrayList<>();
		towers = new ArrayList<>();
		attrs = new PlayerAttributes();
	}

	public Color getColor() {
		return color;
	}
	
	public List<PlayerUnit> getUnits() {
		return units;
	}

	public List<Tower> getTowers() {
		return towers;
	}
	
	public PlayerAttributes getAttributes() {
		return attrs;
	}

	public Position2D getTarget() {
		return target;
	}

	
	/*
	 * If you don't own any Towers, you're dead.
	 */
	public boolean isAlive() {
		return towers.size() > 0;
	}

	/**
	 * Updates the target position for the Player's units.
	 */
	public void updateTarget(final Position2D target) {
		this.target = target;
	}
	
	/**
	 * Unsets the target position for the Player's units.
	 */
	public void cancelTarget() {
		updateTarget(null);
	}

	/**
	 * Shifts all units in their respective directions.
	 */
	public void moveUnits(final long dt) {
		for(final PlayerUnit u : units) {
			u.moveTowardsTarget(dt);
			u.setMoving(); // we go to the Move state after moving so that collisions can determine the next state.
			collider.placeCircle(u);
		}
	}

	/**
	 * If enough time has passed, the Player
	 * can generate a new PlayerUnit at a random tower.
	 */
	public void tryGenerateUnit(final long dt) {
		unitGenTime += dt;
		if(   units.size()  < Math.min(5, towers.size()) * attrs.getMaxUnitsPerTowerCount()
		   &&  unitGenTime >= attrs.getUnitCreationWaitTime()) {
			
			unitGenTime -= attrs.getUnitCreationWaitTime();
			
			final int randIndex = (int)(Math.random() * towers.size());
			final Tower tower = towers.get(randIndex);
			final Position2D towerPos = tower.getPosition();
			final Position2D unitPos = towerPos.randomPositionOnCircle(Tower.SPAWN_RADIUS);
			final PlayerUnit unit = new PlayerUnit(this, unitPos);
			units.add(unit);
		}
	}

	public void fireUnits(final long dt) {
		for(final PlayerUnit u : units) {
			u.fireAtTarget(dt);
		}
	}

	public void removeDeadUnits() {
		for(int i=units.size()-1; i>=0; i--) {
			if(!units.get(i).alive()) {
				units.remove(i);
			}
		}
	}
	
	public void addTower(final Tower t) {
		towers.add(t);
	}
	
	public void removeTower(final Tower t) {
		// Since we need random access, we need a List (well, an ArrayList).
		// Unfortunately, this means we have to do an O(n)
		// search to find and remove a Tower.
		// Player's don't lose Towers too often,
		// so this shouldn't slow us down too much.
		
		for(int i=towers.size()-1; i>=0; i--) {
			if(towers.get(i).equals(t)) {
				towers.remove(i);
				return;
			}
		}
	}
	
	
	/**
	 * Decide a target position for the Player's units.
	 */
	public abstract void decideTarget();
	
	/**
	 * Spend achievement points on skills
	 */
	public abstract void spendAchievementPoints();

	
	
}
