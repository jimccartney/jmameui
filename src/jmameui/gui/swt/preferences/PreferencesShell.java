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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;

import jmameui.gui.swt.preferences.options.ArtworkOption;
import jmameui.gui.swt.preferences.options.CorePerformanceOption;
import jmameui.gui.swt.preferences.options.DebuggingOption;
import jmameui.gui.swt.preferences.options.FullScreenOption;
import jmameui.gui.swt.preferences.options.IniOption;
import jmameui.gui.swt.preferences.options.InputAutomatic;
import jmameui.gui.swt.preferences.options.InputOptions;
import jmameui.gui.swt.preferences.options.JMameMSetTab;
import jmameui.gui.swt.preferences.options.MiscOption;
import jmameui.gui.swt.preferences.options.OpenGLOption;
import jmameui.gui.swt.preferences.options.OutputDirOption;
import jmameui.gui.swt.preferences.options.PerWindowVideoOption;
import jmameui.gui.swt.preferences.options.PerformanceOption;
import jmameui.gui.swt.preferences.options.RotationOption;
import jmameui.gui.swt.preferences.options.SDLKeyboardOption;
import jmameui.gui.swt.preferences.options.SDLOption;
import jmameui.gui.swt.preferences.options.ScreenOption;
import jmameui.gui.swt.preferences.options.SearchPathOption;
import jmameui.gui.swt.preferences.options.SoundOption;
import jmameui.gui.swt.preferences.options.StatePlaybackOption;
import jmameui.gui.swt.preferences.options.VectorOption;
import jmameui.gui.swt.preferences.options.VideoOptions;
import jmameui.mame.FileIO;
import jmameui.mame.GuiControls;
import jmameui.mame.MameExecutable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
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
    ScrolledComposite sc;
    ScrolledComposite sc1;

    private HashMap<String, IniOption> options = new HashMap<String, IniOption>();

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
		    mamePrefList.notifyListeners(SWT.Selection, new Event());
		}
	    }
	}
    };
    private SelectionAdapter mamePrefListAdapter = new SelectionAdapter() {
	public void widgetSelected(SelectionEvent arg0) {
	    if (currExec == null) {
		return;
	    }

	    for (Control i : group.getChildren()) {
		i.dispose();

	    }

	    try {
		Class<?> cls = Class.forName(options
			.get(mamePrefList.getItem(mamePrefList
				.getSelectionIndex())).getClass().getName());
		Constructor<?> cons = cls.getConstructor(Group.class,
			GuiControls.class, MameExecutable.class);
		cons.newInstance(group, Gcon, currExec);
	    } catch (ClassNotFoundException e) {
		FileIO.writeToLogFile(e);
	    } catch (NoSuchMethodException e) {
		FileIO.writeToLogFile(e);
	    } catch (SecurityException e) {
		FileIO.writeToLogFile(e);
	    } catch (InstantiationException e) {
		FileIO.writeToLogFile(e);
	    } catch (IllegalAccessException e) {
		FileIO.writeToLogFile(e);
	    } catch (IllegalArgumentException e) {
		FileIO.writeToLogFile(e);
	    } catch (InvocationTargetException e) {
		FileIO.writeToLogFile(e);
	    }
	    
	 
	    sc1.setMinSize(group.computeSize(SWT.DEFAULT,SWT.DEFAULT));
	    group.layout();
	    mameTabsComp.layout(true, true);
	}
    };

    public PreferencesShell(Shell owner, GuiControls g) {
	shell = new Shell(owner, SWT.RESIZE);
	Gcon = g;
	shell.setLayout(new GridLayout(1, false));
	initUI();

	popOptions();
	shell.pack();
	shell.setSize(640, 480);
	shell.open();

	while (!shell.isDisposed()) {
	    if (!shell.getDisplay().readAndDispatch()) {
		shell.getDisplay().sleep();
	    }
	}
    }

    public void popOptions() {
	options.put("Core Performance", new CorePerformanceOption());
	options.put("Output Directory", new OutputDirOption());
	options.put("Performance", new PerformanceOption());
	options.put("Rotation", new RotationOption());
	options.put("Search Paths", new SearchPathOption());
	options.put("State/Playback", new StatePlaybackOption());
	options.put("Video", new VideoOptions());
	options.put("Screen", new ScreenOption());
	options.put("Debugging", new DebuggingOption());
	options.put("SDL Lowlevel", new SDLOption());
	options.put("Vector", new VectorOption());
	options.put("Input", new InputOptions());
	options.put("Input Automatic", new InputAutomatic());
	options.put("Artwork", new ArtworkOption());
	options.put("Sound", new SoundOption());
	options.put("Misc", new MiscOption());
	options.put("Full Screen", new FullScreenOption());
	options.put("SDL Keyboard", new SDLKeyboardOption());
	options.put("SDL Joystick", new SDLKeyboardOption());
	options.put("Per-Window Video", new PerWindowVideoOption());
	options.put("OpenGL", new OpenGLOption());

	String[] keys = options.keySet().toArray(new String[0]);
	Arrays.sort(keys);
	mamePrefList.setItems(keys);
	mamePrefList.setSelection(0);
	sc.setMinSize(mamePrefList.computeSize(150,
		mamePrefList.getItemHeight() * mamePrefList.getItemCount()));
	mameCombo.notifyListeners(SWT.Selection, new Event());
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
	mameCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
		false, 1, 1));
	mameCombo.select(0);
	
	sc1 = new ScrolledComposite(mameTabsComp, SWT.BORDER | SWT.H_SCROLL
		| SWT.V_SCROLL);
	sc1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,1,2));
	sc1.setExpandHorizontal(true);
	sc1.setExpandVertical(true);

	group = new Group(sc1, SWT.SHADOW_ETCHED_IN | SWT.V_SCROLL);
	group.setLayout(new GridLayout(1, false));
	sc = new ScrolledComposite(mameTabsComp, SWT.BORDER | SWT.H_SCROLL
		| SWT.V_SCROLL);
	sc1.setContent(group);
	
	sc.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true));
	sc.setExpandHorizontal(true);
	sc.setExpandVertical(true);
	mamePrefList = new List(sc, SWT.BORDER);
	mamePrefList.addSelectionListener(mamePrefListAdapter);
	sc.setContent(mamePrefList);
	

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
