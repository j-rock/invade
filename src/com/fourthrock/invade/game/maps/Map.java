package com.fourthrock.invade.game.maps;

import java.util.ArrayList;
import java.util.List;

import com.fourthrock.invade.game.physics.BoundingBox2D;
import com.fourthrock.invade.game.physics.Position2D;
import com.fourthrock.invade.game.player.Player;
import com.fourthrock.invade.game.tower.Tower;

/**
 * A class to hold the initial configuration of an Invade game.
 * 
 * 1. Where are the towers located
 * 2. The adjacency of the towers
 * 3. Which color starts in which Tower
 * 
 * @author Joseph
 *
 */
public class Map {
	private BoundingBox2D bounds;
	protected final List<Tower> towers;
	
	public Map(final BoundingBox2D bounds, final List<Tower> towers) {
		this.bounds = bounds;
		this.towers = towers;
	}

	public Map() {
		this(BoundingBox2D.UNBOUNDED, new ArrayList<Tower>());
	}

	public BoundingBox2D getBounds() {
		return bounds;
	}

	public List<Tower> getTowers() {
		return towers;
	}
	
	public void assignPlayers(final List<Player> players) {
		for(int i=0; i<players.size(); i++) {
			towers.get(i).setPlayer(players.get(i));
		}
	}
	
	protected void addNewTower(final float x, final float y) {
		final Position2D p = new Position2D(x, y);
		bounds = bounds.expandWith(p);
		towers.add(new Tower(p));
	}
	

}
