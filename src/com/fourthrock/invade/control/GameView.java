package com.fourthrock.invade.control;

import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.fourthrock.invade.draw.OpenGLRunner;
import com.fourthrock.invade.game.GameState;

/**
 * A view container in which to draw OpenGL graphics.
 */
public class GameView extends GLSurfaceView {
    private final OpenGLRunner renderer;
    private final GameState gameState;
    private final GameInput gameInput;

    public GameView(final Context context, final GameState gameState) {
        super(context);
        setEGLContextClientVersion(2);
        
        this.gameState = gameState;
        gameInput = new GameInput(context, gameState);
        renderer = new OpenGLRunner(gameState);
        setRenderer(renderer);
    }

    @SuppressLint("ClickableViewAccessibility")
	@Override
    public boolean onTouchEvent(final MotionEvent e) {
    	gameInput.onTouchEvent(e);
    	requestRender();
    	return true;
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	gameState.onResume();
    }

}
