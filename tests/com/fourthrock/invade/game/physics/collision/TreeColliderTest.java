package com.fourthrock.invade.game.physics.collision;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fourthrock.invade.game.physics.Position2D;
import com.fourthrock.invade.game.player.Player;
import com.fourthrock.invade.game.player.WhiteAI;
import com.fourthrock.invade.game.tower.Tower;
import com.fourthrock.invade.game.unit.PlayerUnit;
import com.fourthrock.invade.util.Allocateable;
import com.fourthrock.invade.util.Allocator;

public class TreeColliderTest {
	
	@Test
	public void unitOverlappingTowerShouldCollide() {
		final Player player = new WhiteAI();
		
		final Tower t = new Tower(new Position2D(0f, 0f));
		t.adoptNewPlayer(player);
		final List<Tower> towers = new ArrayList<>();
		towers.add(t);
		
		final Allocator<PlayerUnit> allUnits = new Allocator<>(new Allocateable<PlayerUnit>(){
			@Override public PlayerUnit allocate() {
				return new PlayerUnit();
			}
		});
		final PlayerUnit u = allUnits.allocate();
		u.reset(player, new Position2D(0f, 0f), 0f);
		
		final TreeCollider collider = new TreeCollider(towers);
		final CollisionCollection colls = collider.withdrawCollisions(allUnits);
		
		
		assertEquals(0, colls.getAttackTowerCollisions().size());
		assertEquals(0, colls.getAttackUnitCollisions().size());
		assertEquals(1, colls.getMoveBackCollisions().size());
	}

}
