package jmameui.gui.swt.preferences.options;

import jmameui.mame.GuiControls;
import jmameui.mame.MameExecutable;

import org.eclipse.swt.widgets.Group;

public class InputOptions extends IniOption {

    public InputOptions() {
    }

    public InputOptions(Group owner, GuiControls g, MameExecutable me) {
	super(owner, g, me);
    }
    
    @Override
    public void initUI() {
	createCheckBox("Coin Lockout", "coin_lockout");
	createCheckBox("Mouse", "mouse");
	createCheckBox("Joystick", "joystick");
	createCheckBox("Lightgun", "lightgun");
	createCheckBox("Offscreen Reload", "offscreen_reload");
	createCheckBox("Multi Keyboard", "multikeyboard");
	createCheckBox("Multi Mouse", "coin_lockout");
	createCheckBox("Steady Key", "steadykey");
	createCheckBox("Ui Active", "ui_active");
	createCheckBox("Joystick Contradictory", "joystick_contradictory");
	createCheckBox("Natural", "natural");

	createMamePathComp("Ctrlr", "ctrlr", LOAD_DIALOG);
	createMamePathComp("Joystick Map", "joystick_map", LOAD_DIALOG);

	CreateDoubleSpinner("Joystick Deadzone", "joystick_deadzone", 1);
	CreateDoubleSpinner("Joystick Saturation", "joystick_saturation", 1);
	CreateDoubleSpinner("Coin Impulse", "coin_impulse", 10);
    }

}
