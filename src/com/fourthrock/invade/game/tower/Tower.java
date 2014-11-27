package com.fourthrock.invade.game.tower;

import static com.fourthrock.invade.draw.DrawEnum.CIRCLE;
import static com.fourthrock.invade.draw.DrawEnum.SQUARE;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fourthrock.invade.draw.CanvasRenderer;
import com.fourthrock.invade.draw.Color;
import com.fourthrock.invade.draw.ScaleVec;
import com.fourthrock.invade.game.physics.Position2D;
import com.fourthrock.invade.game.physics.collision.ColoredCircle;
import com.fourthrock.invade.game.player.Player;

/**
 * Represents a tower that can be captured by a PlayerUnit.
 * @author Joseph
 *
 */
public class Tower implements ColoredCircle {
	public static final float BORDER_RADIUS = 2.025f;
	public static final float SPAWN_RADIUS = 2f * BORDER_RADIUS;
	public static final ScaleVec SCALE = new ScaleVec(BORDER_RADIUS * (float)Math.sqrt(2));
	public static final float BASE_HEALTH = 1000f;
	public static final float REGEN_RATE = BASE_HEALTH / (90 * 1000); // takes 90 seconds to fully heal
	
	private final Set<Tower> adjacents;
	private final Position2D position;
	private final float orientation;
	private float health;
	private Player player;
	
	public Tower(final Position2D position) {
		this.adjacents = new HashSet<>();
		this.position = position;
		this.orientation = (float)(Math.random() * 360);
		this.health = BASE_HEALTH;
	}

	@Override
	public Position2D getPosition() {
		return position;
	}
	
	@Override
	public float getPhysicalRadius() {
		return BORDER_RADIUS;
	}
	
	@Override
	public float getActiveRadius() {
		return SPAWN_RADIUS;
	}

	@Override
	public Color getColor() {
		return player.getColor();
	}
	
	public Color getRenderColor() {
		final Color healthBlack = Color.BLACK.withAlpha(1 - health/BASE_HEALTH);
		return healthBlack.blend(getColor());
	}
	
	public float getOrientation() {
		return orientation;
	}

	public float getHealth() {
		return health;
	}

	public void takeDamage(final float attackPower) {
		health = Math.max(0f, health - attackPower);
	}

	/**
	 * Every round, the Tower heals a little bit (but never more than its base health).
	 */
	public void regainHealth(final long dt) {
		health = Math.min(BASE_HEALTH, health + REGEN_RATE * dt);
	}
	
	/**
	 * After an enemy captures this Tower, it will immediately
	 * gain half its base health.
	 */
	public void resetHealth() {
		health = BASE_HEALTH / 2;
	}
	
	public void adoptNewPlayer(final Player p) {
		if(player != null) {
			player.removeTower(this);
		}
		p.addTower(this);
		this.player = p;
	}

	public void setAdjacentTo(final Tower t) {
		adjacents.add(t);
		t.adjacents.add(this);
	}

	public Set<Tower> getAdjacents() {
		return adjacents;
	}

	/**
	 * Returns whether or not there is an AdjacentObject<T> in the
	 * passed list that is adjacent to this AdjacenctObject<T>.
	 */
	public boolean adjacentTo(final List<Tower> towers) {
		for(final Tower t : towers) {
			if(adjacents.contains(t)) {
				return true;
			}
		}
		return false;
	}
	
	public void preRender(final CanvasRenderer renderer, final float alpha) {
		final ScaleVec spawnScale = new ScaleVec(Tower.SPAWN_RADIUS);
		renderer.draw(CIRCLE, position, spawnScale, getRenderColor().withAlpha(alpha * 3f /4));
		renderer.draw(CIRCLE, position, spawnScale.scale(0.9f), Color.BLACK);
	}
	
	public void postRender(final CanvasRenderer renderer, final float alpha, final float orientation) {
		final float orienCos = (float)Math.cos(Math.PI*orientation/360f);
		final ScaleVec adjustedScale = Tower.SCALE.scale(1 - (orienCos * orienCos * orienCos * orienCos)/2);
		renderer.draw(SQUARE, position, adjustedScale, orientation, getColor().withAlpha(alpha));
		renderer.draw(SQUARE, position, adjustedScale.scale(0.5f), orientation, Color.BLACK);
	}

}
