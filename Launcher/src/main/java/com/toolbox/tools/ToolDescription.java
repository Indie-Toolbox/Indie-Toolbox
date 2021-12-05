package com.toolbox.tools;

import com.toolbox.render.Quad;

import java.util.Arrays;
import java.util.Objects;

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

	public float label_ypos;
	public float label_ypos_target;
	public Quad back_quad;
	public Quad status_quad;
	public Quad install_quad;
	public Quad run_quad;
	public boolean was_pushed = false;

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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ToolDescription that = (ToolDescription) o;
		return name.equals(that.name) &&
				version.equals(that.version) &&
				title.equals(that.title) &&
				desc.equals(that.desc) &&
				github_link.equals(that.github_link) &&
				Arrays.equals(platforms, that.platforms);
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(name, version, title, desc, github_link);
		result = 31 * result + Arrays.hashCode(platforms);
		return result;
	}

	public ToolDescription makeClone() {
		ToolDescription d = new ToolDescription();
		d.name = name;
		d.version = version;
		d.title = title;
		d.desc = desc;
		d.github_link = github_link;
		d.platforms = platforms;
		return d;
	}

	public void step(float dt) {
		back_quad.step(dt);
		status_quad.step(dt);
		install_quad.step(dt);
		label_ypos += (label_ypos_target - label_ypos) * 20 * dt;
	}
}
