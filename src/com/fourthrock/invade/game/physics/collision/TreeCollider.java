package com.fourthrock.invade.game.physics.collision;

import gnu.trove.TIntProcedure;

import java.util.ArrayList;
import java.util.List;

import com.fourthrock.invade.game.physics.Position2D;
import com.fourthrock.invade.game.tower.Tower;
import com.fourthrock.invade.game.unit.PlayerUnit;
import com.fourthrock.invade.util.ObjectPool;
import com.infomatiq.jsi.Point;
import com.infomatiq.jsi.Rectangle;
import com.infomatiq.jsi.SpatialIndex;
import com.infomatiq.jsi.rtree.RTree;

/**
 * Uses the Java Spatial Index R-Tree package
 * to determine collisions between PlayerUnits
 * and Towers.
 * 
 * @author Joseph
 *
 */
public class TreeCollider<StaticCircle extends ColoredCircle> {
	private final CollisionCollection colls;
	private final List<StaticCircle> staticObjs;
	private final SpatialIndex staticIndex;
	
	public TreeCollider(final List<StaticCircle> allTowers) {
		this.colls = new CollisionCollection();
		this.staticObjs = allTowers;
		this.staticIndex = new RTree();
		staticIndex.init(null);
		
		for(final StaticCircle s : staticObjs) {
			placeCircle(staticIndex, s);
		}
	}
	
	/**
	 * Returns a CollisionCollection for all of the collisions registered.
	 * Note that for every colliding objects A and B,
	 * there will be two collisions processed
	 * (not necessarily added to the collection):
	 * 		A on B and B on A.
	 * 
	 * Assumes this TreeCollider<T> is a TreeCollider<Tower>
	 */
	public CollisionCollection withdrawCollisions(final ObjectPool<PlayerUnit> allUnits) {
		final SpatialIndex unitIndex = new RTree();
		unitIndex.init(null);
		for(final PlayerUnit u : allUnits) {
			placeCircle(unitIndex, u);
		}

		
		colls.clear();
		for(final PlayerUnit u : allUnits) {
			final List<StaticCircle> maybeCollidingStatics = findNearByStatics(u, staticIndex, staticObjs);
			final List<PlayerUnit> maybeCollidingUnits = findNearByPlayerUnits(u, unitIndex, allUnits);
			
			for(final StaticCircle t : maybeCollidingStatics) {
				if(withinActiveRange(u, t)) {
					colls.addAttackTowerCollision(u, (Tower)t);
				}
				if(withinPhysicalRange(u, t)) {
					colls.addMoveBackCollision(u, (Tower)t);
				}
			}
			
			for(final PlayerUnit u2 : maybeCollidingUnits) {
				if(withinActiveRange(u, u2)) {
					colls.addAttackUnitCollision(u, u2);
				}
				if(withinPhysicalRange(u, u2)) {
					colls.addMoveBackCollision(u, u2);
				}
			}
		}
		return colls;
	}
	
	public StaticCircle findStaticCollision(final Position2D pos) {
		final ColoredCircle testPoint = new ColoredPoint(pos);
		final List<StaticCircle> staticColls = findNearByStatics(testPoint, staticIndex, staticObjs);
		if(staticColls.isEmpty()) {
			return null;
		} else {
			return staticColls.get(0);
		}
	}
	
	private static <StaticCircle extends ColoredCircle> List<StaticCircle> findNearByStatics(final ColoredCircle u, final SpatialIndex index, final List<StaticCircle> allStatics) {
		final List<Integer> ids = findNearByIDs(u, index);
		final List<StaticCircle> nearStatics = new ArrayList<>();
		for(final Integer i : ids) {
			nearStatics.add(allStatics.get(i));
		}
		return nearStatics;
	}
	
	/*
	 * If I used parametric polymorphism, it would
	 * involve turning ObjectPool into a List<T>
	 */
	private static List<PlayerUnit> findNearByPlayerUnits(final ColoredCircle u, final SpatialIndex index, final ObjectPool<PlayerUnit> allUnits) {
		final List<Integer> ids = findNearByIDs(u, index);
		final List<PlayerUnit> nearUnits = new ArrayList<>();
		for(final Integer i : ids) {
			nearUnits.add(allUnits.get(i));
		}
		return nearUnits;
	}
	
	private static List<Integer> findNearByIDs(final ColoredCircle u, final SpatialIndex index) {
		final Position2D uPos = u.getPosition();
		final Point testPoint = new Point(uPos.x, uPos.y);
		final CollisionProcedure collProc = new CollisionProcedure();
		final float maxRadius = u.getPhysicalRadius();
		
		index.nearest(testPoint, collProc, maxRadius);
		return collProc.ids;
	}
	
	/**
	 * Checks if the two circle's physical radii
	 * are intersecting. Two circles of the same
	 * color can be too close.
	 */
	private static boolean withinPhysicalRange(final ColoredCircle c0, final ColoredCircle c1) {
		return circlesWithinLength(c0, c1, c0.getPhysicalRadius() + c1.getPhysicalRadius());
	}
	
	/**
	 * Checks if the first circle's active radius
	 * intersects the second circle's physical radius.
	 * Only circles of different colors can meaningfully
	 * be within an active range.
	 */
	private static boolean withinActiveRange(final ColoredCircle c0, final ColoredCircle c1) {
		return    !c0.getColor().equals(c1.getColor())
			   && circlesWithinLength(c0, c1, c0.getActiveRadius() + c1.getPhysicalRadius());
	}
	
	private static boolean circlesWithinLength(final ColoredCircle c0, final ColoredCircle c1, final float length) {
		if(c0 == c1) {
			return false;
		}
		final Position2D p0 = c0.getPosition();
		final Position2D p1 = c1.getPosition();
		
		final float sqrDist = p0.minus(p1).sqrMagnitude();
		return sqrDist <= (length * length);
	}

	private static void placeCircle(final SpatialIndex spatialIndex, final ColoredCircle c) {
		final Rectangle r = makeRectangle(c.getPosition(), c.getActiveRadius());
		final int circleIndex = spatialIndex.size();
		spatialIndex.add(r, circleIndex);
	}
	
	private static Rectangle makeRectangle(final Position2D position, final float maxRadius) {
		return new Rectangle(
			position.x - maxRadius,
			position.y - maxRadius,
			position.x + maxRadius,
			position.y + maxRadius
		);
	}
	
	private static class CollisionProcedure implements TIntProcedure {
		public final List<Integer> ids;
		
		public CollisionProcedure() {
			ids = new ArrayList<>();
		}
		
		public boolean execute(final int id) {
			ids.add(id);
			return true;
		}
	}
}
