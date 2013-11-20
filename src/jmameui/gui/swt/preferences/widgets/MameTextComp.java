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
import org.eclipse.swt.widgets.Text;

public class MameTextComp extends Composite {
    private Label label;
    private Text text;
    private String option;

    public MameTextComp(Composite arg0, int arg1) {
	super(arg0, arg1);
	this.setLayout(new GridLayout(2, false));
	initUI();
    }

    private void initUI() {
	label = new Label(this, SWT.NONE);
	label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
	
	text = new Text(this, SWT.BORDER);
	text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    }

    public void setLabelText(String text) {
	label.setText(text);
    }

    public String getLabelText() {
	return label.getText();
    }

    public Label getLabel() {
	return label;
    }

    public void setLabel(Label label) {
	this.label = label;
    }

    public Text getText() {
	return text;
    }

    public void setText(Text text) {
	this.text = text;
    }

    public String getOption() {
	return option;
    }

    public void setOption(String option) {
	this.option = option;
    }
}
