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
 * 3. What zoom levels are appropriate
 * 4. The bounds of all the towers
 * 5. How many players are supported on this Map
 * 
 * Subclasses must specify appropriate zooms, add towers,
 * and remember to mark their adjacency. This class
 * takes care of the rest. Note that the WhiteAI will
 * be assigned to the first Tower and the Human will
 * be assigned to the second Tower.
 * 
 * @author Joseph
 *
 */
public abstract class Map {
	private final float minZoom, maxZoom;
	private BoundingBox2D bounds;
	protected final List<Tower> towers;

	
	private Map(final List<Tower> towers, final BoundingBox2D bounds, final float minZoom, final float maxZoom) {
		this.towers = towers;
		this.bounds = bounds;
		this.minZoom = minZoom;
		this.maxZoom = maxZoom;
	}

	protected Map(final float minZoom, final float maxZoom) {
		this(new ArrayList<Tower>(), BoundingBox2D.ORIGIN_POINT, minZoom, maxZoom);
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
		return minZoom;
	}
	
	public float getMaxZoom() {
		return maxZoom;
	}
	
	public void assignPlayers(final List<Player> players, final Player white) {
		for(int i=0; i<players.size() && i<towers.size(); i++) {
			towers.get(i).setPlayer(players.get(i));
		}
		
		//If there are any spots left over, fill them with the White AI
		for(int i=players.size(); i<towers.size(); i++) {
			towers.get(i).setPlayer(white);
		}
	}
	
	protected Tower addNewTower(final float x, final float y) {
		final Position2D p = new Position2D(x, y);
		bounds = bounds.expandWith(p, Tower.RADIUS);
		final Tower t = new Tower(p);
		towers.add(t);
		return t;
	}
}
