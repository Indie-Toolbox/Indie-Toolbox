package com.toolbox.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class OsUtil {
	public enum OS {
		Windows, Linux, Mac
	};

	
	public static OS getOS() {
		OS os = null;
		if (os == null) {
			String sys = System.getProperty("os.name").toLowerCase();
			if (sys.contains("win")) {
				os = OS.Windows;
			} else if (sys.contains("nix") || sys.contains("nux")
					|| sys.contains("aix")) {
				os = OS.Linux;
			} if (sys.contains("mac")) {
				os = OS.Mac;
			} else {
				assert false : "OS Not Supported";
			}
		}
		return os;
	}
	
	private static OS os = getOS();

	/* ---- Path getters ---- */
	public static String getDownloadFilepath(String name) {
		String path = null;
		switch (os) {
			case Windows -> {
				path = "C:/toolbox/" + name + "/";
			}
			case Linux -> {
				path = System.getProperty("user.home") + "/.toolboxlauncher/" + name + "/";
			}
			case Mac -> {
				path = System.getProperty("user.home") + "/.toolboxlauncher/" + name + "/";
			}
		}
		return path;
	}

	private static String getToolListPath () {
		String path = "";
		switch (os) {
			case Windows -> {
				path = "C:/toolbox/toollist.txt";
			}
			case Linux -> {
				path = System.getProperty("user.home") + "/.toolboxlauncher/toollist.txt";
			}
			case Mac -> {
				path = System.getProperty("user.home") + "/.toolboxlauncher/toollist.txt";
			}
		}
		return path;
	}

	public static String getToolboxFilepath() {
		OS os = getOS();
		String path = null;
		switch (os) {
			case Windows -> {
				path = "C:/toolbox/";
			}
			case Linux -> {
				path = System.getProperty("user.home") + "/.toolboxlauncher/";
			}
			case Mac -> {
				path = System.getProperty("user.home") + "/.toolboxlauncher/";
			}
		}
		return path;
	}

	/* ---- File Operations ---- */
	public static boolean installedToolsFileExists() {
		return Files.exists(Paths.get(getToolListPath()));
	}

	public static BufferedWriter openInstalledToolsFile() throws Exception {
		File file = new File(getToolListPath());
		file.getParentFile().mkdirs();
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		return writer;
	}

	public static FileReader openInstalledToolsFileRead() throws Exception {
		File file = new File(getToolListPath());
		return new FileReader(file);
	}

	public static boolean installedToolsFileIsEmpty() {
		File file = new File(getToolListPath());
		return file.length() == 0;
	}
}
