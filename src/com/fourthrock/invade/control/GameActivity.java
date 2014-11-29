package com.fourthrock.invade.control;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import com.fourthrock.invade.game.GameState;
import com.fourthrock.invade.game.scene.LevelChooserScene;

/**
 * A class to handle the basic control flow logic of the OpenGL bits.
 * 
 * This is the main entry point into the program.
 * 
 * @author Joseph
 *
 */
public class GameActivity extends Activity {
	private static GameState gameState;
    private GLSurfaceView view;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(gameState == null) {
        	gameState = new GameState(new LevelChooserScene());
        }
        view = new GameView(this, gameState);
        setContentView(view);
    }

    @Override
    protected void onPause() {
        super.onPause();
        view.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        view.onResume();
    }
}