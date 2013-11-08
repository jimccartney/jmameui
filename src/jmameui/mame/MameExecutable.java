package jmameui.mame;

import java.io.File;

public class MameExecutable {

	private String path = "";
	private String version = "";
	private boolean systemMame = false;
	private String iniPath = "";
	private File rootDir = null;
	private File iniFile = null;
	private File mameExec = null;

	public MameExecutable(String path, String version, String romPath,boolean system) {
		this.path = path;
		this.version = version;
		this.systemMame = system;
		if (system) {
			this.version += " (System)";
		} else {
			initPaths(romPath);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MameExecutable other = (MameExecutable) obj;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		return true;
	};

	public void initPaths(String romPath) {
		rootDir = new File(path).getParentFile();
		iniFile = new File(rootDir, "mame.ini");
		mameExec = new File(path);
		iniPath = rootDir.getPath();
		File[] dirs = new File[] { new File(rootDir, "cfg"),
				new File(rootDir, "nvram"), new File(rootDir, "memcard"),
				new File(rootDir, "inp"), new File(rootDir, "states"),
				new File(rootDir, "snap"), new File(rootDir, "diff"),
				new File(rootDir, "comments") };
		
		for (File i : dirs) {
			if (!i.isDirectory()) {
				i.mkdir();
			}
		}

		if (!iniFile.exists()) {
			FileIO.writeTofile(FileIO.getProcessOutput(path + " -sc", false),
					iniFile, false);
			FileIO.changeMameIniValue(this, "inipath", rootDir.getPath());
			FileIO.changeMameIniValue(this, "cfg_directory", rootDir.getPath()
					+ "/cfg");
			FileIO.changeMameIniValue(this, "nvram_directory",
					rootDir.getPath() + "/nvram");
			FileIO.changeMameIniValue(this, "memcard_directory",
					rootDir.getPath() + "/memcard");
			FileIO.changeMameIniValue(this, "input_directory",
					rootDir.getPath() + "/inp");
			FileIO.changeMameIniValue(this, "state_directory",
					rootDir.getPath() + "/states");
			FileIO.changeMameIniValue(this, "snapshot_directory",
					rootDir.getPath() + "/snap");
			FileIO.changeMameIniValue(this, "diff_directory", rootDir.getPath()
					+ "/diff");
			FileIO.changeMameIniValue(this, "comment_directory",
					rootDir.getPath() + "/comments");
			FileIO.changeMameIniValue(this, "rompath",romPath);
		}
	}

	public void delete() {
		iniFile.delete();
		mameExec.delete();
		rootDir.delete();
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public boolean isSystemMame() {
		return systemMame;
	}

	public void setSystemMame(boolean system) {
		this.systemMame = system;
	}

	public String getIniPath() {
		return iniPath;
	}

	public void setIniPath(String iniPath) {
		this.iniPath = iniPath;
	}

	public File getRootDir() {
		return rootDir;
	}

	public void setRootDir(File rootDir) {
		this.rootDir = rootDir;
	}

	public File getIniFile() {
		return iniFile;
	}

	public void setIniFile(File iniFile) {
		this.iniFile = iniFile;
	}

}
