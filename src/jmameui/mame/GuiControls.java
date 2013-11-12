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
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;

public class GuiControls {
    private String home = System.getProperty("user.home");
    private File mainDir = new File(home, ".jmameui");
    private File logDir = new File(mainDir, "logs");
    private File mameDir = new File(mainDir, "mame");
    private File goodGames = new File(mainDir, "goodgames");
    private File bestGames = new File(mainDir, "bestgames");
    private File badGames = new File(mainDir, "badgames");
    private File settings = new File(mainDir, "Settings");
    private File gameDir = new File(mainDir, "game");
    private File favourites = new File(mainDir, "Favorites");
    private File[] directorys = { mainDir, logDir, mameDir, gameDir };
    private File[] files = { goodGames, bestGames, badGames, favourites,
	    settings };
    private ArrayList<MameExecutable> mameExecutables = new ArrayList<MameExecutable>();
    private String romPath = "";
    private TreeSet<MameRom> romSet = new TreeSet<MameRom>();
    private int bestRomsetCount = 0;
    private Comparator<MameExecutable> olderPref = new Comparator<MameExecutable>() {
	public int compare(MameExecutable o1, MameExecutable o2) {
	    return o1.getVersion().compareTo(o2.getVersion());
	}
    };

    private Comparator<MameExecutable> newerPref = new Comparator<MameExecutable>() {
	public int compare(MameExecutable o1, MameExecutable o2) {
	    return o2.getVersion().compareTo(o1.getVersion());
	}
    };

    private Comparator<MameExecutable> SystemPref = new Comparator<MameExecutable>() {
	public int compare(MameExecutable o1, MameExecutable o2) {
	    return new Boolean(o2.isSystemMame()).compareTo(new Boolean(o1
		    .isSystemMame()));
	}
    };

    public GuiControls() {
	FileIO.setLogDir(logDir);
    }

    /**
     * Runs a mame game
     * <p>
     * Runs mame if romName is in the database, writes any output from mame or
     * errors to the log file
     * 
     * @param romName
     *            the romset to be run
     */
    public void runGame(String romName) {
	MameRom m = getRom(romName);
	MameExecutable me = getMameExecutable(m.getMamePath());
	String cmd = me.getPath() + " " + m.getName() + " -inipath "
		+ me.getIniPath();

	ArrayList<String> tmp = FileIO.getProcessOutput(cmd, true);
	if (tmp != null) {
	    tmp.add(cmd);
	    FileIO.writeToLogFile(tmp);
	}
    }

    /**
     * Gets a rom from the collection
     * 
     * @param name
     *            The name of the rom to get
     * @return returns a MameRom if found otherwise null
     */
    public MameRom getRom(String name) {
	for (MameRom i : romSet) {
	    if (i.getName().equals(name))
		return i;
	}
	return null;
    }

    /**
     * Copy a mame executable to the mamedir
     * 
     * @param mameExec
     *            mame exec to be copied
     */
    public void addMameExecutable(File mameExec) {
	try {
	    String version = getMameVersion(mameExec.getPath());
	    File dir = new File(mameDir, version);

	    FileIO.copyFile(mameExec, dir, true);

	    File nMame = new File(dir, "mame");
	    File tmp = new File(dir, mameExec.getName());
	    tmp.renameTo(nMame);

	    mameExecutables.add(new MameExecutable(nMame.getPath(), version,
		    nMame.getPath(), false));
	} catch (IOException e) {
	    FileIO.writeToLogFile(e);
	}
    }

    /**
     * remove a mame executable from the mamedir
     * 
     * @param mameExec
     *            mame exec to be removed
     */
    public void removeMameExecutable(File mameExec) {
	MameExecutable me = getMameExecutable(mameExec.getAbsolutePath());
	if (me != null && !me.isSystemMame()) {
	    me.delete();
	    mameExecutables.remove(me);
	}
    }

