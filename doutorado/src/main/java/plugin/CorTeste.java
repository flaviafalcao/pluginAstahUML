package plugin;

import java.awt.Color;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import com.change_vision.jude.api.inf.presentation.*;
import com.change_vision.jude.api.inf.project.ProjectAccessor;
import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.editor.TransactionManager;
import com.change_vision.jude.api.inf.model.IConnector;
import com.change_vision.jude.api.inf.model.IModel;
import com.change_vision.jude.api.inf.model.INamedElement;



public class CorTeste  extends JDialog {
	
	private JTextField tf;
	private JButton findButton;
	private JButton applyButton;
	private JFileChooser fc;
	private JLabel msg;
	private Properties prop;
	
	
	
	public CorTeste(JFrame frame, boolean modal) throws FileNotFoundException, IOException, ClassNotFoundException {
		super(frame, modal);
		initComponents();
		this.setTitle("COR teste");
		this.setLocation(new Point(276, 182));
		this.setSize(new Dimension(450, 150));
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
		
		
	}
	
	
	private void initComponents() {
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;

		add(new JLabel("FDR3 folder:"), gbc);
		gbc.gridx++;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 100;

		tf = new JTextField();

		tf.setText("testecor");
		tf.setSize(300, tf.getHeight());
		add(tf, gbc);
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridx++;
		gbc.weightx = 0;
		findButton = new JButton("Find");
				add(findButton, gbc);
		gbc.gridx = 2;
		gbc.gridy++;
		applyButton = new JButton("Apply");
		applyButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				
				CompositionView v = new CompositionView();
				try {
					INamedElement[] temp = v.findConnector();
				//	ProjectAccessor projectAccessor;
					  TransactionManager.beginTransaction();
					IConnector c = (IConnector)temp[0];
					IPresentation ip = c.getPresentations()[0];
				//	ip.setProperty(PresentationPropertyConstants.Key.LINE_COLOR, "#FF0000");
					
					System.out.println(ip.getProperty(PresentationPropertyConstants.Key.LINE_COLOR));
					
					ip.setProperty(PresentationPropertyConstants.Key.LINE_COLOR, "#FF0000");
					TransactionManager.endTransaction();
		         //   projectAccessor.save();
					
					//projectAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
				System.out.println(ip.getProperty(PresentationPropertyConstants.Key.LINE_COLOR));
				
					
					
					
					
					/* for (INamedElement element3 : temp) {
							IConnector tempc = (IConnector)element3;
							v.firstConection(tempc);
					 }*/
					
					
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
	    }
		});		add(applyButton, gbc);
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.weightx = 3;
		msg = new JLabel();
		msg.setForeground(Color.RED);
		add(msg, gbc);
	}



}
