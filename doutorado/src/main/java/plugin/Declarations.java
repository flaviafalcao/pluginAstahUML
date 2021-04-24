package plugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;

import com.change_vision.jude.api.inf.model.IConnector;
import com.change_vision.jude.api.inf.model.IStateMachine;
import com.change_vision.jude.api.inf.presentation.ILinkPresentation;

public class Declarations {

	// identificador de components

	private int num;
	private int memory_num;
	private int trs_id;
	private ArrayList<Name_id> nameid;
	private ArrayList<Port> portas;
	private ArrayList<Operation> operations;
	private ArrayList<Signal> signal;
	private ArrayList<Attribute> attributes;
	private ArrayList<PartType> parts;
	private HashSet<Instance> instances;
	private ArrayList<BasicComponent> basicComponent;
	private ArrayList<Input_Output> inputoutput;
	private ArrayList<SetSinc> setsinc;
	private ArrayList<ComponentType> componentType;
	private ArrayList<StateMachine> stateMachine;
	private ArrayList<AssertionConection> assertionConection;
	private ArrayList<ModeloAssertion> assertions;
	private ArrayList<Memory> memories;
	private ArrayList<MachineClone> machineclones;
	private ArrayList<ExtendTransition> extendTransition;
	private ArrayList<ILinkPresentation> array_counter;
	private ArrayList<Contrato> contratos;
	private ArrayList<String> nameComponents;
	private ArrayList<HierarchicalComponent> hierarchicalComponent;
	private String strContrato;

	//
	private ArrayList<String> msgErro;

	private static Declarations instance;

	public Declarations() {

		this.setNum(0);
		this.setMemory_num(1);
		this.setTrs_id(1);
		this.nameid = new ArrayList<Name_id>();
		this.portas = new ArrayList<Port>();
		this.operations = new ArrayList<Operation>();
		this.signal = new ArrayList<Signal>();
		this.parts = new ArrayList<PartType>();
		this.instances = new HashSet<Instance>();
		this.basicComponent = new ArrayList<BasicComponent>();
		this.inputoutput = new ArrayList<Input_Output>();
		this.setsinc = new ArrayList<SetSinc>();
		this.componentType = new ArrayList<ComponentType>();
		this.stateMachine = new ArrayList<StateMachine>();
		this.setAssertionConection(new ArrayList<AssertionConection>());
		this.assertions = new ArrayList<ModeloAssertion>();
		this.memories = new ArrayList<Memory>();
		this.attributes = new ArrayList<Attribute>();
		this.setMachineclones(new ArrayList<MachineClone>());
		this.setExtendTransition(new ArrayList<ExtendTransition>());
		this.setArray_counter(new ArrayList<ILinkPresentation>());
		this.contratos = new ArrayList<Contrato>();
		this.setNameComponents(new ArrayList<String>());
		this.setMsgErro(new ArrayList<String>());
		this.hierarchicalComponent = new ArrayList<HierarchicalComponent>();
		this.setStrContrato("");

	}

	public synchronized void destroy() {

		this.nameid = null;
		this.portas = null;
		this.operations = null;
		this.signal = null;
		this.parts = null;
		this.instances = null;
		this.basicComponent = null;
		this.inputoutput = null;
		this.setsinc = null;
		this.componentType = null;
		this.stateMachine = null;
		this.assertionConection = null;
		this.assertions = null;
		this.setMachineclones(null);
		this.setNameComponents(null);
		this.setMsgErro(null);
		this.hierarchicalComponent = null;
		this.setStrContrato("");

		this.nameid = new ArrayList<Name_id>();
		this.portas = new ArrayList<Port>();
		this.operations = new ArrayList<Operation>();
		this.signal = new ArrayList<Signal>();
		this.parts = new ArrayList<PartType>();
		this.instances = new HashSet<Instance>();
		this.basicComponent = new ArrayList<BasicComponent>();
		this.inputoutput = new ArrayList<Input_Output>();
		this.setsinc = new ArrayList<SetSinc>();
		this.componentType = new ArrayList<ComponentType>();
		this.stateMachine = new ArrayList<StateMachine>();
		this.setAssertionConection(new ArrayList<AssertionConection>());
		this.assertions = new ArrayList<ModeloAssertion>();

		this.memories = new ArrayList<Memory>();
		this.setMachineclones(new ArrayList<MachineClone>());
		this.contratos = new ArrayList<Contrato>();
		this.setMsgErro(new ArrayList<String>());
		this.hierarchicalComponent = new ArrayList<HierarchicalComponent>();

	}

