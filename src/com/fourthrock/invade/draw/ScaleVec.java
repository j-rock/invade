package com.fourthrock.invade.draw;

/**
 * Represents a vector by which to scale
 * OpenGL drawables. Specifically created to reject
 * type errors caused by passing in incorrect vectors.
 * 
 * @author Joseph
 *
 */
public class ScaleVec {
	public static final ScaleVec UNIT = new ScaleVec(1f, 1f, 1f);
	
	public final float sx, sy, sz;
	
	public ScaleVec(final float x, final float y, final float z) {
		this.sx = x;
		this.sy = y;
		this.sz = z;
	}
	
	public ScaleVec scale(final float k) {
		return new ScaleVec(k*sx, k*sy, k*sz);
	}
	
}
