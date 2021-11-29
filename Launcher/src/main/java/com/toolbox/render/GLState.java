package com.toolbox.render;

import static org.lwjgl.opengl.GL11.*;

public class GLState {
	private static boolean alpha_blending;

	public static void enableAlphaBlending() {
		if (!alpha_blending) {
			alpha_blending = true;
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		}
	}

	public static void disableAlphaBlending() {
		if (alpha_blending) {
			alpha_blending = false;
			glDisable(GL_BLEND);
		}
	}
}
