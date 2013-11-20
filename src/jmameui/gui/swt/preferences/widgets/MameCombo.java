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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class MameCombo extends Composite {

    private Combo combo;
    private Label label;
    private String option = "";
    
    public MameCombo(Composite arg0, int arg1) {
	super(arg0, arg1);
	this.setLayout(new GridLayout(2,false));
	
	initUI(arg1);
    }
    
    private void initUI(int arg1) {
	label = new Label(this, SWT.NONE);
	label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
	combo = new Combo(this, arg1);
	combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));	
    }


    public void setLabelText(String text){
	label.setText(text);
    }
    
    public String getLabelText(){
	return label.getText();
    }

    public Combo getCombo() {
        return combo;
    }

    public void setCombo(Combo combo) {
        this.combo = combo;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }
}
