package plugin;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.project.ProjectAccessor;
import com.change_vision.jude.api.inf.project.ProjectAccessorFactory;
import com.change_vision.jude.api.inf.project.ProjectEvent;
import com.change_vision.jude.api.inf.project.ProjectEventListener;
import com.change_vision.jude.api.inf.ui.IPluginExtraTabView;
import com.change_vision.jude.api.inf.ui.ISelectionListener;

public class TabContrato  extends JPanel implements IPluginExtraTabView, ProjectEventListener {
	
	
	ProjectAccessor projectAccessor;
	
	
	public TabContrato() {
		try {
		 projectAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
		 initComponents(projectAccessor);
		
	  } 
		
		catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
 	}
	
		
	}
	
	
	private void initComponents(ProjectAccessor projectAccessor) {
		
		setLayout(new GridBagLayout());		
		JButton button = new JButton("CONTRACTS: ");
		JTextArea area = new JTextArea();
		JTextArea detalhe = new JTextArea();
	    GridBagConstraints c = new GridBagConstraints();
	    c.gridx = 0;
		c.gridy = 0;
		
		add(button, c);
		
		
		 c.gridx = 0;
		 c.gridy = 5;
			
		
		add(area,c);
		
		c.gridx = 0;
		c.gridy = 0;
		
		add(detalhe,c);
		
	    addProjectEventListener();
	    
	    
	    button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				try {
					
					System.out.print("contrato");
					
					Declarations declaration = Declarations.getInstance();
					String contrato = declaration.getStrContrato();
					
					ArrayList<Contrato> arrayC = declaration.getContratos();
					
					String strCTR = "";
					
					for(int i =0; i < arrayC.size(); i ++) {
						
						strCTR =  strCTR +"CONTRACT NAME:" + 	arrayC.get(i).getName() + "\n";
						strCTR =  strCTR +"BEHAVIOUR:" + 	arrayC.get(i).getBehaviour() + "\n";
						strCTR =  strCTR +"PORTS:" + arrayC.get(i).getPortas() + "\n";
						strCTR =  strCTR +"OPERATIONs:" + arrayC.get(i).getOp()+ "\n";
						strCTR =  strCTR +"Relation:" + arrayC.get(i).getRelacao()+ "\n";
					}
					
					area.setText(contrato + "\n" + strCTR);
					
					//detalhe.setText(strCTR);
					
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
    	});

    }
	
	
	
	 private void addProjectEventListener() {
		    try {
		      ProjectAccessor projectAccessor = ProjectAccessorFactory.getProjectAccessor();
		      projectAccessor.addProjectEventListener(this);
		    } catch (ClassNotFoundException e) {
		      e.getMessage();
		    }
		  }


	        
		
		
	
	
	

	@Override
	public void projectChanged(ProjectEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void projectClosed(ProjectEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void projectOpened(ProjectEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void activated() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addSelectionListener(ISelectionListener arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deactivated() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Component getComponent() {
		// TODO Auto-generated method stub
		 return this;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "COMPONENT CONTRACT";
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "COMPONENT CONTRACT";
	}
	
	 private Container createLabelPane() {
	      JLabel label = new JLabel("COUNTEREXAMPLE");
	        JScrollPane pane = new JScrollPane(label);
	        return pane;
	    }


}
