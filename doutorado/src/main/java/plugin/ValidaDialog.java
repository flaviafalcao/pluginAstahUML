package plugin;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.AbstractButton;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.editor.BasicModelEditor;
import com.change_vision.jude.api.inf.editor.ClassDiagramEditor;
import com.change_vision.jude.api.inf.editor.CompositeStructureDiagramEditor;
import com.change_vision.jude.api.inf.editor.ModelEditorFactory;
import com.change_vision.jude.api.inf.editor.TransactionManager;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IAssociation;
import com.change_vision.jude.api.inf.model.IAttribute;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IClassDiagram;
import com.change_vision.jude.api.inf.model.ICompositeStructureDiagram;
import com.change_vision.jude.api.inf.model.IConnector;
import com.change_vision.jude.api.inf.model.IModel;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.model.IPackage;
import com.change_vision.jude.api.inf.presentation.ILinkPresentation;
import com.change_vision.jude.api.inf.presentation.INodePresentation;
import com.change_vision.jude.api.inf.project.ModelFinder;
import com.change_vision.jude.api.inf.project.ProjectAccessor;

import plugin.HiearchicalComponentDialog.MyTableModel;
import plugin.HiearchicalComponentDialog.selected;
import plugin.bahaviour.CSPBehaviour;
import plugin.bahaviour.ResourceAllocation;
import plugin.bahaviour.ResourceOrder;
import plugin.bahaviour.UserResource;

public class ValidaDialog extends JFrame {

	private JTextField tf;
	ProjectAccessor projectAccessor;
	private boolean DEBUG = false;
	ArrayList<String> ArrayName = new ArrayList<String>();	
	// lista de users
	ArrayList<String> users = new ArrayList<String>();
	// lista de recursos
	ArrayList<String> resources = new ArrayList<String>();
	
	ArrayList<ResourceAllocation> resourceAllocation  = new ArrayList<ResourceAllocation>();
	ArrayList<UserResource> UserResource  = new ArrayList<UserResource>();
	
	ArrayList<SetSinc> setsinc;
		


