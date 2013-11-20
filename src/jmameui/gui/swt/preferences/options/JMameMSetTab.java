/**
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
 * 
 */
package jmameui.gui.swt.preferences.options;

import java.io.File;
import java.util.ArrayList;

import jmameui.mame.GuiControls;
import jmameui.mame.MameExecutable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.TabFolder;

public class JMameMSetTab {

	private Group jMSetGroup;
	private Label jMSortLab;
	private Combo jMameVBCombo;
	private Button mameExecAddBtn;
	private Button mameExecRmBtn;
	private List mameExecList;
	private Label mameExecLab;
	private GuiControls gCon;

	private SelectionAdapter rmMameListener = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent arg0) {
			int index = mameExecList.getSelectionIndex();
			String text = mameExecList
					.getItem(mameExecList.getSelectionIndex());

			if (text.equals(gCon.getSystemMame().getPath())) {
				gCon.changeSettingsFile("use_system_mame", "false");
			}
			gCon.removeMameExecutable(new File(text));
			mameExecList.remove(index);
		};
	};

	private SelectionAdapter addMameListener = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent arg0) {
			FileDialog dialog = new FileDialog(jMSetGroup.getShell());
			dialog.setText("Choose Mame executable");
			dialog.setFilterPath(System.getProperty("user.home"));
			String newMame = dialog.open();
			if (newMame != null) {
				gCon.addMameExecutable(new File(newMame),false);
				mameExecList.removeAll();
				addToList(mameExecList, gCon.getMameExecutables());
			}
		}
	};

	private SelectionAdapter comboListener = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent arg0) {
			gCon.changeSettingsFile("mame_sort", jMameVBCombo.getText());
		};
	};

	public JMameMSetTab(TabFolder owner, GuiControls g, int option) {
		jMSetGroup = new Group(owner, option);
		gCon = g;
		initUI();
	}

	private void initUI() {
		jMSetGroup.setLayout(new GridLayout(4, false));

		mameExecLab = new Label(jMSetGroup, SWT.None);
		mameExecLab.setText("Mame executables:");
		mameExecLab.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false));

		mameExecList = new List(jMSetGroup, SWT.MULTI | SWT.BORDER | SWT.WRAP
				| SWT.V_SCROLL);
		addToList(mameExecList, gCon.getMameExecutables());
		mameExecList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1));

		mameExecRmBtn = new Button(jMSetGroup, SWT.PUSH);
		mameExecRmBtn.setText("Remove");
		mameExecRmBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false));
		mameExecRmBtn.addSelectionListener(rmMameListener);

		mameExecAddBtn = new Button(jMSetGroup, SWT.PUSH);
		mameExecAddBtn.setText("Add");
		mameExecAddBtn.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		mameExecAddBtn.addSelectionListener(addMameListener);

		jMSortLab = new Label(jMSetGroup, SWT.NONE);
		jMSortLab.setText("Mame version bias");
		jMSortLab
				.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));

		jMameVBCombo = new Combo(jMSetGroup, SWT.READ_ONLY);
		jMameVBCombo.setItems(new String[] { "newer", "older", "system" });
		jMameVBCombo.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false,
				false));
		jMameVBCombo.addSelectionListener(comboListener);
		jMameVBCombo.select(jMameVBCombo.indexOf(gCon
				.readSettingsFile("mame_sort")));

	}

	public void addToList(List l, ArrayList<MameExecutable> items) {
		for (MameExecutable i : items) {
			l.add(i.getPath());
		}
	}

	public Group getGroup() {
		return jMSetGroup;
	}
}