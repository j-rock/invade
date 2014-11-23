package com.fourthrock.invade.game.unit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fourthrock.invade.draw.Color;
import com.fourthrock.invade.game.player.Human;
import com.fourthrock.invade.game.player.Player;

public class PlayerUnitTest {
	
	@Test
	public void playerUnitTakesDamageAndDies() {
		final PlayerUnitAllocator allocator = new PlayerUnitAllocator();
		
		final Player p = new Human(Color.GREEN);
		final PlayerUnit u = allocator.allocateUnit(p, null, 0f);
		final float health = p.getAttributes().getBaseUnitHealth();
		
		final float damage = health / 2.7f;
		
		u.takeDamage(damage);
		assertTrue(u.alive());
		
		u.takeDamage(damage);
		assertTrue(u.alive());
		
		u.takeDamage(damage);
		assertFalse(u.alive());
	}
}
