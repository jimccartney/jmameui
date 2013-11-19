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
