package com.fourthrock.invade.game.physics;

import java.util.ArrayList;
import java.util.List;

import android.util.Pair;

import com.fourthrock.invade.draw.Color;
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
		this.bucketSize = Math.max(bounds.getWidth(), bounds.getHeight()) / 9;
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
	 * Since the number of towers is small and so are
	 * the number of queries to findTower (only on user taps),
	 * we do an O(n) scan and leave it at that.
	 * 
	 * I would use the more optimal findCollisions method,
	 * but this method is called in a different thread
	 * and would otherwise generate ConcurrentModificationExceptions.
	 */
	public static Tower findTower(final Color playerColor, final Position2D target, final List<Tower> towers) {
		for(final Tower t : towers) {
			if(!t.getColor().equals(playerColor)) {
				if(circlesCollide(t, new ColoredPoint(playerColor, target))) {
					return t;
				}
			}
		}
		return null;
	}
	
	/**
	 * Returns a pair of sets of collisions and resets the internal spatial structure.
	 * Note that for every colliding objects A and B, there will be two collisions registered:
	 * 		A on B and B on A.
	 * 
	 * First list:
	 * 	 Unit-Tower collisions
	 * 
	 * Second list:
	 * 	 Unit-Unit collisions
	 */
	public Pair<List<TowerCollision>, List<UnitCollision>> withdrawCollisions() {
		final List<TowerCollision> towerColls = new ArrayList<>();
		final List<UnitCollision> unitColls = new ArrayList<>();
		
		for(int x=0; x<buckets.length; x++){ // TODO - parallelize
			for(int y=0; y<buckets[x].length; y++) {
				final ColoredCircleBucket bucket = buckets[x][y];
				final List<Index2D> bucketNeighborIndices = getBucketNeighbors(x, y);
				for(final ColoredCircle c : bucket) {
					final Pair<List<TowerCollision>, List<UnitCollision>> colls = findCollisions(c, bucketNeighborIndices);
					towerColls.addAll(colls.first);
					unitColls.addAll(colls.second);
				}
			}
		}
		clear();
		return new Pair<>(towerColls, unitColls);
	}

	private Pair<List<TowerCollision>, List<UnitCollision>> findCollisions(final ColoredCircle c, final List<Index2D> neighbors) {
		final Pair<List<TowerCollision>, List<UnitCollision>> colls =
				new Pair<List<TowerCollision>, List<UnitCollision>>(new ArrayList<TowerCollision>(),
																  new ArrayList<UnitCollision>());
		
		for(final Index2D b : neighbors) {
			for(final ColoredCircle c2 : buckets[b.x][b.y]) {
				final Pair<TowerCollision, UnitCollision> maybeColl = tryMakeCollision(c, c2);
				if(maybeColl != null) {
					if(maybeColl.first != null) {
						colls.first.add(maybeColl.first);
					} else if(maybeColl.second != null) {
						colls.second.add(maybeColl.second);
					}
				}
			}
		}
		return colls;
	}
	
	/**
	 * If the circles collide, it will return a pair of the form
	 * Pair(T, null) or
	 * Pair(null, U)
	 * where T is a TowerCollision and U is a UnitCollision
	 * 
	 * otherwise it will return null
	 */
	private Pair<TowerCollision, UnitCollision> tryMakeCollision(final ColoredCircle c, final ColoredCircle c2) {
		return (!circlesCollide(c, c2))
				? null
				: new Pair<>(makeTowerCollision(c, c2), makeUnitCollision(c, c2));
	}

	/*
	 * Assuming these circles overlap, if both are PlayerUnits
	 * return a UnitCollision, else null
	 */
	private UnitCollision makeUnitCollision(final ColoredCircle c, final ColoredCircle c2) {
		if(c instanceof PlayerUnit && c2 instanceof PlayerUnit) {
			return new UnitCollision((PlayerUnit)c, (PlayerUnit)c2);
		}
		return null;
	}

	/*
	 * Assuming these circles overlap, if one is a Tower and the other a PlayerUnit
	 * return a TowerCollision, else null
	 */
	private TowerCollision makeTowerCollision(final ColoredCircle c, final ColoredCircle c2) {
		if(c instanceof Tower && c2 instanceof PlayerUnit) {
			return new TowerCollision((Tower)c, (PlayerUnit)c2);
		} else if (c2 instanceof Tower && c instanceof PlayerUnit) {
			return new TowerCollision((Tower)c2, (PlayerUnit)c);
		} else {
			return null;
		}
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
	 * Checks that the centers of each circle
	 * are closer than the sum of radii, as well
	 * as the circles having different colors.
	 * 
	 * Retroactively only care about colors
	 * if both circles are units.
	 */
	private static boolean circlesCollide(final ColoredCircle c0, final ColoredCircle c1) {
		if(!(c0 instanceof Tower || c1 instanceof Tower)) {
			if(c0.getColor().equals(c1.getColor())) {
				return false;
			}
		}
		
		final Position2D p0 = c0.getPosition();
		final Position2D p1 = c1.getPosition();
		
		final float sqrDist = p0.minus(p1).sqrMagnitude();
		final float dR = c0.getCollideRadius() + c1.getCollideRadius();
		
		return sqrDist < (dR * dR);
	}
}
