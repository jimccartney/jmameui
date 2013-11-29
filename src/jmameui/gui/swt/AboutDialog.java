package jmameui.gui.swt;

import java.util.ArrayList;

import jmameui.mame.FileIO;
import jmameui.mame.GuiControls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class AboutDialog {
    private Label proNameLab;
    private Label proDesLab;
    private Label proVerLab;
    private Label copyWriteLab;
    private Button closeBtn;
    private Button licBtn;
    private Shell shell;

    public AboutDialog(Shell owner) {
	shell = new Shell(shell, SWT.RESIZE);
	shell.setLayout(new GridLayout(2, false));
	initUI();
	shell.pack();
	shell.open();
    }

    private void initUI() {
	proNameLab = new Label(shell, SWT.None);
	Font initialFont = proNameLab.getFont();
	FontData[] fontData = initialFont.getFontData();
	for (int i = 0; i < fontData.length; i++) {
	    fontData[i].setHeight(24);
	}
	proNameLab.setFont(new Font(Display.getDefault(), fontData));
	proNameLab.setText("JMameUI");
	proNameLab.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
		false, 2, 1));

	proDesLab = new Label(shell, SWT.None);
	proDesLab.setText("Java Mame User Interface");
	proDesLab.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
		false, 2, 1));

	proVerLab = new Label(shell, SWT.None);
	proVerLab.setText(GuiControls.getJMameUIVersion());
	proVerLab.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
		false, 2, 1));

	copyWriteLab = new Label(shell, SWT.None);
	copyWriteLab.setText("Copywrite © James McCartney 2013");
	copyWriteLab.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
		false, 2, 1));

	licBtn = new Button(shell, SWT.PUSH);
	licBtn.setText("Licence");
	licBtn.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false,
		1, 1));
	licBtn.addSelectionListener(new SelectionAdapter() {
	    public void widgetSelected(SelectionEvent arg0) {
		ArrayList<String> lin = FileIO.readInputStream(getClass()
			.getClassLoader().getResourceAsStream("LICENSE"));

		new MameDialog(shell, lin, MameDialog.TEXTAREA);
	    }
	});

	closeBtn = new Button(shell, SWT.PUSH);
	closeBtn.setText("Close");
	closeBtn.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
		false, 1, 1));
	closeBtn.addSelectionListener(new SelectionAdapter() {
	    public void widgetSelected(SelectionEvent arg0) {
		shell.dispose();
	    }
	});
    }
}
