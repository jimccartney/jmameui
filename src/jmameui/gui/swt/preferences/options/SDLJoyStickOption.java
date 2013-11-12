package jmameui.gui.swt.preferences.options;

import jmameui.mame.GuiControls;
import jmameui.mame.MameExecutable;

import org.eclipse.swt.widgets.Group;

public class SDLJoyStickOption extends IniOption {

    public SDLJoyStickOption() {
    }

    public SDLJoyStickOption(Group owner, GuiControls g, MameExecutable me) {
	super(owner, g, me);
    }
    
    @Override
    public void initUI() {
	createTextBox("Joy Idx1", "joy_idx1");
	createTextBox("Joy Idx2", "joy_idx2");
	createTextBox("Joy Idx3", "joy_idx3");
	createTextBox("Joy Idx4", "joy_idx4");
	createTextBox("Joy Idx5", "joy_idx5");
	createTextBox("Joy Idx6", "joy_idx6");
	createTextBox("Joy Idx7", "joy_idx7");
	createTextBox("Joy Idx 8", "joy_idx8");
	
	createCheckBox("Six Axis", "sixaxis");
    }
}
