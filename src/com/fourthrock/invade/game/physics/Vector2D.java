package com.fourthrock.invade.game.physics;

import com.fourthrock.invade.draw.Screen2D;

/**
 * Simple 2D vector class.
 * @author Joseph
 *
 */
public class Vector2D {
	public final float x, y;
	
	public Vector2D(final float x, final float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2D unitize() {
		final float mag = (float)Math.sqrt(x*x + y*y);
		return new Vector2D(x/mag, y/mag);
	}

	public Vector2D scale(final float f) {
		return new Vector2D(f * x, f * y);
	}
	
	public Vector2D minus(final Vector2D that) {
		return new Vector2D(x - that.x, y - that.y);
	}

	public Vector2D add(final Vector2D that) {
		return new Vector2D(x + that.x, y + that.y);
	}
	
	public Position2D asPosition() {
		return new Position2D(x, y);
	}

	public float sqrMagnitude() {
		return (x*x) + (y*y);
	}

	public Screen2D asScreen2D() {
		return new Screen2D(x, y);
	}
}
