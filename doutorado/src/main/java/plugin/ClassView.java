package plugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IAttribute;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IClassDiagram;
import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.model.IModel;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.model.IPackage;
import com.change_vision.jude.api.inf.model.IParameter;
import com.change_vision.jude.api.inf.model.IPort;
import com.change_vision.jude.api.inf.project.ModelFinder;
import com.change_vision.jude.api.inf.project.ProjectAccessor;

public class ClassView { // {

	private ProjectAccessor projectAccessor;
	HashSet<IClass> classeList = new HashSet<IClass>();

	public ClassView(String uri) {
		try {
			projectAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
			projectAccessor.open(uri, true);
			r_operation();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public ClassView() {
		try {
			projectAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
			r_operation();
			createType();
			findPorts();

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// initComponents();
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @return
	 * @throws ProjectNotFoundException
	 */
	private INamedElement[] findSequence() throws ProjectNotFoundException {
		INamedElement[] foundElements = projectAccessor.findElements(new ModelFinder() {
			public boolean isTarget(INamedElement namedElement) {
				return namedElement instanceof IClassDiagram;
			}
		});
		return foundElements;
	}

	/**
	 * 
	 * @param project
	 * @return
	 * @throws ProjectNotFoundException
	 */

	public INamedElement[] findPackage(ProjectAccessor project) throws ProjectNotFoundException {
		INamedElement[] foundElements = project.findElements(new ModelFinder() {
			public boolean isTarget(INamedElement namedElement) {
				return namedElement instanceof IPackage;
			}
		});
		return foundElements;
	}

	/**
	 * 
	 * 
	 * @return
	 * @throws Exception
	 */

	public INamedElement[] findClass() throws Exception {
		INamedElement[] foundElements = projectAccessor.findElements(new ModelFinder() {
			public boolean isTarget(INamedElement namedElement) {
				return namedElement instanceof IClass;
			}
		});
		return foundElements;
	}

	/**
	 * @throws Exception
	 * 
	 * 
	 */
	public void findPorts() throws Exception {

		Declarations declaration = Declarations.getInstance();
		ArrayList<Port> ports = declaration.getPortas();
		boolean multiplicity = false;
		int nr_multiplicity = 1;

		try {
			IModel iCurrentProject = projectAccessor.getProject();
			INamedElement[] foundElements3 = findClass();
			for (INamedElement element : foundElements3) {
				IClass temp = (IClass) element;

				// verifica o esteriotipo da classe
				// se for basicComponent crio o range de id
				if (temp.getStereotypes().length > 0) {

					if (temp.getStereotypes()[0].equalsIgnoreCase("BasicComponentClass")) {

						if (temp.getPorts().length > 0) {

							for (int x = 0; x < temp.getPorts().length; x++) {

								// inserir no array de portas
								// inclusao da interface da porta
								if (temp.getPorts()[x].getMultiplicity() != null
										&& temp.getPorts()[x].getMultiplicity().length > 0
										&& temp.getPorts()[x].getMultiplicity()[0].getUpper() > 0) {

									multiplicity = true;
									nr_multiplicity = temp.getPorts()[x].getMultiplicity()[0].getUpper();

								}

								Port temp_portA = new Port(temp.getPorts()[x].getName(), temp.getName(),
										getTypeInterface(temp.getPorts()[x]), temp.getPorts()[x].getTypeModifier(),
										this.getNameInterface(temp.getPorts()[x]), multiplicity, nr_multiplicity);
								ports.add(temp_portA);

								multiplicity = false;
								nr_multiplicity = 1;

							}

						}
					}
				}

			}
		}

		catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProjectNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Cria um tipo BasicComponent ou HierarchicalComponent
	 * 
	 * 
	 */

	public void createType() {

		Declarations declaration = Declarations.getInstance();

		try {
			IModel iCurrentProject = projectAccessor.getProject();

			getAllClasses(iCurrentProject, classeList);

			Iterator<IClass> iclassList = classeList.iterator();

			while (iclassList.hasNext()) {
				IClass tempClass = (IClass) iclassList.next();

				// verificar se ha esteriotipo

				if (tempClass.getStereotypes().length > 0) {
					if (tempClass.getStereotypes()[0].equalsIgnoreCase("BasicComponentClass")) {

						if (!declaration.contemType(tempClass.getName())) {

							declaration.addComponentType(tempClass.getName());

						}
					}

					if (tempClass.getStereotypes()[0].equalsIgnoreCase("HiearchicalComponentClass")) {

						// criar um objeto hierchicalComponent
						HierarchicalComponent tempHC = new HierarchicalComponent(tempClass.getName());

						declaration.getHierarchicalComponent().add(tempHC);

					}
				}
			}

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProjectNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * 
	 * 
	 * recupera as operacoes na interfaces E insere no array de Operacoes
	 * 
	 */

	public void r_operation() {

		Declarations declaration = Declarations.getInstance();
		Operation op;

		try {

			// nome das classes
			IModel iCurrentProject = projectAccessor.getProject();
			getAllClasses(iCurrentProject, classeList);

			Iterator iteratorclass = classeList.iterator();

			while (iteratorclass.hasNext()) {

				IClass temp_class = (IClass) iteratorclass.next();
				// aqui eu poderia ver se eh interface

				if (temp_class.getOperations().length > 0) {
					for (int j = 0; j < temp_class.getOperations().length; j++) {

						String paramDirection = "";
						String paramType = "";
						String paramName = "";
						//
						IParameter[] pam = temp_class.getOperations()[j].getParameters();
						if (pam.length > 0) {

							paramDirection = pam[0].getDirection();
							paramType = pam[0].getType().getName();
							paramName = pam[0].getName();

						}

						String[] esteriotipo = temp_class.getOperations()[j].getStereotypes();
						String esteriotipo_str = "";

						if (esteriotipo.length > 0) {

							esteriotipo_str = esteriotipo[0];

						}

						op = new Operation(temp_class.getOperations()[j].getName(), temp_class.getName(),
								temp_class.getStereotypes()[0].toString(),
								temp_class.getOperations()[j].getReturnTypeExpression(),
								temp_class.getOperations()[j].getTypeModifier(), paramDirection, paramType,
								esteriotipo_str, paramName);

						declaration.addOperation(op);
					}
				}
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/*
	 * 
	 * retorna apenas as operacoes com sinais
	 * 
	 */

	public ArrayList<Operation> _signal(ArrayList<Operation> op) {

		ArrayList<Operation> retorno = new ArrayList<Operation>();

		ArrayList<Operation> temp = op;
		for (int i = 0; i < temp.size(); i++) {

			if (temp.get(i).getModifier() != null) {

				if (temp.get(i).getEsteriotipo().trim().equalsIgnoreCase("async")) {
					retorno.add(temp.get(i));

				}
			}
		}

		return retorno;
	}

	/*
	 * 
	 * retorna apenas as operacoes NAO sinais
	 * 
	 */

	public ArrayList<Operation> _N_signal(ArrayList<Operation> op) {

		ArrayList<Operation> retorno = new ArrayList<Operation>();

		ArrayList<Operation> temp = op;
		for (int i = 0; i < temp.size(); i++) {

			if (temp.get(i).getModifier() != null) {

				if (!temp.get(i).getEsteriotipo().trim().equalsIgnoreCase("async")) {
					retorno.add(temp.get(i));

				}
			}
		}

		return retorno;
	}

	/*
	 * 
	 * Operacoes sem esteriotipo e nao eh env
	 * 
	 */

	public String to_stringOp() {
		Declarations declaration = Declarations.getInstance();
		String stringOp = " ";
		ArrayList<Operation> op = declaration.getOperations(); // recupera as ops

		// as operacoes idependente de ser env ou nao

		if (op.size() > 0) {
			stringOp = "datatype operation = ";
		}
		// fitra operacoes sem modificador env -----

		ArrayList<Operation> temp2 = _N_signal(op);

		for (int i = 0; i < temp2.size(); i = i + 1) {

			// verificar se ha parametro

			if (!temp2.get(i).getDirection().isEmpty()) {

				String direction = temp2.get(i).getDirection();

				// se eh in direction
				if (direction.equalsIgnoreCase("in")) {

					stringOp = stringOp + temp2.get(i).getName_in() + "." + temp2.get(i).getParamName() + " | "
							+ temp2.get(i).getName_out();

				}

				// se eh out direction
				if (direction.equalsIgnoreCase("out")) {

					stringOp = stringOp + temp2.get(i).getName_in() + " | " + temp2.get(i).getName_out() + "."
							+ temp2.get(i).getParamName();

				}

			}

			else {

				stringOp = stringOp + temp2.get(i).getName_in() + " | " + temp2.get(i).getName_out();

			}

			if (i != (temp2.size() - 1)) {
				stringOp = stringOp + " | ";
			}

		}

		// -- sinais

		ArrayList<Operation> temp3 = _signal(op);

		// se ha sinais

		String tempSignal = to_stringOpEnv(temp3);

		if (temp3.size() > 0 && tempSignal.trim().length() > 0) {

			stringOp = stringOp + "|" + tempSignal;

		}

		// sinais

		return stringOp;
	}

	public String to_stringOpEnv(ArrayList<Operation> op) {

		// rotina apenas retorna sinais e nao operations do ambiente

		String stringOp = "";
		String pre = " ";
		// ArrayList<Operation> op = declaration.getOperations();

		ArrayList<Operation> temp; // = this.op_env(op);

		temp = op;
		// fitra operacoes sem modificador env

		for (int i = 0; i < temp.size(); i = i + 1) {

			// se eh signal

			// verifica se tem o esteriotipo async

			if (temp.get(i).getEsteriotipo().equalsIgnoreCase("async")) {

				// verificar se ha parametro

				if (!temp.get(i).getDirection().isEmpty()) {

					String direction = temp.get(i).getDirection();

					// se eh in direction
					if (direction.equalsIgnoreCase("in")) {

						stringOp = stringOp + temp.get(i).getName() + "." + temp.get(i).getParamName();

					}

					// se eh out direction
					if (direction.equalsIgnoreCase("out")) {

						stringOp = stringOp + temp.get(i).getName() + "." + temp.get(i).getParamName();

					}

				} else { // caso a direcao esteja vazia

					stringOp = stringOp + temp.get(i).getName();
				}

				if (i != (temp.size() - 1)) {
					stringOp = stringOp + " | ";
				}

			}

		}

		if (stringOp.length() > 0) {

			stringOp = pre + stringOp;
		}

		return stringOp;
	}

	// retorna a lista de operacoes que nao sao de ambiente
	public ArrayList<Operation> op_nao_env(ArrayList<Operation> op) {

		ArrayList<Operation> retorno = new ArrayList<Operation>();

		ArrayList<Operation> temp = op;
		for (int i = 0; i < temp.size(); i++) {

			if (temp.get(i).getModifier() != null) {

				if (!temp.get(i).getModifier().trim().equalsIgnoreCase("env")) {
					retorno.add(temp.get(i));

				}
			}
		}

		return retorno;
	}

	// retorna a lista de operacoes que sao de ambiente

	public ArrayList<Operation> op_env(ArrayList<Operation> op) {

		ArrayList<Operation> retorno = new ArrayList<Operation>();

		ArrayList<Operation> temp = op;
		for (int i = 0; i < temp.size(); i++) {

			if (temp.get(i).getModifier() != null) {

				if (temp.get(i).getModifier().trim().equalsIgnoreCase("env")) {
					retorno.add(temp.get(i));

				}

			}
		}

		return retorno;
	}

//----------------------------------------------------
//formatar os canais de operacao para cada classe
//so fazer se a classe tiver pelo menos uma opeacao

//29/05 ajuste para 

	public String to_StringOpI() {
		String stringI = "";

		try {
			Declarations declaration = Declarations.getInstance();
			ArrayList<Operation> classOperation;
			HashSet<Operation> interfaceOperation;

			// nome das classes
			IModel iCurrentProject = projectAccessor.getProject();
			getAllClasses(iCurrentProject, classeList);
			String texto = "";

			Iterator iclassList = classeList.iterator();

			while (iclassList.hasNext()) {
				IClass tempClass = (IClass) iclassList.next();
				interfaceOperation = new HashSet<Operation>();
				classOperation = declaration.getClassOperation(tempClass.getName());

				// recuperar as operacoes de suas interfaces

				if (tempClass.getPorts().length > 0) {

					// retirar as portas env aqui

					IPort[] temp_port = tempClass.getPorts();

					for (int k = 0; k < temp_port.length; k++) {
						// required
						if (getTypeInterface(temp_port[k]) == 1) {

							IClass interfaces[] = temp_port[k].getRequiredInterfaces();

							for (int m = 0; m < interfaces.length; m++) {
								ArrayList<Operation> interfaceOp = declaration
										.getClassOperation(interfaces[m].getName());

								for (int n = 0; n < interfaceOp.size(); n++) {
									interfaceOperation.add(interfaceOp.get(n));
								}
							}
						}
						if (getTypeInterface(temp_port[k]) == 0) {
							// provide
							IClass interfaces[] = temp_port[k].getProvidedInterfaces();
							for (int m = 0; m < interfaces.length; m++) {
								ArrayList<Operation> interfaceOp = declaration
										.getClassOperation(interfaces[m].getName());

								for (int n = 0; n < interfaceOp.size(); n++) {
									interfaceOperation.add(interfaceOp.get(n));
								}
							}
						}
					}
					Iterator it = interfaceOperation.iterator();
					while (it.hasNext()) {
						classOperation.add((Operation) it.next());
					}

					//////////////////////////////////////

					if (classOperation.size() > 0) {
						ArrayList<Operation> op = classOperation;

						if (!(tempClass.getStereotypes()[0].equalsIgnoreCase("interface"))) {
							texto = tempClass.getName();
							stringI = stringI + "subtype " + texto + "_I = ";
							ArrayList<String> ops = new ArrayList<String>();

							for (int j = 0; j < op.size(); j = j + 1) {

								if (!op.get(j).getEsteriotipo().trim().equalsIgnoreCase("async")) {

									// verificar se ha direcao
									if (!op.get(j).getDirection().isEmpty()) {

										String direction = op.get(j).getDirection();

										// nao eh sync

										if (direction.equalsIgnoreCase("in")) {

											ops.add(op.get(j).getName_in() + "." + op.get(j).getParamName());
										}

										else {

											ops.add(op.get(j).getName_in());

										}

									}

									else {

										ops.add(op.get(j).getName_in());

									}

								}

								else {

									// verificar se ha direcao
									if (!op.get(j).getDirection().isEmpty()) {

										String direction = op.get(j).getDirection();

										// nao eh sync

										if (direction.equalsIgnoreCase("in")) {

											ops.add(op.get(j).getName() + "." + op.get(j).getParamName());
										}

									}

									else {

										ops.add(op.get(j).getName());

									}

								}

							}

							for (int k = 0; k < ops.size(); k++) {

								stringI = stringI + ops.get(k);

								if (k != (ops.size() - 1)) {
									stringI = stringI + " | ";
								}

							}

							stringI = stringI + "\n";
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return stringI;
	}

	// --- retorna apenas portas nao ambiente

	/*
	 * public IPort[] port_n_env(IPort[] p) {
	 * 
	 * int j = 0; int i;
	 * 
	 * for (i = 0; i < p.length; i++) {
	 * 
	 * if (!p[i].getTypeModifier().equalsIgnoreCase("env")) {
	 * 
	 * j++; // retorno[j] = p[i]; } }
	 * 
	 * IPort[] retorno = new IPort[j];
	 * 
	 * int l = 0; for (i = 0; i < p.length; i++) {
	 * 
	 * if (!p[i].getTypeModifier().equalsIgnoreCase("env")) {
	 * 
	 * retorno[l] = p[i]; l++; } }
	 * 
	 * return retorno;
	 * 
	 * }
	 */

	// ----------------------------------------------------

	public String to_StringOpO() {
		String stringO = "";
		boolean sep = false;
		try {
			Declarations declaration = Declarations.getInstance();
			ArrayList<Operation> classOperation;
			HashSet<Operation> interfaceOperation;

			String texto = "";
			// nome das classes
			IModel iCurrentProject = projectAccessor.getProject();
			getAllClasses(iCurrentProject, classeList);

			Iterator iclassList = classeList.iterator();

			while (iclassList.hasNext()) {
				IClass tempClass = (IClass) iclassList.next();
				// System.out.println(tempClass.getName());
				interfaceOperation = new HashSet<Operation>();
				classOperation = declaration.getClassOperation(tempClass.getName());

				IPort[] temp_port = tempClass.getPorts();

				if (temp_port.length > 0) {

					for (int k = 0; k < temp_port.length; k++) {

						if (getTypeInterface(temp_port[k]) == 1) {

							IClass interfaces[] = temp_port[k].getRequiredInterfaces();
							for (int m = 0; m < interfaces.length; m++) {
								ArrayList<Operation> interfaceOp = declaration
										.getClassOperation(interfaces[m].getName());

								for (int n = 0; n < interfaceOp.size(); n++) {
									interfaceOperation.add(interfaceOp.get(n));
								}
								// System.out.println(interfaceOperation.size());
							}
						}

						if (getTypeInterface(temp_port[k]) == 0) {

							IClass interfaces[] = temp_port[k].getProvidedInterfaces();
							for (int m = 0; m < interfaces.length; m++) {
								ArrayList<Operation> interfaceOp = declaration
										.getClassOperation(interfaces[m].getName());

								for (int n = 0; n < interfaceOp.size(); n++) {
									interfaceOperation.add(interfaceOp.get(n));
								}
							}
						}
					}
					Iterator it = interfaceOperation.iterator();
					while (it.hasNext()) {
						classOperation.add((Operation) it.next());
						// System.out.println("while" + classOperation.size());
					}

					if (classOperation.size() > 0) {
						ArrayList<Operation> op = classOperation;

						if (!(tempClass.getStereotypes()[0].equalsIgnoreCase("interface"))) {
							texto = tempClass.getName();
							stringO = stringO + "subtype " + texto + "_O = ";
							ArrayList<String> ops = new ArrayList<String>();

							for (int j = 0; j < op.size(); j = j + 1) {

								if (!op.get(j).getEsteriotipo().trim().equalsIgnoreCase("async")) {

									// verificar se ha direcao
									if (!op.get(j).getDirection().isEmpty()) {

										String direction = op.get(j).getDirection();

										if (direction.equalsIgnoreCase("out")) {

											ops.add(op.get(j).getName_out() + "." + op.get(j).getParamName());

										} else {

											ops.add(op.get(j).getName_out());

										}

									} // fim ha direcao

									else { // sem direcao

										ops.add(op.get(j).getName_out());

									}

								} else {

									// verificar se ha direcao
									if (!op.get(j).getDirection().isEmpty()) {

										String direction = op.get(j).getDirection();

										if (direction.equalsIgnoreCase("out")) {

											ops.add(op.get(j).getName() + "." + op.get(j).getParamName());
										}

									}

									// fim ha direcao

									else { // sem direcao

										ops.add(op.get(j).getName());
									}

								}

							}

							for (int k = 0; k < ops.size(); k++) {

								stringO = stringO + ops.get(k);

								if (k != (ops.size() - 1)) {
									stringO = stringO + " | ";
								}

							}

							stringO = stringO + "\n";
						}
					}

				}
			}
		}

		catch (Exception e) {
		}

		return stringO;

	}

	protected void getAllClasses(INamedElement element, HashSet<IClass> classList)
			throws ClassNotFoundException, ProjectNotFoundException {
		if (element instanceof IPackage) {
			for (INamedElement ownedNamedElement : ((IPackage) element).getOwnedElements()) {
				getAllClasses(ownedNamedElement, classList);
			}
		} else if (element instanceof IClass) {
			classList.add((IClass) element);
			for (IClass nestedClasses : ((IClass) element).getNestedClasses()) {
				getAllClasses(nestedClasses, classList);
			}
		}
	}

	public int getTypeInterface(IPort port) {

		int retorno = -1;
		if (port.getRequiredInterfaces().length > 0) {
			retorno = 1;
		} else if (port.getProvidedInterfaces().length > 0) {
			retorno = 0;
		}
		return retorno;
	}

// hierarchical component

	public void hierarchicalComponent() {

		// varre as classes

	}

	/**
	 * 
	 * Define os channels gets e sets
	 * 
	 * @return
	 */

	public String getChannelGetSet() {

		String channel = new String();
		Declarations declaration = Declarations.getInstance();
		ArrayList<Attribute> temp_att;

		try {
			INamedElement[] foundElements3 = findClass();
			temp_att = declaration.getAttributes();

			for (INamedElement element3 : foundElements3) {
				IClass temp = (IClass) element3;

				if (temp.getAttributes().length > 0) {
					for (int i = 0; i < temp.getAttributes().length; i++) {
						if (temp.getAttributes()[i].getAssociation() == null
								&& temp.getAttributes()[i].isChangeable()) { // atributos readonly nao tem get e set

							// adiciona no array de variaveis locais e globais

							Attribute att = new Attribute(temp.getAttributes()[i].getName(),
									temp.getAttributes()[i].getInitialValue(), temp.getName());

							temp_att.add(att);

							channel = channel + "channel " + "get_" + temp.getAttributes()[i] + " : id_"
									+ temp.getName() + "." + "type_" + temp.getAttributes()[i] + "\n";
							channel = channel + "channel " + "set_" + temp.getAttributes()[i] + " : id_"
									+ temp.getName() + "." + "type_" + temp.getAttributes()[i] + "\n";

						}
					}
				}
			}

		} catch (Exception e) {

			e.printStackTrace();

		}

		return channel;
	}

	/*
	 * 
	 * retorna variaveis q nao sao de tipos
	 * 
	 */

	public String getVariaveis() {

		String var = "";

		try {
			INamedElement[] foundElements3 = findClass();

			for (INamedElement element3 : foundElements3) {
				IClass temp = (IClass) element3;

				if (temp.getAttributes().length > 0) {
					
					for (int i = 0; i < temp.getAttributes().length; i++) {
						
						if (temp.getAttributes()[i].getAssociation() == null) {

							// verificar se eh uma constante

							if (temp.getAttributes()[i].isChangeable()) {

								if (temp.getAttributes()[i].getInitialValue().length() > 0) {

									// verifica a necessidade de criar datatype
									// se os valores nao forem inteiros
									// {1..2}
									boolean isInt = false;

									String split_temp[] = temp.getAttributes()[i].getInitialValue()
											.replaceAll("[{}]", "").split("[,.]");
									
									isInt = split_temp[0].matches ("^([+-]?\\d+)$");;
								//	char arrrayCharTemp[] = split_temp[0].toCharArray();
								//	isInt = Character.isDigit(arrrayCharTemp[0]);
									String varData = ""; 

									if (!isInt) {

										varData = "datatype dt_" + temp.getAttributes()[i] +  " = ";
										
										String varTemp = "";

										for (int j = 0; j < split_temp.length; j++) {
											
											

											varTemp =  varTemp + split_temp[j];
											
											if( split_temp.length > (j+1)) { 
											
												varTemp = varTemp   + "|";
												
											}

										}

										var = var + varData + varTemp + "\n";

										var = var + "type_" + temp.getAttributes()[i];

										var = var + "=" + temp.getAttributes()[i].getInitialValue() + "\n";

									}

									else {

										var = var + "=" + temp.getAttributes()[i].getInitialValue() + "\n";

									}

								//	var = var + "=" + temp.getAttributes()[i].getInitialValue() + "\n";
									
								} else {

									var = var + "=" + temp.getAttributes()[i].getType().getName() + " \n";
								}
							} else {

								var = var + temp.getAttributes()[i];

								if (temp.getAttributes()[i].getInitialValue().length() > 0) {

									var = var + "=" + temp.getAttributes()[i].getInitialValue() + "\n";
								} else {

									var = var + "=" + temp.getAttributes()[i].getType().getName() + " \n";
								}

							}

						}
					}
				}

			}
		} catch (Exception e) {

			e.printStackTrace();

		}

		return var;
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

}
