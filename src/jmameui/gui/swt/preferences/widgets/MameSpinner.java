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
	    spin.setIncrement(1);
	} else if (option == INT_SPINNER) {
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
