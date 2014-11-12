package com.fourthrock.invade.game.player;

import java.util.ArrayList;
import java.util.List;

import com.fourthrock.invade.draw.Color;
import com.fourthrock.invade.game.physics.ColoredCircleCollider;
import com.fourthrock.invade.game.physics.Position2D;
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
	private final List<PlayerUnit> units;
	private final List<Tower> towers;
	private final ColoredCircleCollider collider;
	private final PlayerAttributes attrs;
	private Position2D target;
	private Tower targetTower;
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
	
	/*
	 * If you don't own any Towers, you're dead.
	 */
	public boolean isAlive() {
		return towers.size() > 0;
	}
	
	/**
	 * Decide a target position for the Player's units.
	 */
	public abstract void decideTarget();
	
	/**
	 * Updates the target position for the Player's units.
	 * If the target contains a Tower, we update
	 * the target Tower as well so PlayerUnits know to fire
	 * when they get close.
	 */
	public void updateTarget(final Position2D target) {
		this.target = target;
		targetTower = collider.findTower(color, target);
	}

	/**
	 * Shifts all units in their respective directions.
	 */
	public void moveUnits(final long dt) {
		for(final PlayerUnit u : units) {
			u.moveTowards(target, targetTower, dt);
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
		if(   units.size() < attrs.getMaxUnitCount()
		      && unitGenTime >= attrs.getUnitCreationWaitTime()) {
			
			unitGenTime -= attrs.getUnitCreationWaitTime();
			
			final int randIndex = (int)(Math.random() * towers.size());
			final Tower tower = towers.get(randIndex);
			final Position2D towerPos = tower.getPosition();
			final Position2D unitPos = towerPos.randomPositionOnCircle(tower.getRadius());
			final PlayerUnit unit = new PlayerUnit(color, unitPos, attrs);
			units.add(unit);
		}
	}

	public void fireUnits(final long dt) {
		for(final PlayerUnit u : units) {
			u.fireAtTarget();
		}
	}

}
