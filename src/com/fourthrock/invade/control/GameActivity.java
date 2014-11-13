package com.fourthrock.invade.control;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import com.fourthrock.invade.game.GameState;
import com.fourthrock.invade.game.scene.GamePlayScene;

/**
 * A class to handle the basic control flow logic of the OpenGL bits
 * @author Joseph
 *
 */
public class GameActivity extends Activity {
	private static GameState gameState;
    private GLSurfaceView view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(gameState == null) {
        	gameState = new GameState(new GamePlayScene());
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