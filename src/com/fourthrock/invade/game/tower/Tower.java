package com.fourthrock.invade.game.tower;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fourthrock.invade.draw.Color;
import com.fourthrock.invade.draw.ScaleVec;
import com.fourthrock.invade.game.physics.ColoredCircle;
import com.fourthrock.invade.game.physics.Position2D;
import com.fourthrock.invade.game.player.Player;

/**
 * Represents a tower that can be captured by a PlayerUnit.
 * @author Joseph
 *
 */
public class Tower implements ColoredCircle {
	public static final float RADIUS = 0.0825f;
	public static final ScaleVec SCALE = new ScaleVec(RADIUS, RADIUS, RADIUS);
	private static final float BASE_HEALTH = 10000f;
	private static final float REGEN_RATE = BASE_HEALTH / (60 * 1000); // takes one minute to fully heal
	
	private final Position2D position;
	private final Set<Tower> adjacents;
	private float health;
	private Player player;
	
	public Tower(final Position2D position) {
		this.position = position;
		this.adjacents = new HashSet<>();
		this.health = BASE_HEALTH;
	}

	@Override
	public Position2D getPosition() {
		return position;
	}
	
	@Override
	public float getRadius() {
		return RADIUS;
	}

	@Override
	public Color getColor() {
		return player.getColor();
	}
	
	public Color getRenderColor() {
		final Color healthBlack = new Color(0f, 0f, 0f, 1 - health/BASE_HEALTH);
		return healthBlack.blend(player.getColor());
	}

	public float getHealth() {
		return health;
	}

	public void takeDamage(final float attackPower) {
		health = Math.max(0f, health - attackPower);
	}

	public void regainHealth(final float dt) {
		health += REGEN_RATE * dt;
	}
	
	public void resetHealth() {
		health = BASE_HEALTH / 2;
	}
	
	public void adoptNewPlayer(final Player p) {
		player.removeTower(this);
		setPlayer(p);
	}
	
	public void setPlayer(final Player p) {
		this.player = p;
		p.addTower(this);
	}

	/**
	 * Attacking units should stay around the radius of the tower.
	 * This method returns the Position2D to put the unit at.
	 */
	public Position2D positionForAttackingUnit(final Position2D unitPos) {
		return position.nearestOnCircle(getRadius(), unitPos);
	}

	public boolean adjacentTo(final List<Tower> towers) {
		for(final Tower t : towers) {
			if(adjacents.contains(t)) {
				return true;
			}
		}
		return false;
	}

	public void setAdjacentTo(final Tower tower) {
		adjacents.add(tower);
	}

	public Set<Tower> getAdjacents() {
		return adjacents;
	}

}
