package com.fourthrock.invade.game.player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fourthrock.invade.draw.Color;
import com.fourthrock.invade.game.physics.Position2D;
import com.fourthrock.invade.game.tower.Tower;
import com.fourthrock.invade.game.unit.PlayerUnit;
import com.fourthrock.invade.util.ObjectPool;

/**
 * A class to represent one legion in the game.
 * 
 * @author Joseph
 *
 */
public abstract class Player {
	private final Color color;
	private final List<PlayerUnit> units;
	private final List<Tower> towers;
	private final PlayerAttributes attrs;
	protected Position2D target;
	private long unitGenTime;

	protected Player(final Color color, final PlayerAttributes playerAttributes) {
		this.color = color;
		units = new ArrayList<>();
		towers = new ArrayList<>();
		attrs = playerAttributes;
	}

	protected Player(final Color color) {
		this(color, new PlayerAttributes());
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
	
	public Set<Tower> getAdjacentTowers() {
		final Set<Tower> adjacents = new HashSet<>();
		for(final Tower t : towers) {
			adjacents.add(t);
			adjacents.addAll(t.getAdjacents());
		}
		return adjacents;
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
		}
	}

	/**
	 * If enough time has passed, the Player
	 * can generate a new PlayerUnit at a random tower.
	 */
	public void tryGenerateUnit(final ObjectPool<PlayerUnit> allUnits, final long dt) {
		final int maxUnitCount = Math.min(6, towers.size()) * attrs.getMaxUnitsPerTowerCount();
		if (units.size() >= maxUnitCount) {
			unitGenTime = 0;
		} else if (towers.size() > 0) {
			unitGenTime += dt;
	
			if(unitGenTime >= attrs.getUnitCreationWaitTime()) {
				unitGenTime -= attrs.getUnitCreationWaitTime();
			
				final int randIndex = (int)(Math.random() * towers.size());
				final Tower tower = towers.get(randIndex);
				final Position2D towerPos = tower.getPosition();
				final Position2D unitPos = towerPos.randomPositionOnCircle(Tower.SPAWN_RADIUS);
				final float radialOrientation = unitPos.minus(towerPos).theta();
				final PlayerUnit unit = allUnits.allocate();
				unit.reset(this, unitPos, radialOrientation);
				units.add(unit);
			}
		}
	}

	public void fireUnits(final long dt) {
		for(final PlayerUnit u : units) {
			u.fireAtTarget(dt);
		}
	}

	public void removeDeadUnits(final ObjectPool<PlayerUnit> allUnits) {
		for(int i=units.size()-1; i>=0; i--) {
			final PlayerUnit u = units.get(i);
			if(!u.alive()) {
				allUnits.deallocate(u);
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
