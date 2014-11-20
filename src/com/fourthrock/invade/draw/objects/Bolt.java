package com.fourthrock.invade.draw.objects;

import android.opengl.GLES20;

public class Bolt extends DrawObject {

	public Bolt(final int program) {
		super(program, generateVertexCoords(),
				new DrawMethod(GLES20.GL_TRIANGLE_STRIP, 7));
	}
	
	private static float[] generateVertexCoords() {
		return new float[]{
		     	-1f,     -1f, 0f,
			 -0.38f,   0.62f, 0f,
			     0f,   0.16f, 0f,
			 -0.12f,    0.5f, 0f,
			  0.19f, -0.025f, 0f,
			 0.218f,      1f, 0f,
			     1f,      1f, 0f
		};
	}
	
}
