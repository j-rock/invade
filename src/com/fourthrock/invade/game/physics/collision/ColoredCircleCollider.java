package com.fourthrock.invade.game.physics.collision;

import java.util.ArrayList;
import java.util.List;

import com.fourthrock.invade.game.physics.BoundingBox2D;
import com.fourthrock.invade.game.physics.Position2D;
import com.fourthrock.invade.game.tower.Tower;
import com.fourthrock.invade.game.unit.PlayerUnit;
import com.fourthrock.invade.util.Index2D;

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
	private final ColoredCircleBucket[][] buckets;
	
	public ColoredCircleCollider(final BoundingBox2D worldBounds) {
		this.bounds = worldBounds;
		this.bucketSize = Tower.SPAWN_RADIUS;
		final int xBuckets = (int) Math.ceil(bounds.getWidth() / bucketSize);
		final int yBuckets = (int) Math.ceil(bounds.getHeight() / bucketSize);
		buckets = new ColoredCircleBucket[xBuckets][yBuckets];
		for(int x=0; x<buckets.length; x++){
			for(int y=0; y<buckets[x].length; y++) {
				buckets[x][y] = new ColoredCircleBucket();
			}
		}
	}

	/**
	 * Keeps track of the colored circle in the internal spatial structure.
	 */
	public void placeCircle(final ColoredCircle c) {
		final Index2D b = getBucketIndexFor(c.getPosition());
		buckets[b.x][b.y].add(c);
	}
	
	/**
	 * Returns a CollisionCollection for all of the collisions registered.
	 * Note that for every colliding objects A and B,
	 * there will be two collisions processed
	 * (not necessarily added to the collection):
	 * 		A on B and B on A.
	 */
	public CollisionCollection withdrawCollisions() {
		final CollisionCollection colls = new CollisionCollection();
		for(int x=0; x<buckets.length; x++){
			for(int y=0; y<buckets[x].length; y++) {
				final ColoredCircleBucket bucket = buckets[x][y];
				final List<Index2D> bucketNeighborIndices = getBucketNeighbors(x, y);
				for(final ColoredCircle c : bucket) {
					colls.addAll(findCollisions(c, bucketNeighborIndices));
				}
			}
		}
		clear();
		return colls;
	}

	private List<Collision> findCollisions(final ColoredCircle c, final List<Index2D> neighbors) {
		final List<Collision> colls = new ArrayList<Collision>();
		for(final Index2D b : neighbors) {
			for(final ColoredCircle c2 : buckets[b.x][b.y]) {
				colls.addAll(makeCollisions(c, c2));
			}
		}
		return colls;
	}
	
	/**
	 * If the circles collide meaningfully, it will try to 
	 * construct a Collision object, or return null as a failure.
	 * 
	 * Tower x _ => No collisions
	 * Unit x Tower =>
	 * 		1. If unit is too close: move back
	 * 		2. If different colors, attack Tower
	 * Unit x Unit2 =>
	 * 		1. If Unit1 too close, move it back
	 * 		2. If different colors, attack Unit2
	 * 
	 */
	private List<Collision> makeCollisions(final ColoredCircle c, final ColoredCircle c2) {
		final List<Collision> colls = new ArrayList<>();
		if(c instanceof PlayerUnit) {
			if(tooCloseTo(c, c2)) {
				if(c2 instanceof Tower) {
					colls.add(new MoveBackCollision((PlayerUnit)c, (Tower)c2));
				} else {
					colls.add(new MoveBackCollision((PlayerUnit)c, (PlayerUnit)c2));
				}
			}
			if(withinActiveRange(c, c2)) {
				if(c2 instanceof Tower) {
					colls.add(new AttackTowerCollision((PlayerUnit)c, (Tower)c2));
				} else {
					colls.add(new AttackUnitCollision((PlayerUnit)c, (PlayerUnit)c2));
				}
			}
		}
		return colls;
	}

	
	
	/**
	 * Converts a world space Position2D
	 * into the bucket that keeps track of those positions.
	 */
	private Index2D getBucketIndexFor(final Position2D p) {
		final int x = (int) Math.floor((p.x - bounds.getMinX()) / bucketSize);
		final int y = (int) Math.floor((p.y - bounds.getMinY()) / bucketSize);
		
		final int xI = Math.max(0, Math.min(x, buckets.length - 1));
		final int yI = Math.max(0, Math.min(y, buckets[xI].length - 1));
		
		return new Index2D(xI, yI);
	}
	
	/**
	 * Returns the indices for all 8 adjacent buckets to buckets[x][y],
	 * and also includes the index [x][y].
	 */
	private List<Index2D> getBucketNeighbors(final int x, final int y) {
		final List<Index2D> neighbors = new ArrayList<>();
		if(inBounds(x, y)) {
			for(int i=x-1; i<=x+1; i++) {
				for(int j=y-1; j<=y+1; j++) {
					if(inBounds(i, j)) {
						neighbors.add(new Index2D(i,j));
					}
				}
			}
		}
		return neighbors;
	}
	
	private boolean inBounds(final int x, final int y) {
		return x >= 0 && x < buckets.length && y >= 0 && y < buckets[x].length;
	}
	
	private void clear() {
		for(int x=0; x<buckets.length; x++) {
			for(int y=0; y<buckets[x].length; y++) {
				buckets[x][y].clear();
			}
		}
	}
	
	/**
	 * Checks if the two circle's physical radii
	 * are intersecting. Two circles of the same
	 * color can be too close.
	 */
	private static boolean tooCloseTo(final ColoredCircle c0, final ColoredCircle c1) {
		return distWithinLength(c0, c1, c0.getPhysicalRadius() + c1.getPhysicalRadius());
	}
	
	/**
	 * Checks if the first circle's active radius
	 * intersects the second circle's physical radius.
	 * Only circles of different colors can meaningfully
	 * be within an active range.
	 */
	private static boolean withinActiveRange(final ColoredCircle c0, final ColoredCircle c1) {
		return    !c0.getColor().equals(c1.getColor())
			   && distWithinLength(c0, c1, c0.getActiveRadius() + c1.getPhysicalRadius());
	}
	
	private static boolean distWithinLength(final ColoredCircle c0, final ColoredCircle c1, final float length) {
		if(c0 == c1) {
			return false;
		}
		final Position2D p0 = c0.getPosition();
		final Position2D p1 = c1.getPosition();
		
		final float sqrDist = p0.minus(p1).sqrMagnitude();
		return sqrDist <= (length * length);
	}
}
