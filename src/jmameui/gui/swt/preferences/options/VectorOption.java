package jmameui.gui.swt.preferences.options;

import org.eclipse.swt.widgets.Group;

import jmameui.mame.GuiControls;
import jmameui.mame.MameExecutable;

public class VectorOption extends IniOption {

    public VectorOption() {
    }

    public VectorOption(Group owner, GuiControls g, MameExecutable me) {
	super(owner, g, me);
    }
    
    @Override
    public void initUI() {
	createCheckBox("Antialias", "antialias");
	
	CreateDoubleSpinner("Beam", "beam", 1);
	
	createIntSpinner("Flicker", "flicker", 10);
    }
}
