package com.fourthrock.invade.draw;

import java.util.ArrayList;
import java.util.List;

/**
 * A class to represent RGBA colors.
 * 
 * @author Joseph
 *
 */
public class Color {
	public static final Color BLACK  = new Color(    0f,       0f,      0f, 1f);
	public static final Color SLATE  = new Color( 0.05f,    0.05f,   0.05f, 1f);
	public static final Color WHITE  = new Color(    1f,       1f,      1f, 1f);
	public static final Color SNOW   = new Color( 0.95f,    0.96f,  0.965f, 1f);
	public static final Color GREEN  = new Color(    0f, 170/255f, 85/255f, 1f);
	public static final Color RED    = new Color(0.839f,   0.117f,  0.323f, 1f);
	public static final Color BLUE   = new Color(    0f, 190/255f,      1f, 1f);
	public static final Color PURPLE = new Color(  0.6f,       0f,    0.8f, 1f);
	public static final Color YELLOW = new Color(    1f, 190/255f,      0f, 1f);
	
	public static final Color[] LIGHT_COLORS = new Color[]{
		WHITE, SNOW, GREEN, RED, BLUE, PURPLE, YELLOW
	};
	
	public static List<Color> allPlayableColors() {
		final List<Color> cs = new ArrayList<>(5);
		cs.add(GREEN);
		cs.add(RED);
		cs.add(BLUE);
		cs.add(PURPLE);
		cs.add(YELLOW);
		return cs;
	}
	
	public final float r, g, b, a;
	
	public Color(final float r, final float g, final float b, final float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}

	public float[] toFloatArray() {
		return new float[]{r, g, b, a};
	}

	public Color blend(final Color that) {
		final float  m =  (1f - a) * that.a;
		final float rN = a * r + m * that.r;
		final float gN = a * g + m * that.g;
		final float bN = a * b + m * that.b;
		final float aN = Math.min(1f, a + that.a);
		return new Color(rN, gN, bN, aN);
	}
	
	@Override
	public boolean equals(final Object that) {
		if (that == null || !(that instanceof Color)) {
			return false;
		}
		if(this == that) {
			return true;
		}
		
		final Color c = (Color)that;
		return this.r == c.r
			&& this.g == c.g
			&& this.b == c.b
			&& this.a == c.a;
	}

	public Color withAlpha(float alpha) {
		return new Color(r, g, b, alpha);
	}
}
