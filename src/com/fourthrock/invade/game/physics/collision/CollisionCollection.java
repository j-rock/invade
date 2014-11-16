package com.fourthrock.invade.game.physics.collision;

import java.util.ArrayList;
import java.util.List;

/**
 * A convenient way to store each kind of Collision,
 * so the GamePlayScene does not have to worry.
 * 
 * @author Joseph
 *
 */
public class CollisionCollection {
	public final ArrayList<AttackTowerCollision> attackTowers;
	public final ArrayList<AttackUnitCollision> attackUnits;
	public final ArrayList<MoveBackCollision> moveBacks;
	
	public CollisionCollection() {
		attackTowers = new ArrayList<>();
		attackUnits  = new ArrayList<>();
		moveBacks	 = new ArrayList<>();
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
	
	public void addCollision(final Collision coll) {
		switch(coll.type) {
			case ATTACK_TOWER:
				attackTowers.add((AttackTowerCollision)coll);
				return;
			case ATTACK_UNIT:
				attackUnits.add((AttackUnitCollision)coll);
				return;
			case MOVE_BACK:
				moveBacks.add((MoveBackCollision)coll);
				return;
			default:
		}
	}

	public void addAll(final List<Collision> colls) {
		for(final Collision c : colls) {
			addCollision(c);
		}
	}
}
