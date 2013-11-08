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

import java.io.File;

import jmameui.mame.GuiControls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class MameNotFound {

	Label iconLabel;
	Label textLabel;
	Button ignoreBtn;
	Button alwaysignCkBox;
	Button browseBtn;
	Shell shell;
	GuiControls gCon;

	private SelectionAdapter ignoreBtnListener = new SelectionAdapter() {
		public void widgetSelected(org.eclipse.swt.events.SelectionEvent arg0) {
			boolean b = alwaysignCkBox.getSelection();
			if (b) {
				gCon.changeSettingsFile("use_system_mame", "false");
			}
			shell.dispose();
		}
	};
	private SelectionAdapter addMameListener = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent arg0) {
			FileDialog dialog = new FileDialog(shell);
			dialog.setText("Choose Mame executable");
			dialog.setFilterPath(System.getProperty("user.home"));
			String newMame = dialog.open();
			if (newMame != null) {
				gCon.addMameExecutable(new File(newMame));
				shell.dispose();
			}
		}
	};

	public MameNotFound(JMameUI mUi) {
		shell = new Shell(mUi.getShell(), SWT.RESIZE);
		gCon = mUi.getgCon();
		shell.setLayout(new GridLayout(3, false));
		initUI();
	}

	private void initUI() {
		iconLabel = new Label(shell, SWT.None);
		iconLabel.setImage(shell.getDisplay().getSystemImage(SWT.ICON_WARNING));
		iconLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

		textLabel = new Label(shell, SWT.NONE);
		textLabel
				.setText("System Mame not found!\nClick browse to locate Mame\nOr ignore");
		textLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2,
				1));

		ignoreBtn = new Button(shell, SWT.PUSH);
		ignoreBtn.setText("Ignore");
		ignoreBtn.addSelectionListener(ignoreBtnListener);
		ignoreBtn.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
				false));

		alwaysignCkBox = new Button(shell, SWT.CHECK);
		alwaysignCkBox.setText("Always Ignore");
		alwaysignCkBox.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER,
				false, false));

		browseBtn = new Button(shell, SWT.PUSH);
		browseBtn.setText("Browse");
		browseBtn.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
				false));
		browseBtn.addSelectionListener(addMameListener);

		shell.pack();
		shell.open();
	}
}
