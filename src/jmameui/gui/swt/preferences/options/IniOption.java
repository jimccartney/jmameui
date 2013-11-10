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
package jmameui.gui.swt.preferences.options;

import java.io.File;
import java.util.ArrayList;

import jmameui.gui.swt.preferences.widgets.MameCombo;
import jmameui.gui.swt.preferences.widgets.MamePathComp;
import jmameui.gui.swt.preferences.widgets.MameSpinner;
import jmameui.gui.swt.preferences.widgets.MameTextComp;
import jmameui.mame.FileIO;
import jmameui.mame.GuiControls;
import jmameui.mame.MameExecutable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

public class IniOption {

    private GuiControls gCon;
    private MameExecutable mExec;
    private ArrayList<String> iniFile;
    private Group group;
    public static final int LOAD_DIALOG = 0;
    public static final int DIRECTORY_DIALOG = 1;
    public static final int SAVE_DIALOG = 2;

    private SelectionAdapter mameComboAdapter = new SelectionAdapter() {
	public void widgetSelected(SelectionEvent arg0) {
	    Combo combo = (Combo) arg0.widget;
	    MameCombo mc = (MameCombo) combo.getParent();
	    gCon.changeMameIniValue(iniFile, mc.getOption(),
		    combo.getItem(combo.getSelectionIndex()));

	    writeIni();
	}
    };

    private SelectionAdapter checkBoxAdapter = new SelectionAdapter() {
	public void widgetSelected(SelectionEvent arg0) {
	    Button btn = (Button) arg0.widget;
	    String option = btn.getText().replace(" ", "").toLowerCase();
	    String newValue = "";

	    if (btn.getSelection() == true) {
		newValue = "1";
	    } else {
		newValue = "0";
	    }

	    gCon.changeMameIniValue(iniFile, option, newValue);
	    writeIni();
	}
    };

    private SelectionAdapter mameDirPathListener = new SelectionAdapter() {
	public void widgetSelected(SelectionEvent e) {
	    Button btn = (Button) e.widget;
	    MamePathComp mpc = (MamePathComp) btn.getParent();
	    Text txt = mpc.getText();
	    DirectoryDialog dialog = new DirectoryDialog(group.getShell());
	    dialog.setText("Select directory");
	    dialog.setFilterPath(System.getProperty("user.home"));
	    String newPath = dialog.open();
	    if (newPath != null) {
		if (txt.getTextChars()[txt.getCharCount() - 1] == ';') {
		    txt.append(newPath);
		} else {
		    txt.setText(newPath);
		}

		gCon.changeMameIniValue(iniFile, mpc.getOption(), txt.getText()
			.replace(System.getProperty("user.home"), "$HOME"));
		writeIni();
	    }
	}
    };

    private SelectionAdapter mameSavePathListener = new SelectionAdapter() {
	public void widgetSelected(SelectionEvent e) {
	    Button btn = (Button) e.widget;
	    MamePathComp mpc = (MamePathComp) btn.getParent();
	    Text txt = mpc.getText();
	    FileDialog dialog = new FileDialog(group.getShell(), SWT.SAVE);
	    dialog.setText("Save file");
	    dialog.setFilterPath(System.getProperty("user.home"));
	    String newFile = dialog.open();
	    if (newFile != null) {
		txt.setText(newFile);

		gCon.changeMameIniValue(iniFile, mpc.getOption(), txt.getText()
			.replace(System.getProperty("user.home"), "$HOME"));
		writeIni();
	    }
	}
    };

    private SelectionAdapter mameLoadPathListener = new SelectionAdapter() {
	public void widgetSelected(SelectionEvent e) {
	    Button btn = (Button) e.widget;
	    MamePathComp mpc = (MamePathComp) btn.getParent();
	    Text txt = mpc.getText();
	    FileDialog dialog = new FileDialog(group.getShell(), SWT.OPEN);
	    dialog.setText("Load file");
	    dialog.setFilterPath(System.getProperty("user.home"));
	    String newFile = dialog.open();
	    if (newFile != null) {
		txt.setText(newFile);

		gCon.changeMameIniValue(iniFile, mpc.getOption(), txt.getText()
			.replace(System.getProperty("user.home"), "$HOME"));
		writeIni();
	    }
	}
    };

    private ModifyListener mameTxtModifyListener = new ModifyListener() {
	public void modifyText(ModifyEvent e) {
	    Text txt = (Text) e.widget;
	    MameTextComp mtc = (MameTextComp) txt.getParent();

	    gCon.changeMameIniValue(iniFile, mtc.getOption(), txt.getText());
	    writeIni();
	}
    };

    private SelectionAdapter doubleSpinnerAdapter = new SelectionAdapter() {
	public void widgetSelected(SelectionEvent e) {
	    Spinner spin = (Spinner) e.widget;
	    MameSpinner ms = (MameSpinner) spin.getParent();

	    gCon.changeMameIniValue(iniFile, ms.getMameOption(), spin.getText());
	    writeIni();
	};
    };

