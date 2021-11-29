package com.toolbox.render;

import com.toolbox.util.Rect;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;

public class Renderer {
	private final ArrayList<RenderBucket> buckets;
	private int curr_bucket_index;

	public Renderer() {
		buckets = new ArrayList<>(1);
		buckets.add(new RenderBucket());
		curr_bucket_index = 0;
	}

	public void immediateBegin() {
		for (RenderBucket bucket : buckets) bucket.begin();
		curr_bucket_index = 0;
	}

	public void drawQuad(Quad q) {
		boolean submitted = false;
		for (int i = 0; i < curr_bucket_index; i++) {
			RenderBucket bucket = buckets.get(i);
			if (bucket.isFullCuzTextures) {
				if (bucket.textures.contains(Texture.White)) {
					bucket.submitQuad(new Rect(q.pos, q.size), new Rect(q.uvs, q.uvs_size), q.color, q.rounding, Texture.White);
					submitted = true;
					break;
				}
			}
		}
		if (!submitted)
			buckets.get(curr_bucket_index).submitQuad(new Rect(q.pos, q.size), new Rect(q.uvs, q.uvs_size), q.color, q.rounding, Texture.White);
		if (!buckets.get(curr_bucket_index).hasRoom) {
			curr_bucket_index++;
			if (curr_bucket_index >= buckets.size())
				buckets.add(new RenderBucket());
		}
	}

	public void immediateEnd() {
		for (int i = 0; i <= curr_bucket_index; i++) {
			RenderBucket bucket = buckets.get(curr_bucket_index);
			bucket.bind();
			bucket.updateBuffer();
			glDrawArrays(GL_TRIANGLES, 0, bucket.getVertexCount());
			bucket.unbind();
		}
	}
}
