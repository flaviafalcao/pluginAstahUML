package plugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.model.IStateMachine;
import com.change_vision.jude.api.inf.model.IStateMachineDiagram;

public class VerifyWellFormednessConditions {

	/**
	 * Verifica de a maquina de estados eh vazia
	 * 
	 * @param stm
	 * @return
	 */
	public boolean isSTMEmpty(IStateMachineDiagram stm) {

		Declarations declaration = Declarations.getInstance();

		boolean retorno = false;
		IStateMachine machine = stm.getStateMachine();
		if (machine.getTransitions().length == 0) {
			retorno = true;
			String name = stm.getStateMachine().getStateMachineDiagram().getName();
			declaration.getMsgErro().add("State Machine:  " + name + " is empty");

		}

		return retorno;

	}

	/**
	 * verificar se para todos os components basicos ha pelo menos uma porta em cada
	 * 
	 * 
	 * @return
	 * 
	 */

	public boolean HasNoPort() {
		Declarations declaration = Declarations.getInstance();

		ArrayList<BasicComponent> components = declaration.getBasicComponent();

		boolean retorno = false;

		for (int i = 0; i < components.size(); i++) {

			if (declaration.getPortbyType(components.get(i).getName()).size() == 0) {

				retorno = true;

				declaration.getMsgErro()
						.add("Basic Component has no port: " + components.get(i).getName() + " component");

				break;
			}

		}

		return retorno;

	}

	/**
	 * Verifica se toda porta tem nome
	 * 
	 * @return
	 */

	public boolean portWithoutName() {

		boolean retorno = false;
		Declarations declaration = Declarations.getInstance();
		
		ArrayList<Port> portas = declaration.getPortas();
		
		if (portas.size() > 0) {
			for (int i = 0; i < portas.size(); i++) {

				if (portas.get(i).getName().length() == 0) {

					retorno = true;
					declaration.getMsgErro().add("Port(s) whithout name");
					break;
				}

			}

		}

		return retorno;

	}

	//
	/**
	 * verificar se todas as portas tem uma interface associada
	 * 
	 * @return
	 */

	public boolean portWithoutInterface() {
		boolean retorno = false;
		Declarations declaration = Declarations.getInstance();
		
		ArrayList<Port> portas = declaration.getPortas();
		if (portas.size() > 0) {
			for (int i = 0; i < portas.size(); i++) {
				if (portas.get(i).getReq_prov() == -1 & (!portas.get(i).getOwner().contains("STM_"))) {
					retorno = true;
					declaration.getMsgErro().add("Port doesnt require/provide interface");
					break;

				}

			}
		}

		return retorno;
	}

	
	/**
	 * verificar se portas usadas na maquina de estados sao validos para aquele
	 *  componente
	 *  
	 * @return
	 */

	public boolean StmPortInvalid() {

		boolean retorno = false;
		Declarations declaration = Declarations.getInstance();
		ArrayList<BasicComponent> components = declaration.getBasicComponent();

		// para um dado componente

		for (int i = 0; i < components.size(); i++) {
			// portas deste componente
			ArrayList<Port> portas = declaration.getPortbyType(components.get(i).getName());

			// portas usada na state machine
			ArrayList<Port> portas_stm = declaration.getPortbyType("STM_" + components.get(i).getName());

			// arrays apenas com o nome das portas

			ArrayList<String> portas_str = new ArrayList<String>();

			ArrayList<String> portas_stm_str = new ArrayList<String>();

			for (int k = 0; k < portas.size(); k++) {

				portas_str.add(portas.get(k).getName());

			}

			for (int j = 0; j < portas_stm.size(); j++) {

				portas_stm_str.add(portas_stm.get(j).getName());
			}

			// verificar se todos os sem owner estao no com owner

			for (int m = 0; m < portas_stm_str.size(); m++) {

				if (!portas_str.contains(portas_stm_str.get(m).trim())) {

					retorno = true;
					declaration.getMsgErro().add("State Machine with invalid port");				
					break;
				}
			}

		}
		return retorno;

	}

	
	/**
	 *  verificar se na stm todas as operacoes usadas sao validas 
	 * 
	 * @return
	 */
	

	

	public boolean stmOpInvalid() {

		boolean retorno = false;
		Declarations declaration = Declarations.getInstance();

		ArrayList<BasicComponent> components = declaration.getBasicComponent();

		for (int i = 0; i < components.size(); i++) {

			ArrayList<Port> portas_stm = declaration.getPortbyType(components.get(i).getName());

			// verificar qual a interface das fortas

			ArrayList<Operation> op = new ArrayList<Operation>();
			ArrayList<Operation> op_stm;

			// operacoes usadas na stm do componente

			op_stm = declaration.getClassOperation("STM_" + components.get(i).getName());

			for (int k = 0; k < portas_stm.size(); k++) {

				// com o nome da porta posso recuperar a interface relacionada
				// recupero as portas que tem interface declarada

				if (!portas_stm.get(k).getInterfaceName().isEmpty()) {
					// operacoes do componente
					op.addAll(declaration.getClassOperation(portas_stm.get(k).getInterfaceName()));
				}

			}

			// comparar op_stm com op
			// criar dois arrays com os nomes

			ArrayList<String> op_string = new ArrayList<String>();
			ArrayList<String> op_stm_string = new ArrayList<String>();

			for (int m = 0; m < op.size(); m++) {

				op_string.add(op.get(m).getName());

			}

			for (int n = 0; n < op_stm.size(); n++) {

				op_stm_string.add(op_stm.get(n).getName());

			}

			for (int j = 0; j < op_stm_string.size(); j++) {

				if (!op_string.contains(op_stm_string.get(j))) {

					retorno = true;
					declaration.getMsgErro().add("State Machine with invalid operation - verify interface operations!");
				
					break;
				}

			}

		}
		return retorno;
	}

	
	/**
	 * para todo basicComponent ha um basicComponentClass
	 * 
	 * @param pacotes
	 * @param classeList
	 * @return
	 */


