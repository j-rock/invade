package com.fourthrock.invade.draw.objects;

import android.opengl.GLES20;

/**
 * A two-dimensional triangle for use as a drawn object in OpenGL ES 2.0.
 */
public class Triangle extends DrawObject {

    private static final float[] triangleCoords = {
		0f,  0.622008459f, 0f,
	 -0.5f, -0.311004243f, 0f,
	  0.5f, -0.311004243f, 0f
    };
    
    public Triangle(final int program) {
    	super(program, triangleCoords,
    		new DrawMethod(GLES20.GL_TRIANGLES, 3));
    }
}
