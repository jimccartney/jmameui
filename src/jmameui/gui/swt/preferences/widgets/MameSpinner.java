package jmameui.gui.swt.preferences.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;

public class MameSpinner extends Composite {

    public static int DOUBLE_SPINNER = 0;
    public static int INT_SPINNER = 1;
    private Spinner spin;
    private Label label;
    private String mameOption = "";

    public MameSpinner(Composite arg0, int arg1, int option) {
	super(arg0, arg1);
	this.setLayout(new GridLayout(2, false));

	initUI(option);
    }

    private void initUI(int option) {
	label = new Label(this, SWT.NONE);
	label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));

	spin = new Spinner(this, SWT.BORDER);
	spin.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
	
	if (option == DOUBLE_SPINNER) {
	    spin.setDigits(1);
	    spin.setMinimum(0);
	    spin.setIncrement(1);
	} else if (option == INT_SPINNER) {
	    spin.setMinimum(0);
	    spin.setIncrement(1);
	}
    }

    public Spinner getSpin() {
        return spin;
    }

    public void setSpin(Spinner spin) {
        this.spin = spin;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public String getMameOption() {
        return mameOption;
    }

    public void setMameOption(String mameOption) {
        this.mameOption = mameOption;
    }

    public void setLabelText(String text) {
	label.setText(text);
    }

    public String getLabelText() {
	return label.getText();
    }
}
