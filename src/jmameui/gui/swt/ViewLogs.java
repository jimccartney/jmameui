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
package jmameui.gui.swt;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

import jmameui.mame.FileIO;
import jmameui.mame.GuiControls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

public class ViewLogs {

    private GuiControls gCon;
    private Shell shell;
    private List logList;
    private SimpleDateFormat df = new SimpleDateFormat("dd-MM-yy");

    private Comparator<String> dateComp = new Comparator<String>() {
	public int compare(String o1, String o2) {
	    Date d1 = parseDate(o1);
	    Date d2 = parseDate(o2);
	    if (d1 != null && d2 != null) {
		return d1.compareTo(d2);
	    }
	    return 0;
	}
    };

    public ViewLogs(Shell owner, GuiControls g) {
	shell = new Shell(owner, SWT.RESIZE);
	shell.setLayout(new GridLayout(4, false));
	this.gCon = g;

	initUI();

	shell.setText("JMameUI");
	shell.pack();
	shell.open();
    }

    private void initUI() {
	File[] files = gCon.getLogDir().listFiles();
	String[] fileNames = new String[files.length];

	for (int i = 0; i < files.length; ++i) {
	    fileNames[i] = files[i].getName().replace("log ", "");
	}

	Arrays.sort(fileNames, dateComp);

	logList = new List(shell, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
	logList.setItems(fileNames);
	logList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1));
	logList.addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseDoubleClick(MouseEvent arg0) {
		openFile();
	    }
	});

	Button closeBtn = new Button(shell, SWT.PUSH);
	closeBtn.setText("Close");
	closeBtn.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
	closeBtn.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent arg0) {
		shell.dispose();
	    }
	});

	Button delBtn = new Button(shell, SWT.PUSH);
	delBtn.setText("Delete");
	delBtn.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
	delBtn.addSelectionListener(new SelectionAdapter() {
	    public void widgetSelected(SelectionEvent arg0) {
		deleteLog(false);
	    }
	});

	Button DelAllBtn = new Button(shell, SWT.PUSH);
	DelAllBtn.setText("Delete All");
	DelAllBtn.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
		false));
	DelAllBtn.addSelectionListener(new SelectionAdapter() {
	    public void widgetSelected(SelectionEvent arg0) {
		deleteLog(true);
	    }
	});

	Button viewBtn = new Button(shell, SWT.PUSH);
	viewBtn.setText("View Log");
	viewBtn.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
	viewBtn.addSelectionListener(new SelectionAdapter() {
	    public void widgetSelected(SelectionEvent arg0) {
		openFile();
	    }
	});
    }

    private void openFile() {
	if (logList.getSelectionIndex() >= 0) {
	    File logFile = new File(gCon.getLogDir(), "log "
		    + logList.getSelection()[0]);

	    if (logFile.exists()) {
		new MameDialog(shell, FileIO.readFile(logFile),
			MameDialog.TEXTAREA);
	    } else {
		new MameDialog(shell, logFile.getName() + " not found",
			MameDialog.WARNING);
	    }
	}
    }

    private void deleteLog(boolean all) {
	if (all) {
	    for (File i : gCon.getLogDir().listFiles()) {
		i.delete();
	    }
	    logList.removeAll();
	} else if (logList.getSelectionIndex() >= 0) {
	    new File(gCon.getLogDir(), "log " + logList.getSelection()[0])
		    .delete();
	    logList.remove(logList.getSelection()[0]);
	}
    }

    private Date parseDate(String o1) {
	try {
	    return df.parse(o1);
	} catch (ParseException e) {
	    FileIO.writeToLogFile(e);
	}
	return null;
    }
}
