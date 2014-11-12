package com.fourthrock.invade.game.physics;

/**
 * Represents a 2D position in world space
 * 
 * Unlike an image, larger y-values are supposed to be "higher"
 * 
 * @author Joseph
 *
 */
public class Position2D extends Vector2D {

	public Position2D(final float x, final float y) {
		super(x, y);
	}

	/**
	 * If this Position2D represents the center of a circle
	 * with the given radius, this method returns a random
	 * Position2D on the circumference on the circle.
	 */
	public Position2D randomPositionOnCircle(final float radius) {
		final float angle = (float)(Math.random() * 2 * Math.PI);
		final Vector2D dir = new Vector2D(
				(float)Math.cos(angle),
				(float)Math.sin(angle)
		);
		return lerp(radius, dir);
	}
	
	/**
	 * If this Position2D represents the center of a circle,
	 * this method takes another Position2D p and a radius
	 * and computes the point closest to p on the circumference
	 * of this circle.
	 */
	public Position2D nearestOnCircle(final float radius, final Position2D p) {
		final Vector2D dir = p.minus(this).unitize();
		return lerp(radius, dir);
	}
	
	/**
	 * If this Position2D is (x,y), this method returns
	 * (x + k*dx, y + k*dy)
	 */
	private Position2D lerp(final float k, final Vector2D d) {
		return this.add(d.scale(k)).asPosition();
	}

}