    /**
     * Runs mame -verifyroms and outputs the information to three
     * files(good/best/bad) in the JMameUI root directory
     */
    private void buildGameDatabase() {
	ArrayList<String> goodRoms = new ArrayList<String>();
	ArrayList<String> bestRoms = new ArrayList<String>();
	ArrayList<String> badRoms = new ArrayList<String>();
	TreeSet<String> tmp = new TreeSet<String>();

	sortMameExecs();

	for (MameExecutable i : mameExecutables) {
	    badRoms.add("### Mame Executable = " + i.getPath());
	    ArrayList<String> lines = FileIO.getProcessOutput(i.getPath()
		    + " -verifyroms -inipath " + i.getIniPath(), false);
	    for (String line : lines) {
		if (line.contains("good") && tmp.add(line)) {
		    goodRoms.add(line + "," + i.getPath());
		} else if (line.contains("best") && tmp.add(line)) {
		    bestRoms.add(line + "," + i.getPath());
		} else if (tmp.add(line.replace(" ", ""))
			&& (!line.contains("best") && !line.contains("good"))) {
		    badRoms.add(line);
		}

	    }
	}
	FileIO.writeTofile(goodRoms, goodGames, false);
	FileIO.writeTofile(bestRoms, bestGames, false);
	FileIO.writeTofile(badRoms, badGames, false);
    }

    /**
     * Build the romset database and create MameRom Collection
     * 
     * @param force
     *            rebuild the database
     */
    public void buildDB(boolean force) {
	try {
	    if (force || goodGames.length() == 0) {
		buildGameDatabase();
	    }
	    romSet = createMameRoms();
	} catch (IOException e) {
	    FileIO.writeToLogFile(e);
	}
    }

    /**
     * creates mameroms reads information from the game files(creates file if it
     * doesn't exist) and sets the mamerom with this information
     * 
     * @return TreeSet containing romsets which are either good or best
     *         available
     */
    private TreeSet<MameRom> createMameRoms() throws IOException {
	TreeSet<MameRom> out = new TreeSet<MameRom>();
	ArrayList<String> favs = FileIO.readFile(favourites);
	ArrayList<String> lines = FileIO.readFile(goodGames);
	ArrayList<String> bestLines = FileIO.readFile(bestGames);

	lines.addAll(bestLines);

	for (String line : lines) {
	    String gameName = line.split(" ")[1].split(" ")[0];
	    String mamePath = line.split(",")[1];
	    MameRom m = (bestLines.contains(line)) ? new MameRom(gameName,
		    mamePath, false) : new MameRom(gameName, mamePath, true);

	    File file = new File(gameDir + "/" + m.getName());

	    if (!file.exists()) {
		ArrayList<String> tmp = FileIO.getProcessOutput(mamePath
			+ " -lx " + gameName, false);
		if (tmp != null) {
		    FileIO.createGameFiles(tmp, m, file);
		} else {
		    file.delete();
		    FileIO.removeLineFromFile(bestGames, line);
		    continue;
		}
	    }

	    HashMap<String, String> romInf = FileIO.getSettingFileLines(file);
	    m.setMameVersion(getMameExecutable(mamePath).getVersion());
	    m.setYear(romInf.get("year"));
	    m.setDescription(romInf.get("description"));
	    m.setManufacturer(romInf.get("manufacturer"));
	    m.setEmuState(romInf.get("emulation_state"));
	    if (favs.contains(m.getName())) {
		m.setFavourite(true);
	    }
	    out.add(m);
	}
	return out;
    }

    /**
     * Add a rom name to the favourites file
     * <p>
     * Removes rom name from the favourites file if it's already in there
     * 
     * @param romName
     *            name of rom to check
     * @return true if added otherwise false
     */
    public boolean addToFavourites(String romName) {
	boolean added = false;
	if (FileIO.readFile(favourites, romName) == true) {
	    FileIO.removeLineFromFile(favourites, romName);
	    getRom(romName).setFavourite(false);
	} else {
	    FileIO.writeTofile(romName, favourites);
	    added = true;
	    getRom(romName).setFavourite(true);
	}
	return added;
    }

