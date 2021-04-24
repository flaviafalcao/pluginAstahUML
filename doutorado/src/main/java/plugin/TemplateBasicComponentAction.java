package plugin;

import javax.swing.JFrame;

import com.change_vision.jude.api.inf.ui.IPluginActionDelegate;
import com.change_vision.jude.api.inf.ui.IWindow;
import com.change_vision.jude.api.inf.ui.IPluginActionDelegate.UnExpectedException;

public class TemplateBasicComponentAction implements IPluginActionDelegate {

	@Override
	public Object run(IWindow arg0) throws UnExpectedException {
		try {
			
			
			//frame que gera o csp
			BasicComponentDialog   dialog = new BasicComponentDialog((JFrame) arg0.getParent(), true);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
