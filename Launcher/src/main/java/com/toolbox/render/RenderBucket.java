package com.toolbox.render;

import com.toolbox.util.Rect;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL30.*;

public class RenderBucket {
	private static final Vector4f[] BASE_VERTEX_POSITIONS = new Vector4f[] {
			new Vector4f(-0.5f, -0.5f, 0.0f, 1.0f),
			new Vector4f( 0.5f, -0.5f, 0.0f, 1.0f),
			new Vector4f( 0.5f,  0.5f, 0.0f, 1.0f),
			new Vector4f(-0.5f, -0.5f, 0.0f, 1.0f),
			new Vector4f( 0.5f,  0.5f, 0.0f, 1.0f),
			new Vector4f(-0.5f,  0.5f, 0.0f, 1.0f),
	};

	private static final int POS_COUNT = 2;
	private static final int UV_COUNT = 2;
	private static final int COLOUR_COUNT = 4;
	private static final int TEXINDEX_COUNT = 1;
	private static final int ROUNDING_COUNT = 1;
	private static final int QUAD_DIMS_COUNT = 2;

	private static final int POS_OFFSET = 0;
	private static final int UV_OFFSET = POS_OFFSET + POS_COUNT * Float.BYTES;
	private static final int COLOUR_OFFSET = UV_OFFSET + UV_COUNT * Float.BYTES;
	private static final int TEXINDEX_OFFSET = COLOUR_OFFSET + COLOUR_COUNT * Float.BYTES;
	private static final int ROUNDING_OFFSET = TEXINDEX_OFFSET + TEXINDEX_COUNT * Float.BYTES;
	private static final int QUAD_DIMS_OFFSET = ROUNDING_OFFSET + ROUNDING_COUNT * Float.BYTES;

	private static final int VERTEX_COUNT = 12;
	private static final int VERTEX_SIZE = VERTEX_COUNT * Float.BYTES;
	public List<Texture> textures;

	public boolean hasRoom;
	public boolean isFullCuzTextures;

	private float[] data;
	private int spriteCount;
	private int textureIndex;
	private static final int MAX_BATCH_SIZE = 1000;

	private final int vbo;
	private final int vao;

	public RenderBucket() {
		spriteCount = 0;
		hasRoom = true;
		isFullCuzTextures = false;
		textureIndex = 0;
		textures = new ArrayList<>();

		vao = glGenVertexArrays();
		glBindVertexArray(vao);
		vbo = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, MAX_BATCH_SIZE * 6 * VERTEX_SIZE, GL_DYNAMIC_DRAW);

