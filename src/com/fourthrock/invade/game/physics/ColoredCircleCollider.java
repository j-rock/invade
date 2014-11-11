package com.fourthrock.invade.game.physics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.util.Pair;

import com.fourthrock.invade.draw.Color;
import com.fourthrock.invade.game.tower.Tower;
import com.fourthrock.invade.game.unit.PlayerUnit;

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
	
	public ColoredCircleCollider(final BoundingBox2D worldBounds, final float maxCircleRadius) {
		this.bounds = worldBounds;
		this.bucketSize = maxCircleRadius;
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
	 * @param c
	 */
	public void placeCircle(final ColoredCircle c) {
		final Index2D b = getBucketIndexFor(c.getPosition());
		buckets[b.x][b.y].add(c);
	}

	/**
	 * Assuming that Towers have been placed into the Collider,
	 * this method tries to find any Tower that does not have
	 * color playerColor and is within range of target
	 */
	public Tower findTower(final Color playerColor, final Position2D target) {
		final Index2D b = getBucketIndexFor(target);
		final List<Index2D> neighbors = getBucketNeighbors(b.x, b.y);
		
		final ColoredCircle c = new ColoredPoint(playerColor, target);
		final Set<TowerCollision> towerColls = findCollisions(c, neighbors).first;
		for(final TowerCollision tc : towerColls) {
			return tc.t;
		}
		return null;
	}
	
	/**
	 * Returns a pair of lists of collisions and resets the internal spatial structure.
	 * 
	 * First list:
	 * 	 Unit-Tower collisions
	 * 
	 * Second list:
	 * 	 Unit-Unit collisions
	 */
	public Pair<Set<TowerCollision>, Set<UnitCollision>> withdrawCollisions() {
		final Set<TowerCollision> towerColls = new HashSet<>();
		final Set<UnitCollision> unitColls = new HashSet<>();
		
		for(int x=0; x<buckets.length; x++){
			for(int y=0; y<buckets[x].length; y++) {
				final ColoredCircleBucket bucket = buckets[x][y];
				for(final ColoredCircle c : bucket) {
					final List<Index2D> bucketNeighborIndices = getBucketNeighbors(x, y);
					final Pair<Set<TowerCollision>, Set<UnitCollision>> colls = findCollisions(c, bucketNeighborIndices);
					towerColls.addAll(colls.first);
					unitColls.addAll(colls.second);
				}
			}
		}
		clear();
		return new Pair<>(towerColls, unitColls);
	}

	private Pair<Set<TowerCollision>, Set<UnitCollision>> findCollisions(final ColoredCircle c, final List<Index2D> neighbors) {
		final Pair<Set<TowerCollision>, Set<UnitCollision>> colls =
				new Pair<Set<TowerCollision>, Set<UnitCollision>>(new HashSet<TowerCollision>(),
																  new HashSet<UnitCollision>());
		
		for(final Index2D b : neighbors) {
			for(final ColoredCircle c2 : buckets[b.x][b.y]) {
				final Pair<TowerCollision, UnitCollision> maybeColl = tryMakeCollision(c, c2);
				if(maybeColl != null) {
					if(maybeColl.first != null) {
						colls.first.add(maybeColl.first);
					} else {
						colls.second.add(maybeColl.second);
					}
				}
			}
		}
		return colls;
	}
	
	public Pair<TowerCollision, UnitCollision> tryMakeCollision(final ColoredCircle c, final ColoredCircle c2) {
		return (!circlesCollide(c, c2))
				? null
				: new Pair<>(makeTowerCollision(c, c2), makeUnitCollision(c, c2));
	}


	/*
	 * Circles are assumed to overlap and be different colors
	 */
	private UnitCollision makeUnitCollision(final ColoredCircle c, final ColoredCircle c2) {
		if(c instanceof PlayerUnit && c2 instanceof PlayerUnit) {
			return new UnitCollision((PlayerUnit)c, (PlayerUnit)c2);
		}
		return null;
	}

	private TowerCollision makeTowerCollision(final ColoredCircle c, final ColoredCircle c2) {
		if(c instanceof Tower && c2 instanceof PlayerUnit) {
			return new TowerCollision((Tower)c, (PlayerUnit)c2);
		} else if (c2 instanceof Tower && c instanceof PlayerUnit) {
			return new TowerCollision((Tower)c2, (PlayerUnit)c);
		} else {
			return null;
		}
	}

	private boolean circlesCollide(final ColoredCircle c0, final ColoredCircle c1) {
		if(c0.getColor().equals(c1.getColor())) return false;
		
		final Position2D p0 = c0.getPosition();
		final Position2D p1 = c1.getPosition();
		
		final float dx = p0.x - p1.x;
		final float dy = p0.y - p1.y;
		final float sqrDist = dx*dx + dy*dy;
		final float dR = c0.getRadius() + c1.getRadius();
		
		return sqrDist <= (dR * dR);
	}

	private Index2D getBucketIndexFor(final Position2D p) {
		final int x = (int) Math.floor((p.x - bounds.getMinX()) / bucketSize);
		final int y = (int) Math.floor((p.y - bounds.getMinY()) / bucketSize);
		return new Index2D(x, y);
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
	
	private static class Index2D {
		public final int x,y;
		
		public Index2D(final int x, final int y) {
			this.x = x;
			this.y = y;
		}
	}
}
