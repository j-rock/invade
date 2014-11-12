package com.fourthrock.invade.game.physics;

/**
 * Represents a circular bounds to have smooth bounds enforcement
 * @author Joseph
 *
 */
public class BoundingCircle2D {
	private final Position2D center;
	private final float radius;
	
	public BoundingCircle2D(final Position2D center, final float radius) {
		this.center = center;
		this.radius = radius;
	}

	public Position2D getClosestInBounds(final Position2D p) {
		final float sqrDistance = center.minus(p).sqrMagnitude();
		return sqrDistance <= (radius*radius) ? p : center.nearestOnCircle(radius, p);
	}

}
