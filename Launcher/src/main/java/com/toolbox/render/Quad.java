package com.toolbox.render;

import org.joml.Vector2f;
import org.joml.Vector4f;

public class Quad {
	public Vector2f pos;
	public Vector2f size;
	public Vector2f target_pos;
	public Vector2f target_size;
	public Vector2f uvs;
	public Vector2f uvs_size;
	public Vector4f color;
	public float rounding;
	public float speed;

	public Quad() {
		this.pos = new Vector2f();
		this.size = new Vector2f(1);
		this.target_pos = new Vector2f();
		this.target_size = new Vector2f(1);
		this.uvs = new Vector2f();
		this.uvs_size = new Vector2f(1);
		this.color = new Vector4f();
		this.rounding = 0;
		this.speed = 20;
	}

	public Quad(Vector2f pos, Vector2f size, Vector2f uvs, Vector2f uvs_size, Vector4f color, float rounding) {
		this.pos = pos;
		this.size = size;
		this.uvs = uvs;
		this.uvs_size = uvs_size;
		this.color = color;
		this.rounding = rounding;
	}

	public void step(float dt) {
		pos.x += (target_pos.x - pos.x) * speed * dt;
		pos.y += (target_pos.y - pos.y) * speed * dt;
		size.x += (target_size.x - size.x) * speed * dt;
		size.y += (target_size.y - size.y) * speed * dt;
	}

	public boolean testPoint(float x, float y) {
		return (x > pos.x && y > pos.y) && (x < pos.x + size.x && y < pos.y + size.y);
	}
}