	// singleton
	public static synchronized Declarations getInstance() {
		if (instance == null) {
			instance = new Declarations();
		}
		return instance;

	}

	public ArrayList<Name_id> getNameid() {
		return nameid;
	}

	public void setNameid(ArrayList<Name_id> nameid) {
		this.nameid = nameid;
	}

	public ArrayList<Port> getPortas() {
		return portas;
	}

	public void setPortas(ArrayList<Port> portas) {
		this.portas = portas;
	}

	public ArrayList<Operation> getOperations() {
		return operations;
	}

	public void setOperations(ArrayList<Operation> operations) {
		this.operations = operations;
	}

	public ArrayList<Signal> getSignal() {
		return signal;
	}

	public void setSignal(ArrayList<Signal> signal) {
		this.signal = signal;
	}

	public void addOperation(Operation op) {

		this.operations.add(op);
	}

	public void addPort(Port port) {

		if (!portas.contains(port)) {
			System.out.println("add ->" + port.getName());
			this.portas.add(port);
		} else {
			System.out.println(" nao add ->");
		}
	}

	public ArrayList<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(ArrayList<Attribute> attributes) {
		this.attributes = attributes;
	}

	public ArrayList<PartType> getPartes() {
		return parts;
	}

	public void setPartes(ArrayList<PartType> parts) {
		this.parts = parts;
	}

	public String getType(String name) {
		String retorno = "";
		String[] split1;
		for (int i = 0; i < parts.size(); i++) {

			if (parts.get(i).getName().equals(name)) {
				retorno = parts.get(i).getType();
				split1 = retorno.split("::");
				int j = split1.length;
				retorno = split1[j - 1];

				return retorno;
			}
		}
		return retorno;
	}

	public int getId(String name) {
		Iterator i = this.instances.iterator();
		Instance temp;
		int retorno = 0;
		while (i.hasNext()) {

			temp = (Instance) i.next();

			if (temp.getName().equals(name)) {

				retorno = temp.getNum_id();
				return retorno;
			}
		}
		return retorno;

	}

	// retorna o tipo de uma porta
	public String getPortType(String name) {
		Iterator i = this.instances.iterator();
		Instance temp;
		String retorno = "";
		while (i.hasNext()) {

			temp = (Instance) i.next();

			if (temp.getName().equals(name)) {

				retorno = temp.getType();
				return retorno;
			}
		}
		return retorno;

	}

	// metodo que me diz se uma porta eh provided ou required

	public int getIsProvReq(String portName) {
		int retorno = -1;

		for (int i = 0; i < this.portas.size(); i++) {

			if (this.portas.get(i).getName().equalsIgnoreCase(portName)) {
				if (this.portas.get(i).getReq_prov() != (-1)) {
					return this.portas.get(i).getReq_prov();
				}
			}

		}

		return retorno;

	}

	// retorna o tipo componente de uma porta

	public String getTypeOfPort(String portName) {
		String type = "";

		for (int i = 0; i < this.portas.size(); i++) {

			if (this.portas.get(i).getName().equalsIgnoreCase(portName)
					&& Objects.nonNull(this.portas.get(i).getInterfaceName())) {

				type = this.portas.get(i).getOwner();
				break;
			}
		}
		return type;

	}

	// retorna as portas de um tipo de componente
	public ArrayList<Port> getPortbyType(String typeName) {
		ArrayList<Port> retorno = new ArrayList<Port>();

		for (int i = 0; i < this.portas.size(); i++) {

			if (this.portas.get(i).getOwner().equalsIgnoreCase(typeName)) {

				retorno.add(portas.get(i));
			}
		}

		return retorno;

	}

	/**
	 * Retorna o Objeto conforme seu nome
	 * 
	 * @param name
	 * @return
	 */

	public Port getPort(String name) {

		Port retorno = null;

		for (int i = 0; i < this.portas.size(); i++) {

			if (this.portas.get(i).getName().equalsIgnoreCase(name)) {

				retorno = portas.get(i);
				break;
			}
		}

		return retorno;

	}

