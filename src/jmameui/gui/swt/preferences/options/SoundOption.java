package jmameui.gui.swt.preferences.options;

import jmameui.mame.GuiControls;
import jmameui.mame.MameExecutable;

import org.eclipse.swt.widgets.Group;

public class SoundOption extends IniOption {

    public SoundOption() {
    }

    public SoundOption(Group owner, GuiControls g, MameExecutable me) {
	super(owner, g, me);
    }
    
    @Override
    public void initUI() {
	createCheckBox("Sound", "sound");
	createCheckBox("Samples ", "samples");
	
	createTextBox("Sample Rate", "samplerate");
	
	createIntSpinner("Volume", "volume", -32, 0);
	createIntSpinner("Audio Latency", "audio_latency", 0, 5);
    }

}
