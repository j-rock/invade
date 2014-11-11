package com.fourthrock.invade.draw;

import static com.fourthrock.invade.draw.resource.FragmentShader.USE_VERTEX_COLOR;
import static com.fourthrock.invade.draw.resource.VertexShader.APPLY_MODEL_VIEW_PROJECTION;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.fourthrock.invade.draw.objects.Circle;
import com.fourthrock.invade.draw.objects.DrawObject;
import com.fourthrock.invade.draw.objects.Square;
import com.fourthrock.invade.draw.objects.Triangle;
import com.fourthrock.invade.draw.resource.ShaderProgramManager;
import com.fourthrock.invade.game.GameState;
import com.fourthrock.invade.game.physics.Position2D;

/**
 * Class for rendering the GameState.
 * 
 * Since the game loop is inextricably tied
 * to OpenGL's vsync, we also use the GameRenderer
 * to step the GameState in onDrawFrame.
 * 
 * @author Joseph
 */
public class OpenGLRunner extends CanvasRenderer implements GLSurfaceView.Renderer {
	private final GameState gameState;
	
    private final float[] mvpMat;
    private final float[] projMat;
    private final float[] viewMat;
    protected DrawObject[] drawObjects;
    protected ShaderProgramManager sprgmManager;
    
    
    public OpenGLRunner(final GameState gameState) {
		this.gameState = gameState;
		
		this.mvpMat  = new float[16];
		this.projMat = new float[16];
		this.viewMat = new float[16];
		this.drawObjects = null;
		this.sprgmManager = new ShaderProgramManager(); 
	}

	@Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        allocateDrawObjects();
    }
	
    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        Matrix.frustumM(projMat, 0, -ratio, ratio, -1, 1, 3, 7);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
    	gameState.step();
        generateModelViewProjectionMatrix();
        display();
    }

    /*
     * Must allocate the memory for the DrawObjects we will render
     */
    protected void allocateDrawObjects() {
       	sprgmManager.clear();
		
       	final int standardProgram =
       			sprgmManager.getOrLoadProgram(
       					APPLY_MODEL_VIEW_PROJECTION,
       					USE_VERTEX_COLOR);
       	
    	// To ensure correctness, this array must be
    	// instantiated in the same order
    	// as the DrawEnum states are listed.
    	drawObjects = new DrawObject[]{
    		new Square(standardProgram),
    		new Triangle(standardProgram),
    		new Circle(standardProgram)
    	};
	}
    
    /*
     * From the game's eye position and zoom factor, we set up our mvpMat
     */
    private void generateModelViewProjectionMatrix() {
    	float[] eye = gameState.getEye();
        Matrix.setLookAtM(viewMat, 0, eye[0], eye[1], eye[2], eye[0], eye[1], 0f, 0f, 1.0f, 0.0f);
        Matrix.multiplyMM(mvpMat, 0, projMat, 0, viewMat, 0);
        
        float scale = gameState.getZoom();
        Matrix.scaleM(mvpMat, 0, scale, scale, scale);
    }
    
    /**
     * Actually go and start running the draw commands on the GameState
     */
    private void display() {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        gameState.render(this);
    }

	@Override
	public void draw(final DrawEnum drawEnum, final Position2D p, final ScaleVec s, final float angle, final Color color) {
		float[] updatedMVP = new float[16];
		Matrix.translateM(updatedMVP, 0, mvpMat, 0, -p.x, p.y, 0);
		Matrix.rotateM(updatedMVP, 0, angle, 0f, 0f, 1f);
		Matrix.scaleM(updatedMVP, 0, s.sx, s.sy, s.sz);
		final DrawObject d = drawObjects[drawEnum.ordinal()];
		d.draw(updatedMVP, color.toFloatArray());
	}
}