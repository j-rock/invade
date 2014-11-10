package com.fourthrock.invade.draw.objects;

import android.opengl.GLES20;


/**
 * A two-dimensional circle for use as a drawn object in OpenGL ES 2.0.
 * 
 * Technically it's an n-sided polygon which subsumes triangle and square.
 */
public class Circle extends DrawObject {

	public Circle(final int program) {
		this(program, 50);
	}
	
    public Circle(final int program, final int nSides) {
    	super(program, generateVertexCoords(nSides),
    			new DrawMethod(GLES20.GL_TRIANGLE_FAN, nSides+2));
    }
    
    private static float[] generateVertexCoords(final int nSides) {
    	final int size = 3*(nSides + 2);
    	final float[] verts = new float[size];
    	verts[0] = verts[1] = verts[2] = 0f;

    	for(int v=1; v<=nSides; v++) {
    		final float theta = (float)(2 * Math.PI / nSides) * v;
    		verts[3*v]   = (float)Math.cos(theta);
    		verts[3*v+1] = (float)Math.sin(theta);
    		verts[3*v+2] = 0f;
    	}
    	verts[size-3] = verts[3];
    	verts[size-2] = verts[4];
    	verts[size-1] = 0f;
    	return verts;
    }
}