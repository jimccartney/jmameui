package jmameui.gui.swt.preferences.options;

import jmameui.mame.GuiControls;
import jmameui.mame.MameExecutable;

import org.eclipse.swt.widgets.Group;

public class StatePlaybackOption extends IniOption {

    public StatePlaybackOption(Group owner, GuiControls g, MameExecutable me) {
	super(owner, g, me);
    }

    public void initUI() {
	
	createCheckBox("Auto Save", "autosave");
	createCheckBox("Burn In", "burnin");
	createMamePathComp("State", "state", LOAD_DIALOG);
	createMamePathComp("Play Back", "playback", LOAD_DIALOG);
	createMamePathComp("Mng Write", "mngwrite", SAVE_DIALOG);
	createMamePathComp("Avi Write", "aviwrite", SAVE_DIALOG);
	createMamePathComp("Wav Write", "wavwrite", SAVE_DIALOG);
	createTextBox("Snap Name", "snapname");
	createTextBox("Snap Size", "snapsize");
	createTextBox("Snap View", "snapview");
	createTextBox("State Name", "statename");
	
	getGroup().pack(true);
    }
}
