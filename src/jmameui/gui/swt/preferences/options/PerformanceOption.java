package jmameui.gui.swt.preferences.options;

import jmameui.mame.GuiControls;
import jmameui.mame.MameExecutable;

import org.eclipse.swt.widgets.Group;

public class PerformanceOption extends IniOption {
    public PerformanceOption(Group owner, GuiControls g, MameExecutable me) {
	super(owner, g, me);
    }

    public void initUI() {
	createCheckBox("Multi Threading", "multithreading");
	createCheckBox("Sdl Video FPS", "sdlvideofps");
	createIntSpinner("Number Processors", "numprocessors", Runtime
		.getRuntime().availableProcessors());
	createIntSpinner("Bench", "bench", 10000);
    }
}
