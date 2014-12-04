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
import com.fourthrock.invade.game.physics.Vector2D;
import com.fourthrock.invade.game.physics.collision.ColoredCircle;
import com.fourthrock.invade.game.player.Player;
import com.fourthrock.invade.game.unit.PlayerUnit;

/**
 * Represents a tower that can be captured by a PlayerUnit.
 * @author Joseph
 *
 */
public class Tower implements ColoredCircle {
	private static final float BASE_HEALTH = 1000f;
	public static final float REGEN_RATE = BASE_HEALTH / (90 * 1000); // takes 90 seconds to fully heal
	public static final float BORDER_RADIUS = 2.025f;
	public static final float SPAWN_RADIUS = 2f * BORDER_RADIUS;

	private final Set<Tower> adjacents;
	private final Position2D position;
	private final float orientation;
	private Player player;
	protected float health;
	
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
	
	public int getUnitCapacity() {
		return 15;
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
	
	public int getAttackLevelBuff() {
		return 0;
	}
	
	public int getHealthLevelBuff() {
		return 0;
	}
	
	public int getSpeedLevelBuff() {
		return 0;
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
		preRender(renderer, position, getActiveRadius(), getRenderColor().withAlpha(alpha * 3f / 4));
	}
	
	public void postRender(final CanvasRenderer renderer, final float alpha, final float orientation) {
		postRender(renderer, position, getPhysicalRadius(), orientation, getColor().withAlpha(alpha));
	}
	
	public static void preRender(final CanvasRenderer renderer, final Position2D position, final float radius, final Color rimColor) {
		final ScaleVec spawnScale = new ScaleVec(radius);
		renderer.draw(CIRCLE, position, spawnScale, rimColor);
		renderer.draw(CIRCLE, position, spawnScale.scale(0.9f), Color.BLACK);
	}
	
	public static void postRender(final CanvasRenderer renderer, final Position2D position, final float radius,
			final float orientation, final Color centerColor) {

		final ScaleVec maxScale = new ScaleVec(radius * (float)Math.sqrt(2));
		final ScaleVec adjustedScale = maxScale.scale(scaleOfPhase(orientation));
		renderer.draw(SQUARE, position, adjustedScale, orientation, centerColor);
		renderer.draw(SQUARE, position, adjustedScale.scale(0.5f), orientation, Color.BLACK);
	}
	
	public static void drawLine(final CanvasRenderer renderer, final Position2D s, final float sRad,
			final Position2D t, final float tRad, final float phase, final Color color) {
		
		// Endpoints of the line (points on circumference of Towers)
		final Position2D sPrime = t.nearestOnCircle(tRad, s);
		final Position2D tPrime = s.nearestOnCircle(sRad, t);
		
		final Vector2D displacement = tPrime.minus(sPrime);
		final Position2D midpoint = sPrime.add(tPrime).scale(0.5f).asPosition();
		final float length = displacement.magnitude();
		final float height = PlayerUnit.BORDER_RADIUS * (float)(Math.abs(2 * Math.cos(phase)));
		
		renderer.draw(SQUARE, midpoint, new ScaleVec(length, height, 1f), displacement.theta(), color);
	}
	
	public static float scaleOfPhase(final float phase) {
		final float cosPhase = (float)Math.cos(Math.PI*phase/360f);
		return 1f - (cosPhase * cosPhase * cosPhase * cosPhase) / 2;
	}
}