	// atualiza o indicativo que a porta esta associada a uma trigger de uma
	// determinada porta

	public void setIndicativoTrigger(String name) {

		for (int i = 0; i < this.portas.size(); i++) {

			if (this.portas.get(i).getName().equalsIgnoreCase(name)) {

				this.portas.get(i).setGuard(true);
				;

			}
		}
	}

	public HashSet<Instance> getInstances() {
		return instances;
	}

	public void setInstances(HashSet<Instance> instances) {
		this.instances = instances;
	}

	public ArrayList<BasicComponent> getBasicComponent() {
		return basicComponent;
	}

	public void setBasicComponent(ArrayList<BasicComponent> basicComponent) {
		this.basicComponent = basicComponent;
	}

	/*
	 * 
	 * Retorna as Operacoes de um componente
	 * 
	 */

	public ArrayList<Operation> getClassOperation(String class_name) {

		ArrayList<Operation> classOperation = new ArrayList<Operation>();
		ArrayList<Operation> allClassOperation;

		allClassOperation = this.getOperations();

		for (int i = 0; i < allClassOperation.size(); i++) {
			if (allClassOperation.get(i).getClass_name().equalsIgnoreCase(class_name)) {
				classOperation.add(allClassOperation.get(i));
			}

		}
		return classOperation;
	}

	public ArrayList<Input_Output> getInputoutput() {
		return inputoutput;
	}

	public void setInputoutput(ArrayList<Input_Output> inputoutput) {
		this.inputoutput = inputoutput;
	}

	// inputs por tipo
	public ArrayList<String> getInput(String name) {
		ArrayList<String> inputs = new ArrayList<String>();

		for (int i = 0; i < this.inputoutput.size(); i++) {
			if (this.inputoutput.get(i).getInstance_name().equalsIgnoreCase(name)) {
				inputs.add(this.inputoutput.get(i).getInputs_name());
			}
		}
		return inputs;

	}

	public ArrayList<String> getInputsSemTeste(String name) {

		ArrayList<String> inputs = new ArrayList<String>();

		for (int i = 0; i < this.inputoutput.size(); i++) {

			if (this.inputoutput.get(i).getInstance_name().equalsIgnoreCase(name)
					&& this.getId(this.inputoutput.get(i).getInstance()) != 0) // e o instance nao for de id 0

			{
				inputs.add(this.inputoutput.get(i).getInputs_name());
			}
		}
		return inputs;

	}

	public ArrayList<String> getOutput(String name) {

		ArrayList<String> outputs = new ArrayList<String>();

		for (int i = 0; i < this.inputoutput.size(); i++) {
			if (this.inputoutput.get(i).getInstance_name().equalsIgnoreCase(name)) {
				outputs.add(this.inputoutput.get(i).getOutputs_name());
			}
		}
		return outputs;

	}

	public ArrayList<String> getOutputSemTeste(String name) {

		ArrayList<String> outputs = new ArrayList<String>();

		for (int i = 0; i < this.inputoutput.size(); i++) {
			if (this.inputoutput.get(i).getInstance_name().equalsIgnoreCase(name)
					&& this.getId(this.inputoutput.get(i).getInstance()) != 0) // e o instance nao for de id 0

			{
				outputs.add(this.inputoutput.get(i).getOutputs_name());
			}
		}
		return outputs;

	}

	// canais por instancia

	public ArrayList<String> getChannelsInstance(String name) {

		ArrayList<String> retorno = new ArrayList<String>();
		for (int i = 0; i < this.inputoutput.size(); i++) {
			if (this.inputoutput.get(i).getInstance().equalsIgnoreCase(name)) {
				retorno = this.inputoutput.get(i).getChannels();
				break;
			}
		}

		return retorno;

	}

	public ArrayList<SetSinc> getSetsinc() {
		return setsinc;
	}

	public void setSetsinc(ArrayList<SetSinc> setsinc) {
		this.setsinc = setsinc;
	}

	public ArrayList<ComponentType> getComponentType() {

		return componentType;
	}

	public void setComponentType(ArrayList<ComponentType> componentType) {
		this.componentType = componentType;
	}

	// retorna a quantidade de um tipo
	public int getCountComponentType(String name) {

		int retorno = 0;
		for (int i = 0; i < componentType.size(); i++) {
			if (componentType.get(i).getType().equalsIgnoreCase(name)) {

				retorno = componentType.get(i).getCount();
				break;
			}
		}

		return retorno;
	}

