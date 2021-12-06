package com.toolbox.render;

import com.toolbox.font.TTFont;
import com.toolbox.util.Rect;
import org.joml.Vector4f;
import org.lwjgl.stb.STBTTPackedchar;

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
		drawQuad(q, Texture.White);
	}

	public void drawQuad(Quad q, Texture texture) {
		boolean submitted = false;
		for (int i = 0; i < curr_bucket_index; i++) {
			RenderBucket bucket = buckets.get(i);
			if (bucket.isFullCuzTextures) {
				if (bucket.textures.contains(texture)) {
					bucket.submitQuad(new Rect(q.pos, q.size), new Rect(q.uvs, q.uvs_size), q.color, q.rounding, texture);
					submitted = true;
					break;
				}
			}
		}
		if (!submitted)
			buckets.get(curr_bucket_index).submitQuad(new Rect(q.pos, q.size), new Rect(q.uvs, q.uvs_size), q.color, q.rounding, texture);
		if (!buckets.get(curr_bucket_index).hasRoom) {
			curr_bucket_index++;
			if (curr_bucket_index >= buckets.size())
				buckets.add(new RenderBucket());
		}
	}

	public void drawString(TTFont font, String s, float x, float y, Vector4f color) {
		drawString(font, s, x, y, 1080 - 40, color);
	}

	public void drawString(TTFont font, String s, float x, float y, float wrap_x, Vector4f color) {
		float sx = x;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) >= 32 && s.charAt(i) < 128) {
				STBTTPackedchar info = font.cdata.get(s.charAt(i) - 32);
				Quad q = new Quad();
				q.color = color;
				q.pos.x = x + info.xoff();
				q.pos.y = y + info.yoff();
				q.size.x = info.xoff2() - info.xoff();
				q.size.y = info.yoff2() - info.yoff();
				q.uvs.x = (info.x0()) / 512.f;
				q.uvs.y = (info.y0()) / 512.f;
				q.uvs_size.x = (info.x1() - info.x0()) / 512.f;
				q.uvs_size.y = (info.y1() - info.y0()) / 512.f;
				q.rounding = 0;
				drawQuad(q, font.texture);
				x += info.xadvance();
				if (x >= wrap_x) {
					x = sx;
					y += font.scale * (font.ascent - font.descent);
				}
			}
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
