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
	
	public BoundingBox2D(final Position2D bottomLeft, final Position2D topRight) {
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
	 * Returns the distance between the left and right walls
	 */
	public float getWidth() {
		return topRight.x - bottomLeft.x;
	}
	
	/**
	 * Returns the distance between the bottom and top walls
	 */
	public float getHeight() {
		return topRight.y - bottomLeft.y;
	}

	public float getMinX() {
		return bottomLeft.x;
	}
	
	public float getMinY() {
		return bottomLeft.y;
	}
	
	public BoundingBox2D expandWith(final Position2D p) {
		final Position2D nextBot = new Position2D(
				Math.min(bottomLeft.x, p.x),
				Math.min(bottomLeft.y, p.y)
		);
		
		final Position2D nextTop = new Position2D(
				Math.max(topRight.x, p.x),
				Math.max(topRight.y, p.y)
		);
		
		return new BoundingBox2D(nextBot, nextTop);
	}

	public BoundingCircle2D toCircleBounds() {
		final Position2D center = (bottomLeft.add(topRight)).scale(0.5f).asPosition();
		final float radius = Math.max(getWidth(), getHeight());
		return new BoundingCircle2D(center, radius);
	}
}
