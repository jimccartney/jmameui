package jmameui.gui.swt.preferences.options;

import jmameui.mame.GuiControls;
import jmameui.mame.MameExecutable;

import org.eclipse.swt.widgets.Group;

public class MiscOption extends IniOption {

    public MiscOption() {
    }

    public MiscOption(Group owner, GuiControls g, MameExecutable me) {
	super(owner, g, me);
    }
    
    @Override
    public void initUI() {
	createCheckBox("Drc", "drc");
	createCheckBox("Drc Use C", "drc_use_c");
	createCheckBox("Cheat", "cheat");
	createCheckBox("Skip Gameinfo", "skip_gameinfo");
	createCheckBox("Confirm Quit", "confirm_quit");
	createCheckBox("Ui Mouse", "ui_mouse");
	
	createTextBox("Bios", "bios");
	createTextBox("Ui Font", "uifont");
	createTextBox("Ramsize", "ramsize");
	
	createTextBox("Autoboot Command", "autoboot_command");
	createIntSpinner("Autoboot Delay", "autoboot_delay", 0, 60000);
	createMamePathComp("Autoboot Script", "autoboot_script", LOAD_DIALOG);
	
	createTextBox("Http", "http");
	createTextBox("Http Port", "http_port");
	createTextBox("Http Path", "http_path");
    }

}
