package com.fourthrock.invade.draw.objects;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;

/**
 * A specification for what to draw on the screen.
 * @author Joseph
 *
 */
public class DrawObject {
	private static final int COORDS_PER_VERTEX = 3;
	private static final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4;
	
	private final int program;
	private final FloatBuffer vertexBuffer;
	private final DrawMethod method;

	public DrawObject(final int program, final float[] vertexCoords, final DrawMethod method) {
		this.program = program;
		this.vertexBuffer = floatBufferFromArray(vertexCoords);
		this.method = method;
	}
	
	/**
     * Encapsulates the OpenGL ES instructions for drawing this shape.
     *
     * @param mvpMat, the model view projection matrix in which to draw
     * 			this shape.
     * @param color, the RGBA color with which to color this shape.
     */
	public void draw(final float[] mvpMat, final float[] color) {
		final HandleObject handles = loadHandlesFromProgram(program);
		GLES20.glEnableVertexAttribArray(handles.position);
		GLES20.glVertexAttribPointer(handles.position, COORDS_PER_VERTEX,
				GLES20.GL_FLOAT, false, VERTEX_STRIDE, vertexBuffer);

		GLES20.glUniform4fv(handles.color, 1, color, 0);
		GLES20.glUniformMatrix4fv(handles.mvpMat, 1, false, mvpMat, 0);

		if(method.drawsInOrder) {
			GLES20.glDrawArrays(method.drawMode, 0, method.vertexCount);
		} else {
			GLES20.glDrawElements(method.drawMode, method.vertexCount, GLES20.GL_UNSIGNED_SHORT, method.drawList);
		}
		GLES20.glDisableVertexAttribArray(handles.position);
	}

	private static HandleObject loadHandlesFromProgram(final int program) {
		GLES20.glUseProgram(program);
		final int position = GLES20.glGetAttribLocation(program, "vPosition");
		final int color = GLES20.glGetUniformLocation(program, "vColor");
		final int mvpMat = GLES20.glGetUniformLocation(program, "uMVPMatrix");
		return new HandleObject(position, color, mvpMat);
	}
	
	private static class HandleObject {
		public final int position, color, mvpMat;
		
		public HandleObject(final int position, final int color, final int mvpMat) {
			this.position = position;
			this.color = color;
			this.mvpMat = mvpMat;
		}
	}

	/**
	 * Creates a properly formatted FloatBuffer to hold vertices
	 */
	public static FloatBuffer floatBufferFromArray(final float[] coords) {
		final FloatBuffer vertexBuffer = ByteBuffer
											.allocateDirect(coords.length * 4)
											.order(ByteOrder.nativeOrder())
											.asFloatBuffer()
											.put(coords);
		vertexBuffer.position(0);
		return vertexBuffer;
	}
}
