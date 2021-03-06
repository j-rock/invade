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
	
	public final BoundingCircle2D scale(final float k) {
		return new BoundingCircle2D(center, k*radius);
	}

	public Position2D getClosestInBounds(final Position2D p) {
		final float sqrDistance = center.minus(p).sqrMagnitude();
		return sqrDistance <= (radius*radius) ? p : center.nearestOnCircle(radius, p);
	}
	
	public float getRadius() {
		return radius;
	}
	
	public Position2D getCenter() {
		return center;
	}

}
