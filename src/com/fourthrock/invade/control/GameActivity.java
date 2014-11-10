package com.fourthrock.invade.control;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

/**
 * A class to handle the basic control flow logic of the OpenGL bits
 * @author Joseph
 *
 */
public class GameActivity extends Activity {

    private GLSurfaceView view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = new GameView(this);
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