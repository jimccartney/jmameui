package jmameui.gui.swt.preferences.options;

import jmameui.mame.GuiControls;
import jmameui.mame.MameExecutable;

import org.eclipse.swt.widgets.Group;

public class OpenGLOption extends IniOption {

    public OpenGLOption() {
    }

    public OpenGLOption(Group owner, GuiControls g, MameExecutable me) {
	super(owner, g, me);
    }

    @Override
    public void initUI() {
	createCheckBox("Filter", "filter");
	createCheckBox("Gl Forcepow2texture", "gl_forcepow2texture");
	createCheckBox("Gl No texture Rect", "gl_notexturerect");
	createCheckBox("Gl Vbo", "gl_vbo");
	createCheckBox("Gl Pbo", "gl_pbo");
	createCheckBox("Gl Glsl", "gl_glsl");
	createCheckBox("Gl Glsl Filter", "gl_glsl_filter");
	createCheckBox("Gl Glsl Vid Attr", "gl_glsl_vid_attr");

	createIntSpinner("Prescale", "prescale", 0, 10);

	for (int i = 0; i < 10; i++) {
	    createTextBox("Glsl Shader Mame " + i, "glsl_shader_mame" + i);
	    createTextBox("Glsl Shader Screen " + i, "glsl_shader_screen" + i);
	}
    }
}
