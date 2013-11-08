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
import java.util.Collections;
import java.util.Comparator;

import jmameui.gui.swt.preferences.PreferencesShell;
import jmameui.mame.GuiControls;
import jmameui.mame.MameRom;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public abstract class SWTJMameUI {

	Shell shell;
	GuiControls gCon = new GuiControls();
	Table goodTable;
	Table favTable;
	Table searchTable;
	Button launchButton;
	String currRomset = "";
	TabFolder tabs;
	Text searchField;
	Label statusLabel;
	MenuItem addToFav;
	String[] tableTitles = { "Game name", "Description", "Manufacturer",
			"Year", "Emulation state", "Mame version" };
	Image favImg = loadImage("emblem-favorite.png");
	Image notFavImg = loadImage("emblem-not-favorite.png");

	SelectionAdapter exitAdapter = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			shell.getDisplay().dispose();
			System.exit(0);
		}
	};

	MouseAdapter tableDoubleClicked = new MouseAdapter() {
		public void mouseDoubleClick(MouseEvent e) {
			if (!currRomset.equals("")) {
				gCon.runGame(currRomset);
			}
		}
	};

	SelectionAdapter launchBtnListener = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent arg0) {
			if (!currRomset.equals("")) {
				gCon.runGame(currRomset);
			}
		}
	};

	SelectionAdapter rebuildBtnListener = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent arg0) {
			buildDB(true);
		}
	};

	SelectionAdapter TableSListener = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent arg0) {
			if (goodTable.isVisible()) {
				currRomset = goodTable.getSelection()[0].getText();
				deselectTableIndexes(favTable, searchTable);
			} else if (favTable.isVisible()) {
				currRomset = favTable.getSelection()[0].getText();
				deselectTableIndexes(goodTable, searchTable);
			} else if (searchTable.isVisible()) {
				currRomset = searchTable.getSelection()[0].getText();
				deselectTableIndexes(goodTable, favTable);
			}

			if (gCon.getRom(currRomset).isFavourite()) {
				addToFav.setText("Remove " + currRomset + " from favourites");
			} else {
				addToFav.setText("Add " + currRomset + " to favourites");

			}
		}
	};

	SelectionAdapter tabChangeListener = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent arg0) {
			if (tabs.getSelectionIndex() == 2 && searchField != null) {
				searchField.setVisible(true);
			} else {
				searchField.setVisible(false);
			}
		}
	};

	ModifyListener searchListener = new ModifyListener() {
		public void modifyText(ModifyEvent e) {
			searchTable.removeAll();

			for (MameRom i : gCon.getRomSet()) {
				if (searchRoms(i)) {
					addToTable(searchTable, i);
				}
			}
			packTable(searchTable);
		}
	};

	SelectionAdapter addFavListener = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			if (!currRomset.equals("") && gCon.addToFavourites(currRomset)) {
				changeTableImg(goodTable, true);
				changeTableImg(searchTable, true);
				addToTable(favTable, gCon.getRom(currRomset));
			} else {
				changeTableImg(goodTable, false);
				changeTableImg(searchTable, false);
				for (int i = 0; i < favTable.getItemCount(); i++) {
					if (favTable.getItem(i).getText().equals(currRomset)) {
						favTable.remove(i);
					}
				}
			}
		}
	};

	SelectionAdapter viewUnavailRomsets = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent arg0) {
			viewBadRomsets();
		}
	};

	SelectionAdapter viewPrefListener = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent arg0) {
			new PreferencesShell(shell, gCon);
		};
	};
	
	SelectionAdapter addRomAdapter = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent arg0) {
			new MameAddRom(shell, gCon);
		};
	};

	ArrayList<MameRom> roms;
	Listener sortListener = new Listener() {
		public void handleEvent(Event e) {
			TableColumn tablecol = (TableColumn) e.widget;
			Table table = tablecol.getParent();
			String colName = tablecol.getText();

			for (String i : tableTitles) {
				if (colName.equals(i)) {
					sortRoms(table, i);
				}
			}

			table.removeAll();
			for (MameRom mr : roms) {
				if (table == favTable && !mr.isFavourite()) {
					continue;
				} else if (table == searchTable && !searchRoms(mr)) {
					continue;
				}
				addToTable(table, mr);
			}
			packTable(table);
		}
	};

	public boolean searchRoms(MameRom m) {
		String text = searchField.getText();
		if (text.equals("")) {
			return false;
		}

		if (m.getName().contains(text)
				|| m.getDescription().toLowerCase().contains(text)) {
			return true;
		}
		return false;
	}

	public void sortRoms(Table table, String colName) {
		if (table.getSortDirection() == SWT.UP) {
			Collections.sort(roms, getComparator(colName, true));
			table.setSortDirection(SWT.DOWN);
		} else {
			Collections.sort(roms, getComparator(colName, false));
			table.setSortDirection(SWT.UP);
		}
	}

	public Comparator<MameRom> getComparator(String colName, boolean revSort) {
		if (colName.equals(tableTitles[0])) {
			if (revSort) {
				return romNameRevSort;
			}
			return romNameSort;
		} else if (colName.equals(tableTitles[1])) {
			if (revSort) {
				return romDescRevSort;
			}
			return romDescSort;
		} else if (colName.equals(tableTitles[2])) {
			if (revSort) {
				return romManuRevSort;
			}
			return romManuSort;
		} else if (colName.equals(tableTitles[3])) {
			if (revSort) {
				return romYearRevSort;
			}
			return romYearSort;
		} else if (colName.equals(tableTitles[4])) {
			if (revSort) {
				return romEmuRevSort;
			}
			return romEmuSort;
		} else if (colName.equals(tableTitles[5])) {
			if (revSort) {
				return romMameRevSort;
			}
			return romMameSort;
		}
		return null;
	}

	Comparator<MameRom> romNameSort = new Comparator<MameRom>() {
		public int compare(MameRom o1, MameRom o2) {
			return o1.getName().compareToIgnoreCase(o2.getName());
		}
	};
	Comparator<MameRom> romNameRevSort = new Comparator<MameRom>() {
		public int compare(MameRom o1, MameRom o2) {
			return o2.getName().compareToIgnoreCase(o1.getName());
		}
	};
	Comparator<MameRom> romDescSort = new Comparator<MameRom>() {
		public int compare(MameRom o1, MameRom o2) {
			return o1.getDescription().compareToIgnoreCase(o2.getDescription());
		}
	};
	Comparator<MameRom> romDescRevSort = new Comparator<MameRom>() {
		public int compare(MameRom o1, MameRom o2) {
			return o2.getDescription().compareToIgnoreCase(o1.getDescription());
		}
	};
	Comparator<MameRom> romManuSort = new Comparator<MameRom>() {
		public int compare(MameRom o1, MameRom o2) {
			return o1.getManufacturer().compareToIgnoreCase(
					o2.getManufacturer());
		}
	};
	Comparator<MameRom> romManuRevSort = new Comparator<MameRom>() {
		public int compare(MameRom o1, MameRom o2) {
			return o2.getManufacturer().compareToIgnoreCase(
					o1.getManufacturer());
		}
	};
	Comparator<MameRom> romYearSort = new Comparator<MameRom>() {
		public int compare(MameRom o1, MameRom o2) {
			return o1.getYear().compareToIgnoreCase(o2.getYear());
		}
	};
	Comparator<MameRom> romYearRevSort = new Comparator<MameRom>() {
		public int compare(MameRom o1, MameRom o2) {
			return o2.getYear().compareToIgnoreCase(o1.getYear());
		}
	};
	Comparator<MameRom> romEmuSort = new Comparator<MameRom>() {
		public int compare(MameRom o1, MameRom o2) {
			return o1.getEmuState().compareToIgnoreCase(o2.getEmuState());
		}
	};
	Comparator<MameRom> romEmuRevSort = new Comparator<MameRom>() {
		public int compare(MameRom o1, MameRom o2) {
			return o2.getEmuState().compareToIgnoreCase(o1.getEmuState());
		}
	};
	Comparator<MameRom> romMameSort = new Comparator<MameRom>() {
		public int compare(MameRom o1, MameRom o2) {
			return o1.getMameVersion().compareToIgnoreCase(o2.getMameVersion());
		}
	};
	Comparator<MameRom> romMameRevSort = new Comparator<MameRom>() {
		public int compare(MameRom o1, MameRom o2) {
			return o2.getMameVersion().compareToIgnoreCase(o1.getMameVersion());
		}
	};

	public abstract void initUI();

	public abstract void initMenuAndToolbar();

	public abstract void buildDB(final boolean forceRebuild);

	public abstract void viewBadRomsets();

	public MenuItem createMenuItem(Menu menu, String text, String imgName,
			SelectionAdapter sa) {
		MenuItem tmp = new MenuItem(menu, SWT.PUSH);
		tmp.setText(text);
		tmp.setImage(loadImage(imgName));
		tmp.addSelectionListener(sa);
		return tmp;
	}

	public Menu createMenu(String text) {
		Menu tmpMenu = new Menu(shell, SWT.DROP_DOWN);
		MenuItem casMenu = new MenuItem(shell.getMenuBar(), SWT.CASCADE);
		casMenu.setText(text);
		casMenu.setMenu(tmpMenu);
		return tmpMenu;
	}

	public void addMenuItemToToolBar(MenuItem action, ToolBar tb,
			SelectionListener sl) {
		ToolItem toolItem = new ToolItem(tb, SWT.PUSH);
		toolItem.setImage(action.getImage());
		toolItem.addSelectionListener(sl);
	}

	public Table createTable(TabItem control) {

		Table tmp = new Table(tabs, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		tmp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		tmp.setHeaderVisible(true);
		tmp.setLinesVisible(true);
		control.setControl(tmp);
		for (int i = 0; i < tableTitles.length; i++) {
			TableColumn tc1 = new TableColumn(tmp, SWT.None);
			tc1.addListener(SWT.Selection, sortListener);
			tc1.setText(tableTitles[i]);
			tc1.pack();
		}

		tmp.addMouseListener(tableDoubleClicked);
		tmp.addSelectionListener(TableSListener);
		return tmp;
	}

	public Image loadImage(String fileName) {
		return new Image(Display.getDefault(), this.getClass().getClassLoader()
				.getResourceAsStream("jmameui/gui/icons/" + fileName));

	}

	public void deselectTableIndexes(Table... t) {
		for (Table i : t) {
			i.deselectAll();
		}
	}

	public void changeTableImg(Table table, boolean favadded) {
		for (TableItem i : table.getItems()) {
			if (i.getText().equals(currRomset)) {
				Image tmpImg = (favadded) ? favImg : notFavImg;
				i.setImage(tmpImg);
			}
		}
	}

	public void addToTable(Table table, MameRom mr) {
		String[] tmp = { mr.getName(), mr.getDescription(),
				mr.getManufacturer(), mr.getYear(), mr.getEmuState(),
				mr.getMameVersion() };
		Image tmpImg = (mr.isFavourite()) ? favImg : notFavImg;
		TableItem item = new TableItem(table, SWT.NONE);
		item.setImage(0, tmpImg);
		item.setText(tmp);
	}

	public void packTable(Table... table) {
		for (Table i : table) {
			for (TableColumn j : i.getColumns()) {
				j.pack();
			}
		}
	}

	public Shell getShell() {
		return shell;
	}

	public GuiControls getgCon() {
		return gCon;
	}
}
