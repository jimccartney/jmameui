package jmameui.gui.swt.preferences.options;

import jmameui.mame.FileIO;
import jmameui.mame.GuiControls;
import jmameui.mame.MameExecutable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;

public class VideoOptions extends IniOption {
	
	public VideoOptions(Group owner, GuiControls g, MameExecutable me) {
		setGroup(owner);
		setmExec(me);
		setgCon(g);
		setIniFile(FileIO.readFile(me.getIniFile()));
		initUI();
	}

	private void initUI() {
		getGroup().setLayout(new GridLayout(2, false));

		createMameCombo("Video","video", new String[] { "opengl", "soft" });
		createMameCombo("Num Screens","numscreens", new String[] { "1" });
		createMameCombo("Scale Mode","scalemode", new String[] { "none", "async", "yv12",
				"yuy2", "yv12x2", "yuy2x2 (-video soft only)" });
		createCheckBox("Window","window");
		createCheckBox("Maximize","maximize");
		createCheckBox("Keep Aspect","keepaspect");
		createCheckBox("Center H","centerh");
		createCheckBox("Center V","centerv");
		createCheckBox("Sync Refresh","syncrefresh");
		createCheckBox("Wait Vsync","waitvsync");
		CreateDoubleSpinner("Uneven Stretch","unevenstretch", 10);
		//createText("Uneven Stretch","unevenstretch");

		getGroup().pack();
	}

}