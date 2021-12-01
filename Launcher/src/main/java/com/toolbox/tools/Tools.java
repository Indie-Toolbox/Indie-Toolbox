package com.toolbox.tools;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;

public class Tools {
	public static final List<ToolDescription> tools = new ArrayList<>();

	public static void load(String src) {
		try {
			JSONParser parser = new JSONParser();
			JSONArray obj = (JSONArray)parser.parse(src);
			for (Object tool_obj : obj) {
				ToolDescription toolDescription = new ToolDescription();
				JSONObject tool = (JSONObject) tool_obj;
				toolDescription.name = (String) tool.getOrDefault("name", "no-name");
				toolDescription.version = (String) tool.getOrDefault("version", "0.0.0");
				toolDescription.title = (String) tool.getOrDefault("title", "no-title");
				toolDescription.desc = (String) tool.getOrDefault("description", "no-description");
				toolDescription.github_link = (String) tool.getOrDefault("git", "https://github.com/INVALID");

				JSONArray platforms = (JSONArray) tool.getOrDefault("supportedPlatforms", new JSONArray());
				List<String> plats = new ArrayList<>();
				for (Object p : platforms) plats.add((String)p);
				toolDescription.platforms = new String[plats.size()];
				plats.toArray(toolDescription.platforms);
				tools.add(toolDescription);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}