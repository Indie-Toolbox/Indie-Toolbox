package com.toolbox.util;

import org.joml.Vector2f;
import org.lwjgl.system.MemoryStack;

import java.nio.DoubleBuffer;
import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.*;

public class Input {
	public static Vector2f mouse;
	public static long mouseX = 0;
	public static long mouseY = 0;
	public static Vector2f pmouse;
	public static long pmouseX = 0;
	public static long pmouseY = 0;
	public static Vector2f mouseScroll;
	public static float scrollX = 0;
	public static float scrollY = 0;
	public static boolean[] mouseButton = new boolean[3];
	public static boolean mouseDragged;
	public static byte[] keystateBitfields;
	private static long window;
	private static int _button;
	private static int _action;

	static {
		keystateBitfields = new byte[400];
	}

	public static void setup(long window) {
		Input.window = window;

		glfwSetScrollCallback(window, (w, xOffset, yOffset) -> {
			scrollX = (float) xOffset;
			scrollY = (float) yOffset;
			mouseScroll = new Vector2f(scrollX, scrollY);

			Events.mouseScrollEvent.onEvent(new EventData.MouseScrollEventData(xOffset, yOffset));
		});

		glfwSetMouseButtonCallback(window, (w, button, action, mods) -> {
			if (action == GLFW_PRESS) {
				if (button < mouseButton.length)
					mouseButton[button] = true;
			} else if (action == GLFW_RELEASE) {
				if (button < mouseButton.length) {
					mouseButton[button] = false;
					mouseDragged = false;
				}
			}

			Events.mouseButtonEvent.onEvent(new EventData.MouseButtonEventData(button, action, mods));
		});

		glfwSetKeyCallback(window, (w, keycode, scancode, action, mods) -> {
			switch (action) {
				case GLFW_PRESS -> {
					setKeyDownBit(keycode);
					resetKeyUpBit(keycode);
					resetKeyHeldBit(keycode);
				}
				case GLFW_RELEASE -> {
					resetKeyDownBit(keycode);
					setKeyUpBit(keycode);
					resetKeyHeldBit(keycode);
				}
				case GLFW_REPEAT -> {
					resetKeyDownBit(keycode);
					resetKeyUpBit(keycode);
					setKeyHeldBit(keycode);
				}
			}

			Events.keyEvent.onEvent(new EventData.KeyEventData(keycode, scancode, action, mods));
		});
	}

	private static void setKeyDownBit(int keycode) {
		keystateBitfields[keycode] |= 0b00000001;
	}

	private static void resetKeyDownBit(int keycode) {
		keystateBitfields[keycode] &= 0b11111110;
	}

	private static void setKeyUpBit(int keycode) {
		keystateBitfields[keycode] |= 0b00000010;
	}

	private static void resetKeyUpBit(int keycode) {
		keystateBitfields[keycode] &= 0b11111101;
	}

	private static void setKeyHeldBit(int keycode) {
		keystateBitfields[keycode] |= 0b00000100;
	}

	private static void resetKeyHeldBit(int keycode) {
		keystateBitfields[keycode] &= 0b11111011;
	}

	public static boolean key(int keycode) {
		return glfwGetKey(Input.window, keycode) != GLFW_RELEASE;
	}

	public static boolean keyDown(int keycode) {
		return ((keystateBitfields[keycode] & 0b00000001) /*>> 0*/) != 0;
	}

	public static boolean keyUp(int keycode) {
		return ((keystateBitfields[keycode] & 0b00000010) >> 1) != 0;
	}

	public static boolean keyHeld(int keycode) {
		return ((keystateBitfields[keycode] & 0b00000100) >> 2) != 0;
	}

	public static void update() {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			DoubleBuffer x = stack.doubles(0);
			DoubleBuffer y = stack.doubles(0);

			glfwGetCursorPos(window, x, y);
			x.rewind();
			y.rewind();

			long pmouseX = mouseX;
			long pmouseY = mouseY;
			pmouse = new Vector2f(pmouseX, pmouseY);

			mouseX = (long) x.get();
			mouseY = (long) y.get();
			mouse = new Vector2f(mouseX, mouseY);

			if (mouseX != pmouseX || mouseY != pmouseY) {
				mouseDragged = mouseButton[0] || mouseButton[1] || mouseButton[2];
			}
		}
	}

	public static boolean mouseButtonDown(int button) {
		if (button < mouseButton.length) {
			return mouseButton[button];
		}
		return false;
	}

	public static void clear() {
		scrollX = 0;
		scrollY = 0;
		mouseScroll = new Vector2f(scrollX, scrollY);
		pmouseX = mouseX;
		pmouseY = mouseY;
		pmouse = new Vector2f(pmouseX, pmouseY);
		Arrays.fill(mouseButton, false);
		Arrays.fill(keystateBitfields, (byte) 0b00000000);
	}
}
