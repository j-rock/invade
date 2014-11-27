package com.fourthrock.invade.game.levels;

import java.util.List;

import com.fourthrock.invade.game.maps.WishboneMap;
import com.fourthrock.invade.game.player.Player;

/**
 * Player attacks two empty Towers.
 * Has to attack right one first,
 * so that the Player can attack the
 * top one.
 * 
 * No enemies yet.
 * 
 * @author Joseph
 *
 */
public class LevelTwo extends Level {

	public LevelTwo() {
		super(new WishboneMap(), 2);
	}

	@Override
	protected void assignWhites(final List<Player> players) {
		addEmptyPlayer(players, 1);
		addEmptyPlayer(players, 2);
	}

}
