package com.fourthrock.invade.draw.resource;

import android.opengl.GLES20;

/**
 * Shaders are specialized OpenGL programs that help color
 * vertices and fragments. They provide more functionality
 * than vanilla OpenGL.
 * 
 * Here we capture the shared logic of Vertex and Fragment
 * Shaders so that they can be reified as first-class objects.
 * 
 * @author Joseph
 *
 */
public abstract class Shader {
	private final String code;
	private final int shaderType;
	
	protected Shader(final String code, final int shaderType) {
		this.code = code;
		this.shaderType = shaderType;
	}
	
	/**
	 * Compiles an OpenGL shader and returns a handle to it.
	 */
	public int load() {
		return loadShader(shaderType, code);
	}

	@Override
	public boolean equals(final Object that) {
		return that instanceof Shader && ((Shader)(that)).code.equals(this.code);
	}
	
	@Override
	public int hashCode() {
		// The only unique component of a Shader is the code to be compiled.
		return code.hashCode();
	}
	
	private static int loadShader(final int type, final String shaderCode) {
		final int shader = GLES20.glCreateShader(type);
		GLES20.glShaderSource(shader, shaderCode);
		GLES20.glCompileShader(shader);
		return shader;
	}
}
