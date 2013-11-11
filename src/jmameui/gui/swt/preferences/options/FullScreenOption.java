package jmameui.gui.swt.preferences.options;

import jmameui.mame.GuiControls;
import jmameui.mame.MameExecutable;

import org.eclipse.swt.widgets.Group;

public class FullScreenOption extends IniOption {

    public FullScreenOption() {
    }

    public FullScreenOption(Group owner, GuiControls g, MameExecutable me) {
	super(owner, g, me);
    }
    
    @Override
    public void initUI() {
	createCheckBox("Switch Res", "switchres");
	createCheckBox("Use All Heads", "useallheads");
    }
}
