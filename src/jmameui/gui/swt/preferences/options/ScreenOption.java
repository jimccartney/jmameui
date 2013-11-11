package jmameui.gui.swt.preferences.options;

import jmameui.mame.GuiControls;
import jmameui.mame.MameExecutable;

import org.eclipse.swt.widgets.Group;


public class ScreenOption extends IniOption {

    public ScreenOption(Group owner, GuiControls g, MameExecutable me) {
	super(owner,g,me);
    }
    
    public ScreenOption() {
    }
    
    @Override
    public void initUI() {
	CreateDoubleSpinner("Brightness", "brightness", 1);
	CreateDoubleSpinner("Contrast", "contrast", 1);
	CreateDoubleSpinner("Gamma", "gamma", 1);
	CreateDoubleSpinner("Pause Brightness", "pause_brightness", 1);
	
	createMamePathComp("Effect", "effect", LOAD_DIALOG);
    }
}
