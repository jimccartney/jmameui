package jmameui.gui.swt.preferences.options;

import jmameui.mame.FileIO;
import jmameui.mame.GuiControls;
import jmameui.mame.MameExecutable;

import org.eclipse.swt.widgets.Group;

public class OutputDirOption extends IniOption {
    public OutputDirOption(Group owner, GuiControls g, MameExecutable me) {
	setGroup(owner);
	setmExec(me);
	setgCon(g);
	setIniFile(FileIO.readFile(me.getIniFile()));
	initUI();
    }

    private void initUI() {
	
	createMamePathComp("Cfg Directory", "cfg_directory");
	createMamePathComp("Nvram Directory", "nvram_directory");
	createMamePathComp("Memcard Directory", "memcard_directory");
	createMamePathComp("Input Directory", "input_directory");
	createMamePathComp("State Directory", "state_directory");
	createMamePathComp("Snapshot Directory", "snapshot_directory");
	createMamePathComp("Diff Directory", "diff_directory");
	createMamePathComp("Comment Directory", "comment_directory");
	getGroup().pack(true);
    }
}
