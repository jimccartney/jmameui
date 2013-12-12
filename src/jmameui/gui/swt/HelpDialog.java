package jmameui.gui.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationAdapter;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;

public class HelpDialog {

	private Shell shell;
	private ToolBar tb;
	private ToolItem backBtn;
	private ToolItem fwdBtn;
	private Tree tree;
	private Browser browser;
	private Button closeBtn;
	
	private SelectionAdapter backBtAdapter= new SelectionAdapter() {
		public void widgetDefaultSelected(SelectionEvent arg0) {
			browser.back();
		};
	};
	
	private SelectionAdapter fwdBtAdapter= new SelectionAdapter() {
		public void widgetDefaultSelected(SelectionEvent arg0) {
			browser.forward();
		};
	};
	
	private LocationAdapter browserLocationAdapter = new LocationAdapter() {
		public void changed(LocationEvent e) {
			Browser b =(Browser)e.widget;
			backBtn.setEnabled(b.isBackEnabled());
			fwdBtn.setEnabled(b.isForwardEnabled());
		};
	};

	public HelpDialog(Shell owner) {
		shell = new Shell(owner, SWT.RESIZE);
		shell.setText(Display.getAppName() + "-Help");
		shell.setLayout(new GridLayout(2, false));

		initUI();

		shell.pack();
		shell.open();
	}

	private void initUI() {
		tb = new ToolBar(shell, SWT.None);
		tb.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1));

		backBtn = new ToolItem(tb, SWT.PUSH);
		backBtn.setText("Back");
		backBtn.setImage(SWTJMameUI.loadImage("arrow-left.png"));
		backBtn.addSelectionListener(backBtAdapter);
		
		fwdBtn = new ToolItem(tb, SWT.PUSH);
		fwdBtn.setText("Forward");
		fwdBtn.setImage(SWTJMameUI.loadImage("arrow-right.png"));
		fwdBtn.addSelectionListener(fwdBtAdapter);
		
		tree = new Tree(shell, SWT.None);
		tree.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1));

		browser = new Browser(shell, SWT.NONE);
		browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		browser.addLocationListener(browserLocationAdapter);
		
		closeBtn = new Button(shell, SWT.PUSH);
		closeBtn.setText("Close");
		closeBtn.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 2, 1));

	}

}
