/*
 * This file is part of JMameUI.
 * 
 * JMameUI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * JMameUI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with JMameUI.  If not, see <http://www.gnu.org/licenses/>.
 */
package jmameui.gui.swt.preferences;

import jmameui.gui.swt.preferences.options.JMameMSetTab;
import jmameui.gui.swt.preferences.options.OutputDirOption;
import jmameui.gui.swt.preferences.options.PerformanceOption;
import jmameui.gui.swt.preferences.options.RotationOption;
import jmameui.gui.swt.preferences.options.SearchPathOption;
import jmameui.gui.swt.preferences.options.StatePlaybackOption;
import jmameui.gui.swt.preferences.options.VideoOptions;
import jmameui.mame.GuiControls;
import jmameui.mame.MameExecutable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

public class PreferencesShell {

    private Shell shell;
    private TabFolder mainTabs;
    private Composite mameTabsComp;
    private TabItem mameTab;
    private Button closeBtn;
    private Combo mameCombo;
    private TabItem jMameTab;
    private Composite jMameTabsComp;
    private TabFolder jMameTabs;
    private TabItem jMameMTab;
    private Group group;
    private List mamePrefList;
    private MameExecutable currExec = null;
    private GuiControls Gcon;

    private SelectionAdapter closeBtnListener = new SelectionAdapter() {
	public void widgetSelected(SelectionEvent arg0) {
	    shell.dispose();
	};
    };

    private SelectionAdapter mameComboAdapter = new SelectionAdapter() {
	public void widgetSelected(SelectionEvent arg0) {
	    for (MameExecutable i : Gcon.getMameExecutables()) {
		if (i.getVersion().equals(mameCombo.getText())) {
		    currExec = i;
		    mamePrefListAdapter.widgetSelected(arg0);
		}
	    }
	}
    };
    private SelectionAdapter mamePrefListAdapter = new SelectionAdapter() {
	public void widgetSelected(SelectionEvent arg0) {
	    if (currExec == null || group == null) {
		return;
	    }
	    for (Control i : group.getChildren()) {
		i.dispose();
	    }
	    String text = mamePrefList
		    .getItem(mamePrefList.getSelectionIndex());
	    if (text.equals("Output Directory")) {
		new OutputDirOption(group, Gcon, currExec);
	    } else if (text.equals("Video")) {
		new VideoOptions(group, Gcon, currExec);
	    } else if (text.equals("Performance")) {
		new PerformanceOption(group, Gcon, currExec);
	    } else if (text.equals("Search Paths")) {
		new SearchPathOption(group, Gcon, currExec);
	    } else if (text.equals("State/Playback")) {
		new StatePlaybackOption(group, Gcon, currExec);
	    } else if (text.equals("Rotation")) {
		new RotationOption(group, Gcon, currExec);
	    }

	    group.layout();
	}
    };

    public PreferencesShell(Shell owner, GuiControls g) {
	shell = new Shell(owner, SWT.RESIZE);
	Gcon = g;
	shell.setLayout(new GridLayout(1, false));
	initUI();

	shell.pack();
	shell.setSize(640, 480);
	shell.open();

    }

    private void initUI() {
	mainTabs = new TabFolder(shell, SWT.NONE);
	mainTabs.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

	mameTab = new TabItem(mainTabs, SWT.NONE);
	mameTab.setText("Mame");

	jMameTab = new TabItem(mainTabs, SWT.NONE);
	jMameTab.setText("JMameUI");

	mameTabsComp = new Composite(mainTabs, SWT.NONE);
	mameTabsComp.setLayout(new GridLayout(2, false));
	mameTab.setControl(mameTabsComp);

	jMameTabsComp = new Composite(mainTabs, SWT.NONE);
	jMameTabsComp.setLayout(new GridLayout(1, false));
	jMameTab.setControl(jMameTabsComp);

	jMameTabs = new TabFolder(jMameTabsComp, SWT.BORDER);
	jMameTabs.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

	mameCombo = new Combo(mameTabsComp, SWT.READ_ONLY);
	addToMameCombo();
	mameCombo.addSelectionListener(mameComboAdapter);
	mameCombo.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false,
		false, 1, 1));
	mameCombo.select(0);
	mameCombo.notifyListeners(SWT.Selection, new Event());

	group = new Group(mameTabsComp, SWT.SHADOW_ETCHED_IN | SWT.H_SCROLL);
	group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
	group.setLayout(new GridLayout(1, false));

	mamePrefList = new List(mameTabsComp, SWT.V_SCROLL | SWT.BORDER);
	mamePrefList.addSelectionListener(mamePrefListAdapter);
	mamePrefList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
		false, 1, 1));
	mamePrefList.setItems(new String[] { "Output Directory", "Performance",
		"Rotation", "Search Paths", "State/Playback", "Video" });
	mamePrefList.setSelection(0);

	jMameMTab = new TabItem(jMameTabs, SWT.NONE);
	jMameMTab.setText("Mame");
	jMameMTab.setControl(new JMameMSetTab(jMameTabs, Gcon,
		SWT.SHADOW_ETCHED_OUT).getGroup());

	closeBtn = new Button(shell, SWT.PUSH);
	closeBtn.setText("Close");
	closeBtn.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
	closeBtn.addSelectionListener(closeBtnListener);
    }

    private void addToMameCombo() {
	for (MameExecutable i : Gcon.getMameExecutables()) {
	    mameCombo.add(i.getVersion());
	}
    }
}
