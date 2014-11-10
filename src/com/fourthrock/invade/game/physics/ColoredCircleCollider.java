package com.fourthrock.invade.game.physics;

import java.util.PriorityQueue;

/**
 * This class will determine physics collisions between ColoredCircles
 * so we can process the events afterward.
 * 
 * A collision is only triggered if the circle colors are different.
 * 
 * @author Joseph
 *
 */
public class ColoredCircleCollider {
	private final BoundingBox2D bounds;
	private final float bucketSize;
	private final CircleBucket[][] buckets;
	
	public ColoredCircleCollider(final BoundingBox2D worldBounds, final float maxCircleRadius) {
		this.bounds = worldBounds;
		this.bucketSize = maxCircleRadius;
		final int xBuckets = (int) Math.ceil(bounds.getWidth() / bucketSize);
		final int yBuckets = (int) Math.ceil(bounds.getHeight() / bucketSize);
		buckets = new CircleBucket[xBuckets][yBuckets];
		for(int x=0; x<buckets.length; x++){
			for(int y=0; y<buckets[x].length; y++) {
				buckets[x][y] = new CircleBucket();
			}
		}
	}

	
	/**
	 * Keeps track of the colored circle in the internal spatial structure.
	 * @param c
	 */
	public void placeCircle(final ColoredCircle c) {
		final Position2D p = c.getPosition();
		final int xBucket = (int) Math.floor((p.x - bounds.getMinX()) / bucketSize);
		final int yBucket = (int) Math.floor((p.y - bounds.getMinY()) / bucketSize);
		buckets[xBucket][yBucket].add(c);
	}
	
	/**
	 * Returns an ordered queue of collisions.
	 * 
	 * First:
	 * 	 Unit-Tower collisions where the colors are different
	 * 
	 * Second:
	 * 	 Unit-Unit collisions where the 
	 * 	
	 * @return
	 */
	public PriorityQueue<Collision> getCollisions() {
		
		
		
		
		clear();
		return null;
	}
	
	private void clear() {
		for(int x=0; x<buckets.length; x++) {
			for(int y=0; y<buckets[x].length; y++) {
				buckets[x][y].clear();
			}
		}
	}
}
