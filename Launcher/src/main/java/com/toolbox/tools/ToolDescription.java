package com.toolbox.tools;

import java.util.Arrays;

public class ToolDescription {
//	"p5pj": {
//		"version": "0b1.0.0",
//		"title": "P5.js Project Manager",
//		"description": "A simple and easy to use boilerplate code generator for p5.js",
//		"git": "https://github.com/pricter/p5pj",
//		"supportedPlatforms": [
//			"Windows",
//			"Linux"
//    	]
//	}
	public String name;
	public String version;
	public String title;
	public String desc;
	public String github_link;
	public String[] platforms;

	public ToolDescription() {}

	@Override
	public String toString() {
		return "ToolDescription{" +
				"version='" + version + '\'' +
				", title='" + title + '\'' +
				", desc='" + desc + '\'' +
				", github_link='" + github_link + '\'' +
				", platforms=" + Arrays.toString(platforms) +
				'}';
	}
}
