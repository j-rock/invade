package com.fourthrock.invade.camera;

import android.opengl.Matrix;

import com.fourthrock.invade.draw.OpenGLRunner;
import com.fourthrock.invade.draw.Screen2D;
import com.fourthrock.invade.game.physics.Position2D;
import com.fourthrock.invade.game.scene.Scene;

/**
 * Picker2D performs the object picking operations needed
 * to convert screen coordinates (say, from taps or scrolls)
 * into world coordinates.
 * 
 * We reconstruct the transformations done by OpenGLRunner
 * to invert them.
 * 
 * @author Joseph
 *
 */
public class Picker2D {
	private final float[] projMat;
	private final float[] viewMat;
	private final float[] mvpMat;
	private final float[] invTrans;
	private final Scene scene;
	
	public Picker2D(final Scene scene) {
		this.scene = scene;
		
		this.projMat  = new float[16];
		this.viewMat  = new float[16];
		this.mvpMat   = new float[16];
		this.invTrans = new float[16];
	}
	
	/*
	 * Take a pixel-space coordinate on the current screen
	 * and retrieve its world coordinate mapping.
	 * 
	 * 
	 * May the writer of http://magicscrollsofcode.blogspot.com/2010/10/3d-picking-in-android.html
	 * be forever blessed. Lifetime supply of toilet paper for this code.
	 */
	public Position2D convertScreenToWorld(final Screen2D screenCoords) {
		setUpInverseTransform();

		final float[] point = new float[4];
		point[0] = screenCoords.x * 2f / screenWidth()  - 1f;
		point[1] = (screenHeight() - screenCoords.y) * 2f / screenHeight() - 1f;
		point[2] = -1f;
		point[3] = 1f;
		
		
		final float[] out = new float[4];
		Matrix.multiplyMV(out, 0, invTrans, 0, point, 0);
		
		if(out[3] == 0f) {
			return null;
		} else {
			return new Position2D(-out[0]/out[3], out[1]/out[3]);
		}
	}

	private void setUpInverseTransform() {
        final float ratio = (float) screenWidth() / screenHeight();
        final float zoom = scene.getZoom();
        Matrix.frustumM(projMat, 0, -ratio/zoom, ratio/zoom, -1/zoom, 1/zoom, OpenGLRunner.NEAR, OpenGLRunner.FAR);
        
    	final float[] eye = scene.getEye();
        Matrix.setLookAtM(viewMat, 0, eye[0], eye[1], eye[2], eye[0], eye[1], 0f, 0f, 1.0f, 0.0f);
        Matrix.multiplyMM(mvpMat, 0, projMat, 0, viewMat, 0);
        Matrix.invertM(invTrans, 0, mvpMat, 0);
	}
	
	private int screenWidth() {
		return OpenGLRunner.SCREEN_WIDTH;
	}
	
	private int screenHeight() {
		return OpenGLRunner.SCREEN_HEIGHT;
	}
}
