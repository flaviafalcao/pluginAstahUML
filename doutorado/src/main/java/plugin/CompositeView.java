package plugin;

import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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

public class CompositeView {

	private ProjectAccessor projectAccessor;
	ICompositeStructureDiagram compositeStructureDiagram;
	
	// CRIAR ARQUIVO
		//acesso pasta local do seu projeto
		public static final String USER_DIR = System.getProperty("user.home");
		//separador de arquivos '\' ou '/' dependendo do Sistema Operacional
		public static final String SEPARATOR = System.getProperty("file.separator");

	public CompositeView() {
		try {
			// String uri_local = uri;
			projectAccessor = AstahAPI.getAstahAPI().getProjectAccessor();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public CompositeView(String uri) {
		try {
			// String uri_local = uri;
			projectAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
			projectAccessor.open(uri);
			projectAccessor.open(uri, true);
			projectAccessor.getProject();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public INamedElement[] findComposite() throws Exception {
		INamedElement[] foundElements = projectAccessor.findElements(new ModelFinder() {
			public boolean isTarget(INamedElement namedElement) {
				return namedElement instanceof ICompositeStructureDiagram;
			}
		});
		return foundElements;
	}

	public INamedElement[] findConnector() throws Exception {
		INamedElement[] foundElements = projectAccessor.findElements(new ModelFinder() {
			public boolean isTarget(INamedElement namedElement) {
				return namedElement instanceof IConnector;
			}
		});
		return foundElements;
	}

	private ICompositeStructureDiagram castCompositeStructureDiagaram(INamedElement element) {
		ICompositeStructureDiagram compositeStructureDiagram = null;
		if (element instanceof ICompositeStructureDiagram) {
			compositeStructureDiagram = (ICompositeStructureDiagram) element;
		}
		return compositeStructureDiagram;
	}

	// retorna attibutos

	public INamedElement[] findAttributes() throws Exception {
		INamedElement[] foundElements = projectAccessor.findElements(new ModelFinder() {
			public boolean isTarget(INamedElement namedElement) {
				return namedElement instanceof IAttribute;
			}
		});
		return foundElements;
	}

	/**
	 * 
	 * 
	 * 
	 * @param temp
	 * @return
	 */
	public String getParts(IConnector temp) {

		String retorno = "";
		IAttribute[] partsport;
		partsport = temp.getPartsWithPort();
		Declarations declaration = Declarations.getInstance();
		ArrayList<PartType> partes = declaration.getPartes();
		PartType instance1 = new PartType(partsport[0].getName(), partsport[0].getQualifiedTypeExpression(),
				partsport[0].getOwner().toString());
		PartType instance2 = new PartType(partsport[1].getName(), partsport[1].getQualifiedTypeExpression(),
				partsport[0].getOwner().toString());
		partes.add(instance1);
		partes.add(instance2);
		return retorno;
	}

	// cria a instancia de um componente
	// assim como monta o contrato instanciado

	public String MontaInstancia() {

		String str = "";

		Declarations declaration = Declarations.getInstance();
		ArrayList<PartType> partes = declaration.getPartes();
		HashSet<String> hash = new HashSet<String>();
		HashSet<Instance> instancias = declaration.getInstances();

		for (int i = 0; i < partes.size(); i++) {
			hash.add(partes.get(i).getName());
		}
		Iterator iterator = hash.iterator();
		int count = 1;
		while (iterator.hasNext()) {
			String name = (String) iterator.next();

			// cria classe que representa o tipo do componente

			count = declaration.countComponentType(declaration.getType(name));

			// instancia contrato
			// recupera o contrato com o nome do tipo

			Contrato recuperado = declaration.getbyComponent(declaration.getType(name));

			System.out.println(recuperado.getTipo());

			ArrayList<String> portas = replaceId(recuperado.getPortas(), count);

			ArrayList<String> op = recuperado.getOp();

			ArrayList<relac_op_port> relacao = recuperado.getRelacao();

			str = str + name + " = " + declaration.getType(name) + "(" + count + ") \n";
			Instance nova = new Instance(name, declaration.getType(name), count);

			nova.setPortas(portas);
			instancias.add(nova);

			// adiciona contrato instancia

			Contrato novo = new Contrato("instancia", name, declaration.getType(name) + "(" + count + ")", portas, op,
					relacao);

			declaration.addContrato(novo);
			// count++;
		}

		return str;

	}

	public ArrayList<String> replaceId(ArrayList<String> array, int count) {

		ArrayList<String> retorno = new ArrayList<String>();

		for (int i = 0; i < array.size(); i++) {

			retorno.add(array.get(i).replace("id", "" + count));
		}

		return retorno;

	}

	// retorno se porta tem interface required ou provide
	public int getTypeInterface(IPort port) {

		int retorno = -1;
		if (port.getRequiredInterfaces().length > 0) {
			retorno = 1;
		} else if (port.getProvidedInterfaces().length > 0) {
			retorno = 0;
		}
		return retorno;
	}

	// retorna o nome da interface caso ela exista
	public String getNameInterface(IPort port) {

		String retorno = "";

		if (port.getRequiredInterfaces().length > 0) {
			retorno = port.getRequiredInterfaces()[0].getName();
		} else if (port.getProvidedInterfaces().length > 0) {
			retorno = port.getProvidedInterfaces()[0].getName();
		}
		return retorno;
	}

	public void findPart() throws Exception {

		INamedElement[] attributes = this.findAttributes();
		Declarations declaration = Declarations.getInstance();
		ArrayList<PartType> partes = declaration.getPartes();

		// filtra os atributos tipo parte e coloca no array de partes
		for (INamedElement elementA : attributes) {
			IAttribute temp = (IAttribute) elementA;
			if (temp.getName() != null && temp.getPresentations().length > 0
					&& temp.getPresentations()[0].getType().equalsIgnoreCase("Part")) {
				String name = temp.getName();
				String type = temp.getQualifiedTypeExpression();
				;
				String owner = temp.getOwner().toString();
				PartType instance1 = new PartType(name, type, owner);
				partes.add(instance1);

			}
		}

	}

	public String getPortas(IConnector temp) {

		String retorno = "----PORTAS------";
		IPort[] port;
		port = temp.getPorts();
		IConnector[] temp2;

		retorno = port[0].getName();
		retorno = retorno + "<->" + port[1].getName();

		return retorno;

	}

	public String getPartes(IConnector temp) {

		String retorno = "----PARTES------";
		IAttribute[] parts;
		parts = temp.getParts();

		retorno = parts.length + "";
		for (IAttribute i : parts) {
			retorno = retorno + i.getId();
		}

		return retorno;

	}

	public INamedElement[] findClass() throws Exception {
		INamedElement[] foundElements = projectAccessor.findElements(new ModelFinder() {
			public boolean isTarget(INamedElement namedElement) {
				return namedElement instanceof IClass;
			}
		});
		return foundElements;
	}

	public String memoryProcess() {

		String getset = new String();
		String names = new String();
		Declarations declaration = Declarations.getInstance();
		IAttribute[] filtrada;
		try {
			INamedElement[] foundElements3 = findClass(); /// aqui deveria apenas
			for (INamedElement element3 : foundElements3) {

				if ((element3.getStereotypes()[0].equalsIgnoreCase("BasicComponentClass"))
						|| (element3.getStereotypes()[0].equalsIgnoreCase("HiearchicalComponentClass"))) {

					IClass temp = (IClass) element3;
					// filtrar atributos da classe
					filtrada = null;
					names = "";
					filtrada = filtraAtributos(temp.getAttributes());

					if (filtrada.length > 0) {
						getset = getset + "\n" + temp.getName() + "_state( id,";

						for (int i = 0; i < filtrada.length; i++) {

							names = names + filtrada[i].getName();

							if (i < (filtrada.length - 1)) {
								names = names + ",";
							}
						}

						getset = getset + names + ") =";
					}

					// gets
					for (int j = 0; j < filtrada.length; j++) {
						getset = getset + "get_" + filtrada[j].getName() + ".id!" + filtrada[j].getName() + "->"
								+ temp.getName() + "_state " + "( id, " + names + " ) \n []";

					}
					// System.out.println(getset);

					int aux = 0;
					int aux2 = 0;
					String param = "";

					for (int l = 0; l < filtrada.length; l++) {

						getset = getset + "set_" + filtrada[l].getName() + ".id?vl -> " + temp.getName() + "_state "
								+ "( id, ";
						param = "";
						for (int k = 0; k < filtrada.length; k++) {

							if (aux == k) {
								param = param + "vl";
							} else {
								param = param + filtrada[k].getName();
							}
							if (k < filtrada.length - 1) {
								param = param + " ,";
							}
						}

						getset = getset + param + ") \n";

						aux = aux + 1;

						if (aux2 < filtrada.length - 1) {
							getset = getset + "  [] \n";
						}

						aux2 = aux2 + 1;
					}

					// adiciona memoria
					// adiciona as guardas

					ArrayList<Memory> memory = declaration.memoryType(temp.getName()); // guardas de um componente

					if (memory.size() > 0) {
						getset = getset + "[] \n";

						// loop na memoria
						for (int m = 0; m < memory.size(); m++) {

							// verifica se ha trigger associada a memory
							if (memory.get(m).getTrigger()[0].trim().length() > 0) {

								getset = getset + memory.get(m).getStr_guard() + " & " + memory.get(m).getExp() + " -> "
										+ temp.getName() + "_state " + "( id, " + names + " )" + '\n';

								if (m < memory.size() - 1) {

									getset = getset + "[] \n";
								}

							}
							// }
							else {

								getset = getset + memory.get(m).getExp() + " -> " + temp.getName() + "_state "
										+ "( id, " + names + " )" + '\n';

								if (m < memory.size() - 1) {

									getset = getset + "[] \n";
								}

							}

						}

					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return getset + "\n";
	}

	/**
	 * Channel ports
	 * 
	 * 
	 * @return
	 */

	public String getChannelPort() {

		String channel = new String();
		Declarations declaration = Declarations.getInstance();
		String multiplicity = "";
		String strIndex = "";
		String typeIndex = "";
		String guardStr = "";

		try {

			INamedElement[] foundElements3 = findClass();

			for (INamedElement element3 : foundElements3) {
				IClass temp = (IClass) element3;

				// verifica o esteriotipo da classe
				// se for basicComponent crio o range de id
				if (temp.getStereotypes().length > 0) {

					if (temp.getStereotypes()[0].equalsIgnoreCase("BasicComponentClass")) {

						if (temp.getPorts().length > 0) {
							for (int x = 0; x < temp.getPorts().length; x++) {

								if (temp.getPorts()[x].getMultiplicity() != null
										&& temp.getPorts()[x].getMultiplicity().length > 0
										&& temp.getPorts()[x].getMultiplicity()[0].getUpper() > 0) {

									multiplicity = temp.getPorts()[x].getMultiplicity()[0].getUpper() + "";
									strIndex = ".index";

									typeIndex = "index = {1.." + multiplicity + "} \n";

								}

								Port portTemp = declaration.getPort(temp.getPorts()[x].getName());

								if (portTemp != null && portTemp.isGuard()) {

									guardStr = ".t_id";

								}

								channel = channel + "channel " + temp.getPorts()[x].getName() + " : id_"
										+ temp.getName() + guardStr + strIndex + ".operation \n" + typeIndex;

								strIndex = "";
								typeIndex = "";
								guardStr = "";

								// setar o indicativo de guarda da porta

							}

						}
					}

				}
			}

		}

		catch (Exception e) {

			e.printStackTrace();
		}
		return channel;
	}

// channel range de tipos

	public String rangeTipo() {

		String channel = "";
		Declarations declaration = Declarations.getInstance();
		INamedElement[] foundElements3;
		try {
			foundElements3 = findClass();

			for (INamedElement element3 : foundElements3) {
				IClass temp = (IClass) element3;

				// verifica o esteriotipo da classe
				// se for basicComponent crio o range de id
				if (temp.getStereotypes().length > 0) {
					if (temp.getStereotypes()[0].equalsIgnoreCase("BasicComponentClass")) {

						channel = channel + "id_" + temp.getName() + " = ";

						int count = declaration.getCountComponentType(temp.getName()); // 2
						int i = 0;
						String range = "";
						if (count > 0) {
							while (i <= count) { // 1

								range = range + i + " ";

								if (i < count) {
									range = range + ",";
								}

								i++;
							}
						} else {
							range = "0";
						}

						channel = channel + "{" + range + "}" + "\n";

					}
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return channel;
	}

	public IAttribute[] filtraAtributos(IAttribute[] filtra) {

		IAttribute[] retorno;

		int count = 0;
		for (int j = 0; j < filtra.length; j++) {
			if (filtra[j].getAssociation() == null && filtra[j].isChangeable()) {
				count++;
			}
		}
		retorno = new IAttribute[count];

		int add = 0;
		for (int i = 0; i < filtra.length; i++) {
			if (filtra[i].getAssociation() == null && filtra[i].isChangeable()) {
				retorno[add++] = filtra[i];
			}
		}

		return retorno;
	}

	public String getInputComponent() {

		String InputComponent = "";
		Declarations declaration = Declarations.getInstance();
		ArrayList<Port> ports = declaration.getPortas();
		ArrayList<Input_Output> inputoutput = declaration.getInputoutput();
		// lista de instancias
		HashSet<Instance> instances = declaration.getInstances();
		Iterator it = instances.iterator();
		HashSet<String> ports_comp;
		Iterator<String> it_str;

		while (it.hasNext()) {
			String temp = "";
			Instance instance_temp = (Instance) it.next();
			System.out.print("input_" + instance_temp.getName() + "= {|");

			String input_name = "input_" + instance_temp.getName();
			String output_name = "output_" + instance_temp.getName();
			Input_Output temp_i_o = new Input_Output(instance_temp.getType(), input_name, output_name);

			ports_comp = declaration.getPortComponent(instance_temp.getType());
			it_str = ports_comp.iterator();
			String str_temp = "";
			String str_optype = "";

			ArrayList<String> channels = new ArrayList<String>();

			while (it_str.hasNext()) {

				temp = (String) it_str.next();

				String in_out = temp + "." + declaration.getId(instance_temp.getName());

				channels.add(in_out);

				str_temp = str_temp + in_out + ".op";

				if (it_str.hasNext()) {
					str_temp = str_temp + ", ";

				}

			}
			// interface provide or required ?

			if (declaration.getIsProvReq(temp) == 1) {

				str_optype = declaration.getType(instance_temp.getName()) + "_O";

			} else if (declaration.getIsProvReq(temp) == 0) {

				str_optype = declaration.getType(instance_temp.getName()) + "_I";
			}

			temp_i_o.setChannels(channels);
			inputoutput.add(temp_i_o);

		}

		return InputComponent;
	}

//observar dependencia de c.firstConection

	public String getOutputComponent() {

		String outputComponent = "";
		Declarations declaration = Declarations.getInstance();
		ArrayList<Port> ports = declaration.getPortas();
		// lista de instancias
		HashSet<Instance> instances = declaration.getInstances();
		Iterator it = instances.iterator();
		HashSet<String> ports_comp;
		Iterator<String> it_str;

		while (it.hasNext()) {
			String temp = "";
			Instance instance_temp = (Instance) it.next();

			ports_comp = declaration.getPortComponent(instance_temp.getType());
			it_str = ports_comp.iterator();
			String str_temp = "";
			String str_optype = "";

			while (it_str.hasNext()) {
				temp = (String) it_str.next();
				// outputComponent = outputComponent + str_temp + temp+ "."+
				// declaration.getId(instance_temp.getName()) + ".op" ;
				str_temp = str_temp + temp + "." + declaration.getId(instance_temp.getName()) + ".op";
				if (it_str.hasNext()) {
					str_temp = str_temp + ", ";
					// outputComponent = outputComponent + str_temp;
				}

			}

			// interface provide or required ?

			System.out.print(str_temp + "|" + "op:");

			if (declaration.getIsProvReq(temp) == 1) {

				str_optype = declaration.getType(instance_temp.getName()) + "_I";

			} else if (declaration.getIsProvReq(temp) == 0) {

				str_optype = declaration.getType(instance_temp.getName()) + "_O";
			}

			System.out.println(str_optype + "|}");

			// lista de portas
		}

		return outputComponent;
	}

	/*
	 * public HashSet<String> getPortComponent(String componentName) {
	 * 
	 * HashSet<String> retorno = new HashSet<String>(); Declarations declaration =
	 * Declarations.getInstance(); ArrayList<Port> portas = declaration.getPortas();
	 * for (int i = 0; i < portas.size(); i++) {
	 * 
	 * if (portas.get(i).getOwner().equalsIgnoreCase(componentName)) {
	 * retorno.add(portas.get(i).getName()); } } return retorno; }
	 */

	public HashSet<String> getInstanceType() {
		HashSet<String> instanceType = new HashSet<String>();
		Declarations declaration = Declarations.getInstance();
		HashSet<Instance> instances = declaration.getInstances();
		Iterator<Instance> i = instances.iterator();
		while (i.hasNext()) {
			instanceType.add(((Instance) i.next()).getType());
		}
		return instanceType;

	}

	public HashSet<String> getInstancebyType(String type) {
		HashSet<String> instancebyType = new HashSet<String>();
		Declarations declaration = Declarations.getInstance();
		HashSet<Instance> instances = declaration.getInstances();
		Iterator<Instance> i = instances.iterator();
		while (i.hasNext()) {
			Instance temp = (Instance) i.next();
			if (temp.getType().equalsIgnoreCase(type)) {
				instancebyType.add(temp.getName());
				System.out.println(temp.getName());
			}
		}
		return instancebyType;

	}

	public static void main(String[] args) {
		try {
			CompositeView c = new CompositeView(
				//	"C:/Users/flavi/Documents/Doutorado_2022/PLUGIN/CSP/modelo.asta");
		USER_DIR + SEPARATOR + "CSP" + SEPARATOR + "modelo.asta");


			INamedElement[] foundElements = c.findComposite();
			ICompositeStructureDiagram compositeStructureDiagram;
			System.out.println("foundElements.................  " + foundElements.length);

			for (INamedElement element : foundElements) {
				compositeStructureDiagram = c.castCompositeStructureDiagaram(element);
				if (compositeStructureDiagram == null) {
					continue;
				}
				System.out.println("nome do diagrama   " + compositeStructureDiagram.getName());

			}

			INamedElement[] ele = c.findComposite();
			c.findPart();
			INamedElement[] foundElements3 = c.findClass();
			for (INamedElement element3 : foundElements3) {
				IClass temp = (IClass) element3;
				System.out.println("classe   " + temp.getName());
				if (temp.getStereotypes().length > 0)
					System.out.println("classe  este  " + temp.getStereotypes()[0]);
			}

			System.out.println(c.getChannelPort());
//	System.out.println(c.getChannelGetSet());

			INamedElement[] foundElements2 = null;
			foundElements2 = c.findConnector();
			for (INamedElement element2 : foundElements2) {
				IConnector temp = (IConnector) element2;

				System.out.println(c.getParts(temp));

			}

			c.MontaInstancia();

			// String interleave = c.interleaveProcess();
			// System.out.println("ProcessInterleave = " + interleave);
			String process = "ProcessInterleave";
			String buffer = "Buffer";
			INamedElement[] foundElements4 = null;

			foundElements4 = c.findConnector();

			// System.out.println(foundElements3.length);
			String processcomp = " ";
			if (foundElements4.length > 0) {
				processcomp = "processcomp";
			}

			for (INamedElement element3 : foundElements4) {
				IConnector temp = (IConnector) element3;

				process = processcomp;
				processcomp = processcomp + "_comp";

			}

			c.getInputComponent();
			c.getOutputComponent();
			c.getInstanceType();
			c.getInstancebyType("FORK");
			c.projectAccessor.close();
		} catch (Exception e) {

			System.out.println(e.toString());
			e.printStackTrace();

		}
	}

	/**
	 * 
	 * cria instancia zero para verificar se o processo do componente eh um i/o
	 * processs
	 * 
	 * @return
	 */

	public String montaInstanciaTeste() { // verificar os componentes basicos

		String str = "";
		Declarations declaration = Declarations.getInstance();
		HashSet<Instance> instancias = declaration.getInstances();

		try {
			INamedElement[] foundElements3 = findClass();
			for (INamedElement element3 : foundElements3) {
				IClass temp = (IClass) element3;

				// verifica o esteriotipo da classe \
				// se for basicComponent crio o range de id
				if (temp.getStereotypes().length > 0) {
					if (temp.getStereotypes()[0].equalsIgnoreCase("BasicComponentClass")) {

						str = str + temp.getName() + "0 =" + temp.getName() + "(0) \n";

						Instance nova = new Instance(temp.getName() + "0", temp.getName(), 0);

						// buscar as portas
						ArrayList<String> portas= new ArrayList<String>() ;
						if (temp.getPorts().length > 0) {
							
							for (int x = 0; x < temp.getPorts().length; x++) {
								
								String portName = temp.getPorts()[x].getName() + ".0";
								portas.add(portName);
								
							}


						
						
						}
                        nova.setPortas(portas);
						instancias.add(nova);
						//

					}

				}

			}

		} catch (Exception e) {

		}

		return str;

	}

	public String getContrato() {

		Declarations declaration = Declarations.getInstance();
		ArrayList<BasicComponent> basicComponent = declaration.getBasicComponent();
		String name = "";
		String retorno = "";

		for (BasicComponent temp : basicComponent) {
			name = temp.getName();

			String portas_st = "";
			String stringOp = "";
			ArrayList<Port> portas = declaration.getPortbyType(name);

			// operations
			ArrayList<Operation> op = declaration.getOperations();

			ArrayList<String> nomeporta = new ArrayList<String>();

			for (int i = 0; i < portas.size(); i++) {

				String nmporta = portas.get(i).getName() + ".id";
				nomeporta.add(nmporta);

				portas_st = portas_st + portas.get(i).getName();

				if (i < portas.size() - 1) {

					portas_st = portas_st + ".id" + ",";
				}

			}

			ArrayList<String> nomeOP = new ArrayList<String>();

			// filtrar as operacoes validas

			ArrayList<Operation> op_temp = new ArrayList<Operation>();

			for (int i = 0; i < op.size(); i = i + 1) {

				if (!op.get(i).getClass_esteriotipo().equalsIgnoreCase("tmp")) {

					op_temp.add(op.get(i));

				}
			}
			for (int i = 0; i < op_temp.size(); i = i + 1) {

				nomeOP.add(op_temp.get(i).getName_in());
				nomeOP.add(op_temp.get(i).getName_out());

				stringOp = stringOp + op_temp.get(i).getName_in() + " , " + op_temp.get(i).getName_out();

				if (i != (op_temp.size() - 1)) {
					stringOp = stringOp + " , ";
				}

			}

			// relacao portas e operacoes
			String re_st = "";

			for (int i = 0; i < portas.size(); i++) {

				re_st = re_st + "(" + portas.get(i).getName() + ".id" + " , {" + stringOp + "})";

				if (i < portas.size() - 1) {

					portas_st = portas_st + ".id" + ",";
				}

			}

			ArrayList<relac_op_port> relac = new ArrayList<relac_op_port>();

			// criar objeto contrato
			// tipo do componente
			// behaviour
			// portas
			// operacao
			// relacao operacao- porta

			String behaviour = name + "(id)";
			Contrato novo = new Contrato("Basico", name, behaviour, nomeporta, nomeOP, relac);

			declaration.addContrato(novo);

			retorno = retorno + " \n  --- CTR_" + name + "(id)= < " + name + "(id), {" + portas_st + "}, {" + stringOp
					+ "}, <" + re_st + " >";

			System.out.println(retorno);
		}

		declaration.setStrContrato(retorno);

		return retorno;

	}

}