	// atualiza o numero de instancias daquele tipo
	// ou adiciona um novo tipo
	public int countComponentType(String name) {
		int retorno = 1;

		if (componentType.size() == 0 || !contemType(name)) {

			ComponentType temp = new ComponentType(name, 1);
			this.componentType.add(temp);

		} else
			for (int i = 0; i < componentType.size(); i++) {

				if (componentType.get(i).getType().equalsIgnoreCase(name)) {
					retorno = componentType.get(i).getCount() + 1;
					componentType.get(i).setCount(retorno);
				}
			}

		return retorno;
	}

	// metodo que apenas adiciona um componentType sem criar contados

	public void addComponentType(String name) {

		ComponentType temp = new ComponentType(name, 0);
		this.componentType.add(temp);
	}

	public boolean contemType(String name) {

		boolean retorno = false;

		for (int i = 0; i < componentType.size(); i++) {

			if (componentType.get(i).getType().equalsIgnoreCase(name)) {
				retorno = true;
			}

		}

		return retorno;

	}

	// retorna as instanacia do tipo passado como parametro

	public ArrayList<String> getInstancebyType(String tipo) {

		ArrayList<String> instancias = new ArrayList<String>();

		Iterator<Instance> i = this.instances.iterator();
		Instance temp;

		while (i.hasNext()) {
			temp = (Instance) i.next();

			if (temp.getType().equals(tipo)) {

				instancias.add(temp.getName());

			}

		}
		return instancias;

	}

	// retornar nome da instancia por tipo e id

	public String getInstanceNameByTypeId(String type, int id) {
		String instanceName = "";
		Iterator iterator = instances.iterator();

		while (iterator.hasNext()) {

			Instance i = (Instance) iterator.next();

			if (i.getNum_id() == id && i.getType().equalsIgnoreCase(type)) {

				instanceName = i.getName();
				break;
			}

		}

		return instanceName;

	}

	public ArrayList<StateMachine> getStateMachine() {
		return stateMachine;
	}

	// retorna a maquina de estados de determinado componente

	public String getSTMbytype(String str) {
		String stm = "";
		ArrayList<StateMachine> machine = this.getStateMachine();
		for (StateMachine tmp : machine) {

			if (tmp.getComponent_name().equalsIgnoreCase(str)) {

				stm = tmp.getStm_str();
				break;
			}

		}

		return stm;
	}

	// retorno a IConnection do aquivo passado como parametro

	public IConnector getIConnector(String file) {

		IConnector con = null;

		ArrayList<AssertionConection> assertion = this.getAssertionConection();

		for (AssertionConection tmp : assertion) {

			if (tmp.getFile().equalsIgnoreCase(file)) {

				con = tmp.getConn();
				break;
			}
		}

		return con;

	}

	public ArrayList<AssertionConection> getAssertionConection() {
		return assertionConection;
	}

	public void setAssertionConection(ArrayList<AssertionConection> assertionConection) {
		this.assertionConection = assertionConection;
	}

	public ArrayList<ModeloAssertion> getAssertions() {
		return assertions;
	}

