package com.fourthrock.invade.draw;

import static com.fourthrock.invade.draw.DrawEnum.BOLT;
import static com.fourthrock.invade.draw.DrawEnum.CIRCLE;
import static com.fourthrock.invade.draw.DrawEnum.SQUARE;
import static com.fourthrock.invade.draw.DrawEnum.TRIANGLE;
import static com.fourthrock.invade.draw.resource.FragmentShader.USE_VERTEX_COLOR;
import static com.fourthrock.invade.draw.resource.VertexShader.APPLY_MODEL_VIEW_PROJECTION;

import java.util.HashMap;
import java.util.Map;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.fourthrock.invade.draw.objects.Bolt;
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
 * to OpenGL's vsync, we also use the OpenGLRunner
 * to step the GameState in onDrawFrame.
 * 
 * @author Joseph
 */
public class OpenGLRunner extends CanvasRenderer implements GLSurfaceView.Renderer {
	public static int SCREEN_WIDTH;
	public static int SCREEN_HEIGHT;
	public static float SCREEN_RATIO;
	public static final float NEAR = 3f;
	public static final float FAR = 7f;
	
	private final GameState gameState;
    private final float[] mvpMat;
    private final float[] projMat;
    private final float[] viewMat;
    private final float[] screenMat;
    protected Map<DrawEnum, DrawObject> drawObjects;
    protected ShaderProgramManager sprgmManager;
    
    
    public OpenGLRunner(final GameState gameState) {
		this.gameState = gameState;
		this.mvpMat  = new float[16];
		this.projMat = new float[16];
		this.viewMat = new float[16];
		this.screenMat = new float[16];
		this.drawObjects = new HashMap<>();
		this.sprgmManager = new ShaderProgramManager(); 
	}

	@Override
    public void onSurfaceCreated(final GL10 unused, final EGLConfig config) {
        GLES20.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
        allocateDrawObjects();
    }
	
    @Override
    public void onSurfaceChanged(final GL10 unused, final int width, final int height) {
    	SCREEN_WIDTH = width;
    	SCREEN_HEIGHT = height;
    	SCREEN_RATIO = (float)width / height;
        GLES20.glViewport(0, 0, width, height);
        Matrix.orthoM(screenMat, 0, -SCREEN_RATIO, SCREEN_RATIO, -1, 1, -1, 1);
    }

    @Override
    public void onDrawFrame(final GL10 unused) {
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
       	
    	drawObjects.put(SQUARE, new Square(standardProgram));
    	drawObjects.put(TRIANGLE, new Triangle(standardProgram));
    	drawObjects.put(CIRCLE, new Circle(standardProgram));
    	drawObjects.put(BOLT, new Bolt(standardProgram));
    	
    	if(drawObjects.size() != DrawEnum.values().length) {
    		throw new IllegalStateException("OpenGLRunner drawObjects map must support all DrawEnum types.");
    	}
	}
    
    /*
     * From the game's eye position and zoom factor, we set up our mvpMat
     */
    private void generateModelViewProjectionMatrix() {
        final float ratio = (float) SCREEN_WIDTH / SCREEN_HEIGHT;
        final float zoom = gameState.getZoom();
        Matrix.frustumM(projMat, 0, -ratio/zoom, ratio/zoom, -1/zoom, 1/zoom, 3, 7);
        
    	final float[] eye = gameState.getEye();
        Matrix.setLookAtM(viewMat, 0, eye[0], eye[1], eye[2], eye[0], eye[1], 0f, 0f, 1.0f, 0.0f);
        Matrix.multiplyMM(mvpMat, 0, projMat, 0, viewMat, 0);
    }
    
    /**
     * Actually go and start running the draw commands on the GameState
     */
    private void display() {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        gameState.render(this);
        gameState.renderScreen(this);
    }

	@Override
	public void draw(final DrawEnum drawEnum, final Position2D p, final ScaleVec s, final float angle, final Color color) {
		final float[] updatedMVP = new float[16];
		Matrix.translateM(updatedMVP, 0, mvpMat, 0, -p.x, p.y, 0);
		finishDrawPipeline(drawEnum, updatedMVP, s, angle, color);
	}
	
	@Override
	public void drawScreen(final DrawEnum drawEnum, final RenderScreen2D p, final ScaleVec s, final float angle, final Color color) {
		final float[] updatedMVP = new float[16];
		Matrix.translateM(updatedMVP, 0, screenMat, 0, p.x, -p.y, 0);
		finishDrawPipeline(drawEnum, updatedMVP, s, angle, color);
	}
	
	private void finishDrawPipeline(final DrawEnum drawEnum, final float[] mvp, final ScaleVec s, final float angle, final Color color) {
		Matrix.rotateM(mvp, 0, angle, 0f, 0f, 1f);
		Matrix.scaleM(mvp, 0, s.sx, s.sy, s.sz);
		final DrawObject d = drawObjects.get(drawEnum);
		d.draw(mvp, color.toFloatArray());
	}
}