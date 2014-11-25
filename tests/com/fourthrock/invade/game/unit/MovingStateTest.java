package com.fourthrock.invade.game.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fourthrock.invade.draw.Color;
import com.fourthrock.invade.game.physics.Position2D;
import com.fourthrock.invade.game.player.Human;
import com.fourthrock.invade.game.player.Player;
import com.fourthrock.invade.game.tower.Tower;

public class MovingStateTest {
	private static final float EPSILON = 1e-15f;

	@Test
	public void movingStateDoesntHurtAFly() {
		final Player p = new Human(Color.GREEN);
		final Tower t = new Tower(new Position2D(0f, 0f));
		final PlayerUnit u = new PlayerUnit();
		u.reset(p, null, 0f);
		
		final float tHealthBefore = t.getHealth();
		final float uHealthBefore = u.getHealth();
		
		UnitState.MOVING.fireAtTarget(p, t, u, 20L);
		
		final float tHealthAfter = t.getHealth();
		final float uHealthAfter = u.getHealth();
		
		assertEquals(tHealthBefore, tHealthAfter, EPSILON);
		assertEquals(uHealthBefore, uHealthAfter, EPSILON);
	}
	
	@Test
	public void movingStateWantsToMove() {
		final Position2D a = new Position2D(0f, 0f);
		final Position2D b = new Position2D(10f, 10f);
		
		final float distABefore = a.minus(a).sqrMagnitude();
		final float distBBefore = b.minus(a).sqrMagnitude();
		
		final Position2D currPos = UnitState.MOVING.moveTowards(a, b, 1f, 4L);
		
		final float distAAfter = a.minus(currPos).sqrMagnitude();
		final float distBAfter = b.minus(currPos).sqrMagnitude();

		assertTrue(distABefore < distAAfter);
		assertTrue(distBBefore > distBAfter);
	}
}
