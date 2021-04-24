package plugin;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.editor.BasicModelEditor;
import com.change_vision.jude.api.inf.editor.ClassDiagramEditor;
import com.change_vision.jude.api.inf.editor.CompositeStructureDiagramEditor;
import com.change_vision.jude.api.inf.editor.ModelEditorFactory;
import com.change_vision.jude.api.inf.editor.StateMachineDiagramEditor;
import com.change_vision.jude.api.inf.editor.TransactionManager;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IClassDiagram;
import com.change_vision.jude.api.inf.model.ICompositeStructureDiagram;
import com.change_vision.jude.api.inf.model.IModel;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.model.IPackage;
import com.change_vision.jude.api.inf.model.IStateMachineDiagram;
import com.change_vision.jude.api.inf.presentation.INodePresentation;
import com.change_vision.jude.api.inf.project.ModelFinder;
import com.change_vision.jude.api.inf.project.ProjectAccessor;

public class BasicComponentDialog extends JDialog{
	
	private JTextField tf;
	ProjectAccessor projectAccessor;
	
	public BasicComponentDialog (JFrame frame, boolean modal) throws FileNotFoundException, IOException, 
	ClassNotFoundException {
		
		super(frame, modal);
		initComponents();
		this.setTitle("Basic Component");
		this.setLocation(new Point(276, 182));
		this.setSize(new Dimension(450, 150));
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
}

	private void initComponents()  {
		setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        
        // colocar nome do componente
        add(new JLabel("Basic Component Name:"), gbc);
		gbc.gridx++;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 100;

		tf = new JTextField();
		tf.setSize(300, tf.getHeight());
		add(tf, gbc);
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridx++;
		gbc.weightx = 0;
		
        
        
        JButton createButton;
        createButton = new JButton("Create");
        
        
        
        
        
        createButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//boolean check;
				try {
					
					
					projectAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
		    		String name =  tf.getText();
		    		
		    		//tenho q recuperar do view
		    		Declarations declaration = Declarations.getInstance();
		    		
		    	/*	IPackage pkg_comp;
		    		INamedElement[] foundElements = findComponents();
		    		
		    		
		    		for (INamedElement element : foundElements) {
		    			
		    			pkg_comp = castIPackage(element);
		    			declaration.getNameComponents().add(pkg_comp.getName());
		    		}*/
		    		
		    		
		    		
		    		//declaration.getNameComponents().add(name);
		    		
		    		
		    		// quando criar um basic componente colocar na lista de componentes 
		    		
		    	   System.out.println("nr componentes" +declaration.getNameComponents().size());
		    		 
		    		// create sequence diagram
		        	IModel project = projectAccessor.getProject();
		        	
		        	TransactionManager.beginTransaction();
		            StateMachineDiagramEditor de = projectAccessor.getDiagramEditorFactory().getStateMachineDiagramEditor();
		            
		            //maquina de estados
		            IStateMachineDiagram newDgm = de.createStatemachineDiagram(project, "STM_" + name);
		            System.out.println("name" + newDgm.getName());
		            newDgm.setName("STM_" + name);
		            newDgm.setAlias1("STM_" + name);
		            newDgm.setDefinition("STM_" + name);
		            newDgm.setAlias2("STM_" + name);
		            
		            de.setDiagram(newDgm);
		            
		            //diagrama de classes
		            ClassDiagramEditor dec = projectAccessor.getDiagramEditorFactory().getClassDiagramEditor();
		                        
		            IClassDiagram newDclass = dec.createClassDiagram(project, name);
		            dec.setDiagram(newDclass);
		            
		            
		            BasicModelEditor editorBasico = ModelEditorFactory.getBasicModelEditor();
		    		
		            //pacote
		    		IPackage  pkg = editorBasico.createPackage((IPackage)newDclass.getOwner(), name);
		    		pkg.addStereotype("BasicComponent");
		    		
		    		//classe
		    		IClass basic = editorBasico.createClass(pkg, name);
		    		basic.addStereotype("BasicComponentClass");
			    		
		           
		            INodePresentation iNPackage=  dec.createNodePresentation(pkg, new Point2D.Double(10, 100));
		    		iNPackage.setWidth(500);
		    		iNPackage.setHeight(500);		    	
		    		
			            
		    		INodePresentation iNClass =dec.createNodePresentation(basic, new Point2D.Double(40, 150));
		    		
		            //nota 	str
		    		
		    		
		    	//	INodePresentation iNNote1  = dec.createNodePresentation(note1, new Point2D.Double(260, 140));
		    		
			    		 
		    		//nota 	stm
		    		INodePresentation iNNote2  = dec.createNote("STM_"+ name, new Point2D.Double(260, 150));
		    		
		    		INodePresentation iNNote1  = dec.createNote("STR_"+ name, new Point2D.Double(260, 280));
		    		
		    		System.out.println(iNNote2.getProperties());
		    		System.out.println("model" + iNNote2.getModel());
			    	
		            
		    	 	dec.createNoteAnchor(iNClass, iNNote1);
		    	 	dec.createNoteAnchor(iNClass, iNNote2);
		    		
		    	 	
		    		//diagrama de estrutura compostas 
		            CompositeStructureDiagramEditor decom = projectAccessor.getDiagramEditorFactory().getCompositeStructureDiagramEditor();
		            ICompositeStructureDiagram newCS = decom.createCompositeStructureDiagram(project, "STR_" + name);
		            decom.setDiagram(newCS);
		            
		            decom.createStructuredClassPresentation(basic, new Point2D.Double(10, 10));
		            
		            TransactionManager.endTransaction();
		            projectAccessor.save();
		            
		            
		        	
		            
		        	fechar();
		        	
		            
					
					
				}
				
				catch(Exception ex) {
					ex.printStackTrace();
				}
				}
        });
        
        
        add(createButton, gbc);
        gbc.gridx = 2;
		gbc.gridy++;
	
	}
	
	
	private void fechar() {
		
		this.setVisible(false);
	}
	private INamedElement[] findComponents() throws ProjectNotFoundException {
		INamedElement[] foundElements = projectAccessor
				.findElements(new ModelFinder() {
					public boolean isTarget(INamedElement namedElement) {
						return namedElement instanceof IPackage;
					}
				});
		return foundElements;
	}
	
	
	private IPackage castIPackage(INamedElement element) {
		IPackage ipackage = null;
		if (element instanceof IPackage) {
			ipackage = (IPackage) element;
		}
		return ipackage;
	}
	

}
