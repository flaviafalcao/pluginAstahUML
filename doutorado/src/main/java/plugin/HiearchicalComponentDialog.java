package plugin;

import java.awt.BorderLayout;
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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.table.AbstractTableModel;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.editor.BasicModelEditor;
import com.change_vision.jude.api.inf.editor.ClassDiagramEditor;
import com.change_vision.jude.api.inf.editor.CompositeStructureDiagramEditor;
import com.change_vision.jude.api.inf.editor.ModelEditorFactory;
import com.change_vision.jude.api.inf.editor.StateMachineDiagramEditor;
import com.change_vision.jude.api.inf.editor.TransactionManager;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IAssociation;
import com.change_vision.jude.api.inf.model.IAssociationClass;
import com.change_vision.jude.api.inf.model.IAttribute;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IClassDiagram;
import com.change_vision.jude.api.inf.model.ICompositeStructureDiagram;
import com.change_vision.jude.api.inf.model.IModel;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.model.IPackage;
import com.change_vision.jude.api.inf.model.IStateMachineDiagram;
import com.change_vision.jude.api.inf.presentation.ILinkPresentation;
import com.change_vision.jude.api.inf.presentation.INodePresentation;
import com.change_vision.jude.api.inf.project.ModelFinder;
import com.change_vision.jude.api.inf.project.ProjectAccessor;


public class HiearchicalComponentDialog extends JDialog{
	
	private JTextField tf;
	ProjectAccessor projectAccessor;
	private boolean DEBUG = false;
	INamedElement[] foundElements;
	
