package com.fourthrock.invade.draw.resource;

import android.opengl.GLES20;

public abstract class Shader {
	private final String code;
	private final int shaderType;
	
	protected Shader(final String code, final int shaderType) {
		this.code = code;
		this.shaderType = shaderType;
	}
	
	public int load() {
		return loadShader(shaderType, code);
	}

	@Override
	public boolean equals(final Object that) {
		return that instanceof Shader && ((Shader)(that)).code.equals(this.code);
	}
	
	@Override
	public int hashCode() {
		return code.hashCode();
	}
	
	/**
	 * Compiles an OpenGL shader and returns a handle to it.
	 */
	private static int loadShader(final int type, final String shaderCode) {
		final int shader = GLES20.glCreateShader(type);
		GLES20.glShaderSource(shader, shaderCode);
		GLES20.glCompileShader(shader);
		return shader;
	}
}
