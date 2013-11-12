package jmameui.gui.swt.preferences.options;

import jmameui.mame.GuiControls;
import jmameui.mame.MameExecutable;

import org.eclipse.swt.widgets.Group;

public class PerWindowVideoOption extends IniOption {

    public PerWindowVideoOption() {
    }

    public PerWindowVideoOption(Group owner, GuiControls g, MameExecutable me) {
	super(owner, g, me);
    }
    
    @Override
    public void initUI() {
	createTextBox("Screen", "screen");
	createTextBox("Aspect", "aspect");
	createTextBox("Resolution", "resolution");
	createTextBox("View", "view");
	createTextBox("Screen 1", "screen1");
	createTextBox("Aspect 1", "aspect1");
	createTextBox("Resolution 1", "resolution1");
	createTextBox("View 1", "view1");
	createTextBox("Screen 2", "screen2");
	createTextBox("Aspect 2", "aspect2");
	createTextBox("Resolution 2", "resolution2");
	createTextBox("View 2", "view2");
	createTextBox("Screen 3", "screen3");
	createTextBox("Aspect 3", "aspect3");
	createTextBox("Resolution 3", "resolution3");
	createTextBox("View 3", "view3");
    }
}
