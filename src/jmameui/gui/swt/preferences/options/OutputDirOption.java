package jmameui.gui.swt.preferences.options;

import jmameui.mame.GuiControls;
import jmameui.mame.MameExecutable;

import org.eclipse.swt.widgets.Group;

public class OutputDirOption extends IniOption {
    public OutputDirOption(Group owner, GuiControls g, MameExecutable me) {
	super(owner, g, me);
    }

    public void initUI() {
	createMamePathComp("Cfg Directory", "cfg_directory", DIRECTORY_DIALOG);
	createMamePathComp("Nvram Directory", "nvram_directory",
		DIRECTORY_DIALOG);
	createMamePathComp("Memcard Directory", "memcard_directory",
		DIRECTORY_DIALOG);
	createMamePathComp("Input Directory", "input_directory",
		DIRECTORY_DIALOG);
	createMamePathComp("State Directory", "state_directory",
		DIRECTORY_DIALOG);
	createMamePathComp("Snapshot Directory", "snapshot_directory",
		DIRECTORY_DIALOG);
	createMamePathComp("Diff Directory", "diff_directory", DIRECTORY_DIALOG);
	createMamePathComp("Comment Directory", "comment_directory",
		DIRECTORY_DIALOG);
	getGroup().pack(true);
    }
}
