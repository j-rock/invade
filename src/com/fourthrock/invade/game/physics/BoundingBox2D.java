package com.fourthrock.invade.game.physics;



/**
 * A class to represent a 2D bounding box in world space.
 * @author Joseph
 *
 */
public class BoundingBox2D {
	public static final BoundingBox2D UNBOUNDED =
			new BoundingBox2D(new Position2D(Float.MIN_VALUE, Float.MIN_VALUE),
							  new Position2D(Float.MAX_VALUE, Float.MAX_VALUE));

	public static final BoundingBox2D ORIGIN_POINT =
			new BoundingBox2D(new Position2D(0f, 0f), new Position2D(0f, 0f));
	
	private final Position2D bottomLeft, topRight;
	
	private BoundingBox2D(final Position2D bottomLeft, final Position2D topRight) {
		this.bottomLeft = bottomLeft;
		this.topRight = topRight;
	}
	
	/**
	 * Takes a test point and retrieves the nearest point contained
	 * within the bounding box.
	 */
	public Position2D getClosestInBounds(final Position2D p) {
		final float nearX = Math.min(topRight.x, Math.max(bottomLeft.x, p.x));
		final float nearY = Math.min(topRight.y, Math.max(bottomLeft.y, p.y));
		return new Position2D(nearX, nearY);
	}
	
	/**
	 * Tries to create the smallest BoundingBox2D that contains the point
	 * centered at Position2D and also contains this BoundingBox2D.
	 */
	public BoundingBox2D expandWith(final Position2D p, final float radius) {
		final float minRad = 1.2f * radius;
		final Position2D nextBot = new Position2D(
				Math.min(bottomLeft.x, p.x - minRad),
				Math.min(bottomLeft.y, p.y - minRad)
		);
		
		final Position2D nextTop = new Position2D(
				Math.max(topRight.x, p.x + minRad),
				Math.max(topRight.y, p.y + minRad)
		);
		
		return new BoundingBox2D(nextBot, nextTop);
	}

	/**
	 * Try to make a BoundingCircle to represent this box (even if crudely).
	 */
	public BoundingCircle2D toCircleBounds() {
		final Position2D center = (bottomLeft.add(topRight)).scale(0.5f).asPosition();
		final float radius = Math.max(topRight.x - bottomLeft.x, topRight.y - bottomLeft.y)/2;
		return new BoundingCircle2D(center, radius);
	}
}
