package com.fourthrock.invade.game.levels;

import java.util.List;

import com.fourthrock.invade.game.maps.TwoTowerMap;
import com.fourthrock.invade.game.player.Player;

/**
 * Simple level.
 * Player attacks and captures empty Tower.
 * 
 * @author Joseph
 *
 */
public class LevelOne extends Level {

	public LevelOne() {
		super(new TwoTowerMap(), 1);
	}
	
	@Override
	protected void assignWhites(final List<Player> players) {
		addEmptyPlayer(players, 1);
	}

}