		glVertexAttribPointer(0, POS_COUNT, GL_FLOAT, false, VERTEX_SIZE, POS_OFFSET);
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(1, UV_COUNT, GL_FLOAT, false, VERTEX_SIZE, UV_OFFSET);
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(2, COLOUR_COUNT, GL_FLOAT, false, VERTEX_SIZE, COLOUR_OFFSET);
		glEnableVertexAttribArray(2);
		glVertexAttribPointer(3, TEXINDEX_COUNT, GL_FLOAT, false, VERTEX_SIZE, TEXINDEX_OFFSET);
		glEnableVertexAttribArray(3);
		glVertexAttribPointer(4, ROUNDING_COUNT, GL_FLOAT, false, VERTEX_SIZE, ROUNDING_OFFSET);
		glEnableVertexAttribArray(4);
		glVertexAttribPointer(5, QUAD_DIMS_COUNT, GL_FLOAT, false, VERTEX_SIZE, QUAD_DIMS_OFFSET);
		glEnableVertexAttribArray(5);
	}

	public void begin() {
		spriteCount = 0;
		data = new float[MAX_BATCH_SIZE * 6 * VERTEX_COUNT];
		textureIndex = 0;
	}

	public void submitQuad(Rect verts, Rect uvRect, Vector4f colour, float rounding, Texture texture) {
		if (hasRoom) {
			int texIndex;

			if (textures.contains(texture)) {
				texIndex = textures.indexOf(texture);
			} else {
				textures.add(texture);
				texIndex = textureIndex++;
				if (texture.boundslot != -1) texIndex = texture.boundslot;
			}

			Vector2f[] vertices = new Vector2f[] {
					new Vector2f(verts.x, verts.y),
					new Vector2f(verts.x + verts.width, verts.y),
					new Vector2f(verts.x + verts.width, verts.y + verts.height),
					new Vector2f(verts.x, verts.y),
					new Vector2f(verts.x + verts.width, verts.y + verts.height),
					new Vector2f(verts.x, verts.y + verts.height),
			};

			int index = spriteCount++;
			int offset = index * 6 * VERTEX_COUNT;

			StoreData(offset, vertices, texIndex, uvRect, colour, rounding, verts.width, verts.height);

			if (textureIndex >= 8) {
				hasRoom = false;
				isFullCuzTextures = true;
			}
			if (spriteCount >= MAX_BATCH_SIZE) {
				hasRoom = false;
				isFullCuzTextures = false;
			}
		}
	}

	private void StoreData(int offset, Vector2f[] vertexPositions, int textureIndex, Rect uvRect, Vector4f colour, float rounding, float width, float height) {
		StoreVertex(offset, vertexPositions[0].x, vertexPositions[0].y, uvRect.x, uvRect.y, colour.x, colour.y, colour.z, colour.w, textureIndex, rounding, width, height);
		StoreVertex(offset + VERTEX_COUNT, vertexPositions[1].x, vertexPositions[1].y, uvRect.x + uvRect.width, uvRect.y, colour.x, colour.y, colour.z, colour.w, textureIndex, rounding, width, height);
		StoreVertex(offset + VERTEX_COUNT * 2, vertexPositions[2].x, vertexPositions[2].y, uvRect.x + uvRect.width, uvRect.y + uvRect.height, colour.x, colour.y, colour.z, colour.w, textureIndex, rounding, width, height);
		StoreVertex(offset + VERTEX_COUNT * 3, vertexPositions[3].x, vertexPositions[3].y, uvRect.x, uvRect.y, colour.x, colour.y, colour.z, colour.w, textureIndex, rounding, width, height);
		StoreVertex(offset + VERTEX_COUNT * 4, vertexPositions[4].x, vertexPositions[4].y, uvRect.x + uvRect.width, uvRect.y + uvRect.height, colour.x, colour.y, colour.z, colour.w, textureIndex, rounding, width, height);
		StoreVertex(offset + VERTEX_COUNT * 5, vertexPositions[5].x, vertexPositions[5].y, uvRect.x, uvRect.y + uvRect.height, colour.x, colour.y, colour.z, colour.w, textureIndex, rounding, width, height);
	}

	private void StoreVertex(int offset, float x, float y, float u, float v, float r, float g, float b, float a, int t, float ro, float w, float h) {
		data[offset] = x;
		data[offset + 1] = y;
		data[offset + 2] = u;
		data[offset + 3] = v;
		data[offset + 4] = r;
		data[offset + 5] = g;
		data[offset + 6] = b;
		data[offset + 7] = a;
		data[offset + 8] = t;
		data[offset + 9] = ro;
		data[offset + 10] = w;
		data[offset + 11] = h;
	}

	public void updateBuffer() {
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferSubData(GL_ARRAY_BUFFER, 0, data);
	}

	public void bind() {
		glBindVertexArray(vao);
		for (int i = 0; i < textures.size(); i++)
			textures.get(i).bindToSlot(i);
	}

	public void unbind() {
		for (Texture texture : textures)
			texture.unbind();
		glBindVertexArray(0);
	}

	public void delete() {
		glDeleteBuffers(vbo);
		glDeleteVertexArrays(vao);
	}

	public int getVertexCount() {
		return spriteCount * 6;
	}
}
