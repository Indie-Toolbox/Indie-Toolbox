package com.toolbox;

import com.toolbox.render.GLState;
import com.toolbox.render.Renderer;
import com.toolbox.render.Shader;
import com.toolbox.render.Texture;
import com.toolbox.tools.ToolDescription;
import com.toolbox.tools.Tools;
import com.toolbox.util.Input;
import com.toolbox.util.OsUtil;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class LaunchToolbox {
	private static final boolean useLocal = true;

	public static void main(String[] args) throws Exception {
		glfwInit();
		glfwWindowHint(GLFW_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_VERSION_MINOR, 3);

		long window = glfwCreateWindow(1080, 720, "Indiedev Toolbox", 0, 0);
		glfwMakeContextCurrent(window);
		GL.createCapabilities();

		Input.setup(window);

		Texture.initializeTextures();
		GLState.enableAlphaBlending();

		List<ToolDescription> installed = new ArrayList<>();
		BufferedReader tools_reader = null;
		if (!useLocal) {
			URL url = new URL("https://raw.githubusercontent.com/Indie-Toolbox/Indie-Toolbox/main/tools.json");
			tools_reader = new BufferedReader(new InputStreamReader(url.openStream()));
		} else {
			tools_reader = new BufferedReader(new FileReader("P:/Java/Projects/ToolboxLauncher/tools.json"));
		}

		StringBuilder stringBuilder = new StringBuilder();
		String inputLine;
		while ((inputLine = tools_reader.readLine()) != null) {
			stringBuilder.append(inputLine);
			stringBuilder.append(System.lineSeparator());
		}
		tools_reader.close();
		Tools.load(stringBuilder.toString());

		if (!OsUtil.installedToolsFileExists()) {
			FileWriter writer = OsUtil.openInstalledToolsFile();
			writer.close();
		} else {
			BufferedReader reader = new BufferedReader(OsUtil.openInstalledToolsFileRead());
			if (!OsUtil.installedToolsFileIsEmpty()) {
				String line;
				StringBuilder contentBuilder = new StringBuilder();
				while ((line = reader.readLine()) != null) {
					contentBuilder.append(line);
				}
				reader.close();
				String content = contentBuilder.toString();
				String[] installed_f = content.split("\n");
				String[] names = new String[installed_f.length];
				String[] versions = new String[installed_f.length];
				for (int i = 0, installed_fLength = installed_f.length; i < installed_fLength; i++) {
					String s = installed_f[i];
					String[] split = s.split(":");
					names[i] = split[0];
					versions[i] = split[1];
				}

				for (ToolDescription d : Tools.tools) {
					int i = 0;
					for (String name : names)
						if (name.equals(d.name)) {
							ToolDescription td = d.makeClone();
							td.version = versions[i];
							installed.add(td);
						}
				}
			}
		}

		Matrix4f proj = new Matrix4f();
		proj.ortho(0, 1080, 720, 0, -1, 1000);

		Shader shader = new Shader("src/main/resources/shader");
		shader.bind();
		shader.uploadIntArray("u_TextureSlots", new int[]{0, 1, 2, 3, 4, 5, 6, 7});
		shader.uploadMatrix("u_Projection", proj);

		Renderer renderer = new Renderer();
		glClearColor(0.1f, 0.1f, 0.1f, 1.0f);

		double prev = glfwGetTime();
		double now = glfwGetTime();
		float delta = (float) (now - prev);

		while (!glfwWindowShouldClose(window)) {
			Input.clear();
			glfwPollEvents();
			Input.update();

			prev = now;
			now = glfwGetTime();
			delta = (float) (now - prev);

			glClear(GL_COLOR_BUFFER_BIT);

			renderer.immediateBegin();
			Tools.render(renderer, delta, installed);
			renderer.immediateEnd();

			glfwSwapBuffers(window);
		}

		shader.unbind();
		glfwDestroyWindow(window);
		glfwTerminate();
	}
}