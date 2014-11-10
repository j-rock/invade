package com.fourthrock.invade.draw.resource;

import android.opengl.GLES20;

public class VertexShader extends Shader {
	public static final VertexShader APPLY_MODEL_VIEW_PROJECTION = new VertexShader(
			"uniform mat4 uMVPMatrix;"
		  + "attribute vec4 vPosition;"
		  + "void main() {"
		  + "  gl_Position = uMVPMatrix * vPosition;"
		  + "}");
	
	private VertexShader(final String code) {
		super(code, GLES20.GL_VERTEX_SHADER);
	}
}