	public ValidaDialog(JFrame frame, boolean modal) throws FileNotFoundException, IOException, ClassNotFoundException {
		// super(frame, modal);
		super(); 
		initComponents();
		this.setTitle("Verify Model");
		this.setLocation(new Point(276, 182));
		this.setSize(new Dimension(850, 600)); // 450 200
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);

	}

	private void initComponents() {
		
        setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		JTextArea textArea = new JTextArea(2,40);
		
		gbc.gridx = 0;
		gbc.gridy = 2;

		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(new TitledBorder(null, "VERIFICATION", TitledBorder.LEADING, TitledBorder.TOP, null, null));
	    add(panel,gbc); 
		
		// colocar nome do componente
		panel.add(new JLabel("Do you want to apply some behavioural pattern?"), gbc);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 100;
		// gbc.gridy++;
		gbc.gridx++;

		// create checkbox
		JRadioButton op = new JRadioButton("YES");
		panel.add(op, gbc);

		gbc.gridx++;
		JRadioButton op2 = new JRadioButton("NO");
		panel.add(op2, gbc);

		// grupo de radio button
		ButtonGroup grp = new ButtonGroup();
	

		grp.add(op);
		grp.add(op2);

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 100;
		gbc.gridy++;
		gbc.gridx = 0;

		// create checkbox
		JCheckBox c1 = new JCheckBox("Resource-Allocation");
		c1.setVisible(false);
		panel.add(c1, gbc);
		

		gbc.gridx = 0;
		gbc.gridy++;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		
		JLabel compL = new JLabel("Components:");
		compL.setVisible(false);
		panel.add(compL, gbc);

		
		// criar uma lista de selecao e quantidade
		JTable componentes = new JTable();
		componentes.setVisible(false);
		//componentes.setVisible(true);	

		// Create the scroll pane and add the table to it.
		JScrollPane scrollPane = new JScrollPane();
		//scrollPane.setViewportView(componentes);
		scrollPane.setVisible(false);
		
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		//panel.add(componentes,gbc);
		 panel.add(scrollPane,gbc);

		 
		 gbc.gridx = 0;
		 gbc.gridy++;
		 gbc.fill = GridBagConstraints.NONE;
 
        JButton stepButton;
		stepButton = new JButton("Next Step");
		stepButton.setVisible(false);

	
		gbc.fill = GridBagConstraints.VERTICAL;
		gbc.gridx++;
		gbc.weightx = 0;

		JButton verifyButton;
		verifyButton = new JButton("Verify");
		
		//
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridheight = 3;
		gbc.gridwidth = 2;

		gbc.weightx = 1;
		gbc.weighty = 1;

		// essa parte nao deveria ta aqui e sim numa classe a parte

		Declarations declaration = Declarations.getInstance();

		IClass pkg_comp;
		INamedElement[] foundElements;
		try {
			projectAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
			foundElements = findComponents();

			ClassView classview = new ClassView();

			Composition comp = new Composition();
			comp.createBasicComponent();

			CompositeView c = new CompositeView();

			// c.getChannelPort(); ok

			c.getContrato();

			c.findPart();
			c.MontaInstancia();

			INamedElement[] foundElements3 = null;

			foundElements3 = c.findConnector();

			for (INamedElement element3 : foundElements3) {
				IConnector temp = (IConnector) element3;
				comp.firstConection(temp);
			}

			for (INamedElement element : foundElements) {

				pkg_comp = castIPackage(element);
				if (pkg_comp.getStereotypes()[0].equalsIgnoreCase("BasicComponentClass")) {

					// intancias de um basic component
					ArrayName.addAll(declaration.getInstancebyType(pkg_comp.getName()));
				}
			}
			
			
			ActionListener actionListener = new ActionListener() {
				public void actionPerformed(ActionEvent actionEvent) {
					AbstractButton abstractButton = (AbstractButton) actionEvent.getSource();
					boolean selected = abstractButton.getModel().isSelected();
					System.out.println(selected);
					c1.setVisible(true);
					compL.setVisible(true);
					componentes.setVisible(true);
				    scrollPane.setVisible(true);
					stepButton.setVisible(true);
							}
			};

			// listerner NO
			ActionListener actionListenerNo = new ActionListener() {
				public void actionPerformed(ActionEvent actionEvent) {
					AbstractButton abstractButton = (AbstractButton) actionEvent.getSource();
					boolean selected = abstractButton.getModel().isSelected();
					System.out.println(selected);
					c1.setVisible(false);
					compL.setVisible(false);
					componentes.setVisible(false);
					scrollPane.setVisible(false);
					stepButton.setVisible(false);
					textArea.setVisible(false);
			}
			};

			op.addActionListener(actionListener);
			op2.addActionListener(actionListenerNo);

			
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		componentes.setPreferredScrollableViewportSize(new Dimension(300, 100));
		componentes.setFillsViewportHeight(false);
		scrollPane.setViewportView(componentes);
		// Add the scroll pane to this panel.
		componentes.setModel(new MyTableModel());
		scrollPane.setSize(300, 50);
	
		gbc.gridx = 0;
		gbc.gridy = 6;
		gbc.fill = GridBagConstraints.NONE;
		panel.add(stepButton, gbc);
		
		gbc.gridx = 0;
		gbc.gridy ++;
		gbc.gridy ++;
		
		gbc.gridy = gbc.gridy + 2;
		gbc.fill = GridBagConstraints.VERTICAL;

		panel.add(verifyButton, gbc);
		

	// listenr button
		stepButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				boolean check;
				try {
				
					Object obj_name = new Object();
					Object obj_user = new Object();
					Object obj_resource = new Object();

					int i = 0;

					while (i < componentes.getRowCount()) {

						obj_name = componentes.getValueAt(i, 0);
						obj_user = componentes.getValueAt(i, 1);

						if (obj_user instanceof Boolean) {
							Boolean isSelectedRow = (Boolean) obj_user;
							if (isSelectedRow) {
								users.add(obj_name.toString());
							}

						}

						obj_resource = componentes.getValueAt(i, 2);

						if (obj_resource instanceof Boolean) {
							Boolean isSelectedRow = (Boolean) obj_resource;
							if (isSelectedRow) {
								resources.add(obj_name.toString());
							}

						}

						i++;

					}
					
				// percorrer o setSicin
					//ArrayList<SetSinc> 
					setsinc = declaration.getSetsinc();
					
					for (int j = 0; j < setsinc.size(); j++) {
						
						  
						 String channelSplit[] = setsinc.get(j).getChannel1().split("\\.");
							// saber qual o tipo componente da porta
						 String nomeComponente = declaration.getTypeOfPort(channelSplit[0].trim());
							// o id eh semOp[1]
						 int id = Integer.parseInt(channelSplit[1].trim());
							// qual o nome da instancia
						 String instance1 = declaration.getInstanceNameByTypeId(nomeComponente, id);
						 
						// channel2
						 String channelSplit2[] = setsinc.get(j).getChannel2().split("\\.");
							// saber qual o tipo componente da porta
						 String nomeComponente2 = declaration.getTypeOfPort(channelSplit2[0].trim());
							// o id eh semOp[1]
						 int id2 = Integer.parseInt(channelSplit2[1].trim());
							// qual o nome da instancia
						 String instance2 = declaration.getInstanceNameByTypeId(nomeComponente2, id2);
						 
						 
						 //instence1 eh user
						 boolean  existsUser = false;
						 
						 /// preencher resourceAllocation
						 if(users.contains(instance1)) {
						 
							 // ve se ja existe esse user no resourceAllocation
							 int k;
							 for( k =0; k< resourceAllocation.size(); k++) {
								 
								 if(resourceAllocation.get(k).getUser().equalsIgnoreCase(instance1)) {
									 existsUser = true;
									 break;									 
								 }
							 } 
							 
							 if(!existsUser) {								 
								 ResourceAllocation	 temp_resourceAllocation = new ResourceAllocation();
								 temp_resourceAllocation.setUser(instance1);
								 ArrayList<String> temp_resource = new  ArrayList<String>();
								 temp_resource.add(instance2);
								 temp_resourceAllocation.setResource(temp_resource);
								 resourceAllocation.add(temp_resourceAllocation);
								 
								 
								 
							 } // ja existe
							 else {
								 resourceAllocation.get(k).updateResource(instance2);
							 }
							 // instance2
						 } else if (users.contains(instance2)) {
							 
							 // ve se ja existe esse user no resourceAllocation
							 int k;
							 for( k =0; k< resourceAllocation.size(); k++) {
								 
								 existsUser = false;
								 if(resourceAllocation.get(k).getUser().equalsIgnoreCase(instance2)) {
									 existsUser = true;
									 break;									 
								 }
							 } 
							 
							 if(!existsUser) {								 
								 ResourceAllocation	 temp_resourceAllocation = new ResourceAllocation();
								 temp_resourceAllocation.setUser(instance2);
								 ArrayList<String> temp_resource = new  ArrayList<String>();
								 temp_resource.add(instance1);
								 temp_resourceAllocation.setResource(temp_resource);
								 resourceAllocation.add(temp_resourceAllocation);
								 
								 
								 
							 } // ja existe
							 else {
								 resourceAllocation.get(k).updateResource(instance1);
							 }
							 
							 
							 
							 
							 
						 }// end if users.contains(instance1)
						 					 
						 
						 //preencherUserResource 1
						 if(resources.contains(instance1)) {
							 
							 
							 
							 // ve se ja existe esse user no resourceAllocation
							 int r;
							 for( r =0; r< UserResource.size(); r++) {
								 
								 existsUser = false;
								 if(UserResource.get(r).getResource().equalsIgnoreCase(instance1)) {
									 existsUser = true;
									 break;									 
								 }
							 } 
							 
							 if(!existsUser) {								 
								 UserResource	 temp_userResource = new UserResource();
								 temp_userResource.setResource(instance1);								 
								 ArrayList<String> temp_user = new  ArrayList<String>();
								 temp_user.add(instance2);
								 temp_userResource.setUser(temp_user);
								 UserResource.add(temp_userResource);
								 
								 
								 
							 } // ja existe
							 else {
								 if(r< UserResource.size())
                                    UserResource.get(r).updateUser(instance2);
							 }
							 
						 }
						 
						 
                         if(resources.contains(instance2)) {
							 
							 
							 
							 // ve se ja existe esse user no resourceAllocation
							 int r;
							 for( r =0; r< UserResource.size(); r++) {
								 
								 existsUser = false;
								 if(UserResource.get(r).getResource().equalsIgnoreCase(instance2)) {
									 existsUser = true;
									 break;									 
								 }
							 } 
							 
							 if(!existsUser) {								 
								 UserResource	 temp_userResource = new UserResource();
								 temp_userResource.setResource(instance2);								 
								 ArrayList<String> temp_user = new  ArrayList<String>();
								 temp_user.add(instance1);
								 temp_userResource.setUser(temp_user);
								 UserResource.add(temp_userResource);
								 
								 
								 
							 } // ja existe
							 else {
								 if(r< UserResource.size())
                                    UserResource.get(r).updateUser(instance1);
							 }
							 
						 }
						
						 
						 
						 
					} 

					
					textArea.setVisible(true);
					String str = "";
					
	
					
					NovaFrame nova  =  new NovaFrame();
					nova.show();
			
					
				
				
				}
				
				

				catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		
		
		///-----

		/// listener Button
		verifyButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				boolean check;
				try {
					declaration.destroy();
					dispose();

					Principal p = new Principal();
					check = p.valida();
					Declarations declaration = Declarations.getInstance();
					ArrayList<String> msgerro = declaration.getMsgErro();

					if (!check) {

						if (msgerro.size() > 0) {

							String msg = "";

							for (int k = 0; k < msgerro.size(); k++) {

								msg = msg + msgerro.get(k) + "\n";
							}

							JOptionPane.showMessageDialog(null, msg);

						} else

						{
							JOptionPane.showMessageDialog(null, "Model with problems!");

						}
					} else {

						JOptionPane.showMessageDialog(null, "Model  OK!");

					}

					declaration.destroy();

				}

				catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		
		
				
		
		
		
		

	}

	private void fechar() {

		this.setVisible(false);
	}

	// data model3
	class MyTableModel extends AbstractTableModel {

		private String[] columnNames = { "COMPONENT", "USER ", "RESOURCE" };

		ArrayList<String> names = ArrayName;

		// montar lista
		private Object[][] data = new Object[names.size()][3];

		MyTableModel() {
			montaData();
		}

		public void montaData() {

			for (int i = 0; i < names.size(); i++) {

				data[i][0] = names.get(i);
				data[0][1] = new Boolean(false); // new Integer(0);
				data[0][2] = new Boolean(true);
				
				data[1][1] = new Boolean(false); // new Integer(0);
				data[1][2] = new Boolean(true);

				data[2][1] = new Boolean(true); // new Integer(0);
				data[2][2] = new Boolean(false);

				data[3][1] = new Boolean(true); // new Integer(0);
				data[3][2] = new Boolean(false);

				
			}
			

			// System.out.println(names.size());
		}

		public int getColumnCount() {
			return columnNames.length;
		}

		public int getRowCount() {
			return data.length;
		}

		public String getColumnName(int col) {
			return columnNames[col];
		}

		public Object getValueAt(int row, int col) {
			return data[row][col];
		}

		/*
		 * JTable uses this method to determine the default renderer/ editor for each
		 * cell. If we didn't implement this method, then the last column would contain
		 * text ("true"/"false"), rather than a check box.
		 */
		public Class getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}

		/*
		 * Don't need to implement this method unless your table's editable.
		 */
		public boolean isCellEditable(int row, int col) {
			// Note that the data/cell address is constant,
			// no matter where the cell appears onscreen.
			if (col == 0) {
				return false;
			} else {
				return true;
			}
		}

		/*
		 * Don't need to implement this method unless your table's data can change.
		 */
		public void setValueAt(Object value, int row, int col) {
			if (DEBUG) {
				System.out.println("Setting value at " + row + "," + col + " to " + value + " (an instance of "
						+ value.getClass() + ")");
			}

			data[row][col] = value;
			fireTableCellUpdated(row, col);

			if (DEBUG) {
				System.out.println("New value of data:");
				printDebugData();
			}
		}

		private void printDebugData() {
			int numRows = getRowCount();
			int numCols = getColumnCount();

			for (int i = 0; i < numRows; i++) {
				System.out.print("    row " + i + ":");
				for (int j = 0; j < numCols; j++) {
					System.out.print("  " + data[i][j]);
				}
				System.out.println();
			}
			System.out.println("--------------------------");
		}
	}
	
	
	
	
	///modelo tabela ordem recursos de um  user
	
	class MyTableModelUser extends AbstractTableModel {

		private String[] columnNames = { "USER", "RESOURCE ", "ORDER", "RESOURCE", "ORDER" };

		Declarations declaration = Declarations.getInstance();
		
		// aqui seria o ResourceAllocation 
		
		ArrayList<String> names_User = users;
		ArrayList<String> names_Resouce = resources;
		
		
		// montar lista
		private Object[][] data = new Object[resourceAllocation.size()][5];
		
		MyTableModelUser() {
			montaData();
		}

		public void montaData() {

			for (int i = 0; i < resourceAllocation.size(); i++) {
 
			//	data[i][0] = names_User.get(i);
				data[i][0] =  resourceAllocation.get(i).getUser();
				// aqui deveria ser os recursos desse user 
				//data[i][1] = names_Resouce.get(0); // new Integer(0);
				data[i][1] =  resourceAllocation.get(i).getResource().get(0);
				data[i][2] = new Integer(1);
				data[i][3] =   resourceAllocation.get(i).getResource().get(1); //names_Resouce.get(1);
				data[i][4] = new Integer(2);
			}

			// System.out.println(names.size());
		}

		public int getColumnCount() {
			return columnNames.length;
		}

		public int getRowCount() {
			return data.length;
		}

		public String getColumnName(int col) {
			return columnNames[col];
		}

		public Object getValueAt(int row, int col) {
			return data[row][col];
		}

		/*
		 * JTable uses this method to determine the default renderer/ editor for each
		 * cell. If we didn't implement this method, then the last column would contain
		 * text ("true"/"false"), rather than a check box.
		 */
		public Class getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}

		/*
		 * Don't need to implement this method unless your table's editable.
		 */
		public boolean isCellEditable(int row, int col) {
			// Note that the data/cell address is constant,
			// no matter where the cell appears onscreen.
			if (col == 0) {
				return false;
			} else {
				return true;
			}
		}

		/*
		 * Don't need to implement this method unless your table's data can change.
		 */
		public void setValueAt(Object value, int row, int col) {
			if (DEBUG) {
				System.out.println("Setting value at " + row + "," + col + " to " + value + " (an instance of "
						+ value.getClass() + ")");
			}

			data[row][col] = value;
			fireTableCellUpdated(row, col);

			if (DEBUG) {
				System.out.println("New value of data:");
				printDebugData();
			}
		}

		private void printDebugData() {
			int numRows = getRowCount();
			int numCols = getColumnCount();

			for (int i = 0; i < numRows; i++) {
				System.out.print("    row " + i + ":");
				for (int j = 0; j < numCols; j++) {
					System.out.print("  " + data[i][j]);
				}
				System.out.println();
			}
			System.out.println("--------------------------");
		}
	}
	
	
	///modelo tabela ordem recursos de um  user
	
		class MyTableModelRs extends AbstractTableModel {

			private String[] columnNames = { "RESOURCE", "USER ", "ORDER", "USER", "ORDER" };

			Declarations declaration = Declarations.getInstance();
			ArrayList<String> names_User = users;
			ArrayList<String> names_Resouce = resources;
			
			
			// montar lista
			private Object[][] data = new Object[names_Resouce.size()][5];
			// = {
			// {"Fork", new Integer(0), new Boolean(false)},
			// {"Phil", new Integer(0), new Boolean(true)},
			// };

			MyTableModelRs() {
				montaData();
			}

			public void montaData() {

				for (int i = 0; i < names_User.size(); i++) {

					data[i][0] = names_Resouce.get(i);
					data[i][1] = names_User.get(0); // new Integer(0);
					data[i][2] = new Integer(0);
					data[i][3] = names_User.get(1);
					data[i][4] = new Integer(0);
				}

				// System.out.println(names.size());
			}

			public int getColumnCount() {
				return columnNames.length;
			}

			public int getRowCount() {
				return data.length;
			}

			public String getColumnName(int col) {
				return columnNames[col];
			}

			public Object getValueAt(int row, int col) {
				return data[row][col];
			}

			/*
			 * JTable uses this method to determine the default renderer/ editor for each
			 * cell. If we didn't implement this method, then the last column would contain
			 * text ("true"/"false"), rather than a check box.
			 */
			public Class getColumnClass(int c) {
				return getValueAt(0, c).getClass();
			}

			/*
			 * Don't need to implement this method unless your table's editable.
			 */
			public boolean isCellEditable(int row, int col) {
				// Note that the data/cell address is constant,
				// no matter where the cell appears onscreen.
				if (col == 0) {
					return false;
				} else {
					return true;
				}
			}

			/*
			 * Don't need to implement this method unless your table's data can change.
			 */
			public void setValueAt(Object value, int row, int col) {
				if (DEBUG) {
					System.out.println("Setting value at " + row + "," + col + " to " + value + " (an instance of "
							+ value.getClass() + ")");
				}

				data[row][col] = value;
				fireTableCellUpdated(row, col);

				if (DEBUG) {
					System.out.println("New value of data:");
					printDebugData();
				}
			}

			private void printDebugData() {
				int numRows = getRowCount();
				int numCols = getColumnCount();

				for (int i = 0; i < numRows; i++) {
					System.out.print("    row " + i + ":");
					for (int j = 0; j < numCols; j++) {
						System.out.print("  " + data[i][j]);
					}
					System.out.println();
				}
				System.out.println("--------------------------");
			}
		}

	
	
	
	
