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

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class LaunchToolbox {
	private static final boolean useLocal = false;
	private static float scroll = 0.000f;
	private static float scroll_target = 0.000f;

	private static volatile String downloaded_content = null;

	public static void main(String[] args) throws Exception {
		glfwInit();
		glfwWindowHint(GLFW_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

		long window = glfwCreateWindow(1080, 720, "Indiedev Toolbox", 0, 0);
		glfwMakeContextCurrent(window);
		GL.createCapabilities();

		Input.setup(window);

		Texture.initializeTextures();
		GLState.enableAlphaBlending();

		List<ToolDescription> installed = new ArrayList<>();

		{
			boolean set = false;
			// Use cache if exists
			File in_f = new File(OsUtil.getToolboxFilepath() + "tool_list_cache.json");
			if (in_f.exists()) {
				BufferedReader input = new BufferedReader(new FileReader(in_f));

				StringBuilder stringBuilder = new StringBuilder();
				String line = null;
				while ((line = input.readLine()) != null) {
					stringBuilder
							.append(line)
							.append(System.lineSeparator());
				}
				String content = stringBuilder.toString();

				input.close();

				Tools.load(content);
				set = true;
			} else {
				downloadList();
				Tools.load(downloaded_content);
			}
			installedToolFileRefresh(installed);
			if (set) new Thread(LaunchToolbox::downloadList).start();
		}

		Matrix4f proj = new Matrix4f();
		proj.ortho(0, 1080, 720, 0, -1, 1000);

		Matrix4f view = new Matrix4f();

		Shader shader = new Shader("shader");
		shader.bind();
		shader.uploadIntArray("u_TextureSlots", new int[]{0, 1, 2, 3, 4, 5, 6, 7});

		Renderer renderer = new Renderer();
		glClearColor(0.1f, 0.1f, 0.1f, 1.0f);

		double prev = glfwGetTime();
		double now = glfwGetTime();
		float delta = (float) (now - prev);

		while (!glfwWindowShouldClose(window)) {
			if (downloaded_content != null) {
				Tools.load(downloaded_content);
				downloaded_content = null;
				installedToolFileRefresh(installed);
			}

			Input.clear();
			glfwPollEvents();
			Input.update();

			prev = now;
			now = glfwGetTime();
			delta = (float) (now - prev);
			scroll_target -= Input.scrollY * 100 * delta;
			scroll += (scroll_target - scroll) * 10 * delta;
			scroll_target = scroll_target < 0 ? 0 : scroll_target;

			view.identity();
			view.translate(0, scroll, 0);
			shader.uploadMatrix("u_Projection", view.mul(proj));

			glClear(GL_COLOR_BUFFER_BIT);

			renderer.immediateBegin();
			Tools.render(renderer, delta, installed, scroll_target * 360);
			renderer.immediateEnd();

			glfwSwapBuffers(window);
		}

		for (ToolDescription desc : installed) {
			BufferedWriter wr = OsUtil.openInstalledToolsFile();
			wr.append(desc.name).append(':').append(desc.version).append(System.lineSeparator());
			wr.close();
		}

		shader.unbind();
		glfwDestroyWindow(window);
		glfwTerminate();
	}

	private static void installedToolFileRefresh(List<ToolDescription> installed) throws Exception {
		installed.clear();
		if (!OsUtil.installedToolsFileExists()) {
			BufferedWriter writer = OsUtil.openInstalledToolsFile();
			writer.close();
		} else {
			BufferedReader reader = new BufferedReader(OsUtil.openInstalledToolsFileRead());
			if (!OsUtil.installedToolsFileIsEmpty()) {
				String line;
				StringBuilder contentBuilder = new StringBuilder();
				while ((line = reader.readLine()) != null) {
					contentBuilder.append(line).append(System.lineSeparator());
				}
				reader.close();
				String content = contentBuilder.toString();
				String[] installed_f = content.split(System.lineSeparator());
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
	}

	private static void downloadList() {
		try {
			BufferedReader tools_reader = null;
			if (!useLocal) {
				URL url = new URL("https://raw.githubusercontent.com/Indie-Toolbox/Indie-Toolbox/main/tools.json");
				tools_reader = new BufferedReader(new InputStreamReader(url.openStream()));
			} else {
				tools_reader = new BufferedReader(new FileReader("P:/Java/Projects/ToolboxLauncher/tools.json"));
			}

			File out_f = new File(OsUtil.getToolboxFilepath() + "tool_list_cache.json");
			System.out.println(out_f);
			out_f.getParentFile().mkdirs();
			BufferedWriter output_file = new BufferedWriter(new FileWriter(out_f));

			StringBuilder stringBuilder = new StringBuilder();
			String inputLine;
			while ((inputLine = tools_reader.readLine()) != null) {
				stringBuilder
						.append(inputLine)
						.append(System.lineSeparator());
			}
			downloaded_content = stringBuilder.toString();
			output_file.write(downloaded_content);

			tools_reader.close();
			output_file.close();
		} catch (Exception ignored) {}
	}
}