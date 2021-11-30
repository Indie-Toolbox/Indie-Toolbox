package com.toolbox;

import com.toolbox.font.TTFont;
import com.toolbox.render.*;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class LaunchToolbox {
	public static void main(String[] args) {
		glfwInit();
		glfwWindowHint(GLFW_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_VERSION_MINOR, 3);

		long window = glfwCreateWindow(1080, 720, "Indiedev Toolbox", 0, 0);
		glfwMakeContextCurrent(window);
		GL.createCapabilities();

		Texture.InitTextures();
		GLState.enableAlphaBlending();

		Matrix4f proj = new Matrix4f();
		proj.ortho(0, 1080, 720, 0, -1, 1000);

		TTFont comicsans = new TTFont("src/main/resources/comic.ttf", 30);
		TTFont inconsolata = new TTFont("src/main/resources/Inconsolata.ttf", 30);

		Shader shader = new Shader("src/main/resources/shader");
		shader.bind();
		shader.uploadIntArray("u_TextureSlots", new int[] { 0, 1, 2, 3, 4, 5, 6, 7 });
		shader.uploadMatrix("u_Projection", proj);

		Renderer renderer = new Renderer();
		Quad q = new Quad(
				new Vector2f(1.5f, 1.5f),
				new Vector2f(500.f, 500.f),
				new Vector2f(0, 0),
				new Vector2f(1, 1),
				new Vector4f(.8f, .2f, .3f, 1.f),
				10f
		);

		while (!glfwWindowShouldClose(window)) {
			glfwPollEvents();

			glClear(GL_COLOR_BUFFER_BIT);

			renderer.immediateBegin();
			renderer.drawQuad(q);
			renderer.drawString(comicsans, "POG", 20, 40, new Vector4f(1.0f));
			renderer.drawString(inconsolata, "POG AGAIN", 20, 80, new Vector4f(1.0f));
			renderer.immediateEnd();

			glfwSwapBuffers(window);
		}

		shader.unbind();
		glfwDestroyWindow(window);
		glfwTerminate();
	}
}
