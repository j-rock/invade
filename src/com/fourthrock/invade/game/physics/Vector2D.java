package com.fourthrock.invade.game.physics;

import com.fourthrock.invade.draw.PixelScreen2D;
import com.fourthrock.invade.draw.RenderScreen2D;

/**
 * Simple 2D vector class.
 * @author Joseph
 *
 */
public class Vector2D {
	public static final Vector2D ZERO = new Vector2D(0f, 0f);
	public float x, y;
	
	public Vector2D(final float x, final float y) {
		this.x = floatEquals(0f, x) ? 0f : x;
		this.y = floatEquals(0f, y) ? 0f : y;
	}
	
	/**
	 * Returns a Vector2D with the same direction
	 * but a magnitude of unit length.
	 */
	public Vector2D unitize() {
		final float mag = magnitude();
		if(floatEquals(0f, mag)) {
			return ZERO;
		} else {
			return this.scale(1/mag);
		}
	}

	/**
	 * Returns a Vector2D with the same direction
	 * but scaled to have an "f" times longer
	 * magnitude.
	 */
	public Vector2D scale(final float f) {
		return new Vector2D(f * x, f * y);
	}
	
	/**
	 * Returns the Vector2D subtraction
	 * of this minus that.
	 */
	public Vector2D minus(final Vector2D that) {
		return new Vector2D(x - that.x, y - that.y);
	}

	/**
	 * Returns the Vector2D addition
	 * of this plus that.
	 */
	public Vector2D add(final Vector2D that) {
		return new Vector2D(x + that.x, y + that.y);
	}
	
	/**
	 * Returns a Vector2D with a perpendicular direction
	 * and the same magnitude.
	 */
	public Vector2D perpendicular() {
		return new Vector2D(-y, x);
	}

	/**
	 * Returns the square of the magnitude of this Vector2D.
	 */
	public float sqrMagnitude() {
		return (x*x) + (y*y);
	}
	
	/**
	 * Returns the magnitude of this Vector2D.
	 */
	public float magnitude() {
		return (float)Math.sqrt(sqrMagnitude());
	}
	
	/**
	 * Returns the dot product of this Vector2D
	 * with that Vector2D.
	 */
	public float dot(final Vector2D that) {
		return x*that.x + y*that.y;
	}

	/**
	 * Returns whether or not this Vector2D points
	 * in a direction more than 90 degrees apart
	 * from that Vector2D.
	 */
	public boolean antiparallel(final Vector2D that) {
		return this.dot(that) < 0f;
	}

	public PixelScreen2D asPixelScreen2D() {
		return new PixelScreen2D(x, y);
	}
	
	public Position2D asPosition() {
		return new Position2D(x, y);
	}

	public RenderScreen2D asRenderScreen2D() {
		return new RenderScreen2D(x, y);
	}

	/**
	 * Returns the number of degrees between this Vector2D
	 * and the positive x-axis, positive values indicating
	 * counter-clockwise rotation.
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
}
