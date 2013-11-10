package jmameui.gui.swt.preferences.options;

import jmameui.mame.FileIO;
import jmameui.mame.GuiControls;
import jmameui.mame.MameExecutable;

import org.eclipse.swt.widgets.Group;

public class RotationOption extends IniOption {

    public RotationOption(Group owner, GuiControls g, MameExecutable me) {
	setGroup(owner);
	setmExec(me);
	setgCon(g);
	setIniFile(FileIO.readFile(me.getIniFile()));
	initUI();
    }

    private void initUI() {
	createCheckBox("Rotate", "rotate");
	createCheckBox("Rotate Clockwise", "ror");
	createCheckBox("Rotate Anticlockwise", "rol");
	createCheckBox("Auto Rotate Clockwise", "autoror");
	createCheckBox("Auto Rotate Anticlockwise", "autorol");
	createCheckBox("Flip X", "flipx");
	createCheckBox("Flip Y", "flipy");

	getGroup().pack(true);
    }
}
