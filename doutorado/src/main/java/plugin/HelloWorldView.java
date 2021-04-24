package plugin;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.editor.StateMachineDiagramEditor;
import com.change_vision.jude.api.inf.editor.TransactionManager;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.model.IModel;
import com.change_vision.jude.api.inf.model.IStateMachineDiagram;
import com.change_vision.jude.api.inf.presentation.ILinkPresentation;
import com.change_vision.jude.api.inf.presentation.INodePresentation;
import com.change_vision.jude.api.inf.presentation.PresentationPropertyConstants;
import com.change_vision.jude.api.inf.project.ProjectAccessor;
import com.change_vision.jude.api.inf.project.ProjectAccessorFactory;
import com.change_vision.jude.api.inf.project.ProjectEvent;
import com.change_vision.jude.api.inf.project.ProjectEventListener;
import com.change_vision.jude.api.inf.ui.IPluginExtraTabView;
import com.change_vision.jude.api.inf.ui.ISelectionListener;

public class HelloWorldView extends JPanel implements IPluginExtraTabView, ProjectEventListener {

	ProjectAccessor projectAccessor;
	private int index = 0;

	public HelloWorldView() {
		try {
			projectAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
			initComponents(projectAccessor);

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initComponents(ProjectAccessor projectAccessor) {

		setLayout(new GridBagLayout());

		JButton button = new JButton(">>>>>");
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 6;
		c.gridwidth = 2;
		add(button, c);
		addProjectEventListener();

		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				CounterExample ce = new CounterExample();
				try {
					System.out.println("teste contra");

					// colorir uma transition
					// 1. recuperar o diagrama s
					// pintar
					color(index);
					uncolor(index);
					index++;

					// ce.counterExampleInit();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});

	}

	public void color(int index) {

		try {

			Declarations declaration = Declarations.getInstance();
			ArrayList<ILinkPresentation> array_transitions = declaration.getArray_counter();

			if (index < array_transitions.size()) {

				projectAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
				// create sequence diagram
				IModel project = projectAccessor.getProject();
				if (!TransactionManager.isInTransaction()) {

					TransactionManager.beginTransaction();

				}
				array_transitions.get(index).setProperty(PresentationPropertyConstants.Key.LINE_COLOR, "#FF0000");
				String label = array_transitions.get(index).getLabel();
				
				String labelSplit[];
				String newlabel = "";
				if(label.contains(">")) {
					labelSplit = label.split(">");
					newlabel = labelSplit[0] + "," + index + "> \n    " +  labelSplit[1];
				}
				else {
					
					newlabel = "<" + index  + ">  \n  " + label;
				}
				
				
				array_transitions.get(index).setLabel(newlabel);
				
				System.out.println("LABEL ---- > " + array_transitions.get(index).getLabel());
				
				
				/*
				//StateMachineDiagramEditor de = projectAccessor.getDiagramEditorFactory().getStateMachineDiagramEditor();
				//INodePresentation texto = de.createText("[" + index + "]",array_transitions.get(index).getAllPoints()[1]);
				//INodePresentation no = de.createNote("[" + index + "]", array_transitions.get(index).getAllPoints()[0]);
			    //System.out.print("--->" + no.getHeight());
			    //double height = 20.0;
			    //no.setHeight(height);
				// no.setWidth(20.0);
				// no.setProperty(PresentationPropertyConstants.Key.AUTO_RESIZE, "true");
			   //System.out.println("LABEL ---- > " + array_transitions.get(index).getLabel());
			    * 
				*/
				
				TransactionManager.endTransaction();
				projectAccessor.save();
			}
			if (index == array_transitions.size()) {

				projectAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
				// create sequence diagram
				IModel project = projectAccessor.getProject();

				TransactionManager.beginTransaction();

				array_transitions.get(index - 1).setProperty(PresentationPropertyConstants.Key.LINE_COLOR, "#FF1493");
				TransactionManager.endTransaction();
				projectAccessor.save();

			}
		} catch (Exception e) {
			System.out.print(e.getMessage() + "---");
			e.printStackTrace();
		}

	}

// pintar de preto a transicao anterior 

	public void uncolor(int index) {

		int vindex = index - 1;
		try {

			Declarations declaration = Declarations.getInstance();
			ArrayList<ILinkPresentation> array_transitions = declaration.getArray_counter();
			projectAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
			// create sequence diagram
			IModel project = projectAccessor.getProject();

			if (!TransactionManager.isInTransaction()) {

				TransactionManager.beginTransaction();

			}
			
	
			if (vindex >= 0 && (index != array_transitions.size()) && (index < array_transitions.size())) {

				array_transitions.get(vindex).setProperty(PresentationPropertyConstants.Key.LINE_COLOR, "#000000");
			

			}

			TransactionManager.endTransaction();
			projectAccessor.save();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	
		
		
	



	private void addProjectEventListener() {
		try {
			ProjectAccessor projectAccessor = ProjectAccessorFactory.getProjectAccessor();
			projectAccessor.addProjectEventListener(this);
		} catch (ClassNotFoundException e) {
			e.getMessage();
		}
	}

	private Container createLabelPane() {
		JLabel label = new JLabel("COUNTEREXAMPLE");
		JScrollPane pane = new JScrollPane(label);
		return pane;
	}

	@Override
	public void projectChanged(ProjectEvent e) {
	}

	@Override
	public void projectClosed(ProjectEvent e) {
	}

	@Override
	public void projectOpened(ProjectEvent e) {
	}

	@Override
	public void addSelectionListener(ISelectionListener listener) {
	}

	@Override
	public Component getComponent() {
		return this;
	}

	@Override
	public String getDescription() {
		return "COUNTEREXAMPLE";
	}

	@Override
	public String getTitle() {
		return "COUNTEREXAMPLE  View";
	}

	public void activated() {

	}

	public void deactivated() {

	}
}