    private SelectionAdapter intSpinnerAdapter = new SelectionAdapter() {
	public void widgetSelected(SelectionEvent e) {
	    Spinner spin = (Spinner) e.widget;
	    MameSpinner ms = (MameSpinner) spin.getParent();

	    gCon.changeMameIniValue(iniFile, ms.getMameOption(), spin.getText());
	    writeIni();
	};
    };

    public void createMameCombo(String name, String mameOption, String[] items) {
	MameCombo mc = new MameCombo(group, SWT.READ_ONLY);
	Combo combo = mc.getCombo();
	combo.setItems(items);
	String iniOp = gCon.getMameIniValue(iniFile, mameOption);

	if (iniOp.equals("")) {
	    combo.setEnabled(false);
	    mc.getLabel().setToolTipText(
		    "This option is not available in this version of Mame");
	} else {
	    combo.select(combo.indexOf(iniOp));

	}
	combo.addSelectionListener(mameComboAdapter);
	mc.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
	mc.setLabelText(name);
	mc.setOption(mameOption);
    }

    public void createCheckBox(String name, String mameOption) {
	Button btn = new Button(group, SWT.CHECK);
	btn.setText(name);
	String iniOp = gCon.getMameIniValue(iniFile, mameOption);
	if (iniOp.equals("1")) {
	    btn.setSelection(true);
	} else if (iniOp.equals("")) {
	    btn.setEnabled(false);
	} else {
	    btn.setSelection(false);
	}
	btn.addSelectionListener(checkBoxAdapter);
	btn.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
    }

    public void createMamePathComp(String name, String MameOption, int option) {
	MamePathComp mpc = new MamePathComp(group, SWT.NONE);
	Text txt = mpc.getText();

	mpc.setOption(MameOption);
	String iniOp = gCon.getMameIniValue(iniFile, mpc.getOption());
	if (iniOp.equals("")) {
	    txt.setEnabled(false);
	} else {
	    txt.setText(iniOp.replace("$HOME", System.getProperty("user.home")));
	}

	mpc.setLabelText(name);
	mpc.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false));

	switch (option) {
	case LOAD_DIALOG:
	    mpc.getButton().addSelectionListener(mameLoadPathListener);
	    break;
	case SAVE_DIALOG:
	    mpc.getButton().addSelectionListener(mameSavePathListener);
	    break;
	case DIRECTORY_DIALOG:
	    mpc.getButton().addSelectionListener(mameDirPathListener);
	}
    }

    public void CreateDoubleSpinner(String name, String mameOption, int maxValue) {
	MameSpinner ms = new MameSpinner(group, SWT.NONE,
		MameSpinner.DOUBLE_SPINNER);
	Spinner spin = ms.getSpin();
	ms.setLabelText(name);

	ms.setMameOption(mameOption);
	String iniOp = gCon.getMameIniValue(iniFile, ms.getMameOption());
	if (iniOp.equals("")) {
	    spin.setEnabled(false);
	} else {
	    spin.setSelection((int) ((new Double(iniOp).doubleValue()) * 10));
	    spin.setMaximum(maxValue * 10);
	}

	spin.addSelectionListener(doubleSpinnerAdapter);
	ms.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    }

    public void createIntSpinner(String name, String mameOption, int maxValue) {
	MameSpinner ms = new MameSpinner(group, SWT.NONE,
		MameSpinner.INT_SPINNER);
	Spinner spin = ms.getSpin();
	ms.setLabelText(name);

	ms.setMameOption(mameOption);
	String iniOp = gCon.getMameIniValue(iniFile, ms.getMameOption());
	if (iniOp.equals("")) {
	    spin.setEnabled(false);
	} else {
	    spin.setSelection(new Integer(iniOp).intValue());
	    spin.setMaximum(maxValue);
	}

	spin.addSelectionListener(intSpinnerAdapter);
	ms.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    }

    public void createTextBox(String name, String mameOption) {
	MameTextComp mtc = new MameTextComp(group, SWT.NONE);
	Text txt = mtc.getText();

	mtc.setOption(mameOption);
	String iniOp = gCon.getMameIniValue(iniFile, mtc.getOption());
	if (iniOp.equals("")) {
	    txt.setEnabled(false);
	} else {
	    txt.setText(iniOp.replace("$HOME", System.getProperty("user.home")));
	}

	mtc.setLabelText(name);
	mtc.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
	txt.addModifyListener(mameTxtModifyListener);
    }

    public void createLabel(String name) {
	Label tmp = new Label(group, SWT.NONE);
	tmp.setText(name);
	tmp.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
    }

    private void writeIni() {
	FileIO.writeTofile(iniFile, new File(mExec.getIniPath() + "/mame.ini"),
		false);
    }

    public GuiControls getgCon() {
	return gCon;
    }

    public void setgCon(GuiControls gCon) {
	this.gCon = gCon;
    }

    public MameExecutable getmExec() {
	return mExec;
    }

    public void setmExec(MameExecutable mExec) {
	this.mExec = mExec;
    }

    public ArrayList<String> getIniFile() {
	return iniFile;
    }

    public void setIniFile(ArrayList<String> iniFile) {
	this.iniFile = iniFile;
    }

    public Group getGroup() {
	return group;
    }

    public void setGroup(Group group) {
	this.group = group;
    }
}