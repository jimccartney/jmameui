package jmameui.gui.swt.preferences.options;

import jmameui.mame.GuiControls;
import jmameui.mame.MameExecutable;

import org.eclipse.swt.widgets.Group;

public class ArtworkOption extends IniOption {

    public ArtworkOption() {
    }

    public ArtworkOption(Group owner, GuiControls g, MameExecutable me) {
	super(owner, g, me);
    }
    
    @Override
    public void initUI() {
	createCheckBox("Artwork Crop", "artwork_crop");
	createCheckBox("Use Backdrops", "use_backdrops");
	createCheckBox("Use Overlays", "use_overlays");
	createCheckBox("Use Bezels", "use_bezels");
	createCheckBox("Use Cpanels", "use_cpanels");
	createCheckBox("Use Marquees", "use_marquees");

    }

}
