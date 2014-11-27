package com.fourthrock.invade.game.levels;

import java.util.List;

import com.fourthrock.invade.game.maps.WishboneMap;
import com.fourthrock.invade.game.player.Player;

/**
 * Same as LevelTwo except this time,
 * there is a WhiteAI waiting at
 * the second tower.
 * 
 * @author Joseph
 *
 */
public class LevelThree extends Level {
	
	public LevelThree() {
		super(new WishboneMap(), 3);
	}
	
	@Override
	public void assignWhites(final List<Player> players) {
		addEmptyPlayer(players, 1);
		addWhitePlayer(players, 2);
	}

}