	public void setAssertions(ArrayList<ModeloAssertion> assertions) {
		this.assertions = assertions;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	// retornar assertions de um modelo

	public ArrayList<String> assertionsModel(String nameModel) {

		ArrayList<String> retorno = null;

		ArrayList<ModeloAssertion> assertion = this.getAssertions();

		for (ModeloAssertion tmp : assertion) {

			if (tmp.getFileModel().equalsIgnoreCase(nameModel)) {

				retorno = tmp.getAssertivas();
				break;
			}
		}

		return retorno;
	}

	public void addAssertionModel(ModeloAssertion modelcsp) {

		this.assertions.add(modelcsp);
	}

	public int getMemory_num() {
		return memory_num;
	}

	public void setMemory_num(int memory_num) {
		this.memory_num = memory_num;
	}

	public void incrementaNumMemory() {
		int num;

		num = this.getMemory_num() + 1;
		this.setMemory_num(num);

	}

	public ArrayList<Memory> getMemories() {
		return memories;
	}

	public void setMemories(ArrayList<Memory> memories) {
		this.memories = memories;
	}

	// setar a trigger de uma memory

	public void setMemoryTrigger(int id_str, String[] trigger) {

		ArrayList<Memory> temp = this.getMemories();

		for (int i = 0; i < temp.size(); i++) {

			if (temp.get(i).getId_trs() == id_str) {
				temp.get(i).setTrigger(trigger);
			}
		}

	}

	// setar a trigger de uma memory

	public void setMemoryExp(int id_str, String exp) {

		ArrayList<Memory> temp = this.getMemories();

		for (int i = 0; i < temp.size(); i++) {

			if (temp.get(i).getId_trs() == id_str) {
				temp.get(i).setExp(exp);
			}
		}

	}

	// retornar o a expressao internal memory por component

	public ArrayList<Memory> memoryType(String type) {

		ArrayList<Memory> retorno = new ArrayList<Memory>();

		ArrayList<Memory> temp = this.getMemories();

		for (int i = 0; i < temp.size(); i++) {

			if (temp.get(i).getType().equalsIgnoreCase(type)) {
				retorno.add(temp.get(i));
			}
		}

		return retorno;
	}

	// retorna a internal memory por target

	public ArrayList<String> memoryTarget(String target) {

		ArrayList<String> retorno = new ArrayList<String>();

		ArrayList<Memory> temp = this.getMemories();

		for (int i = 0; i < temp.size(); i++) {

			if (temp.get(i).getTarget().equalsIgnoreCase(target)) {
				retorno.add(temp.get(i).getName());
			}
		}

		return retorno;
	}

	// retorna o internal da transicao
	public String memoryID(int id_trs) {

		String retorno = "";

		ArrayList<Memory> temp = this.getMemories();

		for (int i = 0; i < temp.size(); i++) {

			if (temp.get(i).getId_trs() == id_trs) {
				retorno = temp.get(i).getName();
			}
		}

		return retorno;
	}

	public Memory getMemoryIdTRS(int id_trs) {

		Memory retorno = null;

		ArrayList<Memory> temp = this.getMemories();

		for (int i = 0; i < temp.size(); i++) {

			if (temp.get(i).getId_trs() == id_trs) {
				retorno = temp.get(i);
			}
		}

		return retorno;

	}

	// retorna os atributos de um tipo de componente
	// @version 17/01
	public ArrayList<String> getAttributesNameByType(String type) {

		ArrayList<String> att = new ArrayList<String>();

		for (int i = 0; i < this.attributes.size(); i++) {

			if (this.attributes.get(i).getType().equalsIgnoreCase(type)) {

				att.add(this.attributes.get(i).getName());
			}
		}

		return att;

	}

	public ArrayList<Attribute> getAttributesType(String type) {

		ArrayList<Attribute> att = new ArrayList<Attribute>();

		for (int i = 0; i < this.attributes.size(); i++) {

			if (this.attributes.get(i).getType().equalsIgnoreCase(type)) {

				att.add(this.attributes.get(i));
			}
		}

		return att;

	}

	// retorna o tipo do compnente pelo nome da instancia
	public String getTypeInstance(String nomeInstance) {

		String type = "";

		Iterator i = this.instances.iterator();
		Instance temp;
		while (i.hasNext()) {

			temp = (Instance) i.next();

			if (temp.getName().equals(nomeInstance)) {

				type = temp.getType();
				return type;
			}
		}

		return type;

	}

	public ArrayList<MachineClone> getMachineclones() {
		return machineclones;
	}

	public void setMachineclones(ArrayList<MachineClone> machineclones) {
		this.machineclones = machineclones;
	}

	public ArrayList<ExtendTransition> getExtendTransition() {
		return extendTransition;
	}

	public void setExtendTransition(ArrayList<ExtendTransition> extendTransition) {
		this.extendTransition = extendTransition;
	}

	public ArrayList<ILinkPresentation> getArray_counter() {
		return array_counter;
	}

	public void setArray_counter(ArrayList<ILinkPresentation> array_counter) {
		this.array_counter = array_counter;
	}

	public ArrayList<Contrato> getContratos() {
		return contratos;
	}

	public void setContratos(ArrayList<Contrato> contratos) {
		this.contratos = contratos;
	}

	public void addContrato(Contrato ctr) {

		this.contratos.add(ctr);
	}

	public Contrato getbyComponent(String name) {

		Contrato retorno = null;
		for (int i = 0; i < this.contratos.size(); i++) {

			if (contratos.get(i).getName().equalsIgnoreCase(name)) {

				retorno = contratos.get(i);
				break;
			}

		}

		return retorno;
	}

	public ArrayList<String> getNameComponents() {
		return nameComponents;
	}

	public void setNameComponents(ArrayList<String> nameComponents) {
		this.nameComponents = nameComponents;
	}

	public ArrayList<String> getMsgErro() {
		return msgErro;
	}

	public void setMsgErro(ArrayList<String> msgErro) {
		this.msgErro = msgErro;
	}

	public ArrayList<HierarchicalComponent> getHierarchicalComponent() {
		return hierarchicalComponent;
	}

	public void setHierarchicalComponent(ArrayList<HierarchicalComponent> hierarchicalComponent) {
		this.hierarchicalComponent = hierarchicalComponent;
	}

	public String getStrContrato() {
		return strContrato;
	}

	public void setStrContrato(String strContrato) {
		this.strContrato = strContrato;
	}

	// fucao que retorna a operacao usando o nome como

	public Operation opByName(String opName) {

		Operation retorno = null;

		for (Operation tmp : operations) {

			if (tmp.getName().equalsIgnoreCase(opName)) {
				retorno = tmp;
				break;
			}
		}

		return retorno;

	}

	public int getTrs_id() {
		return this.trs_id;
	}

	public void setTrs_id(int trs_id) {
		this.trs_id = trs_id;
	}

	public void incrementaTrs_id() {
		int num;

		num = this.getTrs_id() + 1;
		this.setTrs_id(num);

	}

	public String getOtherChannelSinc(String channel) {

		String retorno = "";

		for (SetSinc temp : setsinc) {

			if (temp.getChannel1().equalsIgnoreCase(channel)) {
				return temp.getChannel2();
			} else if (temp.getChannel2().equalsIgnoreCase(channel)) {

				return temp.getChannel1();
			}

		}

		return retorno;

	}

	/**
	 * 
	 * Retorna uma lista nao repedita dos nomes das portas de um componente
	 * 
	 * @param componentName
	 * @return
	 */

	public HashSet<String> getPortComponent(String componentName) {

		HashSet<String> retorno = new HashSet<String>();
		Declarations declaration = Declarations.getInstance();
		ArrayList<Port> portas = declaration.getPortas();

		for (int i = 0; i < portas.size(); i++) {

			if (portas.get(i).getOwner().equalsIgnoreCase(componentName)) {
				retorno.add(portas.get(i).getName());
			}
		}
		return retorno;
	}

	// retorna um hashSET com os nomes das portas de um componente
	public HashSet<String> getPortComponentNotEnv(String componentName) {

		HashSet<String> retorno = new HashSet<String>();
		Declarations declaration = Declarations.getInstance();
		ArrayList<Port> portas = declaration.getPortas();
		for (int i = 0; i < portas.size(); i++) {

			if (portas.get(i).getOwner().equalsIgnoreCase(componentName)
					&& !(portas.get(i).getModifier().equalsIgnoreCase("env"))) {
				retorno.add(portas.get(i).getName());
			}
		}
		return retorno;
	}

// RETORNA PORTAS DO AMBIEMBTE

	public HashSet<String> getPortComponenteEnv(String componentName) {

		HashSet<String> retorno = new HashSet<String>();
		Declarations declaration = Declarations.getInstance();
		ArrayList<Port> portas = declaration.getPortas();
		for (int i = 0; i < portas.size(); i++) {

			if (portas.get(i).getOwner().equalsIgnoreCase(componentName)
					&& (portas.get(i).getModifier().equalsIgnoreCase("env"))) {
				retorno.add(portas.get(i).getName());
			}
		}
		return retorno;
	}

	/**
	 * Retorna um basic component conforme o nome
	 * 
	 */

	public BasicComponent getBasicComponentbyName(String componentName) {

		BasicComponent retorno = null;

		for (int i = 0; i < this.basicComponent.size(); i++) {

			if (this.basicComponent.get(i).getName().equalsIgnoreCase(componentName)) {

				retorno = this.basicComponent.get(i);
				break;
			}
		}

		return retorno;

	}
	
	
	

}