    public void sortMameExecs() {
	String sortSet = FileIO.readSettingsFile(settings, "mame_sort");

	if (sortSet.equals("system"))
	    Collections.sort(mameExecutables, SystemPref);
	else if (sortSet.equals("older"))
	    Collections.sort(mameExecutables, olderPref);
	else if (sortSet.equals("newer"))
	    Collections.sort(mameExecutables, newerPref);
    }

    /**
     * Gets the contents of the favourites file
     * 
     * @return arraylist contains mameroms of the favourites file
     */
    public ArrayList<MameRom> getFavourites() {
	ArrayList<MameRom> out = new ArrayList<MameRom>();
	for (String i : FileIO.readFile(favourites)) {
	    MameRom j = getRom(i);
	    if (j != null) {
		out.add(j);
	    }
	}
	return out;
    }

    /**
     * Create all files for JMameUI
     */
    public void createFiles() throws IOException {
	for (File i : directorys) {
	    if (!i.isDirectory())
		i.mkdir();
	}

	for (File i : files) {
	    if (!i.exists()) {
		i.createNewFile();
		if (i == settings) {
		    HashMap<String, String> tmp = new HashMap<String, String>();
		    tmp.put("mame_ini_path", home + "/.mame");
		    tmp.put("mame_rom_path", home + "/.mame/roms");
		    tmp.put("system_mame_version", "null");
		    tmp.put("system_mame_path", "null");
		    tmp.put("use_system_mame", "true");
		    tmp.put("mame_sort", "system");
		    FileIO.createSettingsFile(i, tmp);
		}
	    }
	}
    }

    /**
     * Change a property in the main settings file
     * 
     * @param key
     *            the property to change
     * @param value
     *            the new value for the property
     */
    public void changeSettingsFile(String key, String value) {
	try {
	    FileIO.changeConfFile(settings, key, value);
	} catch (IOException e) {
	    FileIO.writeToLogFile(e);
	}
    }

    /**
     * Read a setting in the main settings file
     * 
     * @param key
     *            the property to get the value for
     * @return the value of the property if it exists otherwise null
     */
    public String readSettingsFile(String key) {
	return FileIO.readSettingsFile(settings, key);
    }

    /**
     * Get the output from mame about the unavailable romsets
     * 
     * @return ArrayList contain the information about unavailable romsets
     */
    public ArrayList<String> getUnavailableRomText() {
	return FileIO.readFile(badGames);
    }

    /**
     * Attempts to find the system Mame Executable
     * 
     * @return true if mame found
     */
    public Boolean start() {
	Boolean mameFound = false;
	try {
	    createFiles();
	    boolean b = Boolean.valueOf(FileIO.readSettingsFile(settings,
		    "use_system_mame"));
	    String systemPath = FileIO.readSettingsFile(settings,
		    "system_mame_path");
	    if (b && !systemPath.equals("null")) {
		mameFound = true;
		setSystemMameInfo(systemPath);
	    } else if (b && systemPath.equals("null")) {
		systemPath = getSystemMameInfo();

		if (systemPath != null) {
		    setSystemMameInfo(systemPath);
		    changeSettingsFile("system_mame_path", systemPath);
		    changeSettingsFile("system_mame_version",
			    getMameVersion(systemPath));
		    mameFound = true;
		}
	    } else if (!b) {
		return null;
	    }
	    for (File i : mameDir.listFiles()) {
		if (i.isDirectory()) {
		    File me = new File(i, "mame");
		    mameExecutables.add(new MameExecutable(me.getPath(),
			    getMameVersion(me.getPath()), romPath, false));
		}
	    }

	} catch (IOException e) {
	    FileIO.writeToLogFile(e);
	}
	return mameFound;
    }

    /**
     * Get the version of Mame
     * 
     * @param path
     *            the location of the Mame executable
     * @return String of mame version
     */
    public String getMameVersion(String path) {
	String out = null;
	try {
	    Process p = Runtime.getRuntime().exec(path + " -h");
	    BufferedReader br = new BufferedReader(new InputStreamReader(
		    p.getInputStream()));

	    String j = br.readLine();
	    br.close();
	    if (j != null)
		out = j.split(" ")[1].split(" ")[0];
	} catch (IOException e) {
	    FileIO.writeToLogFile(e);
	}
	return out;
    }

