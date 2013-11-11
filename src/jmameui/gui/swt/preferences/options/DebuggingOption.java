package jmameui.gui.swt.preferences.options;

import jmameui.mame.GuiControls;
import jmameui.mame.MameExecutable;

import org.eclipse.swt.widgets.Group;

public class DebuggingOption extends IniOption {

    public DebuggingOption() {
    }

    public DebuggingOption(Group owner, GuiControls g, MameExecutable me) {
	super(owner, g, me);
    }
    
    @Override
    public void initUI() {
	createCheckBox("Log", "log");
	createCheckBox("Verbose", "verbose");
	createCheckBox("Update In Pause", "update_in_pause");
	createCheckBox("Debug", "debug");
	createCheckBox("Debug Internal", "debug_internal");
    
	createMamePathComp("Debug Script", "debugscript", LOAD_DIALOG);
    }

}
