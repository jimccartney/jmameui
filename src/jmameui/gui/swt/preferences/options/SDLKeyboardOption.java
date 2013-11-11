package jmameui.gui.swt.preferences.options;

import jmameui.mame.GuiControls;
import jmameui.mame.MameExecutable;

import org.eclipse.swt.widgets.Group;

public class SDLKeyboardOption extends IniOption {

    public SDLKeyboardOption() {
    }

    public SDLKeyboardOption(Group owner, GuiControls g, MameExecutable me) {
	super(owner, g, me);
    }
    
    @Override
    public void initUI() {
	createCheckBox("Keymap", "keymap");
	
	createMamePathComp("Keymap File", "keymap_file", LOAD_DIALOG);
	
	createTextBox("Ui Mode Key", "uimodekey");
    }
}
