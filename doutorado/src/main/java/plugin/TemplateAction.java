package plugin;


import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.project.ProjectAccessor;
import com.change_vision.jude.api.inf.ui.IPluginActionDelegate;
import com.change_vision.jude.api.inf.ui.IWindow;

//import ui.FDR3LocationDialog;

public class TemplateAction implements IPluginActionDelegate {

	public Object run(IWindow window) throws UnExpectedException {
		FDR3LocationDialog dialog;
		try {
	        AstahAPI api = AstahAPI.getAstahAPI();
	        ProjectAccessor projectAccessor = api.getProjectAccessor();
	        projectAccessor.getProject();
	       // JOptionPane.showMessageDialog(window.getParent(),"Hello");
			 dialog = new FDR3LocationDialog((JFrame) window.getParent(), true);
		 } catch (Exception e) {
				JOptionPane.showMessageDialog(window.getParent(), "Plugin Property file not found!","File Error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			
		 }
		      return null;
	}


}