	public HiearchicalComponentDialog (JFrame frame, boolean modal) throws FileNotFoundException, IOException, 
	ClassNotFoundException {
		super(frame, modal);
		initComponents();
		this.setTitle("Hirarchical Component");
		this.setLocation(new Point(276, 182));
		this.setSize(new Dimension(450, 200)); //450 200
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
        add(new JLabel("Hiearchical Component Name:"), gbc);
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
        //add(createButton, gbc);
        
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
		
        // colocar nome do componente
        add(new JLabel("Components:"), gbc);
		 
        
        
        
        gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridheight = 3;
		gbc.gridwidth = 2;
		
		gbc.weightx = 1;
		gbc.weighty = 1;

       
		Declarations declaration = Declarations.getInstance();
		
		IClass pkg_comp;
		
		try {
			projectAccessor = AstahAPI.getAstahAPI().getProjectAccessor();			
			foundElements = findComponents();
		
		
		for (INamedElement element : foundElements) {
			
			pkg_comp = castIPackage(element);
			if(pkg_comp.getStereotypes()[0].equalsIgnoreCase("BasicComponentClass") || 
					pkg_comp.getStereotypes()[0].equalsIgnoreCase("HiearchicalComponentClass")	) {
			declaration.getNameComponents().add(pkg_comp.getName());
			}
		}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
        
        // criar uma lista de selecao e quantidade 
        
        JTable componentes = new JTable();  
        
          componentes.setPreferredScrollableViewportSize(new Dimension(300, 50));
      //  componentes.setFillsViewportHeight(false);

        //Create the scroll pane and add the table to it.
         JScrollPane scrollPane = new JScrollPane(componentes);
         
         scrollPane.setViewportView(componentes);
        
        //Add the scroll pane to this panel.
        componentes.setModel(new MyTableModel());
        scrollPane.setSize(300, 50);
         
        
	    this.getContentPane().add(scrollPane,gbc);
       
	    gbc.gridx = 0;
        gbc.gridy = 6 ;
        gbc.fill = GridBagConstraints.NONE;
		
	    add(createButton, gbc);
       
	    
        createButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				//Array com os componentes assinalados
				ArrayList<selected> str_comp = new ArrayList<selected>();
				
				
				try {
					
					Object obj_name = new Object();
					Object obj_qtd = new Object();
					Object obj = new Object();
					// verifica as linhas que foram marcadas
					int i =0;
					while(i<componentes.getRowCount()) {
						
						obj = componentes.getValueAt(i, 2);
						if (obj instanceof Boolean) {
							Boolean isSelectedRow = (Boolean) obj;
							if (isSelectedRow) {
								
								obj_name = componentes.getValueAt(i, 0);
								obj_qtd = componentes.getValueAt(i, 1);
								
								selected sel_temp = new selected(obj_name.toString(),new Integer(obj_qtd.toString()) );
								str_comp.add(sel_temp);
								System.out.println(obj_name.toString());
								System.out.println(obj_qtd.toString());
							}
						
					  }
						i++;
					}
					
					
					
					String name =  tf.getText();
		    		
		    		//adicionar a lsita de componentes
		    		
		    		Declarations declaration = Declarations.getInstance();
		    		declaration.getNameComponents().add(name);
		    		
		    		//// criar diagramas
		    		
		    		IModel project = projectAccessor.getProject();
		        	
		        	TransactionManager.beginTransaction();
		            
		            BasicModelEditor editorBasico = ModelEditorFactory.getBasicModelEditor();
		    		
		           //diagrama de classes
		            ClassDiagramEditor dec = projectAccessor.getDiagramEditorFactory().getClassDiagramEditor();
		                        
		            IClassDiagram newDclass = dec.createClassDiagram(project, name);
		            dec.setDiagram(newDclass);
		            
		            
		            //pacote
		    		IPackage  pkg = editorBasico.createPackage((IPackage)newDclass.getOwner(), name);
		    		pkg.addStereotype("HiearchicalComponent");
		    		
		    		//classe
		    		IClass basic = editorBasico.createClass(pkg, name);
		    		basic.addStereotype("HiearchicalComponentClass");
			    		
		           
		            INodePresentation iNPackage=  dec.createNodePresentation(pkg, new Point2D.Double(10, 100));
		    		iNPackage.setWidth(500);
		    		iNPackage.setHeight(500);		    	
		    		
			            
		    		INodePresentation iNClass =dec.createNodePresentation(basic, new Point2D.Double(40, 150));
		    		
		        	INodePresentation iNNote1  = dec.createNote("STR_"+ name, new Point2D.Double(260, 280));
		    	 	dec.createNoteAnchor(iNClass, iNNote1);
		    	 	
		    	 	IClass basic_temp  = null;
		    	 	// criar classes que representam os componentes selecionados 
		    	 	/*for (int k = 0; k < str_comp.size();k++) {
		    	 		
		    	 		//classe
			    		 basic_temp = editorBasico.createClass(pkg, str_comp.get(k).name);
			    		basic_temp.addStereotype("BasicComponent");
			    		
			    		INodePresentation iNClassTemp =dec.createNodePresentation(basic_temp, new Point2D.Double(40, 150 + 80*(k+1)));
			    		
			    		IAssociation association = editorBasico.createAssociation(basic, basic_temp, "", "", "");
			    		
			    		IAttribute[] testeat = association.getAttributes();
			    		
			    		int[][] mult = {{1}};
			    		association.getAttributes()[0].setComposite();
			    		association.getAttributes()[0].setMultiplicity(mult);
			    		int mult_comp = str_comp.get(k).qtd.intValue();
			    		int[][] multb = {{mult_comp}};
			    		association.getAttributes()[1].setMultiplicity(multb);
			    		
			    		/*System.out.println("tamanho --> " +testeat.length);
			    		for(int m =0 ;m<testeat.length ;m++) {
			    			
			    		   
			    			
			    			System.out.println("getDefinition " +  testeat[m].getDefinition());
			    			System.out.println("id "+testeat[m].getId());
			    			System.out.println("navigab " +testeat[m].getNavigability());
			    			System.out.println("expre " +testeat[m].getQualifiedTypeExpression());
			    			System.out.println("qualif " +testeat[m].getQualifiers());
			    			System.out.println("type " +testeat[m].getType());
			    			System.out.println("assoc " +testeat[m].getAssociation());
					    	
			    		}
			    		
			    		
			    	   
			    		
			    		ILinkPresentation ilink = dec.createLinkPresentation(association, iNClass, iNClassTemp);
			    		
			    		
			    		 
			    	
			    		
			    		
				    	
		    	 	}*/
		    	 	
		    	 	
		    	 	
		    		//diagrama de estrutura compostas 
		            CompositeStructureDiagramEditor decom = projectAccessor.getDiagramEditorFactory().getCompositeStructureDiagramEditor();
		            ICompositeStructureDiagram newCS = decom.createCompositeStructureDiagram(project, "STR_" + name);
		            decom.setDiagram(newCS);
		            
		            INodePresentation iNP_str = decom.createStructuredClassPresentation(basic, new Point2D.Double(10, 10));
		            
		            // IClass partBaseClass 
		            
		            IClass  temp = null; 
		            
		            for (int k = 0; k < str_comp.size();k++) {  // componentes selecionados 
		            
		            // str_comp.get(k).name
		              String component =  str_comp.get(k).name;
		              
		              int nr = str_comp.get(k).qtd.intValue();
		            	
		            	for (INamedElement element : foundElements) {		    			
			    			
			            	temp = castIPackage(element);
			    			if (temp.getName().equalsIgnoreCase(component)) {
			    				basic_temp = temp;
			    				
			    			}			    			
			    		}
		            
		            	
		            	for ( int j = 0; j<nr ;j++) {
		            	
		            	 IAssociation asso = editorBasico.createAssociation(basic, basic_temp, "", "", component + j );
		            	 IAttribute[] memberEnds = asso.getMemberEnds();
			             memberEnds[0].setComposite();
			             
 			            decom.setDiagram(newCS);
			             //create presentation
			             INodePresentation ps = null;
			             ps = decom.createNodePresentation(memberEnds[1], iNP_str, new Point2D.Double(15*k+j+20,15*k+j+30));
			        
		            	}     
		            
		            }
		            
		            
		            
		         
		            
		            
		            
		            
		           // IAssociation asso = editorBasico.createAssociation(basic, basic_temp, "", "", "teste01");
		           // IAttribute[] memberEnds = asso.getMemberEnds();
		          //   memberEnds[0].setComposite();
		             
		             //set diagram
		        
		         //    decom.setDiagram(newCS);
		             //create presentation
		         //    INodePresentation ps = null;
		        //     ps = decom.createNodePresentation(memberEnds[1], iNP_str, new Point2D.Double(15,15));
		             
		           
		            System.out.println( "nr filhos" + iNP_str.getChildren().length);
		            
		         
		            
		            
		           declaration.setNameComponents(new ArrayList<String>());
		            
		            TransactionManager.endTransaction();
		            projectAccessor.save();
		        	
		            
		        	
		        fechar();
					
					
					
				}
				
				catch(Exception ex) {
					ex.printStackTrace();
				}
				}
        });
        
        
       
	    
	
	}
	
   private void fechar() {
		
		this.setVisible(false);
	}
	
	
	class MyTableModel extends AbstractTableModel {
		
		
		
		
        private String[] columnNames = {"Component",
                                        "# ",
                                        ""};
        
        Declarations declaration = Declarations.getInstance();
    	ArrayList<String> names = declaration.getNameComponents();
    	
        //montar lista 
        private Object[][] data = new Object[names.size()][3];
        //= {
	    //{"Fork",  new Integer(0), new Boolean(false)},
	    //{"Phil",  new Integer(0), new Boolean(true)},
	    //};

        
        MyTableModel(){
        	montaData();}
        
        
        public void montaData() {
        	
        	
        	for(int i =0; i< names.size();i++) {
        		
        		data[i][0] = names.get(i);
        				data[i][1] =new Integer(0);
        						data[i][2]=new Boolean(false);     
        	}
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
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            if (col == 0) {
                return false;
            } else {
                return true;
            }
        }

        /*
         * Don't need to implement this method unless your table's
         * data can change.
         */
        public void setValueAt(Object value, int row, int col) {
            if (DEBUG) {
                System.out.println("Setting value at " + row + "," + col
                                   + " to " + value
                                   + " (an instance of "
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

            for (int i=0; i < numRows; i++) {
                System.out.print("    row " + i + ":");
                for (int j=0; j < numCols; j++) {
                    System.out.print("  " + data[i][j]);
                }
                System.out.println();
            }
            System.out.println("--------------------------");
        }
    }

	
	private INamedElement[] findComponents() throws ProjectNotFoundException {
		INamedElement[] foundElements = projectAccessor
				.findElements(new ModelFinder() {
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

	
	class selected{
		
		String name;
		Integer qtd;
		
		
		selected(String name, Integer qtd){
			this.name= name;
			this.qtd = qtd;
		}
	}
	

}
