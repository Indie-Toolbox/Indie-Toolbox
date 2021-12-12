package com.toolbox.render;

import org.joml.Matrix4f;

import java.io.*;
import java.nio.FloatBuffer;

import com.toolbox.util.OsUtil;

import org.lwjgl.*;
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
		if (OsUtil.getOS() != OsUtil.OS.Mac) {
			// For now, unless there is a better solution, this does not work on mac OS using opengl 330 core.
			glValidateProgram(programID);
			if (glGetProgrami(programID, GL_VALIDATE_STATUS) != GL_TRUE) {
				System.err.println(glGetProgramInfoLog(programID));
				System.exit(-1);
			}
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
		String contents = "";
		try (InputStream file = Shader.class.getClassLoader().getResourceAsStream(path)) {
			if (file != null) contents = new String(file.readAllBytes());
			else throw new FileNotFoundException(path);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		int shaderID = glCreateShader(type);
		glShaderSource(shaderID, contents);
		glCompileShader(shaderID);
		if (glGetShaderi(shaderID, GL_COMPILE_STATUS) != GL_TRUE) {
			System.err.println(glGetShaderInfoLog(shaderID));
			System.exit(-1);
		}
		return shaderID;
	}
}