package plugin;




import java.awt.Component;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.change_vision.jude.api.inf.*;
import com.change_vision.jude.api.inf.model.IAttribute;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.ICompositeStructureDiagram;
import com.change_vision.jude.api.inf.model.IConnector;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.model.IPort;
import com.change_vision.jude.api.inf.project.ModelFinder;
import com.change_vision.jude.api.inf.project.ProjectAccessor;
//import com.change_vision.jude.api.inf.project.ProjectAccessorFactory;
import com.change_vision.jude.api.inf.project.ProjectEvent;
import com.change_vision.jude.api.inf.project.ProjectEventListener;


public class CompositionView  implements
ProjectEventListener {
	
	private ProjectAccessor projectAccessor;
	private JComboBox<String> combo1;
	private JTextArea area;
	ICompositeStructureDiagram compositeStructureDiagram;
	
	public CompositionView() {
    	try {
    		//String uri_local = uri;
    		projectAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
    		
 		  //  projectAccessor.open(uri);
 		 //  projectAccessor.open(uri, true);
 		    //projectAccessor.getProject();
 		    
 		     
 		 	}
    	catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
	
	
	public CompositionView(String uri) {
    	try {
    		//String uri_local = uri;
    		projectAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
    		
 		   projectAccessor.open(uri);
 		   projectAccessor.open(uri, true);
 		   projectAccessor.getProject();
 		    
 		     
 		 	}
    	catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
	
	
	
	/*public CompositeView() {
	
	try {
		projectAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
		
		INamedElement[] foundElements = findComposite();
		System.out.println("foundElements.................  " + foundElements.length );
		for (INamedElement element : foundElements) {
			 compositeStructureDiagram = castCompositeStructureDiagaram(element);
			if (compositeStructureDiagram == null) {
				continue;
			}
			System.out.println("nome do diagrama" + compositeStructureDiagram.getName() );
		
		}
		INamedElement[] foundElements2 = null;
		
		foundElements2 =		findConnector();
		
		for (INamedElement element2 : foundElements2) {
			IConnector temp = (IConnector)element2;
			
			System.out.print(" " + temp.getPartsWithPort()[0].getName() + "."+ temp.getPorts()[0].getName() );
			
			System.out.println("  <-> " +"  "+ temp.getPartsWithPort()[1].getName()+"." +temp.getPorts()[1].getName() );

			System.out.println("interface required ->" + temp.getPorts()[0].getRequiredInterfaces()[0].getName());
			System.out.println("interface provided ->" + temp.getPorts()[1].getProvidedInterfaces()[0].getName());
		}
		
		INamedElement[] foundElements3 = findClass();
	for (INamedElement element3 : foundElements3) {
			IClass temp = (IClass)element3;
			System.out.println( "classe" +  temp.getName());
			if (temp.getPorts().length >0 ){
			System.out.println(temp.getPorts()[0].getName());
			System.out.println(temp.getPorts()[1].getName());
			}
		}
		
	for (INamedElement element2 : foundElements2) {
			IConnector temp = (IConnector)element2;
			
			
		}
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   initComponents();

	}
*/	
	private void initComponents() {
		// TODO Auto-generated method stub
		
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

		
	private  INamedElement[] findComposite() throws Exception {
	    INamedElement[] foundElements = projectAccessor.findElements(new ModelFinder() {
		public boolean isTarget(INamedElement namedElement) {
			return namedElement instanceof ICompositeStructureDiagram; 
		}
	});
	return foundElements;
}
	
	public  INamedElement[] findConnector() throws Exception {
	    INamedElement[] foundElements = projectAccessor.findElements(new ModelFinder() {
		public boolean isTarget(INamedElement namedElement) {
		     return namedElement instanceof IConnector; 
		}
	});
	return foundElements;
}
	
	private ICompositeStructureDiagram castCompositeStructureDiagaram(
			INamedElement element) {
		ICompositeStructureDiagram compositeStructureDiagram = null;
		if (element instanceof ICompositeStructureDiagram) {
			compositeStructureDiagram = (ICompositeStructureDiagram) element;
		}
		return compositeStructureDiagram;
	}
	
/*public String getParts(IConnector temp) {
		
		String retorno = "";
		IAttribute[] partsport;
		partsport = temp.getPartsWithPort();
	    Declarations declaration = Declarations.getInstance();
	    ArrayList<PartType> partes = declaration.getPartes();
	    PartType instance1 = new PartType(partsport[0].getName(), partsport[0].getQualifiedTypeExpression());
	    PartType instance2 = new PartType(partsport[1].getName(), partsport[1].getQualifiedTypeExpression());
	     partes.add(instance1);
	     partes.add(instance2);
		return retorno ; 
	}
	
	
public String MontaInstancia() {
		
		String str = "";
		
		Declarations declaration = Declarations.getInstance();
		ArrayList<PartType> partes =  declaration.getPartes();
		HashSet<String> hash = new HashSet<String>();
		HashSet<Instance> instancias = declaration.getInstances();
	
		for(int i =0 ; i < partes.size();i++) {
			hash.add(partes.get(i).getName());
			}
		Iterator iterator = hash.iterator();
		int count = 1;
		while(iterator.hasNext()) {
			String name = (String)iterator.next();
			
			//cria classe que representa o tipo do componente
			
			count = declaration.countComponentType(declaration.getType(name));
			
			//System.out.println(""  + name + " = "+  declaration.getType(name) + "(" + count + ")");
			str = str  + name + " = "+  declaration.getType(name) + "(" + count + ") \n";
			Instance nova = new Instance(name,declaration.getType(name), count);
			instancias.add(nova);
			//count++;
		}
		
		//System.out.println(" hash" + hash.size());
		return str;
		
	}
	
	
	
	
	public int getTypeInterface(IPort port) {
		
		int retorno = -1;
		if( port.getRequiredInterfaces().length >0  ) {
			retorno = 1;
		}
		else if( port.getProvidedInterfaces().length >0  ) {
			retorno = 0;
		} 
	  	return retorno;
	}

	



	
public String getPortas(IConnector temp) {
		
		String retorno ="----PORTAS------";
		IPort[] port;
		port = temp.getPorts();
		IConnector[] temp2;
		
		retorno = port[0].getName();
		//retorno = retorno + " (TYPE)  " + partsport[0].getQualifiedTypeExpression();
		retorno = retorno +  "<->" + port[1].getName();
		//temp2 = port[0].getConnectors();
		//for (IConnector i : temp2) {
		//	retorno = retorno + temp2[0].getPartsWithPort()[0];
		//}
		//retorno = retorno + " (TYPE)  " + partsport[1].getQualifiedTypeExpression();
		
		 
		return retorno ; 
		
	}
	
	
public String getPartes(IConnector temp) {
	
	String retorno ="----PARTES------";
	IAttribute[] parts;
	parts = temp.getParts();
	
	  retorno =parts.length + "";
	  for (IAttribute i: parts ) {
		  retorno = retorno + i.getId();
	  }
	 
	return retorno ; 
	
}

private  INamedElement[] findClass() throws Exception {
    INamedElement[] foundElements = projectAccessor.findElements(new ModelFinder() {
	public boolean isTarget(INamedElement namedElement) {
	     return namedElement instanceof IClass; 
	}
});
return foundElements;
}

//metodo para montar canais get e set

public String getChannelGetSet() {
	

	String channel = new String();
	try {
		INamedElement[] foundElements3 = findClass();
		for (INamedElement element3 : foundElements3) {
			IClass temp = (IClass)element3;
			//System.out.println( "classe -->>>" +  temp.getName());
			if (temp.getAttributes().length >0 ){
				for( int i = 0; i<temp.getAttributes().length;i++) {
				  if(temp.getAttributes()[i].getAssociation() == null){
			
				
				channel = channel +  "channel " + "get_"+temp.getAttributes()[i] + " : id_" +temp.getName() + ".vl \n" ; 
				channel = channel +  "channel " + "set_"+temp.getAttributes()[i] + " : id_" +temp.getName() + ".vl \n" ; 
				
				  }
				}
			}
		}
	
	
	} catch(Exception e) {
		
		
	}
	
	return channel;
}



public String getProcessGetSet() {
	
	String getset =  new String();
	String names = new String();
	IAttribute[] filtrada;
	try {
		INamedElement[] foundElements3 = findClass();
		for (INamedElement element3 : foundElements3) {
			IClass temp = (IClass)element3;
			// filtrar atributos da classe 
			filtrada = filtraAtributos(temp.getAttributes());
			
			 if (filtrada.length >0 ){
			  getset = getset + "\n" +  temp.getName() + "_state( id," ;
			  
			  for( int i = 0; i<filtrada.length;i++) {
			 
			     names =  names + filtrada[i].getName() ;
			 
			//  System.out.println("tamanho" + filtrada.length);
			 if(i < (filtrada.length  -1)){
				 names = names + ",";
			 }
			}
		  
			
			getset = getset + names + ") =";
		 }
			//gets
			for( int j = 0; j<filtrada.length;j++) {
		    getset = getset +	"get_" + filtrada[j].getName() +".id!" + filtrada[j].getName() + "->" + temp.getName() +"_state "+ "( id, "+ names +" ) \n []";
			
			}
			//System.out.println(getset);
			
			int aux = 0;
			int aux2 =0;
			String param = "";
			
			for( int l = 0; l<filtrada.length;l++) {
				
				getset = getset+ "set_" + filtrada[l].getName() + ".id?v -> " + temp.getName() +"_state "+ "( id, ";
				param = "";
				for ( int k =0 ; k< filtrada.length; k++  ) {
					 
					if (aux == k ) {
						param = param + "vl" ;
					}	
						else {
						param = param + filtrada[k].getName();
					}
					if (k <filtrada.length -1){
						param = param + " ,";}
					}
				
				getset = getset + param + ")";
				
			 aux= aux + 1;	
		
			 if( aux2 <filtrada.length -1) {
				 getset = getset + " \n [] \n"; 
			 }
			 aux2= aux2 + 1;	 
			}	
			}	
		}
		catch(Exception e) {
			
		}	
	return getset;
}


public String getChannelPort() {
	String channel = new String();
	Declarations declaration = Declarations.getInstance();
	try {
	INamedElement[] foundElements3 = findClass();
	for (INamedElement element3 : foundElements3) {
		IClass temp = (IClass)element3;
		
		// verifica o esteriotipo da classe 
		// se for basicComponent crio o range de id 
		if (temp.getStereotypes().length >0) {
		if (temp.getStereotypes()[0].equalsIgnoreCase("BasicComponentClass")){
			
			channel = channel + "id_"  + temp.getName() + "= {1,2} \n";
		}}
		
		
		if (temp.getPorts().length >0 ){
			channel = channel + "channel " + temp.getPorts()[0].getName() + " : id_" +temp.getName() + ".operation \n" ; 
			channel  = channel + "channel " + temp.getPorts()[1].getName() + " : id_" +temp.getName() + ".operation \n" ; 
		} 
	}
	}
	
	catch(Exception e) {
		
		
	}
	 return channel;
}


public IAttribute[] filtraAtributos(IAttribute[] filtra) {
	
	IAttribute[] retorno;
	
	int count = 0;
	for(int j = 0; j < filtra.length ; j++) {		
		if(filtra[j].getAssociation() == null) {			
			count++;			
		}
	}
	retorno = new IAttribute[count];
	
	int add = 0;
    for (int i = 0; i < filtra.length; i++)
    {
        if (filtra[i].getAssociation() == null)
        {
        	retorno[add++] = filtra[i];
        }
    }
	
	return retorno;
}

public String getInputComponent() {
	
	String InputComponent ="";
	Declarations declaration = Declarations.getInstance();
	ArrayList<Port> ports = declaration.getPortas();
	ArrayList<Input_Output> inputoutput = declaration.getInputoutput();
	//lista de instancias
	HashSet<Instance>  instances = declaration.getInstances();
	Iterator it = instances.iterator();
	HashSet<String> ports_comp;
	Iterator<String> it_str;
	
	while(it.hasNext()) {
		 String temp ="";
		Instance instance_temp = (Instance)it.next();
		System.out.print("input_" + instance_temp.getName() + "= {|");
		
		String input_name = "input_" + instance_temp.getName();
		String output_name = "output_" + instance_temp.getName();
		Input_Output temp_i_o = new  Input_Output(instance_temp.getType(), input_name, output_name);
		inputoutput.add(temp_i_o);
		
		
		ports_comp = getPortComponent(instance_temp.getType());
		it_str = ports_comp.iterator();
		String str_temp ="";
		String str_optype ="";
		while(it_str.hasNext()){
			
			 temp = (String)it_str.next() ;
			str_temp =  str_temp + temp+ "."+ declaration.getId(instance_temp.getName()) + ".op";
			if(it_str.hasNext()) {str_temp = str_temp + ", ";}
		
		}
		//interface provide or required ?
		
		System.out.print(str_temp + "|"
				+ "op:");
		
		if(declaration.getIsProvReq(temp)==1) {
			
			str_optype = declaration.getType(instance_temp.getName()) +"_O" ;
			//System.out.println(str_optype);
			
		}
		else
			if(declaration.getIsProvReq(temp)==0) {
				
				str_optype = declaration.getType(instance_temp.getName()) +"_I" ;	
			}
		
       System.out.println(str_optype +	"|}");
		
		//lista de portas
	} 
	
	//for(int i =0; i <ports.size();i++) {
		
		//System.out.println(ports.get(i).getName());
		//System.out.println(ports.get(i).getOwner());
		//System.out.println(ports.get(i).getReq_prov());
	//}
	
	
	
	return InputComponent;
}

//observar dependencia de c.firstConection

public String getOutputComponent() {
	
	String outputComponent ="";
	Declarations declaration = Declarations.getInstance();
	ArrayList<Port> ports = declaration.getPortas();
	//lista de instancias
	HashSet<Instance>  instances = declaration.getInstances();
	Iterator it = instances.iterator();
	HashSet<String> ports_comp;
	Iterator<String> it_str;
	
	while(it.hasNext()) {
		String temp ="";
		Instance instance_temp = (Instance)it.next();
		//outputComponent = outputComponent + " output_" + instance_temp.getName() + "= {|";
		System.out.print("output_" + instance_temp.getName() + "= {|");
		//System.out.println (instance_temp.getType());
		
		ports_comp = getPortComponent(instance_temp.getType());
		it_str = ports_comp.iterator();
		String str_temp ="";
		String str_optype ="";
		while(it_str.hasNext()){
			 temp = (String)it_str.next() ;
			// outputComponent = outputComponent +  str_temp + temp+ "."+ declaration.getId(instance_temp.getName()) + ".op" ;
			str_temp =  str_temp + temp+ "."+ declaration.getId(instance_temp.getName()) + ".op";
			if(it_str.hasNext()) {
				str_temp = str_temp + ", ";
			//	outputComponent = outputComponent + str_temp;
				}
		
		}
		
		
		//interface provide or required ?
		
		System.out.print(str_temp + "|" + "op:");
		
		if(declaration.getIsProvReq(temp)==1) {
			
			str_optype = declaration.getType(instance_temp.getName()) +"_I" ;
			//System.out.println(str_optype);
			
		}
		else
			if(declaration.getIsProvReq(temp)==0) {
				
				str_optype = declaration.getType(instance_temp.getName()) +"_O" ;	
			}
		
       System.out.println(str_optype +	"|}");
		
		//lista de portas
	} 
	
	//for(int i =0; i <ports.size();i++) {
		
		//System.out.println(ports.get(i).getName());
		//System.out.println(ports.get(i).getOwner());
		//System.out.println(ports.get(i).getReq_prov());
	//}
	
	
	
	return outputComponent;
}
/*
public HashSet<String> getPortComponent(String componentName) {
	
	HashSet<String> retorno = new HashSet<String>() ;
	Declarations declaration = Declarations.getInstance();
	ArrayList<Port> portas = declaration.getPortas();
    for(int i =0; i <portas.size();i++) {
    
    
    	if(portas.get(i).getOwner().equalsIgnoreCase(componentName)){
    	  retorno.add(portas.get(i).getName());
    		}
    }
	return retorno;
}

public HashSet<String> getInstanceType(){
	HashSet<String> instanceType = new HashSet<String>();
	Declarations declaration = Declarations.getInstance();
	HashSet<Instance> instances = declaration.getInstances();
	Iterator<Instance> i = instances.iterator();	
	while(i.hasNext()) {		
		instanceType.add(((Instance)i.next()).getType());
	}	
	return instanceType;
	
}
public HashSet<String> getInstancebyType(String type){
	HashSet<String> instancebyType = new HashSet<String>();
	Declarations declaration = Declarations.getInstance();
	HashSet<Instance> instances = declaration.getInstances();
	Iterator<Instance> i = instances.iterator();	
	while(i.hasNext()) {
		Instance temp = (Instance)i.next();
		if(temp.getType().equalsIgnoreCase(type)) {
		instancebyType.add(temp.getName());
		System.out.println(temp.getName());
		}
	}	
	return instancebyType;
	
}

// metodo para recuperar portas de um basic component



para cada tipo de componente eh gerado
public String getBufferInterleave() {
	
	String bufferInterleave = "BUFFER";
	Declarations declaration = Declarations.getInstance();
	HashSet<Instance> instances = declaration.getInstances();
	HashSet<String> instanceType = getInstanceType();
	//verifica os tipos que estao na composicao
	
	Iterator i_instanceType = instanceType.iterator();
	
	while(i_instanceType.hasNext()) {
		//ver as instancias do tipo
		
		
		
	}
	
	return bufferInterleave;
	
}






public static void main(String[]  args) {
	try {
	CompositeView c = new CompositeView("C:/Users/flavi/eclipse-workspace/ref/src/main/resources/modelo.asta");
  
	INamedElement[] foundElements = c.findComposite();
	ICompositeStructureDiagram compositeStructureDiagram;
	System.out.println("foundElements.................  " + foundElements.length );
	
	for (INamedElement element : foundElements) {
		 compositeStructureDiagram = c.castCompositeStructureDiagaram(element);
		if (compositeStructureDiagram == null) {
			continue;
		}
		System.out.println("nome do diagrama   " + compositeStructureDiagram.getName() );
	
	}
	
	
	INamedElement[] foundElements3 = c.findClass();
	for (INamedElement element3 : foundElements3) {
			IClass temp = (IClass)element3;
			System.out.println( "classe   " +  temp.getName());
			if (temp.getStereotypes().length >0)
			System.out.println( "classe  este  " +  temp.getStereotypes()[0]);
	}
	
	System.out.println(c.getChannelPort());
	System.out.println(c.getChannelGetSet());
	System.out.println(c.getProcessGetSet());
	
	
	INamedElement[] foundElements2 = null;
	foundElements2 = c.findConnector();
	for (INamedElement element2 : foundElements2) {
		IConnector temp = (IConnector)element2;
		
		System.out.println(c.getParts(temp));
		//System.out.println(c.getPortas(temp));
		
	
	
	}
	
	c.MontaInstancia();
	
	//String interleave = c.interleaveProcess();
	//System.out.println("ProcessInterleave = " + interleave);
	String process = "ProcessInterleave";
	String buffer = "Buffer";
	INamedElement[]	foundElements4 = null;
	
	foundElements4 =c.findConnector();
	
	//System.out.println(foundElements3.length);
	String processcomp = "processcomp";
	for (INamedElement element3 : foundElements4) {
		IConnector temp = (IConnector)element3;
		
		//System.out.print(" " + temp.getPartsWithPort()[0].getName() + "."+ temp.getPorts()[0].getName() );
		
		//System.out.println("  <<->> " +"  "+ temp.getPartsWithPort()[1].getName()+"." +temp.getPorts()[1].getName() );
		System.out.print(processcomp + "= ");
		System.out.print("(" + process + ") ");
	//	System.out.print(c.firstConection(temp));
		System.out.println(buffer);
		process = processcomp;
		processcomp= processcomp + "_comp";
		
   
	}
	
	c.getInputComponent();
	c.getOutputComponent();
	c.getInstanceType();
	c.getInstancebyType("FORK");
	c.projectAccessor.close();
	}
	catch(Exception e) {
		
		System.out.println(e.toString());
		e.printStackTrace();
		
	}
}
*/
}

