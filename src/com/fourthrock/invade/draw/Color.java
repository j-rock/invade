package com.fourthrock.invade.draw;


public class Color {

	public static final Color WHITE = new Color(1f, 1f, 1f, 1f);
	public static final Color RED = new Color(1f, 0f, 0f, 1f);
	public static final Color GREEN = new Color(0f, 1f, 0f, 1f);
	public static final Color BLUE = new Color(0f, 0f, 1f, 1f);
	public static final Color ORANGE = new Color(1f, 0.4f, 0f, 1f);
	public static final Color PURPLE = new Color(1f, 0f, 1f, 1f);
	
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
		if (!(that instanceof Color)) return false;
		final Color c = (Color)that;
		return this.r == c.r
			&& this.g == c.g
			&& this.b == c.b
			&& this.a == c.a;
	}
}
