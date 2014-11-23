package com.fourthrock.invade.game.maps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.fourthrock.invade.game.physics.BoundingBox2D;
import com.fourthrock.invade.game.physics.Position2D;
import com.fourthrock.invade.game.player.Player;
import com.fourthrock.invade.game.player.WhiteAI;
import com.fourthrock.invade.game.tower.Tower;
import com.fourthrock.invade.util.Index2D;

/**
 * A class to hold the initial configuration of an Invade game.
 * 
 * 1. Where are the towers located
 * 2. The adjacency of the towers
 * 3. What zoom levels are appropriate
 * 4. The bounds of all the towers
 * 5. How many players are supported on this Map
 * 
 * Subclasses must specify appropriate zooms, add towers,
 * and remember to mark their adjacency. This class
 * takes care of the rest. Note that the Human will
 * be assigned to the first Tower.
 * 
 * @author Joseph
 *
 */
public abstract class Map {
	private static final float MIN_ZOOM = 0.024f; // magic constant determined experimentally
	private static final float MAX_ZOOM = 0.16f;  // magic constant determined experimentally
	private BoundingBox2D bounds;
	protected final List<Tower> towers;

	
	private Map(final List<Tower> towers, final BoundingBox2D bounds) {
		this.towers = towers;
		this.bounds = bounds;
	}

	protected Map() {
		this(new ArrayList<Tower>(), BoundingBox2D.ORIGIN_POINT);
	}

	public BoundingBox2D getBounds() {
		return bounds;
	}

	public List<Tower> getTowers() {
		return towers;
	}
	
	public int getNumberOfPlayers() {
		return towers.size();
	}
	
	public float getMinZoom() {
		return MIN_ZOOM;
	}
	
	public float getMaxZoom() {
		return MAX_ZOOM;
	}
	
	/**
	 * Computes list of pairs of indices (i, j) such
	 * that the ith Tower is adjacent to the jth Tower.
	 */
	public List<Index2D> getAdjSet() {
		
		// Get a mapping from Tower to its Index
		final HashMap<Tower, Integer> towerToIndex = new HashMap<>();
		for(int i=0; i<towers.size(); i++) {
			towerToIndex.put(towers.get(i), i);
		}
		
		// Over each tower_i in towers
		//    Over each tower_j adjacent to tower_i
		//         if i < j, add it to the list
		final List<Index2D> adjIndices = new ArrayList<>();
		for(int i=0; i<towers.size(); i++) {
			final Set<Tower> iAdjs = towers.get(i).getAdjacents();
			for(final Tower tJ : iAdjs) {
				final int j = towerToIndex.get(tJ).intValue();
				if(i < j) {
					adjIndices.add(new Index2D(i, j));
				}
			}
		}
		
		return adjIndices;
	}

	
	/**
	 * For each Player in players, we assign one Tower.
	 * The remainder get their own unique WhiteAI protector,
	 * which is added to the list of Players.
	 */
	public void assignPlayers(final List<Player> players) {
		for(int i=0; i<players.size() && i<towers.size(); i++) {
			towers.get(i).adoptNewPlayer(players.get(i));
		}
		
		//If there are any spots left over, fill them with the White AI
		for(int i=players.size(); i<towers.size(); i++) {
			final Player white = new WhiteAI();
			players.add(white);
			towers.get(i).adoptNewPlayer(white);
		}
	}
	
	protected Tower addNewTower(final float x, final float y) {
		final Position2D p = new Position2D(x, y);
		bounds = bounds.expandWith(p);
		final Tower t = new Tower(p);
		towers.add(t);
		return t;
	}
}
