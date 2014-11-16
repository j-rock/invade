package com.fourthrock.invade.game.physics;

import com.fourthrock.invade.draw.Screen2D;

/**
 * Simple 2D vector class.
 * @author Joseph
 *
 */
public class Vector2D {
	public static final Vector2D ZERO = new Vector2D(0f, 0f);
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
	
	public float magnitude() {
		return (float)Math.sqrt(sqrMagnitude());
	}

	public Screen2D asScreen2D() {
		return new Screen2D(x, y);
	}

	/**
	 * Returns the number of degrees between this Vector2D
	 * and the positive x-axis, assuming the Vector2D
	 * was rotated counter-clockwise.
	 */
	public float theta() {
		if (floatEquals(x,  0f)) {
			return Math.signum(y) * 90f;
		} else {
			final float radAngle = (float) Math.atan(y / x);
			return (float) ((-180f / Math.PI) * radAngle);
		}
	}
	
	@Override
	public boolean equals(final Object that) {
		if(that == null || !(that instanceof Vector2D)) {
			return false;
		}
		final Vector2D v = (Vector2D)that;
		return floatEquals(x, v.x) && floatEquals(y, v.y);
	}
	
	private static boolean floatEquals(final float d1, final float d2) {
		return Math.abs(d1 - d2) <= 1e-12f;
	}

	public boolean antiparallel(final Vector2D that) {
		return this.dot(that) < 0f;
	}

	private float dot(final Vector2D that) {
		return x*that.x + y*that.y;
	}
}
