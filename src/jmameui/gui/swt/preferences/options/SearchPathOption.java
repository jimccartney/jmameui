package jmameui.gui.swt.preferences.options;

import jmameui.mame.FileIO;
import jmameui.mame.GuiControls;
import jmameui.mame.MameExecutable;

import org.eclipse.swt.widgets.Group;

public class SearchPathOption extends IniOption{
    public SearchPathOption(Group owner, GuiControls g, MameExecutable me) {
	setGroup(owner);
	setmExec(me);
	setgCon(g);
	setIniFile(FileIO.readFile(me.getIniFile()));
	initUI();
    }

    private void initUI() {
	createMamePathComp("Rom Path", "rompath");
	createMamePathComp("Hash Path", "hashpath");
	createMamePathComp("Sample Path", "samplepath");
	createMamePathComp("Art Path", "artpath");
	createMamePathComp("Ctrlr Path", "ctrlrpath");
	createMamePathComp("Ini Path", "inipath");
	createMamePathComp("Font Path", "fontpath");
	createMamePathComp("Cheat Path", "cheatpath");
	createMamePathComp("Crosshair Path", "crosshairpath");
	getGroup().pack(true);
    }
}
