package jmameui.install;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;

import jmameui.mame.FileIO;

public class Install {
	static File rootDir = null;
	static String destDir = "/usr/local/games";

	public static void main(String[] args) {
		parseCLA(args);
		rootDir = new File(Install.class.getClassLoader().getResource("")
				.getFile());

		if (!isRoot()) {
			System.out.println("You must be root to perform this operation");
			System.exit(1);
		}

		if (isJar()) {
			File jar = getJarPath();
			try {
				FileIO.unJar(jar, new File(destDir));
				FileIO.copyFile(new File(destDir,
						"jmameui/gui/icons/jmameui.png"), new File(
						"/usr/share/pixmaps"), false);
				parseDesktopFile();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		} else {
					
			try {
				FileIO.cpDirectory(new File(rootDir, "jmameui"), new File(destDir));
				FileIO.copyFile(new File(destDir,
						"jmameui/gui/icons/jmameui.png"), new File(
						"/usr/share/pixmaps"), false);
				parseDesktopFile();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}

	}

	public static boolean isJar() {
		String loc = Install.class.getResource("Install.class").toString();
		if (loc.contains(".jar!"))
			return true;
		return false;
	}

	public static File getJarPath() {
		try {
			return new File(URLDecoder.decode(Install.class
					.getProtectionDomain().getCodeSource().getLocation()
					.getPath(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}

	public static boolean isRoot() {
		if (System.getProperty("user.name").equals("root"))
			return true;
		return false;
	}

	public static void parseDesktopFile() {
		String[] desktopFile = { "[Desktop Entry]", "Name=JMameUI",
				"Comment=Launcher for Mame",
				"Exec=java -cp .:" + destDir + " jmameui.gui.swt.JMameUI",
				"StartupNotify=true", "Terminal=false", "Type=Application",
				"Icon=jmameui", "Categories=Game;Emulator;" };
		FileIO.writeTofile(Arrays.asList(desktopFile), new File(
				"/usr/share/applications/jmameui.desktop"), false);
	}

	public static void parseCLA(String[] args) {
		if (args.length == 0)
			return;

		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-d")) {
				if (args.length > (i + 1) && args[++i].startsWith("/")) {
					destDir = args[i];
				} else {
					System.out.println("Malformed arg " + args[i]);
					System.exit(1);
				}
			}
		}
	}
}
