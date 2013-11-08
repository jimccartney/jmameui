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
import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class MameDialog {

	enum MameDialogOption {
		WARNING, ERRORVIEWLOG, INFORMATION, TEXTAREA
	}

	private Dialog dialog;
	private Shell shell;
	private ArrayList<String> data;
	public MameDialog(Shell owner ,Collection<? extends String> text, MameDialogOption option) {
		shell = owner;
		data = new ArrayList<String>(text);
		switch (option) {
		case WARNING:
			break;
		case ERRORVIEWLOG:
			break;
		case INFORMATION:
			break;
		case TEXTAREA:
			initTEXTAREA();
			break;
		}
	}
	
	
	private void initInformation(){
		
	}

	private void initTEXTAREA() {
		final Shell dialogShell = new Shell(shell,SWT.RESIZE);
		dialogShell.setText("JMameUI - View unavailable romsets");
		dialogShell.setLayout(new GridLayout(1, false));
		Text text = new Text(dialogShell, SWT.BORDER | SWT.WRAP |SWT.V_SCROLL | SWT.H_SCROLL);
		text.setEditable(false);
		StringBuilder sb = new StringBuilder();
		for(String i: data){
			sb.append(i+"\n");
		}
		text.append(sb.toString());
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.heightHint = 5 * text.getLineHeight();
		text.setLayoutData(gridData);
		Button closeBtn = new Button(dialogShell, SWT.PUSH);
		closeBtn.setText("Close");
		closeBtn.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		closeBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent arg0) {
				dialogShell.dispose();
			}
		});
		dialogShell.pack();
		dialogShell.open();
	}

}
