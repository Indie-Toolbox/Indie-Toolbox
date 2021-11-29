package com.toolbox.util;

import org.joml.Vector2f;

public class Rect {
	public float x;
	public float y;
	public float width;
	public float height;

	public Rect() {
		x = 0;
		y = 0;
		width = 1;
		height = 1;
	}

	public Rect(Vector2f a, Vector2f e) {
		x = a.x;
		y = a.y;
		width = e.x;
		height = e.y;
	}

	public Rect(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public Rect(double x, double y, double width, double height) {
		this.x = (float) x;
		this.y = (float) y;
		this.width = (float) width;
		this.height = (float) height;
	}

	public void set(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public void set(double x, double y, double width, double height) {
		this.x = (float) x;
		this.y = (float) y;
		this.width = (float) width;
		this.height = (float) height;
	}

	public Rect scale(float sx, float sy) {
		width *= sx;
		height *= sy;
		return this;
	}

	public Rect div(float dx, float dy) {
		x /= dx;
		width /= dx;
		y /= dy;
		height /= dy;
		return this;
	}
}
