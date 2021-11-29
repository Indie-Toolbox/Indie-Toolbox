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

	public Quad(Vector2f pos, Vector2f size, Vector2f uvs, Vector2f uvs_size, Vector4f color, float rounding) {
		this.pos = pos;
		this.size = size;
		this.uvs = uvs;
		this.uvs_size = uvs_size;
		this.color = color;
		this.rounding = rounding;
	}
}
