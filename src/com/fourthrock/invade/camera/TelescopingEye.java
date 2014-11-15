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
		setPosition(startPos);
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
		setPosition(position.add(velocity.scale(dt)).asPosition());
		
		// v = v0 - a*dt
		velocity = velocity.minus(acceleration.scale(dt));
		
		if(velocity.sqrMagnitude() <= 0.00001) {
			stopMoving();
		}
	}

	/*
	 * Tries to move the camera to the passed in position,
	 * getting as close to within bounds as possible.
	 */
	public void setPosition(final Position2D position) {
		this.position = bounds.getClosestInBounds(position);
	}

	public void zoomTo(final float scaleFactor) {
		zoom = Math.max(minZoom, Math.min(maxZoom, zoom * scaleFactor));
	}

	public void setMoving(final Vector2D eyeVelocity) {
		velocity = eyeVelocity;
		acceleration = velocity.scale(1/3000f);
	}
}
