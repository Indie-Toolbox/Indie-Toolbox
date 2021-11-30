package com.toolbox.render;

import org.joml.Vector2f;
import org.joml.Vector4f;

public class Quad {
	public Vector2f pos;
	public Vector2f size;
	public Vector2f uvs;
	public Vector2f uvs_size;
	public Vector4f color;
	public float rounding;

	public Quad() {
		this.pos = new Vector2f();
		this.size = new Vector2f();
		this.uvs = new Vector2f();
		this.uvs_size = new Vector2f();
		this.color = new Vector4f();
		this.rounding = 0;
	}

	public Quad(Vector2f pos, Vector2f size, Vector2f uvs, Vector2f uvs_size, Vector4f color, float rounding) {
		this.pos = pos;
		this.size = size;
		this.uvs = uvs;
		this.uvs_size = uvs_size;
		this.color = color;
		this.rounding = rounding;
	}
}
