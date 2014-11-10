package com.fourthrock.invade.draw.resource;

import android.opengl.GLES20;

public class FragmentShader extends Shader {
	public static final FragmentShader USE_VERTEX_COLOR = new FragmentShader(
			"precision mediump float;"
		  + "uniform vec4 vColor;"
		  + "void main() {"
		  + "  gl_FragColor = vColor;"
		  + "}");
	
	private FragmentShader(final String code) {
		super(code, GLES20.GL_FRAGMENT_SHADER);
	}
}
