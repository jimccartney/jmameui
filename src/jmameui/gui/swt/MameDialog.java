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
import java.util.ArrayList;
import java.util.Collection;

import jmameui.mame.FileIO;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class MameDialog {

    public static final int WARNING = 0;
    public static final int INFORMATION = 1;
    public static final int TEXTAREA = 2;
    public static final int ERROR = 3;

    private Shell dialogShell;
    private Image icon;
    private ArrayList<String> data = new ArrayList<String>();
    private SelectionAdapter closeAdapter = new SelectionAdapter() {
	public void widgetSelected(SelectionEvent arg0) {
	    dialogShell.dispose();
	}
    };

    public MameDialog(Shell owner, Collection<? extends String> text, int option) {
	dialogShell = new Shell(owner, SWT.RESIZE);
	data = new ArrayList<String>(text);
	start(option);
    }

    public MameDialog(Shell owner, String text, int option) {
	dialogShell = new Shell(owner, SWT.RESIZE);
	data.add(text);
	start(option);
    }

    public MameDialog(Shell owner, int option) {
	start(option);
    }

    private void start(int option) {
	switch (option) {
	case TEXTAREA:
	    initTEXTAREA();
	    break;
	case WARNING:
	    icon = Display.getDefault().getSystemImage(SWT.ICON_WARNING);
	    InitInformation(false);
	    break;
	case INFORMATION:
	    icon = Display.getDefault().getSystemImage(SWT.ICON_INFORMATION);
	    InitInformation(false);
	    break;
	case ERROR:
	    icon = Display.getDefault().getSystemImage(SWT.ICON_ERROR);
	    InitInformation(true);
	    break;
	}
	dialogShell.pack();
	dialogShell.open();
    }

    private void InitInformation(boolean err) {
	dialogShell.setLayout(new GridLayout(2, false));
	dialogShell.setText("JMameUI");

	Label iconLab = new Label(dialogShell, SWT.NONE);
	iconLab.setImage(icon);
	iconLab.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));

	Label txtLab = new Label(dialogShell, SWT.NONE);
	txtLab.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
	StringBuilder sb = new StringBuilder();
	for (String i : data) {
	    sb.append(i + "\n");
	}
	txtLab.setText(sb.toString());

	Button logBtn = new Button(dialogShell, SWT.PUSH);
	logBtn.setText("View Log");
	logBtn.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent e) {
		new MameDialog(dialogShell,
			FileIO.readFile(FileIO.getLogFile()), TEXTAREA);
	    }
	});
	logBtn.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));

	if (err && !FileIO.getLogFile().exists()) {
	    logBtn.setEnabled(false);
	}else if (!err) {
	    logBtn.setVisible(false);
	}
	
	Button btn = new Button(dialogShell, SWT.PUSH);
	btn.setText("Close");
	btn.addSelectionListener(closeAdapter);
	btn.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

    }

    private void initTEXTAREA() {
	dialogShell.setText("JMameUI");
	dialogShell.setLayout(new GridLayout(1, false));
	Text text = new Text(dialogShell, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL
		| SWT.H_SCROLL);
	text.setEditable(false);
	StringBuilder sb = new StringBuilder();
	for (String i : data) {
	    sb.append(i + "\n");
	}
	text.append(sb.toString());
	GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
	gridData.heightHint = 5 * text.getLineHeight();
	text.setLayoutData(gridData);
	Button closeBtn = new Button(dialogShell, SWT.PUSH);
	closeBtn.setText("Close");
	closeBtn.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
	closeBtn.addSelectionListener(closeAdapter);
    }

}
