package com.fourthrock.invade.draw.objects;

import android.opengl.GLES20;

/**
 * A two-dimensional square for use as a drawn object in OpenGL ES 2.0.
 */
public class Square extends DrawObject {

    private static final float squareCoords[] = {
        	-0.5f,  0.5f, 0f,
        	-0.5f, -0.5f, 0f,
        	 0.5f, -0.5f, 0f,
        	 0.5f,  0.5f, 0f
        };
    private static final short drawOrder[] = { 0, 1, 2, 0, 2, 3 };
    
    public Square(final int program) {
    	super(program, squareCoords,
    		new DrawMethod(GLES20.GL_TRIANGLES, drawOrder));
    }
}