    public void addRom(final Collection<? extends File> files1,
	    final String name) {
	new Thread(new Runnable() {
	    @Override
	    public void run() {
		for (File i : files1) {
		    try {
			if (FileIO.getContentType(i).equals("application/zip")) {

			    FileIO.unZip(i, romPath + "/" + name);

			} else if (FileIO.getContentType(i).equals(
				"application/octet-stream")) {

			    FileIO.copyFile(i, new File(romPath + "/" + name),
				    false);
			}
		    } catch (IOException e) {
			FileIO.writeToLogFile(e);
		    }
		}
	    }

	}).start();
    }

    private void setSystemMameInfo(String path) {
	File iniFile = new File(mameDir, "mame.ini");
	MameExecutable me = new MameExecutable(path, getMameVersion(path),
		iniFile.getPath(), true);
	me.setIniFile(iniFile);
	me.setIniPath(iniFile.getPath().replace(iniFile.getName(), ""));
	if (!iniFile.exists()) {
	    ArrayList<String> ini = FileIO.getProcessOutput(path + " -sc",
		    false);
	    romPath = getMameIniValue(ini, "rompath").replace("$HOME", home);
	    FileIO.writeTofile(ini, iniFile, false);
	} else {
	    romPath = FileIO.getMameIniValue(me, "rompath");
	}
	
	mameExecutables.add(me);
    }

    public String getMameIniValue(ArrayList<String> ini, String key) {
	boolean b = false;
	for (String i : ini) {
	    if (i.startsWith(key + " ")) {
		b = true;
		String[] items = i.split(" ");
		for (String j : items) {
		    if (!j.equals("") && !j.equals(key))
			return j;
		}
	    }
	}
	if (b) {
	    return "";
	}
	return null;
    }

    public void changeMameIniValue(ArrayList<String> ini, String key,
	    String newValue) {
	for (String i : ini) {
	    if (i.startsWith(key + " ")) {
		StringBuilder line = new StringBuilder(key);
		while (line.length() < 26) {
		    line.append(" ");
		}
		line.append(newValue);
		ini.set(ini.indexOf(i), line.toString());
	    }
	}
    }

    private String getSystemMameInfo(String... path) throws IOException {
	String[] tmp = { "sdlmame", "mame" };
	if (path.length == 1) {
	    tmp = new String[] { path[0] };
	}

	String out = null;
	for (String i : tmp) {
	    Process p = Runtime.getRuntime().exec("which " + i);
	    BufferedReader br = new BufferedReader(new InputStreamReader(
		    p.getInputStream()));

	    String j = br.readLine();
	    if (j != null && !j.contains("which"))
		out = j;
	    br.close();
	}
	return out;
    }

    public File getMainDir() {
	return mainDir;
    }

    public File getLogDir() {
	return logDir;
    }

    public File getMameDir() {
	return mameDir;
    }

    public File getGoodGames() {
	return goodGames;
    }

    public File getBestGames() {
	return bestGames;
    }

    public File getBadGames() {
	return badGames;
    }

    public File getSettings() {
	return settings;
    }

    public File getGameDir() {
	return gameDir;
    }

    public File getFavouritesFile() {
	return favourites;
    }

    public String getRomPath() {
	return romPath;
    }

    public ArrayList<MameExecutable> getMameExecutables() {
	return mameExecutables;
    }

    public MameExecutable getSystemMame() {
	for (MameExecutable i : mameExecutables) {
	    if (i.isSystemMame()) {
		return i;
	    }
	}
	return null;
    }

    public MameExecutable getMameExecutable(String path) {
	for (MameExecutable i : mameExecutables) {
	    if (i.getPath().equals(path)) {
		return i;
	    }
	}
	return null;
    }

    public TreeSet<MameRom> getRomSet() {
	return romSet;
    }

    public int getGoodRomsetCount() {
	return romSet.size();
    }
}