//////////
	private INamedElement[] findComponents() throws ProjectNotFoundException {
		INamedElement[] foundElements = projectAccessor.findElements(new ModelFinder() {
			public boolean isTarget(INamedElement namedElement) {
				return namedElement instanceof IClass;
			}
		});
		return foundElements;
	}

	private IClass castIPackage(INamedElement element) {
		IClass ipackage = null;
		if (element instanceof IClass) {
			ipackage = (IClass) element;
		}
		return ipackage;
	}

	// class ResourceAllocation

	
	
	
	
	
	
	///novo Frame
	class NovaFrame extends JFrame{
		
		MyTableModelUser userM = new MyTableModelUser();
		MyTableModelRs RM = new MyTableModelRs();;
		
		
		public NovaFrame() throws FileNotFoundException, IOException, ClassNotFoundException {
			// super(frame, modal);
			super();
			initFrame();
			this.setTitle("Verify Model");
			this.setLocation(new Point(276, 182));
			this.setSize(new Dimension(850, 600)); // 450 200
			this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
			this.setResizable(false);
			this.setVisible(true);

		
		}
		
		public void initFrame() {
			
			
			setLayout(new GridBagLayout());
			GridBagConstraints gbc_ = new GridBagConstraints();
			
			JTable userOrdem = new JTable();
			JScrollPane scrollPaneUser = new JScrollPane();
			
			
			JTable userOrdem_ = new JTable();
			JScrollPane scrollPaneUser_ = new JScrollPane();
			
			
			
			gbc_.gridx = 0;
			gbc_.gridy = 2;

			JPanel panel_ = new JPanel(new GridBagLayout());
			panel_.setBorder(new TitledBorder(null, " __", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		    add(panel_,gbc_); 
		    
		    userOrdem.setVisible(false);
			scrollPaneUser.setVisible(false);
			userOrdem.setPreferredScrollableViewportSize(new Dimension(400, 60));
			userOrdem.setFillsViewportHeight(false);
			scrollPaneUser.setViewportView(userOrdem);
			// Add the scroll pane to this panel.
			scrollPaneUser.setSize(300, 50);
		
			userOrdem.setModel(userM);					
			userOrdem.setVisible(true);
			scrollPaneUser.setVisible(true);	
		    
		    
			panel_.add(scrollPaneUser,gbc_);

			
    		gbc_.gridx = 0;
			gbc_.gridy++;			
			gbc_.fill = GridBagConstraints.HORIZONTAL;
			
			
			
			//verify button 2 
			
			
			JButton verifyButton2;
			verifyButton2 = new JButton("Verify");
			
	
			panel_.add(verifyButton2, gbc_);

			verifyButton2.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					
					CSPBehaviour cspRA = new CSPBehaviour() ;
					ArrayList<ResourceOrder>  ordemR = new ArrayList<ResourceOrder>();

					Principal p = new Principal();

					
					try {
						dispose();
						String user1;
						String resource1 ;
						String resource2;						
						
						Declarations declaration = Declarations.getInstance();

						
						///
						int i = 0;
						int user;
						String instanciaNameU;
						
						int id_rescource1;
						String instanciaNameR1;
						
						
						int id_rescource2;
						String instanciaNameR2;
						int o_resource1;
						int o_resource2; 
						
						// na tabela de user definir ordem dos recursos
						while (i < userM.getRowCount()) {
							
							//identifico qual o id da instancia  USER
							user1 = (String)userM.getValueAt(i, 0);
							user = declaration.getId((String)userM.getValueAt(i, 0));							
							//identificar o tipo da Instancia USER 
							instanciaNameU = declaration.getTypeInstance((String)userM.getValueAt(i, 0));
							
							
							// primeiro recurso
							resource1 = (String)userM.getValueAt(i, 1);
							id_rescource1 = declaration.getId((String)userM.getValueAt(i, 1));							
							instanciaNameR1 = declaration.getTypeInstance((String)userM.getValueAt(i, 1));
							//ordem
							o_resource1 = (Integer)userM.getValueAt(i, 2);
							
							
							
							// segundo recurso
							resource2 = (String)userM.getValueAt(i, 3);
							id_rescource2 = declaration.getId((String)userM.getValueAt(i, 3));							
							instanciaNameR2 = declaration.getTypeInstance((String)userM.getValueAt(i, 3));
						    //ordem
							o_resource2 = (Integer)userM.getValueAt(i, 4);
							
							
							//System.out.println( "--");
							//System.out.println("USER" +  instanciaNameU + user  );
							//System.out.println("R1" +  instanciaNameR1 + id_rescource1);
							//System.out.println("R2"  + instanciaNameR2 + id_rescource2);
							
							
							ordemR.add(new ResourceOrder(user1,resource1,o_resource1,resource2,o_resource2));	
							//
							
							
						  
							i++;
						}	
						cspRA.BehaviourRestrictionResourceAllocation();
						cspRA.behaviourVerification(ordemR,resourceAllocation,UserResource,setsinc);
					
					   p.validaPadroes();
					}

					catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			});
		}
		
		

		
		
	}
	
	
	
	
	
	
}
