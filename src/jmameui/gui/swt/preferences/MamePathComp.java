package jmameui.gui.swt.preferences;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class MamePathComp extends Composite {

    private Label label;
    private Text text;
    private Button button;
    private String option;

    public MamePathComp(Composite arg0, int arg1) {
	super(arg0, arg1);
	this.setLayout(new GridLayout(3, false));
	initUI();
    }

    private void initUI() {
	label = new Label(this, SWT.NONE);
	label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));

	text = new Text(this, SWT.BORDER);
	text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	
	button = new Button(this,SWT.PUSH);
	button.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
	button.setText("Browse");
    }

    public void setLabelText(String text){
	label.setText(text);
    }
    
    public String getLabelText(){
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

    public Button getButton() {
	return button;
    }

    public void setButton(Button button) {
	this.button = button;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }   
 }
