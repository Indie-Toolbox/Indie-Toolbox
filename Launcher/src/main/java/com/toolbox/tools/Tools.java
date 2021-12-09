package com.toolbox.tools;

import com.toolbox.font.TTFont;
import com.toolbox.render.Quad;
import com.toolbox.render.Renderer;
import com.toolbox.util.Input;
import com.toolbox.util.OsUtil;
import org.joml.Vector4f;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.lwjgl.glfw.GLFW;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Tools {
	public static final List<ToolDescription> tools = new ArrayList<>();
	private static final float startY = 103;
	public static TTFont inconsolata;
	public static TTFont inconsolata_smaller;
	private static ToolDescription current;

	private static volatile ToolDescription processing;
	private static volatile String prj_name;
	private static volatile double processed_percentage;

	public static void load(String src) {
		tools.clear();
		try {
			JSONParser parser = new JSONParser();
			JSONArray obj = (JSONArray) parser.parse(src);
			float y = startY;
			for (Object tool_obj : obj) {
				ToolDescription toolDescription = new ToolDescription();
				JSONObject tool = (JSONObject) tool_obj;
				toolDescription.name = (String) tool.getOrDefault("name", "no-name");
				toolDescription.version = (String) tool.getOrDefault("version", "0.0.0");
				toolDescription.title = (String) tool.getOrDefault("title", "no-title");
				toolDescription.desc = (String) tool.getOrDefault("description", "no-description");
				toolDescription.github_link = (String) tool.getOrDefault("git", "https://github.com/INVALID");

				toolDescription.back_quad = new Quad();
				toolDescription.back_quad.pos.x = 10;
				toolDescription.back_quad.pos.y = y - 30;
				toolDescription.back_quad.size.x = 1080 - 20;
				toolDescription.back_quad.size.y = 50;
				toolDescription.back_quad.target_pos.x = toolDescription.back_quad.pos.x;
				toolDescription.back_quad.target_pos.y = toolDescription.back_quad.pos.y;
				toolDescription.back_quad.target_size.x = toolDescription.back_quad.size.x;
				toolDescription.back_quad.target_size.y = toolDescription.back_quad.size.y;
				toolDescription.back_quad.rounding = 3;

				toolDescription.status_quad = new Quad();
				toolDescription.status_quad.pos.x = 20;
				toolDescription.status_quad.pos.y = y - 20;
				toolDescription.status_quad.size.x = 30;
				toolDescription.status_quad.size.y = 30;
				toolDescription.status_quad.target_pos.x = toolDescription.status_quad.pos.x;
				toolDescription.status_quad.target_pos.y = toolDescription.status_quad.pos.y;
				toolDescription.status_quad.target_size.x = toolDescription.status_quad.size.x;
				toolDescription.status_quad.target_size.y = toolDescription.status_quad.size.y;
				toolDescription.status_quad.rounding = 3;

				toolDescription.install_quad = new Quad();
				toolDescription.install_quad.pos.x = 1080 - 80;
				toolDescription.install_quad.pos.y = y - 20;
				toolDescription.install_quad.size.x = 60;
				toolDescription.install_quad.size.y = 30;
				toolDescription.install_quad.target_pos.x = toolDescription.install_quad.pos.x;
				toolDescription.install_quad.target_pos.y = toolDescription.install_quad.pos.y;
				toolDescription.install_quad.target_size.x = toolDescription.install_quad.size.x;
				toolDescription.install_quad.target_size.y = toolDescription.install_quad.size.y;
				toolDescription.install_quad.rounding = 3;

				toolDescription.i_progress_quad = new Quad();
				toolDescription.i_progress_quad.pos.x = 1080 - 296;
				toolDescription.i_progress_quad.pos.y = y - 8;
				toolDescription.i_progress_quad.size.x = 0; //200;
				toolDescription.i_progress_quad.size.y = 10;
				toolDescription.i_progress_quad.target_pos.x = toolDescription.i_progress_quad.pos.x;
				toolDescription.i_progress_quad.target_pos.y = toolDescription.i_progress_quad.pos.y;
				toolDescription.i_progress_quad.target_size.x = toolDescription.i_progress_quad.size.x;
				toolDescription.i_progress_quad.target_size.y = toolDescription.i_progress_quad.size.y;
				toolDescription.i_progress_quad.color = new Vector4f(.453f, .649f, .276f, 1.f);
				toolDescription.i_progress_quad.rounding = 2;

				toolDescription.i_progress_bg_quad = new Quad();
				toolDescription.i_progress_bg_quad.pos.x = 1080 - 300;
				toolDescription.i_progress_bg_quad.pos.y = y - 10.5f;
				toolDescription.i_progress_bg_quad.size.x = 208;
				toolDescription.i_progress_bg_quad.size.y = 16.5f;
				toolDescription.i_progress_bg_quad.target_pos.x = toolDescription.i_progress_bg_quad.pos.x;
				toolDescription.i_progress_bg_quad.target_pos.y = toolDescription.i_progress_bg_quad.pos.y;
				toolDescription.i_progress_bg_quad.target_size.x = toolDescription.i_progress_bg_quad.size.x;
				toolDescription.i_progress_bg_quad.target_size.y = toolDescription.i_progress_bg_quad.size.y;
				toolDescription.i_progress_bg_quad.color = new Vector4f(.4f, .4f, .4f, 1.f);
				toolDescription.i_progress_bg_quad.rounding = 2;

				toolDescription.run_quad = new Quad();
				toolDescription.run_quad.pos.x = 1080 - 150;
				toolDescription.run_quad.pos.y = y - 20;
				toolDescription.run_quad.size.x = 60;
				toolDescription.run_quad.size.y = 30;
				toolDescription.run_quad.target_pos.x = toolDescription.run_quad.pos.x;
				toolDescription.run_quad.target_pos.y = toolDescription.run_quad.pos.y;
				toolDescription.run_quad.target_size.x = toolDescription.run_quad.size.x;
				toolDescription.run_quad.target_size.y = toolDescription.run_quad.size.y;
				toolDescription.run_quad.color = new Vector4f(0.18f, 0.18f, 0.18f, 1.0f);
				toolDescription.run_quad.rounding = 2;

				toolDescription.delete_quad = new Quad();
				toolDescription.delete_quad.pos.x = 1080 - 220;
				toolDescription.delete_quad.pos.y = y - 20;
				toolDescription.delete_quad.size.x = 60;
				toolDescription.delete_quad.size.y = 30;
				toolDescription.delete_quad.target_pos.x = toolDescription.delete_quad.pos.x;
				toolDescription.delete_quad.target_pos.y = toolDescription.delete_quad.pos.y;
				toolDescription.delete_quad.target_size.x = toolDescription.delete_quad.size.x;
				toolDescription.delete_quad.target_size.y = toolDescription.delete_quad.size.y;
				toolDescription.delete_quad.color = new Vector4f(0.18f, 0.18f, 0.18f, 1.0f);
				toolDescription.delete_quad.rounding = 2;

				toolDescription.label_ypos = y;
				toolDescription.label_ypos_target = y;

				JSONArray platforms = (JSONArray) tool.getOrDefault("supportedPlatforms", new JSONArray());
				List<String> plats = new ArrayList<>();
				for (Object p : platforms) plats.add((String) p);
				toolDescription.platforms = new String[plats.size()];
				plats.toArray(toolDescription.platforms);

				JSONObject run_commands_platform = (JSONObject) tool.getOrDefault("run", null);
				toolDescription.run_commands = new String[plats.size()];
				for (int i = 0; i < toolDescription.platforms.length; i++) {
					String command = (String) run_commands_platform.getOrDefault(toolDescription.platforms[i], "echo \"No Run Command Specified for tool for this OS\"");
					toolDescription.run_commands[i] = command;
				}

				JSONObject filenames_platform = (JSONObject) tool.getOrDefault("file", null);
				toolDescription.files = new String[plats.size()];
				for (int i = 0; i < toolDescription.platforms.length; i++) {
					String filename = (String) filenames_platform.getOrDefault(toolDescription.platforms[i], "echo \"No Run Command Specified for tool for this OS\"");
					toolDescription.files[i] = filename;
				}

				tools.add(toolDescription);

				y += 60;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		inconsolata = new TTFont("Inconsolata.ttf", 30);
		inconsolata_smaller = new TTFont("Inconsolata.ttf", 15);
		current = null;
	}

	private static int getOsIndex(ToolDescription desc, OsUtil.OS os) {
		String[] platforms = desc.platforms;
		for (int i = 0, platformsLength = platforms.length; i < platformsLength; i++) {
			String platform = platforms[i];
			if (platform.equals(os.name()))
				return i;
		}
		return -1;
	}

	public static void run(ToolDescription desc) throws Exception {
		int idx = getOsIndex(desc, OsUtil.getOS());

		Runtime rt = Runtime.getRuntime();

		OsUtil.OS os = OsUtil.getOS();
		switch (os) {
			case Windows: {
				Process process = rt.exec("cmd /c start cmd.exe /k \"" + desc.run_commands[idx] + "\"");
			}
			case Linux: {
				String[][] terminals = {
						{"gnome-terminal", "--"},
						{"xterm", "-e"},
						{"konsole", "-e"}
				};
				for (String[] i : terminals) {
					try {
						Process process = new ProcessBuilder(i[0], i[1], desc.run_commands[idx]).start();
						System.out.println("Successfully launched using " + i[0]);
					} catch (Exception e) {
						System.out.println("Failed to launch using " + i[0]);
					}
				}
			}
		}
	}

	public static void install(ToolDescription desc, List<ToolDescription> installed_list, boolean is_update) throws Exception {
		if (processing == null) {
			int idx = getOsIndex(desc, OsUtil.getOS());
			if (idx == -1) {
				// Temporary
				System.err.println("Tool doesn't support " + OsUtil.getOS().name());
				return;
			}

			prj_name = desc.name;
			processing = desc;

			// download to file
			System.out.println("Downloading: " + desc.files[idx]);
			new Thread(Tools::download).start();

			// add to installed
			if (!is_update) {
				installed_list.add(desc);
			} else {
				for (ToolDescription td : installed_list) {
					if (td.name.equals(desc.name))
						td.set(desc);
				}
			}
		}
	}

	private static void download() {
		try {
			int idx = getOsIndex(processing, OsUtil.getOS());
			String url_s = processing.github_link + "/releases/download/" + processing.version + "/" + processing.files[idx];

			URL url = new URL(url_s);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			long file_size = connection.getContentLengthLong();
			BufferedInputStream input = new BufferedInputStream(connection.getInputStream());
			File out_f = new File(OsUtil.getDownloadFilepath(prj_name) + processing.files[idx]);
			System.out.println(out_f);
			out_f.getParentFile().mkdirs();
			FileOutputStream output_file = new FileOutputStream(out_f);
			BufferedOutputStream output = new BufferedOutputStream(output_file, 1024);
			byte[] buffer = new byte[1024];
			double downloaded = 0.0;
			int read = 0;
			processed_percentage = 0.0;
			while ((read = input.read(buffer, 0, 1024)) >= 0) {
				output.write(buffer, 0, read);
				downloaded += read;
				processed_percentage = downloaded * 100 / file_size;
			}
			input.close();
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		processing = null;
		prj_name = null;
	}

	public static void delete(ToolDescription desc, List<ToolDescription> installed_list) throws Exception {
		int idx = getOsIndex(desc, OsUtil.getOS());
		if (idx == -1) {
			// Temporary
			System.err.println("Tool doesn't support " + OsUtil.getOS().name());
			return;
		}

		prj_name = desc.name;
		processing = desc;
		System.out.println("Deleting: " + desc.files[idx]);
		new Thread(Tools::deleteFolder).start();

		int index_to_remove = -1;
		for (int i = 0, installed_listSize = installed_list.size(); i < installed_listSize; i++) {
			ToolDescription toolDescription = installed_list.get(i);
			if (toolDescription.name.equals(desc.name)) {
				index_to_remove = i;
				break;
			}
		}
		if (index_to_remove != -1)
			installed_list.remove(index_to_remove);
	}

	private static void deleteFolder() {
		try {
			int idx = getOsIndex(processing, OsUtil.getOS());
			File base_folder = new File(OsUtil.getToolboxFilepath() + processing.name);
			if (!base_folder.isDirectory()) throw new IOException("Trying to delete non existent directory. Launcher error!!");
			File[] files = base_folder.listFiles();
			if (files != null) {
				int sz = files.length;
				int i = 0;
				for (File file : files) {
					file.delete();
					i++;
					processed_percentage = (float)i / (float)sz;
				}
			}
			base_folder.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		processing = null;
		prj_name = null;
	}

	public static void render(Renderer renderer, float dt, List<ToolDescription> installed, float scroll) throws Exception {
		float y = startY;
		boolean shift_next = false;
		for (ToolDescription desc : Tools.tools) {
			if (desc == processing) {
				desc.i_progress_quad.target_size.x = ((float) processed_percentage * 2.f);
				desc.download_coyote = 1000; // keep download widget up for 1000 more frames
			} else {
				if (desc.download_coyote != 0)
					desc.download_coyote--;
				else {
					desc.i_progress_quad.target_size.x = 0;
					desc.i_progress_quad.size.x = 0;
				}
			}

			if (shift_next) {
				if (!desc.was_pushed) {
					desc.label_ypos_target += 50;
					desc.back_quad.target_pos.y += 50;
					desc.status_quad.target_pos.y += 50;
					desc.install_quad.target_pos.y += 50;
					desc.delete_quad.target_pos.y += 50;
					desc.run_quad.target_pos.y += 50;
					desc.i_progress_quad.target_pos.y += 50;
					desc.i_progress_bg_quad.target_pos.y += 50;
					desc.was_pushed = true;
				}
			} else {
				if (desc.was_pushed) {
					desc.label_ypos_target -= 50;
					desc.back_quad.target_pos.y -= 50;
					desc.status_quad.target_pos.y -= 50;
					desc.install_quad.target_pos.y -= 50;
					desc.delete_quad.target_pos.y -= 50;
					desc.run_quad.target_pos.y -= 50;
					desc.i_progress_quad.target_pos.y -= 50;
					desc.i_progress_bg_quad.target_pos.y -= 50;
					desc.was_pushed = false;
				}
			}

			desc.step(dt);

			if (current == desc) {
				desc.back_quad.target_size.y = 100;
				shift_next = true;
			} else {
				desc.back_quad.target_size.y = 50;
			}

			boolean newest = false;
			boolean older = false;
			for (ToolDescription curr : installed) {
				if (curr.name.equals(desc.name)) {
					if (curr.version.equals(desc.version)) {
						desc.status_quad.color.set(0.4f, 0.8f, 0.639215686f, 1.0f);
						newest = true;
					} else {
						desc.status_quad.color.set(1.0f, 0.839215686f, 0.57421875f, 1.0f);
						older = true;
					}
					break;
				}
			}
			if (!(newest || older))
				desc.status_quad.color.set(0.8f, 0.2f, 0.3f, 1.0f);

			if (desc.back_quad.testPoint(Input.mouseX, Input.mouseY + scroll)) {
				if (desc.install_quad.testPoint(Input.mouseX, Input.mouseY + scroll)) {
					if (Input.mouseButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
						if (!newest)
							install(desc, installed, older);
					}
					if (!newest) {
						desc.install_quad.color.set(0.25f, 0.25f, 0.25f, 1.0f);
					}
					desc.back_quad.color.set(0.15f, 0.15f, 0.15f, 1.0f);
					desc.run_quad.color.set(0.18f, 0.18f, 0.18f, 1.0f);
					desc.delete_quad.color.set(0.18f, 0.18f, 0.18f, 1.0f);
				} else if (desc.run_quad.testPoint(Input.mouseX, Input.mouseY + scroll) && (newest || older)) {
					if (Input.mouseButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
						if (!(desc == processing || desc.download_coyote != 0) && (newest || older)) {
							run(desc);
						}
					}
					desc.back_quad.color.set(0.15f, 0.15f, 0.15f, 1.0f);
					desc.install_quad.color.set(0.18f, 0.18f, 0.18f, 1.0f);
					desc.run_quad.color.set(0.25f, 0.25f, 0.25f, 1.0f);
					desc.delete_quad.color.set(0.18f, 0.18f, 0.18f, 1.0f);
				} else if (desc.delete_quad.testPoint(Input.mouseX, Input.mouseY + scroll) && (newest || older)) {
					if (Input.mouseButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
						if (!(desc == processing || desc.download_coyote != 0) && (newest || older)) {
							delete(desc, installed);
						}
					}
					desc.back_quad.color.set(0.15f, 0.15f, 0.15f, 1.0f);
					desc.install_quad.color.set(0.18f, 0.18f, 0.18f, 1.0f);
					desc.run_quad.color.set(0.18f, 0.18f, 0.18f, 1.0f);
					desc.delete_quad.color.set(0.25f, 0.25f, 0.25f, 1.0f);
				} else {
					if (Input.mouseButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
						if (current == desc)
							current = null;
						else current = desc;
					}
					desc.install_quad.color.set(0.18f, 0.18f, 0.18f, 1.0f);
					desc.back_quad.color.set(0.25f, 0.25f, 0.25f, 1.0f);
					desc.run_quad.color.set(0.18f, 0.18f, 0.18f, 1.0f);
					desc.delete_quad.color.set(0.18f, 0.18f, 0.18f, 1.0f);
				}
			} else {
				desc.back_quad.color.set(0.15f, 0.15f, 0.15f, 1.0f);
				desc.install_quad.color.set(0.18f, 0.18f, 0.18f, 1.0f);
				desc.run_quad.color.set(0.18f, 0.18f, 0.18f, 1.0f);
				desc.delete_quad.color.set(0.18f, 0.18f, 0.18f, 1.0f);
			}

			renderer.drawQuad(desc.back_quad);
			renderer.drawQuad(desc.status_quad);
			renderer.drawQuad(desc.install_quad);
			{
				if (newest) {
					renderer.drawString(inconsolata_smaller, "Update", 1080 - 70, desc.label_ypos, 1080, new Vector4f(0.4f, 0.4f, 0.4f, 1.0f));
				} else if (older) {
					renderer.drawString(inconsolata_smaller, "Update", 1080 - 70, desc.label_ypos, 1080, new Vector4f(1.0f, 1.0f, 1.0f, 1.0f));
				} else {
					renderer.drawString(inconsolata_smaller, "Install", 1080 - 75, desc.label_ypos, 1080, new Vector4f(1.0f, 1.0f, 1.0f, 1.0f));
				}
			}

			if (desc == processing || desc.download_coyote != 0) {
				renderer.drawQuad(desc.i_progress_bg_quad);
				renderer.drawQuad(desc.i_progress_quad);
			} else {
				if (newest || older) {
					renderer.drawQuad(desc.run_quad);
					renderer.drawQuad(desc.delete_quad);
					renderer.drawString(inconsolata_smaller, "Run", 1080 - 130, desc.label_ypos, new Vector4f(1.f, 1.f, 1.f, 1.f));
					renderer.drawString(inconsolata_smaller, "Delete", 1080 - 210, desc.label_ypos, 1080, new Vector4f(1.0f, 1.0f, 1.0f, 1.0f));
				}
			}

			if (current == desc) {
				renderer.drawString(inconsolata_smaller, desc.desc, 50, desc.label_ypos + 30, new Vector4f(0.9f, 0.9f, 0.9f, 1.0f));
			}
			renderer.drawString(inconsolata, desc.name, 70, desc.label_ypos, new Vector4f(1.f, 0.8431372549f, .0f, 1.f));
			y += 60;
		}
	}
}