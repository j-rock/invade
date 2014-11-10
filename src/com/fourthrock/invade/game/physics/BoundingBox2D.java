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
	
	private final Position2D bottomLeft, topRight;
	
	public BoundingBox2D(final Position2D bottomLeft, final Position2D topRight) {
		this.bottomLeft = bottomLeft;
		this.topRight = topRight;
	}
	
	/**
	 * Takes a test point and retrieves the nearest point contained
	 * within the bounding box.
	 */
	public Position2D adjustToFit(final float x, final float y) {
		final float nearX = Math.min(topRight.x, Math.max(bottomLeft.x, x));
		final float nearY = Math.min(topRight.y, Math.max(bottomLeft.y, y));
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
}
