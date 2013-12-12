/*
 * This file is part of JMameUI.
 * 
 * JMameUI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * JMameUI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with JMameUI.  If not, see <http://www.gnu.org/licenses/>.
 */
package jmameui.mame;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class FileIO {

	private static File logDir = null;

	/**
	 * Writes an Exception to the log file
	 * <p>
	 * writes an Exception to the file with a timestamp, also prints stacktrace
	 */
	public static void writeToLogFile(Exception e) {
		e.printStackTrace();
		ArrayList<String> tmp = new ArrayList<String>();
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));

		tmp.addAll(Arrays.asList(getLineSeparator(), getTime(), sw.toString(),
				System.getProperty("java.vm.vendor"),
				System.getProperty("java.runtime.version"),
				System.getProperty("os.arch"), System.getProperty("os.name"),
				System.getProperty("os.version")));
		writeTofile(tmp, getLogFile(), true);
	}

	/**
	 * Writes an Collection to the log file
	 * <p>
	 * writes an Collection to the file with a timestamp
	 */
	public static void writeToLogFile(Collection<? extends String> lines) {
		ArrayList<String> tmp = new ArrayList<String>();
		tmp.addAll(Arrays.asList(getLineSeparator(), getTime(), "INFO:"));
		tmp.addAll(lines);
		writeTofile(tmp, getLogFile(), true);
	}

	/**
	 * Gets todays log file
	 * 
	 * @return a File named with todays date
	 */
	public static File getLogFile() {
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yy");
		String path = logDir + "/log "
				+ sdf1.format(Calendar.getInstance().getTime());
		return new File(path);
	}

	/**
	 * Get a line separator
	 * 
	 * @return a String containing 100 hyphens
	 */
	public static String getLineSeparator() {
		char[] c = new char[100];
		Arrays.fill(c, '-');
		return new String(c);
	}

	/**
	 * Gets the current time
	 * 
	 * @return String of the current time formatted to h:mm:ss
	 */
	public static String getTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("h:mm:ss");
		return sdf.format(Calendar.getInstance().getTime());
	}

	/**
	 * Change a property in a settings file
	 * 
	 * @param file
	 *            the file to change
	 * @param key
	 *            the option to change
	 * @param newValue
	 *            the new value for the option
	 */
	public static void changeConfFile(File file, String key, String newValue)
			throws IOException {
		FileInputStream in = new FileInputStream(file);
		Properties props = new Properties();
		props.load(in);
		in.close();

		FileOutputStream out = new FileOutputStream(file);
		props.setProperty(key, newValue);
		props.store(out, null);
		out.close();
	}

	/**
	 * Write a collection of String to a file
	 * <P>
	 * If file doesn't exist it creates a file, then writes the collection to it
	 * 
	 * @param lines
	 *            A collection which extends String to be written to a file
	 * @param file
	 *            The file to write to if it doesn't exist it will be created
	 * @param appendFile
	 *            if true will append file
	 */
	public static void writeTofile(Collection<? extends String> lines,
			File file, boolean appendFile) {
		try {
			if (!file.exists())
				file.createNewFile();

			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(
					file, appendFile)));
			for (String i : lines) {
				pw.println(i);
			}
			pw.close();
		} catch (IOException e) {
			writeToLogFile(e);
		}
	}

	/**
	 * Write a String to a file
	 * <P>
	 * If file doesn't exist it creates a file, then writes the String it
	 * 
	 * @param line
	 *            String to be written to a file
	 * @param file
	 *            The file to write to if it doesn't exist it will be created
	 */
	public static void writeTofile(String line, File file) {
		try {
			if (!file.exists())
				file.createNewFile();

			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(
					file, true)));
			pw.println(line);
			pw.close();
		} catch (IOException e) {
			writeToLogFile(e);
		}
	}

	/**
	 * See if the line exists in a file
	 * 
	 * @param file
	 *            the file to be read
	 * @param find
	 *            what to look for in the file
	 * @return boolean true if the line exists
	 */
	public static boolean readFile(File file, String find) {
		ArrayList<String> tmp = readFile(file);
		for (String i : tmp) {
			if (i.equals(find)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * get the contents of a file
	 * 
	 * @param file
	 *            the file to be read
	 * @return ArrayList containing the lines of the file
	 */
	public static ArrayList<String> readFile(File file) {
		ArrayList<String> tmp = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = "";
			while ((line = br.readLine()) != null)
				tmp.add(line);
			br.close();
		} catch (IOException e) {
			writeToLogFile(e);
		}
		return tmp;
	}

	public static ArrayList<String> readInputStream(InputStream is) {
		ArrayList<String> out = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = "";
		try {
			while ((line = br.readLine()) != null) {
				out.add(line);
			}
		} catch (IOException e) {
			writeToLogFile(e);
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				writeToLogFile(e);
			}
		}
		return out;
	}

	/**
	 * Removes the string argument from the specified file if there is an exact
	 * match
	 */
	public static void removeLineFromFile(File file, String rm) {
		try {
			ArrayList<String> tmp = readFile(file);
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			for (String i : tmp)
				if (!i.equals(rm))
					bw.write(i + "\n");

			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
			writeToLogFile(e);
		}
	}

	/**
	 * gets the value from a settings file
	 * 
	 * @return String of option related to key arg otherwise null
	 */
	public static String readSettingsFile(File file, String key) {
		Properties p = new Properties();
		try {
			FileInputStream fis = new FileInputStream(file);
			p.load(fis);
			fis.close();
		} catch (IOException e) {
			writeToLogFile(e);
		}
		return p.getProperty(key);
	}

	/**
	 * Read in an entire settings file
	 * 
	 * @return HashMap in key value pairs
	 */
	public static HashMap<String, String> getSettingFileLines(File file) {
		Properties p = new Properties();
		try {
			FileInputStream fis = new FileInputStream(file);
			p.load(fis);

			fis.close();
		} catch (IOException e) {
			writeToLogFile(e);
		}
		Set<Object> tmp = p.keySet();
		HashMap<String, String> out = new HashMap<String, String>();
		for (Object i : tmp) {
			out.put(i.toString(), p.getProperty(i.toString()));
		}
		return out;
	}

	/**
	 * Read in an entire settings file
	 * 
	 * @return HashMap in key value pairs
	 */
	public static HashMap<String, String> getSettingFileLines(InputStream is) {
		Properties p = new Properties();
		try {
			p.load(is);
			is.close();
		} catch (IOException e) {
			writeToLogFile(e);
		}
		Set<Object> tmp = p.keySet();
		HashMap<String, String> out = new HashMap<String, String>();
		for (Object i : tmp) {
			out.put(i.toString(), p.getProperty(i.toString()));
		}
		return out;
	}

	/**
	 * Takes the output from mame -lx [gamename] and create a file containing
	 * romset information
	 * 
	 * @param pOutput
	 *            Collection containing the output from mame
	 * @param m
	 *            the Mamerom to process
	 * @param file
	 *            To write the romset information to
	 */
	public static void createGameFiles(Collection<? extends String> pOutput,
			MameRom m, File file) throws IOException {
		StringBuilder xmlOutput = new StringBuilder();
		for (String i : pOutput) {
			xmlOutput.append(i + "\n");
		}
		Document d = null;
		try {
			d = DocumentBuilderFactory
					.newInstance()
					.newDocumentBuilder()
					.parse(new ByteArrayInputStream(xmlOutput.toString()
							.getBytes("UTF-8")));
			d.getDocumentElement().normalize();
		} catch (SAXParseException e) {
			writeToLogFile(e);
			return;
		} catch (SAXException e) {
			writeToLogFile(e);
			return;
		} catch (ParserConfigurationException e) {
			writeToLogFile(e);
			return;
		}

		NodeList list = d.getElementsByTagName("mame");
		for (int i = 0; i < list.getLength(); i++) {
			if (list.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element) list.item(i);
				Properties p = new Properties();
				FileOutputStream fos = new FileOutputStream(file);
				p.setProperty("year", getElement(e, "year"));
				p.setProperty("description", getElement(e, "description"));
				p.setProperty("manufacturer", getElement(e, "manufacturer"));
				p.setProperty("emulation_state", (m.isGoodRom()) ? "good"
						: "best available");

				p.store(fos, null);
				fos.close();

				return;
			}
		}
	}

	static String getElement(Element e, String tagName) {
		try {
			return e.getElementsByTagName(tagName).item(0).getTextContent();
		} catch (NullPointerException ex) {
			return "????";
		}
	}

	/**
	 * Create and set values of a settings file
	 * 
	 * @param file
	 *            file to write to
	 * @param data
	 *            the data to write to file
	 */
	public static void createSettingsFile(File file,
			HashMap<String, String> data) throws IOException {
		Properties p = new Properties();
		FileOutputStream fos = new FileOutputStream(file);
		for (String i : data.keySet()) {
			p.setProperty(i, data.get(i));
		}

		p.store(fos, null);
		fos.close();
	}

	public static String getMameIniValue(MameExecutable me, String key) {
		ArrayList<String> ini = readFile(new File(me.getIniPath() + "/mame.ini"));
		for (String i : ini) {
			if (i.startsWith(key + " ")) {
				String[] items = i.split(" ");
				for (String j : items) {
					if (!j.equals("") && !j.equals(key))
						return j;
				}
			}
		}
		return null;
	}

	public static void changeMameIniValue(MameExecutable me, String key,
			String newValue) {
		File iniFile = me.getIniFile();
		ArrayList<String> ini = readFile(iniFile);
		for (String i : ini) {
			if (i.startsWith(key + " ")) {
				StringBuilder line = new StringBuilder(key);
				while (line.length() < 26) {
					line.append(" ");
				}
				line.append(newValue);
				ini.set(ini.indexOf(i), line.toString());
				FileIO.writeTofile(ini, iniFile, false);
			}
		}
	}

	/**
	 * Runs and gets the output from a system process
	 * 
	 * @param cmd
	 *            The command to be ran
	 * @param stdErr
	 *            Include error stream in return
	 * @return ArrayList<String> if there was output otherwise null
	 */
	public static ArrayList<String> getProcessOutput(String cmd, boolean stdErr) {
		ArrayList<String> out = new ArrayList<String>(0);
		try {
			Process p = Runtime.getRuntime().exec(cmd);

			BufferedReader br = new BufferedReader(new InputStreamReader(
					p.getInputStream()));

			String line = null;
			while ((line = br.readLine()) != null) {
				out.add(line);
			}

			if (stdErr) {
				br = new BufferedReader(new InputStreamReader(
						p.getErrorStream()));
				while ((line = br.readLine()) != null) {
					out.add(line);
				}
			}
			br.close();
			if (out.size() == 0) {
				out = null;
			}
		} catch (IOException e) {
			FileIO.writeToLogFile(e);
		}
		return out;
	}

	/**
	 * Unzip a file
	 * 
	 * @param zip
	 *            file to be unzipped
	 * @param ouputDirectory
	 *            where to write the zip to
	 */
	public static void unZip(File zip, String ouputDirectory)
			throws IOException {
		File dir = new File(ouputDirectory);
		if (!dir.exists()) {
			dir.mkdir();
		}

		ZipInputStream zis = new ZipInputStream(new FileInputStream(zip));
		byte[] buffer = new byte[1024];
		for (ZipEntry ze = zis.getNextEntry(); ze != null; ze = zis
				.getNextEntry()) {

			String fileName = ze.getName();
			File tmp = new File(dir, fileName);

			new File(tmp.getParent()).mkdirs();

			FileOutputStream fos = new FileOutputStream(tmp);

			int len;
			while ((len = zis.read(buffer)) > 0) {
				fos.write(buffer, 0, len);
			}

			fos.close();
		}
		zis.close();

	}

	public static void unJar(File jarFile, File outDir) throws IOException {
		ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(
				jarFile));
		ZipEntry zipEntry = null;

		while ((zipEntry = zipInputStream.getNextEntry()) != null) {
			File zipEntryFile = new File(outDir, zipEntry.getName());

			if (zipEntry.getName().contains("META-INF")) {
				continue;
			} else if (zipEntry.isDirectory()) {
				zipEntryFile.mkdir();
			} else {
				zipEntryFile.getParentFile().mkdirs();
				FileOutputStream fileOutputStream = new FileOutputStream(
						zipEntryFile);

				byte buffer[] = new byte[1024];
				int count;

				while ((count = zipInputStream.read(buffer, 0, buffer.length)) != -1)
					fileOutputStream.write(buffer, 0, count);

				fileOutputStream.flush();
				fileOutputStream.close();
				zipInputStream.closeEntry();
			}
		}

		zipInputStream.close();
	}

	/**
	 * Copy a file to a new location
	 * <p>
	 * Creates destination directory if it doesn't exist
	 * 
	 * @param file
	 *            The File to be copied
	 * @param destDir
	 *            Where the File will be copied to
	 * @param setExec
	 *            Set the file as executable
	 */
	public static void copyFile(File file, File destDir, boolean setExec)
			throws IOException {
		FileInputStream is = null;
		FileOutputStream os = null;

		if (!destDir.isDirectory()) {
			destDir.mkdir();
		}

		try {
			byte[] buffer = new byte[1024];
			is = new FileInputStream(file);
			os = new FileOutputStream(new File(destDir, file.getName()));
			int i = 0;
			while ((i = is.read(buffer)) > 0) {
				os.write(buffer, 0, i);
			}
			new File(destDir, file.getName()).setExecutable(setExec, true);
		} finally {
			if (is != null)
				is.close();
			if (os != null)
				os.close();
		}
	}

	public static void cpDirectory(File dir, File outDir) throws IOException {
		if (dir.isDirectory()) {
			if (outDir.isDirectory()) {
				outDir = new File(outDir, dir.getName());
				outDir.mkdirs();
			} else {
				outDir.mkdir();
			}

			for (File i : dir.listFiles()) {
				File newFile = new File(outDir, i.getName());
				if (i.isDirectory()) {
					cpDirectory(i, newFile);
				} else {
					InputStream is = new FileInputStream(i);
					OutputStream os = new FileOutputStream(newFile);

					byte[] buf = new byte[1024];
					int len = 0;

					while ((len = is.read(buf)) > 0) {
						os.write(buf, 0, len);
					}

					is.close();
					os.close();
				}
			}
		}

	}

	public static void setLogDir(File file) {
		logDir = file;
	}

}