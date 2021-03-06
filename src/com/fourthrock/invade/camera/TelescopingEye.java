package com.fourthrock.invade.camera;

import com.fourthrock.invade.game.physics.BoundingCircle2D;
import com.fourthrock.invade.game.physics.Position2D;
import com.fourthrock.invade.game.physics.Vector2D;

/**
 * A class to represent a camera in a 2D plane that can move smoothly.
 * 
 * @author Joseph
 *
 */
public class TelescopingEye {
	private final float minZoom, maxZoom;
	private float zoom;
	
	private Position2D position;
	private Vector2D velocity;
	private Vector2D acceleration;
	private final BoundingCircle2D bounds;
	

	public TelescopingEye(final float minZoom, final float maxZoom, final Position2D startPos, final BoundingCircle2D bounds) {
		this.minZoom = minZoom;
		this.maxZoom = maxZoom;
		this.zoom = maxZoom;
		
		this.bounds = bounds;
		adjustPosition(startPos);
		stopMoving();
	}

	public Position2D getPosition() {
		return position;
	}
	
	public float getZoom() {
		return zoom;
	}
	
	/**
	 * Zeros out the velocity and acceleration.
	 */
	public void stopMoving() {
		velocity     = Vector2D.ZERO;
		acceleration = Vector2D.ZERO;
	}
	
	/**
	 * Moves the eye by dt milliseconds.
	 */
	public void step(final long dt) {
		
		// p_t = p_0 + v*dt
		adjustPosition(position.add(velocity.scale(dt)).asPosition());
		
		// v = v0 - a*dt
		final Vector2D nextVelocity = velocity.minus(acceleration.scale(dt));
		if(   nextVelocity.sqrMagnitude() <= 1e-15f  // velocity has diminished to epsilon
		   || velocity.antiparallel(nextVelocity)) { // we decelerated to the point of going in the opposite direction.
			stopMoving();
		} else {
			velocity = nextVelocity;
		}
	}

	/*
	 * Tries to move the camera to the passed in position,
	 * getting as close to within bounds as possible.
	 */
	public void adjustPosition(final Position2D target) {
		position = bounds.getClosestInBounds(target);
	}

	public void zoomTo(final float scaleFactor) {
		final float sqrScaleFactor = scaleFactor * scaleFactor;
		zoom = Math.max(minZoom, Math.min(maxZoom, zoom * sqrScaleFactor * sqrScaleFactor));
	}

	/**
	 * Sets the camera velocity and acceleration
	 * such that the camera can move for a second.
	 */
	public void setMoving(final Vector2D eyeVelocity) {
		velocity = eyeVelocity;
		final float timeMovingMillis = 300f;
		acceleration = velocity.scale(1/timeMovingMillis);
	}

	public void displace(final Vector2D d) {
		adjustPosition(position.minus(d).asPosition());
	}
}
