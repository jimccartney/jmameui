package jmameui.gui.swt.preferences.options;

import jmameui.mame.GuiControls;
import jmameui.mame.MameExecutable;

import org.eclipse.swt.widgets.Group;

public class SDLOption extends IniOption {

    public SDLOption() {
    }

    public SDLOption(Group owner, GuiControls g, MameExecutable me) {
	super(owner, g, me);
    }
    
    @Override
    public void initUI() {
	createMameCombo("Video Driver", "videodriver", new String[]{"auto","x11","directfb"});
	createMameCombo("Audio Driver", "audiodriver", new String[]{"auto","alsa","arts"});
	
	createMamePathComp("Gl Lib", "gl_lib", LOAD_DIALOG);
    }

}
