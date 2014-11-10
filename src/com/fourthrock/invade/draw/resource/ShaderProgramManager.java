package com.fourthrock.invade.draw.resource;

import java.util.HashMap;
import java.util.Map;

import android.opengl.GLES20;
import android.util.Pair;

/**
 * Keeps track of a working set of vertex and fragment shaders. This way, it
 * only generates the resources necessary for rendering.
 * 
 * @author Joseph
 *
 */
public class ShaderProgramManager {
	private Map<VertexShader, Integer> vertexShaders;
	private Map<FragmentShader, Integer> fragmentShaders;
	private Map<Pair<VertexShader, FragmentShader>, Integer> programs;

	public ShaderProgramManager() {
		clear();
	}

	public void clear() {
		vertexShaders = new HashMap<>();
		fragmentShaders = new HashMap<>();
		programs = new HashMap<>();
	}

	public int getOrLoadProgram(final VertexShader vShader, final FragmentShader fShader) {
		final Pair<VertexShader, FragmentShader> shaders = new Pair<>(vShader, fShader);
		if (programs.containsKey(shaders)) {
			return programs.get(shaders);
		} else {
			final int vShaderHandle = getOrLoadShader(vShader, vertexShaders);
			final int fShaderHandle = getOrLoadShader(fShader, fragmentShaders);
			final int prgm = createProgram(vShaderHandle, fShaderHandle);
			programs.put(shaders, prgm);
			return prgm;
		}
	}
	
	private <S extends Shader> int getOrLoadShader(final S shader, final Map<S, Integer> mapping) {
		if (mapping.containsKey(shader)) {
			return mapping.get(shader);
		} else {
			final int handle = shader.load();
			mapping.put(shader, handle);
			return handle;
		}
	}

	/**
	 * Creates an OpenGL ES 2.0 program from handles to valid shaders.
	 */
	private static int createProgram(final int vShaderHandle, final int fShaderHandle) {
		final int program = GLES20.glCreateProgram();
		GLES20.glAttachShader(program, vShaderHandle);
		GLES20.glAttachShader(program, fShaderHandle);
		GLES20.glLinkProgram(program);
		return program;
	}
}
