package jmameui.gui.swt.preferences.options;

import jmameui.mame.FileIO;
import jmameui.mame.GuiControls;
import jmameui.mame.MameExecutable;

import org.eclipse.swt.widgets.Group;

public class SearchPathOption extends IniOption {
    public SearchPathOption(Group owner, GuiControls g, MameExecutable me) {
	setGroup(owner);
	setmExec(me);
	setgCon(g);
	setIniFile(FileIO.readFile(me.getIniFile()));
	initUI();
    }

    private void initUI() {
	createMamePathComp("Rom Path", "rompath", DIRECTORY_DIALOG);
	createMamePathComp("Hash Path", "hashpath", DIRECTORY_DIALOG);
	createMamePathComp("Sample Path", "samplepath", DIRECTORY_DIALOG);
	createMamePathComp("Art Path", "artpath", DIRECTORY_DIALOG);
	createMamePathComp("Ctrlr Path", "ctrlrpath", DIRECTORY_DIALOG);
	createMamePathComp("Ini Path", "inipath", DIRECTORY_DIALOG);
	createMamePathComp("Font Path", "fontpath", DIRECTORY_DIALOG);
	createMamePathComp("Cheat Path", "cheatpath", DIRECTORY_DIALOG);
	createMamePathComp("Crosshair Path", "crosshairpath", DIRECTORY_DIALOG);
	getGroup().pack(true);
    }
}
