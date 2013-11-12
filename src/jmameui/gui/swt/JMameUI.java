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
package jmameui.gui.swt;

import java.util.ArrayList;

import jmameui.mame.MameRom;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;

public class JMameUI extends SWTJMameUI {
    public static void main(String[] args) {
	Display display = new Display();
	new JMameUI(display);
	display.dispose();
    }

    public JMameUI(Display display) {

	shell = new Shell(display);
	shell.setText("JMameUI");
	shell.setImage(loadImage("windowicon.png"));
	initUI();
	shell.open();

	Boolean MameFound = gCon.start();

	if (MameFound != null && MameFound) {
	    String ver = gCon.getSystemMame().getVersion().split(" ")[0];
	    String preVer = gCon.readSettingsFile("system_mame_version");
	    if (!ver.equals(preVer)) {
		new MameDialog(	shell,
			"System Mame version changed\nPlease rebuild the database.",
			MameDialog.WARNING);
	    }
	    buildDB(false);
	} else if (MameFound != null) {
	    new MameNotFound(this);
	}

	while (!shell.isDisposed()) {
	    if (!display.readAndDispatch()) {
		display.sleep();
	    }
	}
    }

    public void initUI() {
	initMenuAndToolbar();

	shell.setLayout(new GridLayout(2, false));

	tabs = new TabFolder(shell, SWT.BORDER);
	tabs.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
	tabs.addSelectionListener(tabChangeListener);
	TabItem goodTab = new TabItem(tabs, SWT.NONE);
	goodTab.setText("Good romsets");
	TabItem favTab = new TabItem(tabs, SWT.NONE);
	favTab.setText("Favourites");
	TabItem searchTab = new TabItem(tabs, SWT.NONE);
	searchTab.setText("Search");

	goodTable = createTable(goodTab);
	favTable = createTable(favTab);
	searchTable = createTable(searchTab);

	Menu tableMenu = new Menu(goodTable);
	addToFav = createMenuItem(tableMenu, "Add rom to favourites",
		"emblem-favorite.png", addFavListener);
	goodTable.setMenu(tableMenu);
	searchTable.setMenu(tableMenu);
	favTable.setMenu(tableMenu);

	searchField = new Text(shell, SWT.BORDER);
	searchField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
		false, 1, 1));
	searchField.addModifyListener(searchListener);

	launchButton = new Button(shell, SWT.PUSH);
	launchButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
		false, 1, 1));
	launchButton.setText("Launch");
	launchButton.addSelectionListener(launchBtnListener);

	statusLabel = new Label(shell, SWT.NONE);
	statusLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
		false, 2, 1));
	shell.pack();
	shell.setSize(1200, 600);
    }

    public void initMenuAndToolbar() {
	Menu menuBar = new Menu(shell, SWT.BAR);
	shell.setMenuBar(menuBar);
	ToolBar toolBar = new ToolBar(shell, SWT.BAR);
	toolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
		2, 1));

	Menu fileMenu = createMenu("&File");
	Menu romMenu = createMenu("&Rom");
	Menu viewMenu = createMenu("&View");

	MenuItem exitAct = createMenuItem(fileMenu, "&Exit",
		"application-exit.png", exitAdapter);
	MenuItem addRomAct = createMenuItem(romMenu, "&Add romset",
		"list-add.png", addRomAdapter);
	MenuItem buildDBAct = createMenuItem(romMenu, "&Build rom database",
		"view-refresh.png", rebuildBtnListener);
	MenuItem prefAct = createMenuItem(viewMenu, "&Preferences",
		"configure.png", viewPrefListener);
	MenuItem badRomAct = createMenuItem(viewMenu,
		"&Bad romset information", "dialog-warning.png",
		viewUnavailRomsets);
	addToFav = createMenuItem(romMenu, "Add rom to favourites",
		"emblem-favorite.png", addFavListener);

	addMenuItemToToolBar(addToFav, toolBar, addFavListener);
	addMenuItemToToolBar(buildDBAct, toolBar, rebuildBtnListener);
	addMenuItemToToolBar(addRomAct, toolBar, addRomAdapter);
	addMenuItemToToolBar(prefAct, toolBar, viewPrefListener);
	toolBar.pack();
    }

    public void buildDB(final boolean forceRebuild) {
	shell.setCursor(new Cursor(Display.getDefault(), SWT.CURSOR_WAIT));
	statusLabel.setText("Building database...");
	new Thread(new Runnable() {
	    @Override
	    public void run() {
		gCon.buildDB(forceRebuild);
		Display.getDefault().asyncExec(new Runnable() {
		    public void run() {
			favTable.removeAll();
			goodTable.removeAll();
			roms = new ArrayList<MameRom>(gCon.getRomSet());
			for (MameRom m : roms) {
			    addToTable(goodTable, m);
			    if (m.isFavourite()) {
				addToTable(favTable, m);
			    }
			}
			packTable(goodTable, favTable);
			statusLabel.setText("Ready - available romsets = "
				+ gCon.getGoodRomsetCount());
			shell.setCursor(new Cursor(Display.getDefault(),
				SWT.CURSOR_ARROW));
		    }
		});

	    }
	}).start();
    }

    public void viewBadRomsets() {
	new MameDialog(shell, gCon.getUnavailableRomText(),
		MameDialog.TEXTAREA);

    }
}