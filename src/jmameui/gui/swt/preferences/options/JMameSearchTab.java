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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.TabFolder;

public class JMameSearchTab {

	private Group group;
	private GuiControls gCon;
	private SelectionAdapter btnAdapter = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			Button btn = (Button) e.widget;
			gCon.changeSettingsFile(
					btn.getText().toLowerCase().replaceAll(" ", "_")
							+ "_search", "" + btn.getSelection());
		}
	};

	public JMameSearchTab(TabFolder owner, SWTJMameUI jmui) {
		group = new Group(owner, SWT.BORDER);
		group.setLayout(new GridLayout(1, false));
		gCon = jmui.getgCon();
		initUI();
	}

	private void initUI() {
		HashMap<String, String> ops = gCon.getSearchOptions();
		for (String i : gCon.getTableColumnsNames()) {
			Button btn = new Button(group, SWT.CHECK);
			btn.setText(i);
			btn.setSelection(Boolean.parseBoolean(ops.get(i.toLowerCase()
					.replaceAll(" ", "_") + "_search")));
			btn.setToolTipText("Include column " + i + " in search results");
			btn.addSelectionListener(btnAdapter);
			btn.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		}
	}

	public Control getGroup() {
		return group;
	}
}
