package jmameui.gui.swt.preferences.options;

import jmameui.mame.GuiControls;
import jmameui.mame.MameExecutable;

import org.eclipse.swt.widgets.Group;

public class CorePerformanceOption extends IniOption {

    public CorePerformanceOption(Group owner, GuiControls g, MameExecutable me) {
	super(owner, g, me);
    }

    public CorePerformanceOption() {
    }

    @Override
    public void initUI() {
	createCheckBox("Auto Frame Skip", "autoframeskip");
	createCheckBox("Throttle", "throttle");
	createCheckBox("Sleep", "sleep");
	createCheckBox("Refresh Speed", "refreshspeed");
	
	createIntSpinner("Frame Skip", "frameskip", 10);
	createIntSpinner("Seconds To Run", "seconds_to_run", 60000);
	
	CreateDoubleSpinner("Speed", "speed",20);
    }

}
