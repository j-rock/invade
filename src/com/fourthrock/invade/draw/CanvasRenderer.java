package com.fourthrock.invade.draw;

import com.fourthrock.invade.game.physics.Position2D;

/**
 * A simplified interface to the OpenGLRenderer so that
 * Scene instances are not exposed to its full feature set.
 * 
 * CanvasRenderers only provide instructions for drawing to
 * the canvas.
 * 
 * @author Joseph
 *
 */
public abstract class CanvasRenderer {
	
	/**
	 * Draw the shape specified by the DrawEnum at the specified (x,y) coordinates
	 * with the given color. (The coordinates are in world space).
	 */
	public abstract void draw(final DrawEnum drawEnum, final Position2D p, final ScaleVec s, final float angle, final Color c);
	
	/**
	 * Draws shape specified by drawEnum without scaling.
	 */
	public void draw(final DrawEnum drawEnum, final Position2D p, final float angle, final Color c) {
		draw(drawEnum, p, ScaleVec.UNIT, angle, c);
	}
	
	/**
	 * Draws shape specified by drawEnum without rotation
	 */
	public void draw(final DrawEnum drawEnum, final Position2D p, final ScaleVec s, final Color c) {
		draw(drawEnum, p, s, 0f, c);
	}
	
	/**
	 * Draws shape specified by drawEnum without scaling or rotation
	 */
	public void draw(final DrawEnum drawEnum, final Position2D p, final Color c) {
		draw(drawEnum, p, 0f, c);
	}

	/**
	 * Draws the shape specified by the DrawEnum at the specified (x,y) coordinates
	 * with the given color. (The coordinates are in render screen space.)
	 */
	public abstract void drawScreen(final DrawEnum drawEnum, final RenderScreen2D p, final ScaleVec s, final float angle, final Color color);
	
	public void drawScreen(final DrawEnum drawEnum, final RenderScreen2D p, final ScaleVec s, final Color c) {
		drawScreen(drawEnum, p, s, 0f, c);
	}
}
