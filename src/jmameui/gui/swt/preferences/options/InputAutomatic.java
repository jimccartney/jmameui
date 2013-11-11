package jmameui.gui.swt.preferences.options;

import jmameui.mame.GuiControls;
import jmameui.mame.MameExecutable;

import org.eclipse.swt.widgets.Group;

public class InputAutomatic extends IniOption{

    public InputAutomatic() {
    }
    
    public InputAutomatic(Group owner, GuiControls g, MameExecutable me) {
	super(owner, g, me);
    }
    
    @Override
    public void initUI() {
	String[] ops = new String[]{"none","keyboard","mouse","lightgun","joystick"};
	
	createMameCombo("Paddle Device", "paddle_device", ops);
	createMameCombo("Adstick Device", "adstick_device", ops);
	createMameCombo("Pedal Device", "pedal_device", ops);
	createMameCombo("Dial Device", "dial_device", ops);
	createMameCombo("Trackball Device", "trackball_device", ops);
	createMameCombo("Lightgun Device", "lightgun_device", ops);
	createMameCombo("Positional Device", "positional_device", ops);
	createMameCombo("Mouse Device", "mouse_device", ops);
    }

}
