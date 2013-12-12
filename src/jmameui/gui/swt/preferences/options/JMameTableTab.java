package jmameui.gui.swt.preferences.options;

import java.util.HashMap;

import jmameui.gui.swt.SWTJMameUI;
import jmameui.mame.GuiControls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.TabFolder;

public class JMameTableTab {

	private Group mainGroup;
	private Group subGroup;
	private GuiControls gCon;
	private List visList;
	private List aviList;
	private Label visLab;
	private Label aviLab;
	private Button leftBtn;
	private Button rightBtn;
	private SWTJMameUI jMame;

	private SelectionAdapter leftBtnAdapter = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			if (aviList.getSelectionIndex() >= 0) {
				String item = aviList.getItem(aviList.getSelectionIndex());
				aviList.remove(item);
				visList.add(item);
				gCon.changeSettingsFile(item.toLowerCase().replace(" ", "_")
						+ "_column_visible", "true");
				gCon.refreshMainSettings();
				jMame.packAllTables();
			}
		};
	};

	private SelectionAdapter rightBtnAdapter = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			if (visList.getSelectionIndex() >= 0) {
				String item = visList.getItem(visList.getSelectionIndex());
				visList.remove(item);
				aviList.add(item);
				gCon.changeSettingsFile(item.toLowerCase().replace(" ", "_")
						+ "_column_visible", "false");
				gCon.refreshMainSettings();
				jMame.packAllTables();
			}
		};
	};

	public JMameTableTab(TabFolder owner, SWTJMameUI j) {
		mainGroup = new Group(owner, SWT.BORDER);
		gCon = j.getgCon();
		jMame = j;

		initUI(j);

	}

	private void initUI(SWTJMameUI j) {
		mainGroup.setLayout(new GridLayout(3, false));

		visLab = new Label(mainGroup, SWT.NONE);
		visLab.setText("Visible");
		visLab.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
		
		Label fillLab = new Label(mainGroup, SWT.NONE);
		fillLab.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));

		aviLab = new Label(mainGroup, SWT.NONE);
		aviLab.setText("Available");
		aviLab.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));

		visList = new List(mainGroup, SWT.MULTI);
		visList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		subGroup = new Group(mainGroup, SWT.NATIVE);
		subGroup.setLayout(new GridLayout(1, true));
		subGroup.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, false, true,
				1, 1));

		fillLab = new Label(subGroup, SWT.NONE);
		fillLab.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, false, true));

		leftBtn = new Button(subGroup, SWT.PUSH);
		leftBtn.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
		leftBtn.addSelectionListener(leftBtnAdapter);
		leftBtn.setImage(SWTJMameUI.loadImage("arrow-left.png"));

		rightBtn = new Button(subGroup, SWT.PUSH);
		rightBtn.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
				false));
		rightBtn.addSelectionListener(rightBtnAdapter);
		rightBtn.setImage(SWTJMameUI.loadImage("arrow-right.png"));

		fillLab = new Label(subGroup, SWT.NONE);
		fillLab.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, false, true));

		aviList = new List(mainGroup, SWT.MULTI);
		aviList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		popLists();
	}

	public Group getGroup() {
		return mainGroup;
	}

	private void popLists() {
		String[] colName = gCon.getTableColumnsNames();
		HashMap<String, String> ops = gCon.getTableOptions();
		for (String i : ops.keySet()) {
			if (Boolean.parseBoolean(ops.get(i))) {
				visList.add(getArrayItem(colName, i));
			} else {
				aviList.add(getArrayItem(colName, i));
			}
		}
	}

	private String getArrayItem(String[] arr, String line) {
		for (String i : arr) {
			if ((i.toLowerCase().replace(" ", "_") + "_column_visible")
					.equals(line)) {
				return i;
			}
		}
		return "";
	}
}
