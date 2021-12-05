package com.toolbox.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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
				path = Paths.get("/usr/toolbox/toollist.txt");
			}
		}
		return Files.exists(path);
	}

	public static FileWriter openInstalledToolsFile() throws Exception {
		OS os = getOS();
		String path = null;
		switch (os) {
			case Windows -> {
				path = "C:/toolbox/toollist.txt";
			}
			case Linux -> {
				path = "/opt/toolbox/toollist.txt";
			}
		}
		File file = new File(path);
		file.getParentFile().mkdirs();
		return new FileWriter(file);
	}

	public static FileReader openInstalledToolsFileRead() throws Exception {
		OS os = getOS();
		String path = null;
		switch (os) {
			case Windows -> {
				path = "C:/toolbox/toollist.txt";
			}
			case Linux -> {
				path = "/opt/toolbox/toollist.txt";
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
				path = "/opt/toolbox/toollist.txt";
			}
		}
		File file = new File(path);
		return file.length() == 0;
	}
}
