package com.fourthrock.invade.game.scene;

import android.opengl.GLES20;
import android.opengl.GLU;
import android.opengl.Matrix;

import com.fourthrock.invade.draw.Screen2D;
import com.fourthrock.invade.game.physics.Position2D;

public class Picker2D {
	private static final float NEAR = 3f;
	private static final float FAR = 7f;
	private final int[] viewport;
	private final float[] proj;
	private final float[] modelView;
	private final Scene scene;
	
	public Picker2D(final Scene scene) {
		this.scene = scene;
		
		viewport = new int[4];
		//GLES20.glGetIntegerv(GLES20.GL_VIEWPORT, viewport, 0);
		// TODO - Broken, for now, hard code:
		viewport[2] = 1920;
		viewport[3] = 1005;
		
	    float ratio = (float) viewport[2] / viewport[3];
		proj = new float[16];
	    Matrix.frustumM(proj, 0, -ratio, ratio, -1, 1, NEAR, FAR);
	    
	    modelView = new float[16];
	}
	
	/*
	 * Uses an OpenGL Utility function called UnProject to
	 * convert from Screen2D coordinations to world coordinates.
	 */
	public Position2D convertScreenToWorld(final Screen2D screenCoords) {
		setUpModelView();
		float[] near = new float[4];
		final int nearSuccess = GLU.gluUnProject(screenCoords.x, screenCoords.y, 0f,
												 modelView, 0, proj, 0, viewport, 0, near, 0);

		float[] far = new float[4];
		final int farSuccess = GLU.gluUnProject(screenCoords.x, screenCoords.y, 1f,
												 modelView, 0, proj, 0, viewport, 0, far, 0);
		
		
		if(  nearSuccess == GLES20.GL_TRUE
		   && farSuccess == GLES20.GL_TRUE) {
			// must scale the results to account for perspective.
			final float k = near[2] / (FAR - NEAR);
			final float x = (near[0] + k * (far[0] - near[0])) / -near[3];
			final float y = (2f * scene.getEye()[1]) + (near[1] + k * (far[1] - near[1])) / -near[3];
			
			return (new Position2D(x, y)).asPosition();
		}
		return null;
	}
	
	private void setUpModelView() {
    	final float[] eye = scene.getEye();
        Matrix.setLookAtM(modelView, 0, eye[0], eye[1], eye[2], eye[0], eye[1], 0f, 0f, 1.0f, 0.0f);
        
        final float scale = scene.getZoom();
        Matrix.scaleM(modelView, 0, scale, scale, scale);
	}
}
