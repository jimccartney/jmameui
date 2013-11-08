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

import jmameui.mame.FileIO;
import jmameui.mame.GuiControls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class MameAddRom {

	private Label filesLabel;
	private Label romNameLabel;
	private CLabel warningLabel;
	private Button browseBtn;
	private Button closeBtn;
	private Button okBtn;
	private Button clearBtn;
	private Button autoDectBtn;
	private List filesList;
	private Text romNameText;
	private Shell shell;
	private GuiControls gCon;
	private ArrayList<File> romFiles = new ArrayList<File>();

	
	private SelectionAdapter browseAdapter = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent arg0) {
			FileDialog dialog = new FileDialog(shell,SWT.OPEN);
			dialog.setText("Choose romset zip");
			dialog.setFilterPath(System.getProperty("user.home"));
			dialog.setFilterNames(new String[]{"Zip files","All files (*)"});
			dialog.setFilterExtensions(new String[]{"*.zip","*"});
			String filePath = dialog.open();
			if(filePath != null){
				File file = new File(filePath);
				if (FileIO.getContentType(file).equals("application/zip")){
					filesList.add(filePath);
					romNameText.setText(file.getName().split(".zip")[0]);
					romFiles.add(file);
				}			
			}
		};
	};

	private SelectionAdapter clearAdapter = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent arg0) {
			filesList.removeAll();
			romNameText.setText("");
			romFiles.clear();
		};
	};
	
	private ModifyListener romTextListener = new ModifyListener() {
		public void modifyText(ModifyEvent arg0) {
			if(!romNameText.getText().equals("")){
				if(gCon.getRom(romNameText.getText())==null){
					okBtn.setEnabled(true);
					warningLabel.setVisible(false);
				}else{
					okBtn.setEnabled(false);
					warningLabel.setVisible(true);
				}
			}else{
				okBtn.setEnabled(false);
			}
		}
	};
	
	private SelectionAdapter okBtnAdapter = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent arg0) {
			gCon.addRom(romFiles, romNameText.getText());
		}
	};
	
	private SelectionAdapter closeBtnAdapter = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent arg0) {
			shell.dispose();
		}
	};

	public MameAddRom(Shell owner,GuiControls gCon) {
		shell = new Shell(owner, SWT.RESIZE);
		shell.setLayout(new GridLayout(3, false));
		shell.setImage(owner.getImage());
		this.gCon = gCon;
		initUI();

		shell.pack();
		shell.open();
	}

	private void initUI() {
		filesLabel = new Label(shell, SWT.NONE);
		filesLabel.setText("Select romset zip");
		filesLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 3, 1));

		filesList = new List(shell, SWT.MULTI | SWT.BORDER | SWT.WRAP
				| SWT.V_SCROLL);
		filesList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		browseBtn = new Button(shell, SWT.PUSH);
		browseBtn.setText("Browse");
		browseBtn.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		browseBtn.addSelectionListener(browseAdapter);
		
		clearBtn = new Button(shell, SWT.PUSH);
		clearBtn.setText("Clear");
		clearBtn.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		clearBtn.addSelectionListener(clearAdapter);
		
		romNameLabel = new Label(shell, SWT.NONE);
		romNameLabel.setText("Romset name");
		romNameLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 3, 1));
		
		romNameText = new Text(shell,SWT.BORDER | SWT.WRAP);
		romNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		romNameText.addModifyListener(romTextListener);
		
		warningLabel = new CLabel(shell, SWT.NONE);
		warningLabel.setText("Romset already exists");
		warningLabel.setVisible(false);
		warningLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		
		closeBtn = new Button(shell, SWT.PUSH);
		closeBtn.setText("Close");
		closeBtn.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		closeBtn.addSelectionListener(closeBtnAdapter);
		
		okBtn = new Button(shell, SWT.PUSH);
		okBtn.setText("Ok");
		okBtn.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		okBtn.setEnabled(false);
		okBtn.addSelectionListener(okBtnAdapter);
	}
}
