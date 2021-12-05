package com.toolbox.tools;

import com.toolbox.font.TTFont;
import com.toolbox.render.Quad;
import com.toolbox.render.Renderer;
import com.toolbox.util.Input;
import org.joml.Vector4f;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class Tools {
	public static final List<ToolDescription> tools = new ArrayList<>();
	private static final float startY = 103;
	public static TTFont inconsolata;
	public static TTFont inconsolata_smaller;
	private static ToolDescription current;

	public static void load(String src) {
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

				toolDescription.label_ypos = y;
				toolDescription.label_ypos_target = y;

				JSONArray platforms = (JSONArray) tool.getOrDefault("supportedPlatforms", new JSONArray());
				List<String> plats = new ArrayList<>();
				for (Object p : platforms) plats.add((String) p);
				toolDescription.platforms = new String[plats.size()];
				plats.toArray(toolDescription.platforms);
				tools.add(toolDescription);
				y += 60;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		inconsolata = new TTFont("src/main/resources/Inconsolata.ttf", 30);
		inconsolata_smaller = new TTFont("src/main/resources/Inconsolata.ttf", 15);
		current = null;
	}

	public static void render(Renderer renderer, float dt, List<ToolDescription> installed) {
		float y = startY;
		boolean shift_next = false;
		for (ToolDescription desc : Tools.tools) {
			if (shift_next) {
				if (!desc.was_pushed) {
					desc.label_ypos_target += 50;
					desc.back_quad.target_pos.y += 50;
					desc.status_quad.target_pos.y += 50;
					desc.install_quad.target_pos.y += 50;
					desc.was_pushed = true;
				}
			} else {
				if (desc.was_pushed) {
					desc.label_ypos_target -= 50;
					desc.back_quad.target_pos.y -= 50;
					desc.status_quad.target_pos.y -= 50;
					desc.install_quad.target_pos.y -= 50;
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

			if (desc.back_quad.testPoint(Input.mouseX, Input.mouseY)) {
				if (desc.install_quad.testPoint(Input.mouseX, Input.mouseY)) {
					if (Input.mouseButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {

					}
					desc.install_quad.color.set(0.25f, 0.25f, 0.25f, 1.0f);
					desc.back_quad.color.set(0.15f, 0.15f, 0.15f, 1.0f);
				} else {
					if (Input.mouseButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
						if (current == desc)
							current = null;
						else current = desc;
					}
					desc.install_quad.color.set(0.18f, 0.18f, 0.18f, 1.0f);
					desc.back_quad.color.set(0.25f, 0.25f, 0.25f, 1.0f);
				}
			} else {
				desc.back_quad.color.set(0.15f, 0.15f, 0.15f, 1.0f);
				desc.install_quad.color.set(0.18f, 0.18f, 0.18f, 1.0f);
			}

			renderer.drawQuad(desc.back_quad);
			renderer.drawQuad(desc.status_quad);
			renderer.drawQuad(desc.install_quad);
			{
				if (newest) {
					renderer.drawString(inconsolata_smaller, "Update", 1080 - 70, desc.label_ypos, 1080, new Vector4f(0.6f, 0.6f, 0.6f, 1.0f));
				} else if (older) {
					renderer.drawString(inconsolata_smaller, "Update", 1080 - 70, desc.label_ypos, 1080, new Vector4f(1.0f, 1.0f, 1.0f, 1.0f));
				} else {
					renderer.drawString(inconsolata_smaller, "Install", 1080 - 75, desc.label_ypos, 1080, new Vector4f(1.0f, 1.0f, 1.0f, 1.0f));
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