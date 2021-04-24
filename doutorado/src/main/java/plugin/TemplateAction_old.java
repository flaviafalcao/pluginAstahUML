package plugin;


import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.project.ProjectAccessor;
import com.change_vision.jude.api.inf.ui.IPluginActionDelegate;
import com.change_vision.jude.api.inf.ui.IWindow;



public class TemplateAction_old implements IPluginActionDelegate {

	public Object run(IWindow window) throws UnExpectedException {
	    try {
	      /*  AstahAPI api = AstahAPI.getAstahAPI();
	        ProjectAccessor projectAccessor = api.getProjectAccessor();
	        projectAccessor.getProject();
	        JOptionPane.showMessageDialog(window.getParent(),"Hello");*/
	    	
	    	 AstahAPI api = AstahAPI.getAstahAPI();
		     ProjectAccessor projectAccessor = api.getProjectAccessor();
		    projectAccessor.getProject();
		    CorTeste dialog = new CorTeste((JFrame) window.getParent(), true);
	    } catch (ProjectNotFoundException e) {
	        String message = "Project is not opened.Please open the project or create new project.";
			JOptionPane.showMessageDialog(window.getParent(), message, "Warning", JOptionPane.WARNING_MESSAGE); 
	    } catch (Exception e) {
	    	JOptionPane.showMessageDialog(window.getParent(), "Unexpected error has occurred.", "Alert", JOptionPane.ERROR_MESSAGE); 
	        throw new UnExpectedException();
	    }
	    return null;
	}


}
