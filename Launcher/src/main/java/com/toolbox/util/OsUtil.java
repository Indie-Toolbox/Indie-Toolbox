package com.toolbox.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class OsUtil {
	public enum OS {
		Windows, Linux
	};

	private static OS os = null;

	public static OS getOS() throws Exception {
		if (os == null) {
			String sys = System.getProperty("os.name").toLowerCase();
			if (sys.contains("win")) {
				os = OS.Windows;
			} else if (sys.contains("nix") || sys.contains("nux")
					|| sys.contains("aix")) {
				os = OS.Linux;
			} else {
				throw new Exception("OS Not Supported");
			}
		}
		return os;
	}

	public static boolean installedToolsFileExists() throws Exception {
		OS os = getOS();
		Path path = null;
		switch (os) {
			case Windows -> {
				path = Paths.get("C:/toolbox/toollist.txt");
			}
			case Linux -> {
				path = Paths.get("~/.toolboxlauncher/toollist.txt");
			}
		}
		return Files.exists(path);
	}

	public static BufferedWriter openInstalledToolsFile() throws Exception {
		OS os = getOS();
		String path = null;
		switch (os) {
			case Windows -> {
				path = "C:/toolbox/toollist.txt";
			}
			case Linux -> {
				path = "~/.toolboxlauncher/toollist.txt";
			}
		}
		File file = new File(path);
		file.getParentFile().mkdirs();
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		return writer;
	}

	public static String getDownloadFilepath(String name) throws Exception {
		OS os = getOS();
		String path = null;
		switch (os) {
			case Windows -> {
				path = "C:/toolbox/" + name + "/";
			}
			case Linux -> {
				path = "~/.toolboxlauncher/" + name + "/";
			}
		}
		return path;
	}

	public static String getToolboxFilepath() throws Exception {
		OS os = getOS();
		String path = null;
		switch (os) {
			case Windows -> {
				path = "C:/toolbox/";
			}
			case Linux -> {
				path = "~/.toolboxlauncher/";
			}
		}
		return path;
	}

	public static FileReader openInstalledToolsFileRead() throws Exception {
		OS os = getOS();
		String path = null;
		switch (os) {
			case Windows -> {
				path = "C:/toolbox/toollist.txt";
			}
			case Linux -> {
				path = "~/.toolboxlauncher/toollist.txt";
			}
		}
		File file = new File(path);
		return new FileReader(file);
	}

	public static boolean installedToolsFileIsEmpty() throws Exception {
		OS os = getOS();
		String path = null;
		switch (os) {
			case Windows -> {
				path = "C:/toolbox/toollist.txt";
			}
			case Linux -> {
				path = "~/.toolboxlauncher/toollist.txt";
			}
		}
		File file = new File(path);
		return file.length() == 0;
	}
}
