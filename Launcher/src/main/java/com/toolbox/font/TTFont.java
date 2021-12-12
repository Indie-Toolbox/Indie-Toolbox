package com.toolbox.font;

import com.toolbox.render.Texture;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTTPackContext;
import org.lwjgl.stb.STBTTPackedchar;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.stb.STBTruetype.*;
import static org.lwjgl.system.MemoryUtil.memSlice;

public class TTFont {
	public Texture texture;
	public STBTTPackedchar.Buffer cdata;
	public float scale;
	public int ascent;
	public int descent;
	public int baseline;
	public float size;
	public String filepath;

	public TTFont(String filepath, float size) {
		this.filepath = filepath;
		this.size = size;
		try {

			cdata = STBTTPackedchar.calloc(96);
			ByteBuffer data_buffer = ioResourceToByteBuffer(this.filepath, 512*1024);
			STBTTFontinfo stb_font_info = STBTTFontinfo.create();

			try (MemoryStack stack = MemoryStack.stackPush()) {
				ByteBuffer temp_bitmap = BufferUtils.createByteBuffer(512 * 512);
				STBTTPackContext stb_pack_ctx = STBTTPackContext.malloc(stack);

				stbtt_InitFont(stb_font_info, data_buffer);
				stbtt_PackBegin(stb_pack_ctx, temp_bitmap, 512, 512, 0, 4);
				stbtt_PackSetOversampling(stb_pack_ctx, 1, 1);
				stbtt_PackFontRange(stb_pack_ctx, data_buffer, 0, this.size, 32, cdata);
				stbtt_PackEnd(stb_pack_ctx);

				int id = glGenTextures();

				glBindTexture(GL_TEXTURE_2D, id);
				glTexImage2D(GL_TEXTURE_2D, 0, GL_RED, 512, 512, 0, GL_RED, GL_UNSIGNED_BYTE, temp_bitmap);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
				int[] swizzles = { GL_ONE, GL_ONE, GL_ONE, GL_RED };
				glTexParameteriv(GL_TEXTURE_2D, GL_TEXTURE_SWIZZLE_RGBA, swizzles);

				this.texture = Texture.wrap(id);
				this.scale = stbtt_ScaleForPixelHeight(stb_font_info, size);
				IntBuffer ascent = stack.ints(0);
				IntBuffer descent = stack.ints(0);
				stbtt_GetFontVMetrics(stb_font_info, ascent, descent, null);
				this.ascent = ascent.get();
				this.descent = descent.get();
				this.baseline = (int)(this.ascent * this.scale);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
		ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
		buffer.flip();
		newBuffer.put(buffer);
		return newBuffer;
	}

	public static ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException {
		ByteBuffer buffer;

		Path path = Paths.get(resource);
		if (Files.isReadable(path)) {
			try (SeekableByteChannel fc = Files.newByteChannel(path)) {
				buffer = BufferUtils.createByteBuffer((int)fc.size() + 1);
				while (fc.read(buffer) != -1) {
					;
				}
			}
		} else {
			try (
					InputStream source = TTFont.class.getClassLoader().getResourceAsStream(resource);
					ReadableByteChannel rbc = Channels.newChannel(source)
			) {
				buffer = BufferUtils.createByteBuffer(bufferSize);

				while (true) {
					int bytes = rbc.read(buffer);
					if (bytes == -1) {
						break;
					}
					if (buffer.remaining() == 0) {
						buffer = resizeBuffer(buffer, buffer.capacity() * 3 / 2); // 50%
					}
				}
			}
		}

		buffer.flip();
		return memSlice(buffer);
	}
}