	public boolean NameBasicComponentInvalid(INamedElement[] pacotes, HashSet<IClass> classeList) {

		Boolean retorno = false;
		Declarations declaration = Declarations.getInstance();

		Iterator iclassList = classeList.iterator();

		ArrayList<String> basicName = new ArrayList<String>();
		ArrayList<String> packageName = new ArrayList<String>();

		for (int j = 0; j < pacotes.length; j++) {

			if (pacotes[j].getStereotypes().length > 0) {
				if (pacotes[j].getStereotypes()[0].equalsIgnoreCase("BasicComponent")) {

					packageName.add(pacotes[j].getName());
				}
			}
		}

		while (iclassList.hasNext()) {
			IClass tempClass = (IClass) iclassList.next();
			if (tempClass.getStereotypes().length > 0) {

				if (tempClass.getStereotypes()[0].equalsIgnoreCase("BasicComponentClass")) {

					basicName.add(tempClass.getName());
				}
			}
		}

		for (int i = 0; i < packageName.size(); i++) {

			if (!basicName.contains(packageName.get(i))) {
				retorno = true;
				declaration.getMsgErro().add("BasicComponent name and BasicComponentClass name are diferent");
				break;

			}
		}

		return retorno;
	}
	
	
	/**
	 * 
	 * 
	 * @param pacotes
	 * @param classeList
	 * @return
	 */

	public boolean NameHierarchicalComponentInvalid(INamedElement[] pacotes, HashSet<IClass> classeList) {

		Boolean retorno = false;

		Iterator iclassList = classeList.iterator();
		Declarations declaration = Declarations.getInstance();

		ArrayList<String> HierarchicalName = new ArrayList<String>();
		ArrayList<String> packageName = new ArrayList<String>();

		for (int j = 0; j < pacotes.length; j++) {

			if (pacotes[j].getStereotypes().length > 0) {
				if (pacotes[j].getStereotypes()[0].equalsIgnoreCase("HierarchicalComponent")) {

					packageName.add(pacotes[j].getName());
				}
			}
		}

		while (iclassList.hasNext()) {
			IClass tempClass = (IClass) iclassList.next();
			if (tempClass.getStereotypes().length > 0) {
				if (tempClass.getStereotypes()[0].equalsIgnoreCase("HierarchicalComponentClass")) {

					HierarchicalName.add(tempClass.getName());
				}
			}
		}

		for (int i = 0; i < packageName.size(); i++) {

			if (HierarchicalName.contains(packageName.get(i))) {
				retorno = true;

				declaration.getMsgErro()
						.add("HierarchicalComponent name and HierarchicalComponentClass name are diferent");
			
				break;
			}
		}

		return retorno;
	}

	
	/**
	 * verificar que para um basiccomponent nao pode haver partes no diagrama de estrutura composta
	 * 
	 * @return
	 */
	
	

	public boolean partsInvalid() {

		boolean retorno = false;

		Declarations declaration = Declarations.getInstance();
		ArrayList<PartType> partes = declaration.getPartes();

		// componentes
		ArrayList<HierarchicalComponent> componentesHC = declaration.getHierarchicalComponent();

		ArrayList<BasicComponent> componentesBasic = declaration.getBasicComponent();

		// array com os owners das partes

		ArrayList<String> partesName = new ArrayList<String>();

		for (int m = 0; m < partes.size(); m++) {

			partesName.add(partes.get(m).getOwner());
		}

		// array com apenas os nomes dos basic componentes

		ArrayList<String> basicName = new ArrayList<String>();

		for (int n = 0; n < componentesBasic.size(); n++) {

			basicName.add(componentesBasic.get(n).getName());
		}

		// array com os hc

		ArrayList<String> hcName = new ArrayList<String>();

		for (int p = 0; p < componentesHC.size(); p++) {

			hcName.add(componentesHC.get(p).getName());
		}
		// loop no array de partes

		for (int k = 0; k < partesName.size(); k++) {

			if (basicName.contains(partesName.get(k))) {

				retorno = true;
				declaration.getMsgErro().add("BasicComponent with Part");
				break;
			}

		}

		// verificar se todos os HC tem pelo menos uma parte

		for (int i = 0; i < hcName.size(); i++) {

			if (!partesName.contains(hcName.get(i))) {

				retorno = true;
				declaration.getMsgErro().add("HierarchicalComponent without Part");
				break;

			}
		}

		return retorno;
	}
	
	
	/**
	 * Reset mensagens 
	 *  
	 */

	protected void resetMessage() {


		Declarations declaration = Declarations.getInstance();
		declaration.setMsgErro(null);
		declaration.setMsgErro(new ArrayList<String>());
	}

}
