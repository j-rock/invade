package com.fourthrock.invade.game.physics.collision;

import com.fourthrock.invade.game.physics.Position2D;
import com.fourthrock.invade.game.physics.Vector2D;
import com.fourthrock.invade.game.tower.Tower;
import com.fourthrock.invade.game.unit.PlayerUnit;

public class MoveBackCollision extends Collision {
	public final PlayerUnit unit;
	private final Position2D backOffPosition;
	private final float separation;
	private final boolean goPerpendicular;
	
	private MoveBackCollision(final PlayerUnit unit, final Position2D backOffPosition, final float separation, final boolean goPerpendicular) {
		super(Collision.Type.MOVE_BACK);
		this.unit = unit;
		this.backOffPosition = backOffPosition;
		this.separation = unit.getPhysicalRadius() + separation;
		this.goPerpendicular = goPerpendicular;
	}
	
	public MoveBackCollision(final PlayerUnit unit, final Tower tower) {
		this(unit, tower.getPosition(), tower.getPhysicalRadius(), true);
	}
	
	public MoveBackCollision(final PlayerUnit unit, final PlayerUnit backOffUnit) {
		this(unit, backOffUnit.getPosition(), backOffUnit.getPhysicalRadius(), false);
	}
	
	@Override
	public void process(final long dt) {
		if(goPerpendicular) {
			unit.moveTo(findBackOffTowerPosition(dt));
		} else {
			unit.moveTo(findBackOffUnitPosition());		
		}
	}
	
	private Position2D findBackOffTowerPosition(final long dt) {
		final float speed = unit.getSpeed();
		final Position2D currPos = unit.getPosition();
		final Vector2D perpDir = backOffPosition.minus(currPos).perpendicular().unitize();
		final Position2D perpPos = currPos.add(perpDir.scale(2 * speed * dt)).asPosition();
		return findBackOffPosition(perpPos);
	}
	
	private Position2D findBackOffUnitPosition() {
		return findBackOffPosition(unit.getPosition());
	}
	
	/**
	 * Return a Position2D that backs off from the backOffPosition
	 * by at least the minimum separation distance.
	 * 
	 * We try to move as close to targetPosition as possible.
	 */
	private Position2D findBackOffPosition(final Position2D targetPosition) {
		final Position2D currPosition = unit.getPosition();
		final Position2D onCircle = backOffPosition.nearestOnCircle(separation, targetPosition);
		final float currSqrDistFromBackOff = currPosition.minus(backOffPosition).sqrMagnitude();
		final float onCircleSqrDistFromBackOff = onCircle.minus(backOffPosition).sqrMagnitude();

		return (currSqrDistFromBackOff <= onCircleSqrDistFromBackOff)
			? onCircle
			: currPosition;
	}

}
