package com.toolbox.render;

import org.joml.*;
import org.lwjgl.BufferUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import static org.lwjgl.opengl.GL20.*;

public class Shader {
	private final int programID;

	public Shader(String name) {
		programID = glCreateProgram();
		int vsID = loadShader(name + "V.glsl", GL_VERTEX_SHADER);
		int fsID = loadShader(name + "F.glsl", GL_FRAGMENT_SHADER);
		glAttachShader(programID, vsID);
		glAttachShader(programID, fsID);
		glLinkProgram(programID);
		if (glGetProgrami(programID, GL_LINK_STATUS) != GL_TRUE) {
			System.err.println(glGetProgramInfoLog(programID));
			System.exit(-1);
		}
		glValidateProgram(programID);
		if (glGetProgrami(programID, GL_VALIDATE_STATUS) != GL_TRUE) {
			System.err.println(glGetProgramInfoLog(programID));
			System.exit(-1);
		}
		glDetachShader(programID, vsID);
		glDetachShader(programID, fsID);
		glDeleteShader(vsID);
		glDeleteShader(fsID);
	}

	public void delete() {
		glDeleteProgram(programID);
	}

	public void bind() {
		glUseProgram(programID);
	}

	public void unbind() {
		glUseProgram(0);
	}

	public void uploadFloat(String name, float val) {
		int loc = glGetUniformLocation(programID, name);
		glUniform1f(loc, val);
	}

	public void uploadInt(String name, int val) {
		int loc = glGetUniformLocation(programID, name);
		glUniform1i(loc, val);
	}

	public void uploadMatrix(String name, Matrix4f mat) {
		int loc = glGetUniformLocation(programID, name);
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		mat.get(buffer);
		glUniformMatrix4fv(loc, false, buffer);
	}

	public void uploadIntArray(String name, int[] array) {
		int loc = glGetUniformLocation(programID, name);
		glUniform1iv(loc, array);
	}

	private int loadShader(String path, int type) {
		StringBuilder builder = new StringBuilder();
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(new File(path)));
			String line;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
				builder.append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		int shaderID = glCreateShader(type);
		glShaderSource(shaderID, builder.toString());
		glCompileShader(shaderID);
		if (glGetShaderi(shaderID, GL_COMPILE_STATUS) != GL_TRUE) {
			System.err.println(glGetShaderInfoLog(shaderID));
			System.exit(-1);
		}
		return shaderID;
	}
}