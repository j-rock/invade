package com.fourthrock.invade.draw.resource;

import android.opengl.GLES20;

/**
 * Wraps VertexShader code and the logic involved in loading it.
 * 
 * @author Joseph
 *
 */
public class VertexShader extends Shader {
	public static final VertexShader APPLY_MODEL_VIEW_PROJECTION = new VertexShader(
			"uniform mat4 uMVPMatrix;"
		  + "attribute vec4 vPosition;"
		  + "void main() {"
		  + "  gl_Position = uMVPMatrix * vPosition;"
		  + "}"
	);
	
	public static final VertexShader MODEL_VIEW_WITH_TEXTURE = new VertexShader(
			"attribute vec2 a_TexCoordinate;"
		  + "varying vec2 v_TexCoordinate;"
					
		  + "uniform mat4 uMVPMatrix;"
		  + "attribute vec4 vPosition;"
		  + "void main() {"
		  + "  gl_Position = uMVPMatrix * vPosition;"
		  + "  v_TexCoordinate = a_TexCoordinate;"
		  + "}"
	);
	
	private VertexShader(final String code) {
		super(code, GLES20.GL_VERTEX_SHADER);
	}
}
