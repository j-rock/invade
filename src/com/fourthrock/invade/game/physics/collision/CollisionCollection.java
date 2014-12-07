package com.fourthrock.invade.game.physics.collision;

import java.util.ArrayList;
import java.util.List;

import com.fourthrock.invade.game.tower.Tower;
import com.fourthrock.invade.game.unit.PlayerUnit;

/**
 * A convenient way to store each kind of Collision,
 * so the GamePlayScene does not have to worry.
 * 
 * @author Joseph
 *
 */
public class CollisionCollection {
	private static final int MIN_CAPACITY = 200;
	
	public final ArrayList<AttackTowerCollision> attackTowers;
	public final ArrayList<AttackUnitCollision> attackUnits;
	public final ArrayList<MoveBackCollision> moveBacks;
	
	public CollisionCollection() {
		// TODO - make sure ArrayLists can handle concurrent modification.
		
		attackTowers = new ArrayList<>(MIN_CAPACITY);
		attackUnits  = new ArrayList<>(MIN_CAPACITY);
		moveBacks    = new ArrayList<>(MIN_CAPACITY);
	}
	
	public List<AttackTowerCollision> getAttackTowerCollisions() {
		return attackTowers;
	}
	
	public List<AttackUnitCollision> getAttackUnitCollisions() {
		return attackUnits;
	}
	
	public List<MoveBackCollision> getMoveBackCollisions() {
		return moveBacks;
	}
	
	public void addAttackTowerCollision(final PlayerUnit attacker, final Tower victim) {
		attackTowers.add(new AttackTowerCollision(attacker, victim));
	}
	
	public void addAttackUnitCollision(final PlayerUnit attacker, final PlayerUnit victim) {
		attackUnits.add(new AttackUnitCollision(attacker, victim));
	}
	
	public void addMoveBackCollision(final PlayerUnit unit, final PlayerUnit backOffUnit) {
		moveBacks.add(new MoveBackCollision(unit, backOffUnit));
	}
	
	public void addMoveBackCollision(final PlayerUnit unit, final Tower backOffTower) {
		moveBacks.add(new MoveBackCollision(unit, backOffTower));
	}

	public void clear() {
		attackTowers.clear();
		attackUnits.clear();
		moveBacks.clear();
		
		attackTowers.ensureCapacity(MIN_CAPACITY);
		attackUnits.ensureCapacity(MIN_CAPACITY);
		moveBacks.ensureCapacity(MIN_CAPACITY);
	}
	
	public int size() {
		return attackTowers.size() + attackUnits.size() + moveBacks.size();
	}
}
