package plugin;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.regex.Pattern;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.exception.InvalidUsingException;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.model.IState;
import com.change_vision.jude.api.inf.model.IStateMachine;
import com.change_vision.jude.api.inf.model.IStateMachineDiagram;
import com.change_vision.jude.api.inf.model.ITransition;
import com.change_vision.jude.api.inf.model.IVertex;
import com.change_vision.jude.api.inf.project.ModelFinder;
import com.change_vision.jude.api.inf.project.ProjectAccessor;

public class STMView {

	private ProjectAccessor projectAccessor;
	private static ArrayList<MyTransition> trs;
	IVertex[] vertexes;
	String nome;
	String processo;

	private StringBuilder parser = new StringBuilder();

	public STMView() {
		try {
			// String uri_local = uri;
			projectAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
			// projectAccessor.open(uri, true);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public STMView(String a) {
		try {

			AstahAPI api = AstahAPI.getAstahAPI();
			ProjectAccessor projectAccessor = api.getProjectAccessor();

			openSampleModel(projectAccessor);
			// projectAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
			// projectAccessor = ProjectAccessorFactory.getProjectAccessor();
			// projectAccessor.open("C:/Users/flavi/Desktop/Astah_model/RingBuffer_Artigo-2601_Copia.asta");
			//

			INamedElement[] foundElements = findStateMachine();
			for (INamedElement element : foundElements) {
				IStateMachineDiagram stateMachineDiagram = castStateMachineDiagaram(element);
				if (stateMachineDiagram == null) {
					continue;
				}
				IStateMachine machine = stateMachineDiagram.getStateMachine();
				nome = machine.getName().trim();
				showStates(machine);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * サンプルモデルを開きます。
	 * 
	 * @param projectAccessor
	 * @throws Exception
	 */
	private static void openSampleModel(ProjectAccessor projectAccessor) throws Exception {
		InputStream astahFileStream = STMView.class
				.getResourceAsStream("C:/Users/flavi/Desktop/Astah_model/RingBuffer_Artigo-2601_Copia.asta");
		projectAccessor.open(astahFileStream);
	}

	/**
	 * Statemachine Diagram
	 * 
	 * @param projectAccessor
	 * @return 発見したモデル
	 * @throws Exception
	 */
	public INamedElement[] findStateMachine() throws Exception {
		INamedElement[] foundElements = projectAccessor.findElements(new ModelFinder() {
			public boolean isTarget(INamedElement namedElement) {
				return namedElement instanceof IStateMachineDiagram;
			}
		});
		return foundElements;
	}

	/**
	 * Retorna a maquina de estados de um elemento
	 * 
	 * @param element
	 * @return
	 */

	public IStateMachineDiagram castStateMachineDiagaram(INamedElement element) {
		IStateMachineDiagram stateMachineDiagram = null;
		if (element instanceof IStateMachineDiagram) {
			stateMachineDiagram = (IStateMachineDiagram) element;
		}
		return stateMachineDiagram;
	}

	/**
	 * 
	 * @param machine
	 * @return
	 * @throws InvalidUsingException
	 */

	public StringBuilder showStateMachine(IStateMachine machine) throws InvalidUsingException {
		return showTransitions(machine);
	}

	/**
	 * 
	 * @param vertex
	 */
	private void showVertex(IVertex vertex) {

		showIncoming(vertex);
		showOutgoing(vertex);
		if (vertex instanceof IState) {
			IState state = (IState) vertex;
			try {
				showState(state);
			} catch (InvalidUsingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 状態マシンの呼び出し元の状態を取得します。
	 * 
	 * @param machine
	 * @see http://members.change-vision.com/javadoc/astah-api/6_7_0-43495/api/ja/doc/javadoc/com/change_vision/jude/api/inf/model/IState.html
	 */
	private static void showStates(IStateMachine machine) {
		System.out.println("State start.");
		IState[] states = machine.getStates();
		for (IState state : states) {
			System.out.println(state);
		}
		System.out.println("State end.");
	}

	/**
	 * 
	 * @param state
	 * @throws InvalidUsingException
	 */

	private void showState(IState state) throws InvalidUsingException {
		System.out.println("found vertex is a state.");
		String entry = state.getEntry();
		System.out.println("entry: " + entry);
		String doActivity = state.getDoActivity();
		System.out.println("do activity: " + doActivity);
		String exit = state.getExit();
		System.out.println("exit: " + exit);
		IVertex[] subvertexes = state.getSubvertexes();
		for (IVertex subvertex : subvertexes) {
			System.out.println("found sub vertex");
			showVertex(subvertex);

		}
		IStateMachine submachine = state.getSubmachine();
		if (submachine != null) {

			System.out.println("found sub machine");
			showStateMachine(submachine);
		}
	}

	/**
	 * 
	 * 
	 * @param vertex
	 */

	private static void showIncoming(IVertex vertex) {
		System.out.println("incoming start.");
		ITransition[] incomings = vertex.getIncomings();
		for (ITransition incoming : incomings) {
			System.out.println(incoming.getConstraints()[0].getName());
			System.out.println(incoming);
		}
		System.out.println("incoming end.");
	}

	/**
	 * Vertex
	 * 
	 * @param vertex
	 */
	private static void showOutgoing(IVertex vertex) {
		System.out.println("outgoing start.");
		ITransition[] getOutgoings = vertex.getOutgoings();
		for (ITransition outgoing : getOutgoings) {
			System.out.println(outgoing);
		}
		System.out.println("outgoing end.");
	}

	/**
	 * Constroe o processo da maquina de estados
	 * 
	 * @param machine
	 * @return
	 * @throws InvalidUsingException
	 */

	private StringBuilder showTransitions(IStateMachine machine) throws InvalidUsingException {

		StringBuilder f_return = new StringBuilder();
		nome = machine.getStateMachineDiagram().getName().trim();
		int id_trs;

		System.out.println("nome da stm" + nome);

		Declarations declaration = Declarations.getInstance();
		ArrayList<MachineClone> machineclone = declaration.getMachineclones();

		MachineClone clone = new MachineClone(nome, machine);
		machineclone.add(clone);
		ITransition[] transitions = machine.getTransitions();

		vertexes = machine.getVertexes();
		trs = new ArrayList<MyTransition>();

		for (ITransition transition : transitions) {

			// HashMap<?, ?> items = transition.getPresentations()[0].getProperties();

			// items.forEach((k, v) -> {
			// System.out.println(k + "--------" + v);
			// });

			// actions divididas por ;
			String temp_action = transition.getAction().toString().replaceAll("\n", "");
			String temp_trigger = transition.getEvent().toString().replaceAll("\n", "");
			// DIVIDIR ACTION EM ATUALIZACAI DE ATRIBUTO E COMUNICACAO DE PORTRAS
			String[] tem_array_action = temp_action.split(";|;\\s"); // \\s -> espaço em branco

			// retirar os returns deste array

			String temp_guard = transition.getGuard().toString().replaceAll("\n", "");
			String[] temp_array_guard = temp_guard.split(";|;\\s");
			String[] temp_array_trigger = temp_trigger.split(";|;\\s"); // \\s -> espaço em branco

			id_trs = declaration.getTrs_id();

			String sourceTmp = transition.getSource().getName().replaceAll("\n", "");
			String targetTmp = transition.getTarget().getName().replaceAll("\n", "");

			trs.add(new MyTransition(sourceTmp, tem_array_action, targetTmp, temp_array_guard, temp_array_trigger,
					id_trs));

			declaration.incrementaTrs_id();

			AddOPPortAction(); // insiro num array as portas b
			AddOPPortTrigger(); // insiro num array as portas b

		}

		// captura os estados da maquina de estados

		ExtendTrs(trs); // coleciona as transicoes de uma maquina de estados de um componente

		processo = (montaProcesso(trs));
		parser.append(processo);
		f_return = f_return.append(processo);

		return f_return;
	}

	/*
	 * 
	 * Metodo que inclui nas declaracoes as operacoes de uma porta de uma action
	 * 
	 */

	public void AddOPPortAction() {

		// na lista de transições recupear todas as actions e trigger

		String[] part_action;
		String[] actions;
		String[] guard ;
		boolean haGuarda = false;
		Declarations declaration = Declarations.getInstance();
		boolean multi = false;
		for (int i = 0; i < trs.size(); i++) {

			actions = removeReturn(trs.get(i).getAction());

			guard = trs.get(i).getGuard();

			if (guard[0].length() > 0) {

				haGuarda = true;
				System.out.println("HA GUARDA!!!");

			}

			if (actions.length > 0) {

				for (int j = 0; j < actions.length; j++) {

					// verificar se eh porta ou seja nao te =

					if (!actions[j].contains("=")) {

						if (actions[j].trim().length() > 0) {

							part_action = actions[j].split("\\.");

							if (part_action.length >= 3) {
								multi = true;
							}

							String[] part_action_temp = part_action[0].replace("return", "")
									.split("[" + Pattern.quote(":.[()") + "]");

							if (!part_action_temp[0].trim().isEmpty()
									&& (!declaration.getPortas().contains(new Port((String) part_action_temp[0])))) {

								Port tempPort = new Port((String) part_action_temp[0], nome);
								tempPort.setMulti(multi);
								tempPort.setGuard(haGuarda);

								declaration.addPort(tempPort);
							}

							if (part_action.length > 1) {
								if (!part_action[1].isEmpty() && part_action[1].length() > 0) {
									String[] op;
									if (part_action.length >= 3) {
										op = part_action[2].split("[" + Pattern.quote(":.[()") + "]");

									} else {
										op = part_action[1].split("[" + Pattern.quote(":.[()") + "]");

									}

									declaration.addOperation(new Operation(op[0], nome));
									// array com as operacoes

								}
							}
						}

					}
				}
			}
		}
	}

	/*
	 * 
	 * Metodo que inclui nas declaracoes as operacoes de uma porta de uma trigger
	 * 
	 */

	public void AddOPPortTrigger() {
		// na lista de transições recupear todas as actions e trigger

		String[] part_action;
		String[] actions;
		String[] guard;
		boolean haGuarda = false;
		Declarations declaration = Declarations.getInstance();
		for (int i = 0; i < trs.size(); i++) {

			actions = trs.get(i).getTrigger();

			guard = trs.get(i).getGuard();

			if (guard[0].length() > 0) {

				haGuarda = true;
				System.out.println("HA GUARDA!!!");

			}

			for (int j = 0; j < actions.length; j++) {

				// verificar se eh porta ou seja nao te =

				if (!actions[j].contains("=")) {

					if (actions[j].trim().length() > 0) {

						part_action = actions[j].split("\\.");
						if (!part_action[0].trim().isEmpty()
								&& (!declaration.getPortas().contains(new Port((String) part_action[0])))) {

							Port portTemp = new Port((String) part_action[0], nome);
							portTemp.setGuard(haGuarda);
							declaration.addPort(portTemp);

						}

						if (part_action.length > 1) {
							if (!part_action[1].isEmpty() && part_action[1].length() > 0) {

								String[] op = part_action[1].split("[" + Pattern.quote(":.[()") + "]");

								declaration.addOperation(new Operation(op[0], nome));

							}
						}
					}
				}
			}
		}
	}

	// --------------SPLIT TRIGGER-------------------------//

	/**
	 * 
	 * @param trs
	 * @param trigger
	 * @param cmpt
	 * @return
	 */

	public String SplitTrigger(MyTransition trs, String trigger, String cmpt) {

		String retorno = "";
		String temp1;
		String temp2;
		String temp3 = "";
		String getemp = "";
		String settemp = "";
		String[] part_action2;
		String[] part_action;
		String[] part_action3;
		Declarations declaration = Declarations.getInstance();
		String indGuard = "";
		boolean cnst = false;

		if (trigger.contains(".")) {

			part_action = trigger.split("\\.");
			temp1 = part_action[0] + ".id.";

			// verifica se nesta porta ha guarda em outra transicao
			// retornar o obj porta com o nome part_action[0]
			Port portTemp = declaration.getPort(part_action[0]);

			if (portTemp.isGuard()) {

				indGuard = "0.";
			}

			temp1 = temp1 + indGuard;

			// se a segunda parte tem [ : ]
			if (part_action[1].contains("(")) {

				part_action2 = part_action[1].split("\\(");
				temp2 = part_action2[1].replaceAll("\\)", "");
				part_action3 = temp2.split(":");

				if (part_action3[0].equals("in")) { // in

					temp3 = part_action3[1];

				} else if (part_action3[0].equals("out")) { // out
					temp3 = "!" + part_action3[1];

					// verifica se parte3 1 contem variavel
					if (this.containVar(part_action3[1], cmpt)) {

						String get = this.getcontainVar(part_action3[1].trim(), cmpt);
						getemp = "get_" + get + ".id?" + get + "->";
					}

				}

				else {

					temp3 = "." + part_action3[0];
					cnst = true;
				}

				// fim verificacao tipo retorno

				// ----------verificar se no action ha return com saida -----------------------

				String retorno1 = verificaReturn(trs);

				// -----preciso saber se a operacao é tipo in ou out part_action2[0]
				Operation op = declaration.opByName(part_action2[0]);

				// se a operacao tiver direction out

				// verifica se signal ou operacao
				String esteriotipo = op.getEsteriotipo();

				// string not null

				if (esteriotipo != null) {

					// se nao eh signal
					if (!esteriotipo.equalsIgnoreCase("async")) {

						// verifica a direcao do parametro
						if (op.getDirection().equalsIgnoreCase("out")) { // op out
							// ha retorno

							if (!retorno1.isEmpty()) {

								getemp = "get_" + retorno1 + ".id?" + retorno1 + "->";

								temp1 = temp1 + part_action2[0] + "_I" + "->" + getemp + settemp + temp1
										+ part_action2[0] + "_O" + "!" + retorno1 + "";

							} else { // nao ha retorno

								// se part_action_3 tem 2 elementos

								// verifica se parte3 1 contem variavel
								if (part_action3.length > 1) {
									if (this.containVar(part_action3[1], cmpt)) {

										String get = this.getcontainVar(part_action3[1].trim(), cmpt);
										getemp = "get_" + get + ".id?" + get + "->";
									}

								}

								if (cnst) {

									temp1 = getemp + temp1 + part_action2[0] + "_I" + "->" + "" + temp1
											+ part_action2[0] + "_O" + temp3;

								}

								else {

									temp1 = getemp + temp1 + part_action2[0] + "_I" + "->" + "" + temp1
											+ part_action2[0] + "_O" + "?" + temp3;

								}

							}

							// direcao in
						} else if (op.getDirection().equalsIgnoreCase("in")) {

							if (!temp3.isEmpty()) {

								if (cnst) {
									temp1 = getemp + temp1 + part_action2[0] + "_I" + temp3 + "" + "->" + settemp
											+ temp1 + part_action2[0] + "_O";

								} else {
									// temp1 = getemp + temp1 + part_action2[0] + "_I" + "?" + temp3 + "" + "->" +
									// settemp
									// + temp1 + part_action2[0] + "_O";

									temp1 = getemp + temp1 + part_action2[0] + "_I" + "" + "->" + settemp + temp1
											+ part_action2[0] + "_O" + "?" + temp3;

								}
							}

						}

						else {
							temp1 = getemp + temp1 + part_action2[0] + "_I" + temp3 + "->" + settemp + temp1
									+ part_action2[0] + "_O";
						}

					} // -- fim esteriotipo n async

//---------------------- esteriotipo  async -------------------------------------------------------------------------

					else { // eh um signal
							// -------------------------------------------------------------------------

						// verifica a direcao do parametro
						if (op.getDirection().equalsIgnoreCase("out")) {

							// ha retorno
							if (!retorno1.isEmpty()) {

								getemp = "get_" + retorno1 + ".id?" + retorno1 + "->";
								temp1 = getemp + settemp + temp1 + part_action2[0] + "_O" + "!" + retorno1 + "";

							} else { // nao ha retorno

								if (part_action3.length > 1) {
									if (this.containVar(part_action3[1], cmpt)) {

										String get = this.getcontainVar(part_action3[1].trim(), cmpt);
										getemp = "get_" + get + ".id?" + get + "->";
									}
								}
								temp1 = getemp + temp1 + part_action2[0] + temp3 + "" + "";

							}

							// direcao in
						} else if (op.getDirection().equalsIgnoreCase("in")) {

							if (!temp3.isEmpty()) {

								temp1 = getemp + temp1 + part_action2[0] + "_O" + "?" + temp3 + "" + "->" + settemp;
							}

						}

						else {
							temp1 = getemp + temp1 + part_action2[0] + "_I" + temp3;
							// + "->" + settemp + temp1 + part_action2[0] + "_O";
						}

					}

				} else {

					// se nao vazio

					if (op.getDirection() != null) {

						if (!op.getDirection().isEmpty()) {

							if (!temp3.isEmpty()) {

								temp1 = getemp + temp1 + part_action2[0] + "?" + temp3 + "";
							}

						}
					}

				}

			} // fim if contain"("

			else {

				temp1 = temp1 + part_action[1] + "_I" + " ->" + temp1 + part_action[1] + "_O";

			}

			retorno = temp1;
		}

		else {
			retorno = trigger;
		}
		return retorno;

	}

	/**
	 * 
	 * @param trs
	 * @param trigger
	 * @param cmpt
	 * @param trsid
	 * @return
	 */

	public String SplitTriggerGuard(MyTransition trs, String trigger, String cmpt, int trsid) {

		String retorno = "";
		String temp1;
		String temp2;
		String temp3 = "";
		String getemp = "";
		String settemp = "";
		String[] part_action2;
		String[] part_action;
		String[] part_action3;
		Declarations declaration = Declarations.getInstance();

		Memory memory = declaration.getMemoryIdTRS(trsid);

		int idMemory = memory.getId();

		if (trigger.contains(".")) {

			part_action = trigger.split("\\.");
			temp1 = part_action[0] + ".id.";

			// part_action[0] eh uma porta
			// setar indicativo de trigger na porta

			declaration.setIndicativoTrigger(part_action[0]);

			// se a segunda parte tem [ : ]
			if (part_action[1].contains("(")) {
				part_action2 = part_action[1].split("\\(");
				temp2 = part_action2[1].replaceAll("\\)", "");
				part_action3 = temp2.split(":");

				if (part_action3[0].equals("in")) {

					temp3 = part_action3[1];

				} else if (part_action3[0].equals("out")) {
					temp3 = "!" + part_action3[1];

					// verifica se parte3 1 contem variavel
					if (this.containVar(part_action3[1], cmpt)) {

						String get = this.getcontainVar(part_action3[1].trim(), cmpt);
						getemp = "get_" + get + ".id?" + get + "->";
					}
				}

				// ----------verificar se no action ha return com saida -----------------------

				String retorno1 = verificaReturn(trs).trim();

				// -----preciso saber se a operacao é tipo in ou out part_action2[0]
				Operation op = declaration.opByName(part_action2[0]);

				// se a operacao tiver direction out

				// verifica se signal ou operacao
				String esteriotipo = op.getEsteriotipo();

				// string not null

				if (esteriotipo != null) {

					// se nao eh signal
					if (!esteriotipo.equalsIgnoreCase("async")) {

						// verifica a direcao do parametro
						if (op.getDirection().equalsIgnoreCase("out")) {

							// ha retorno
							if (!retorno1.isEmpty()) {

								getemp = "get_" + retorno1 + ".id?" + retorno1 + "->";
								temp1 = temp1 + idMemory + "." + part_action2[0] + "_I" + temp3 + "->" + getemp
										+ settemp + temp1 + idMemory + "." + part_action2[0] + "_O" + "!" + retorno1
										+ "";

							} else { // nao ha retorno

								// verifica se parte3 1 contem variavel
								if (this.containVar(part_action3[1], cmpt)) {

									String get = this.getcontainVar(part_action3[1].trim(), cmpt);
									getemp = "get_" + get + ".id?" + get + "->";
								}

								// temp1 = getemp + temp1 + part_action2[0] + temp3 + "" + "";

								// temp3 = "";
								temp1 = getemp + temp1 + idMemory + "." + part_action2[0] + "_I" + temp3 + "->" + getemp
										+ temp1 + idMemory + "." + part_action2[0] + "_O";

							}

							// direcao in
						}

						else if (op.getDirection().equalsIgnoreCase("in")) {

							if (!temp3.isEmpty()) {

								temp1 = getemp + temp1 + idMemory + "." + part_action2[0] + "_I" + "?" + temp3 + ""
										+ "->" + settemp + temp1 + idMemory + "." + part_action2[0] + "_O";
							}

							else {

								// trecho novo
								temp1 = temp1 + idMemory + "." + part_action2[0] + "_I" + "->" + "get_" + retorno1
										+ ".id?" + retorno1 + "->" + temp1 + idMemory + "." + part_action2[0] + "_O"
										+ "!" + retorno1;
							}

						}

						else {
							temp1 = getemp + temp1 + idMemory + "." + part_action2[0] + "_I" + temp3 + "->" + settemp
									+ temp1 + idMemory + "." + part_action2[0] + "_O";
						}

					} // -- fim esteriotipo n async

//---------------------- esteriotipo  async -------------------------------------------------------------------------

					else { // eh um signal
							// -------------------------------------------------------------------------

						// verifica a direcao do parametro
						if (op.getDirection().equalsIgnoreCase("out")) {

							// ha retorno
							if (!retorno1.isEmpty()) {

								// getemp = "get_" + retorno1 + ".id?" + retorno1 + "->";
								temp1 = getemp + settemp + temp1 + part_action2[0] + "!" + retorno1 + "";

							} else { // nao ha retorno

								temp1 = getemp + temp1 + part_action2[0] + temp3 + "" + "";

							}

							// direcao in
						} else if (op.getDirection().equalsIgnoreCase("in")) {

							if (!temp3.isEmpty()) {

								temp1 = getemp + temp1 + idMemory + "." + part_action2[0] + "?" + temp3; // + "" + "->"
																											// +
								// settemp;
							}

						}

						else {
							temp1 = getemp + temp1 + idMemory + "." + part_action2[0] + "" + temp3 + "->" + settemp
									+ temp1 + idMemory + "." + part_action2[0];
						}

					}

				} else {

					// se nao vazio

					if (op.getDirection() != null) {

						if (!op.getDirection().isEmpty()) {

							if (!temp3.isEmpty()) {

								temp1 = getemp + temp1 + part_action2[0] + "?" + temp3 + "";
							}

						}
					}

				}

			} else {

				temp1 = temp1 + part_action[1] + "_I" + " ->" + temp1 + part_action[1] + "_O"; // "_O"; versao com _I _O

			}

			retorno = temp1;
		}

		else {
			retorno = trigger;
		}
		return retorno;

	}

	// port_name.operation[in:vl]
	// port_name.operation[out:vl]
	/*
	 * Versao onde uma operacao UML eh traduzida em duas uma in outra out
	 * 
	 */

	public String SplitChannelPort(String action, String cmpt) {

		String retorno = "";
		String temp1;
		String temp2;
		String temp3 = "";
		String getemp = "";
		String settemp = "";
		String[] part_action2;
		String[] part_action;
		String[] part_action3;
		int tmp_index = 0;
		String getFucIndice = "";
		String getNIndice = "";
        String indGuard ="";
		Declarations declaration = Declarations.getInstance();

		if (action.contains(".")) {

			part_action = action.split("\\.");
			temp1 = part_action[0] + ".id."; // se a segunda parte tem [ : ]
			
			//se o canal tem guarda em alguma transition
			
			// retornar o obj porta com o nome part_action[0]
		     Port portTemp = declaration.getPort(part_action[0]);

			if (portTemp.isGuard()) {

				indGuard = "0.";
			}

			temp1 = temp1 + indGuard;


			
			
			tmp_index = part_action.length - 1;

			// se length = 3 , fazer um get do elemento

			if (part_action.length == 3) {
				getFucIndice = "get_" + part_action[1] + ".id?" + part_action[1] + "->";
				getNIndice = part_action[1] + ".";

			}

			if (part_action[tmp_index].contains("(")) { // onde tiver 1 coloco tmp_index
				part_action2 = part_action[tmp_index].split("\\(");
				temp2 = part_action2[1].replaceAll("\\)", "");
				part_action3 = temp2.split(":");

				if (part_action3[0].equals("out")) {

					// temp3 = "?" + part_action3[1];
					temp3 = part_action3[1];
					String set = this.getcontainVar(part_action3[1].trim(), cmpt);
					settemp = "set_" + set + ".id!" + set + "->";

				} else if (part_action3[0].equals("in")) {

					// temp3 = "!" + part_action3[1];
					temp3 = part_action3[1];

					// verifica se parte3 1 contem variavel
					if (this.containVar(part_action3[1], cmpt)) {

						String get = this.getcontainVar(part_action3[1].trim(), cmpt);
						getemp = "get_" + get + ".id?" + get + "->";
					}
				}

				// verifica a direcao

				// -----preciso saber se a operacao é tipo in ou out part_action2[0]
				Operation op = declaration.opByName(part_action2[0]);

				// verifica se signal ou operacao
				String esteriotipo = op.getEsteriotipo();

				if (esteriotipo != null) {

					if (!esteriotipo.equalsIgnoreCase("async")) {

						// se a operacao tiver direction in
						// o canal de entrada ou saida sera no primeiro evento
						if (op.getDirection().equalsIgnoreCase("in")) {

							// se parametro out
							if (part_action3[0].equals("out")) {

								temp3 = part_action3[1];
								String set = this.getcontainVar(part_action3[1].trim(), cmpt);
								temp1 = getFucIndice + temp1 + getNIndice + part_action2[0] + "_I" + "?" + temp3 + ""
										+ "->" + settemp + temp1 + getNIndice + part_action2[0] + "_O";

							}
							// se parametro in
							else if (part_action3[0].equals("in")) {

								temp3 = part_action3[1];
								// verifica se parte3 1 contem variavel
								if (this.containVar(part_action3[1], cmpt)) {

									String get = this.getcontainVar(part_action3[1].trim(), cmpt);
									getemp = "get_" + get + ".id?" + get + "->";
								}

								temp1 = getemp + getFucIndice + temp1 + getNIndice + part_action2[0] + "_I" + "!"
										+ temp3 + "->" + "" + temp1 + getNIndice + part_action2[0] + "_O";

							}

							else {

								temp1 = getFucIndice + temp1 + getNIndice + part_action[tmp_index] + "_I" + " ->" + " "
										+ temp1 + getNIndice + part_action[tmp_index] + "_O"; //

							}

						}

						// se a operacao tiver direction out
						// o canal de entrada ou saida sera no primeiro evento

						else if (op.getDirection().equalsIgnoreCase("out")) {

							// se parametro out
							if (part_action3[0].equals("out")) {

								temp3 = part_action3[1];
								String set = this.getcontainVar(part_action3[1].trim(), cmpt);
								// settemp = "set_" + set + ".id!" + set + "->";
								temp1 = getFucIndice + temp1 + getNIndice + part_action2[0] + "_I" + "->" + "" + temp1
										+ getNIndice + part_action2[0] + "_O" + "?" + temp3;

							}

							// se parametro in
							else if (part_action3[0].equals("in")) {

								temp3 = part_action3[1];
								// verifica se parte3 1 contem variavel
								if (this.containVar(part_action3[1], cmpt)) {

									String get = this.getcontainVar(part_action3[1].trim(), cmpt);
									getemp = "get_" + get + ".id?" + get + "->";
								}

								temp1 = getemp + getFucIndice + temp1 + getNIndice + part_action2[0] + "_I" + "->" + ""
										+ temp1 + getNIndice + part_action2[0] + "_O" + "!" + temp3;

							}

						}

						else {
							temp1 = getemp + getFucIndice + temp1 + getNIndice + part_action2[0] + "_I" + temp3 + "->"
									+ "" + settemp + temp1 + getNIndice + part_action2[0] + "_O";

						}
					}

					// }

					else {

						if (!op.getDirection().isEmpty()) {

							// se parametro out
							if (part_action3[0].equals("out")) {

								temp3 = part_action3[1];
								temp1 = temp1 + part_action2[0] + "" + "?" + temp3;
							}

							// se parametro out
							if (part_action3[0].equals("in")) {

								temp3 = part_action3[1];
								// verifica se parte3 1 contem variavel
								if (this.containVar(part_action3[1], cmpt)) {

									String get = this.getcontainVar(part_action3[1].trim(), cmpt);
									getemp = "get_" + get + ".id?" + get + "->";
								}

								temp1 = getemp + temp1 + part_action2[0] + "!" + temp3;

							}
						}
					}

				}
			} else {

				temp1 = getFucIndice + temp1 + getNIndice + part_action[tmp_index] + "_I" + " ->" + "" + temp1
						+ getNIndice + part_action[tmp_index] + "_O"; //

			}

			retorno = temp1;

		}

		else {

			retorno = action;
		}
		return retorno;

	}

	/*
	 * Versao quando havia um return explicito no diagrama com return na action
	 * public String SplitChannelPort(String action, String cmpt) {
	 * 
	 * String retorno = ""; String temp1; String temp2; String temp3 = ""; String
	 * getemp = ""; String settemp = ""; String[] part_action2; String[]
	 * part_action; String[] part_action3;
	 * 
	 * // tirar o string return
	 * 
	 * String[] splitReturn = action.split("return\\("); String splitTemp =
	 * splitReturn[1];
	 * 
	 * if (splitTemp.contains(".")) {
	 * 
	 * part_action = splitTemp.split("\\."); temp1 = part_action[0] + ".id."; // se
	 * a segunda parte tem [ : ] if (part_action[1].contains("(")) { part_action2 =
	 * part_action[1].split("\\(");
	 * 
	 * temp2 = part_action2[1].replaceAll("\\)", "");
	 * 
	 * part_action3 = temp2.split(":");
	 * 
	 * if (part_action3[0].equals("in")) {
	 * 
	 * temp3 = "?" + part_action3[1];
	 * 
	 * String set = this.getcontainVar(part_action3[1].trim(), cmpt); settemp =
	 * "set_" + set + ".id!" + set + "->";
	 * 
	 * } else if (part_action3[0].equals("out")) { temp3 = "!" + part_action3[1];
	 * 
	 * // verifica se parte3 1 contem variavel if (this.containVar(part_action3[1],
	 * cmpt)) {
	 * 
	 * String get = this.getcontainVar(part_action3[1].trim(), cmpt); getemp =
	 * "get_" + get + ".id?" + get + "->"; } }
	 * 
	 * temp1 = getemp + temp1 + part_action2[0] + "" + temp3 + "->" + settemp +
	 * temp1 + part_action2[0].replace(")", "") + "_O";
	 * 
	 * } else {
	 * 
	 * temp1 = temp1 + part_action[1].replace(")", "") + "_out";// + "_I ->"+ temp1
	 * + part_action[1] +"_O"; }
	 * 
	 * retorno = temp1; }
	 * 
	 * else { retorno = splitTemp; } return retorno;
	 * 
	 * }
	 */

	public String SplitSet(String setAction, String cmpt) {

		String retorno = "";
		String[] part_action2;
		part_action2 = SplitExpr(setAction);
		// verificar se na segunda parte tem mencao a alguma variavel

		if (this.containVar(part_action2[1].trim(), cmpt)) {
			// retorna a variavel
			String get = this.getcontainVar(part_action2[1].trim(), cmpt);
			//
			// separa as variaveis
			String[] getsplit = get.split("\\.");

			for (int i = 0; i < getsplit.length; i++) {

				retorno = retorno + "get_" + getsplit[i] + ".id?" + getsplit[i] + "->";

			}

			// retorno = "get_" + get + ".id?" + get + "->";
		}

		retorno = retorno + "set_" + part_action2[0].trim() + ".id!" + part_action2[1].trim();

		return retorno;

	}

	public String SplitGet(String setAction) {

		String retorno;
		String[] part_action;
		String[] part_action2;
		part_action = (setAction.trim()).split("get_");
		// valo = 0
		part_action2 = SplitExpr(part_action[1]);
		retorno = part_action2[0].trim() + ".id?" + part_action2[1].trim();

		return retorno;

	}

	public String[] SplitExpr(String exp) {

		String retorno[];
		String[] part_exp;
		part_exp = exp.split("=");
		retorno = part_exp;

		return retorno;

	}

	public String r_portas() {

		Declarations declaration = Declarations.getInstance();
		ArrayList<Port> ports = declaration.getPortas();
		HashSet<String> hash = new HashSet<String>(); // = new HashSet<Port>(ports);

		String result = "";

		for (int i = 0; i < ports.size(); i++) {
			hash.add(ports.get(i).getName());
		}
		Iterator<String> iterator = hash.iterator();
		while (iterator.hasNext()) {
			result = result + "channel " + (String) iterator.next() + ":id.operation;" + "\n";

		}
		// System.out.println(hash.size());

		return result;
	}

	public String montaProcesso(ArrayList<MyTransition> trs) {

		String retorno = "";
		String processoI = "";
		String processo = "";
		String processoname = "";
		String escolha = "";
		String processocompleto = "";
		Boolean guardTrigger = false;
		ArrayList<MyTransition> visitou = new ArrayList<MyTransition>();
		Declarations declaration = Declarations.getInstance();
		ArrayList<StateMachine> stateMachine = declaration.getStateMachine();
		String name_comp = splitNameStm(nome);

		// GUARDA A MEMORY
		this.cad_memory(trs);

		// loop transicoes DA MAQUINA DE ESTADOS
		for (int i = 0; i < trs.size(); i++) {

			// ----------------------------------------------------------
			// se transicao inicial----------------------------------------
			// -------------------------------------------------------------

			if (trs.get(i).getSource().startsWith("Initial")) {
				String temp = "";
				processoI = temp + trs.get(i).getTarget().replaceFirst("\n", "");
				processo = "";
			}

			// --------------------------------------------------------
			// se nao eh transicao INICIAL
			// -----------------------------------------------------
			else {

				String temp_guard = "";
				String memory = "";
				// nome do processo eh a state origem da transicao
				processoname = trs.get(i).getSource();

				// SE NO NAO VISITADO
				if (!visitou.contains(trs.get(i))) {

					visitou.add(trs.get(i));
					escolha = "";

					for (int j = 0; j < trs.size(); j++) {

						if (trs.get(j).getSource().equalsIgnoreCase(processoname) && !visitou.contains(trs.get(j))) {

							// há escolha externa
							escolha = escolha + " []" + " (";

							String temp = "";

							// --------------------
							// verifica se há guarda para esta transicao

							memory = declaration.memoryID(trs.get(j).getId());
							String memory_str = "";

							if (!memory.isEmpty()) {

								memory_str = memory + "-> "; // insere a referencia a memoria
							}

							// ------------------------ //SE HA TRIGGER -------------------------------

							for (String string : trs.get(j).getTrigger()) {

								if (string.trim().length() > 0) {

									if (!memory.isEmpty()) {

										memory_str = " ";

										// verificar se o string possui alguma variavel
										if ((!string.contains("="))) {

											String exp = SplitTriggerGuard(trs.get(j), string.trim(), name_comp,
													trs.get(j).getId());
											String[] expSplit = exp.split("->");
											temp = temp + exp + " ->";

											// setar expressao que ficara na memoria
											declaration.setMemoryExp(trs.get(j).getId(), expSplit[0]);

										} else {
											temp = temp + this.SplitSet(string.trim(), name_comp) + " ->";
										}
									}

									/// E NAO HA GUARDA NESTA TRANSICAO

									else {

										// verificar se o string possui alguma variavel
										if ((!string.contains("="))) {
											temp = temp + SplitTrigger(trs.get(j), string.trim(), name_comp) + " ->";
										} else {
											temp = temp + this.SplitSet(string.trim(), name_comp) + " ->";
										}

									}

								}

							}

							// -------------------- // se ha action
							// -----------------------------------------------------
							String[] action_temp = removeReturn(trs.get(j).getAction());

							for (String string : action_temp) {

								// se não é vazio

								if (string.trim().length() > 0) {

									// verificar o tipo de action
									// se começar com set ou get não colocar _I e _O
									// verificar se o string possui alguma variavel

									if ((!string.contains("="))) {
										temp = temp + SplitChannelPort(string.trim(), name_comp) + " ->";
									} else {
										temp = temp + this.SplitSet(string.trim(), name_comp) + " ->";
									}

								}
							}

							// verifica se ha parametro neste state
							String str = getStateEntry(trs.get(j).getTarget().toString());
							String param_escolha = "";
							if (str.length() > 0) {
								param_escolha = ',' + splitParam(str);
							}

							escolha = escolha + memory_str + temp + trs.get(j).getTarget().replaceFirst("\n", "")
									+ "(id" + param_escolha + ") )";
							visitou.add(trs.get(j));

						}
					} /// escolhas externas

					String temp = "";

					// -------------------- nao ha escolha externa -----------
					// ------- trigger-----------------------------------------

					for (String string : trs.get(i).getTrigger()) {

						if (string.trim().length() > 0) {

							// ver se tem guarda para essa trigger
							String memorytmp = declaration.memoryID(trs.get(i).getId());

							if (!memorytmp.isEmpty()) { // ha guarda e trigger

								guardTrigger = true;
								string = string.replace('\n', ' ');

								if (!string.contains("=")) {
									String exp = this.SplitTriggerGuard(trs.get(i), string.trim(), name_comp,
											trs.get(i).getId());
									String[] expSplit = exp.split("->");
									// String newExp = exp.replace("!", "?");
									// temp = temp + newExp + " ->";
									temp = temp + exp + " ->";

									// setar expressao da memoria
									declaration.setMemoryExp(trs.get(i).getId(), expSplit[0]);

								} else {
									temp = temp + SplitSet(string.trim(), name_comp) + " ->";
								}

							}

							else {

								string = string.replace('\n', ' ');
								if (!string.contains("=")) {
									temp = temp + this.SplitTrigger(trs.get(i), string.trim(), name_comp) + " ->";
								} else {
									temp = temp + SplitSet(string.trim(), name_comp) + " ->";
								}
							}

						}
					}
					// ----------------- action-----------------------------------------/

					String[] action_temp2 = removeReturn(trs.get(i).getAction());
					for (String string : action_temp2) {
						string = string.replace('\n', ' ');
						if (string.trim().length() > 0) {

							// !containVar(string, name_comp) &&
							if (!string.contains("=")) {
								temp = temp + SplitChannelPort(string, name_comp) + " ->";
							} else {
								temp = temp + SplitSet(string, name_comp) + " ->";
							}

						}
					}

					// verifica se ha parametro neste state

					String str = getStateEntry(processoname);
					String param = "";
					if (str.length() > 0) {
						param = ',' + splitParam(str);
					}

					// verifica se o target tb tem parametro

					String str_tg = getStateEntry(trs.get(i).getTarget().toString());
					String param_tg = "";
					if (str_tg.length() > 0) {
						param_tg = ',' + splitParam(str_tg);
					}

					memory = declaration.memoryID(trs.get(i).getId()); // verifica se ha guarda
																		// para a transicao

					if (!memory.isEmpty() & !guardTrigger) {

						temp_guard = memory + "-> "; // INTERNAL.N

					}

					processo = processoname + "(id" + param + ") = (" + temp_guard + temp
							+ trs.get(i).getTarget().replaceFirst("\n", "") + "(id" + param_tg + "))" + escolha + "\n";

					processocompleto = processocompleto + processo;

					guardTrigger = false;
				}

			}
		}

		retorno = nome + "(id) = " + processoI + "(id)\n" + processocompleto;

		// adiciona uma statemachine do array de state machine
		// ---------------------------------------
		StateMachine temp_stm = new StateMachine(nome + "(id)", name_comp);
		stateMachine.add(temp_stm);
		// ---------------------------------------------------------------

		return retorno;

	}

	private void cad_memory(ArrayList<MyTransition> trs2) {

		Declarations declaration = Declarations.getInstance();
		int num_memory;
		String name_comp = splitNameStm(nome);

		for (int i = 0; i < trs2.size(); i++) {

			// loop guardas
			for (String string : trs2.get(i).getGuard()) {
				// adiciona no array de memories
				if (string.length() > 0) {
					num_memory = declaration.getMemory_num();
					String internal = "internal." + num_memory;

					Memory guard = new Memory(name_comp, internal, string, string + " & " + "internal." + num_memory,
							trs2.get(i).getTarget(), trs2.get(i).getId(), num_memory, trs2.get(i).getTrigger());

					declaration.getMemories().add(guard);

					declaration.incrementaNumMemory();
				}
			}
		}

	}

	public StringBuilder getParser() {
		return parser;
	}

	public void setParser(StringBuilder parser) {
		this.parser = parser;
	}

	// split nome da maquina de estados e nome do component

	public String splitNameStm(String name) {

		String[] splitname = name.split("_");
		return splitname[1];

	}

	// retorna o entry de um estado

	public String getStateEntry(String name) {
		IState state = null;
		String str = "";

		for (int i = 0; i < vertexes.length; i++) {

			if (vertexes[i].getName().equalsIgnoreCase(name)) {

				if ((vertexes[i]) instanceof IState) {

					state = (IState) vertexes[i];
					str = state.getEntry();
					break;
				}
			}
		}
		return str;
	}

	public String splitParam(String str) {
		String[] splitname = (str.trim().replace(" ", "")).split("param=");
		return splitname[1];

	}

	/**
	 * 
	 * funcao que verifica se na string ha uma variavel do componente
	 * 
	 * @param str
	 * @param cmpt
	 * @return
	 */

	public boolean containVar(String str, String cmpt) {
		boolean retorno = false;
		Declarations declaration = Declarations.getInstance();
		ArrayList<Attribute> atributos = declaration.getAttributesType(cmpt);

		for (int i = 0; i < atributos.size(); i++) {

			if (str.contains(atributos.get(i).getName())) {

				retorno = true;
				break;
			}
		}

		return retorno;

	}

	public String getcontainVar(String str, String cmpt) {
		String retorno = "";
		Declarations declaration = Declarations.getInstance();
		ArrayList<Attribute> atributos = declaration.getAttributesType(cmpt);

		for (int i = 0; i < atributos.size(); i++) {

			if (str.contains(atributos.get(i).getName())) {

				retorno = retorno + atributos.get(i).getName();

				retorno = retorno + ".";
				// break;
			}
		}

		return retorno;

	}

	public void ExtendTrs(ArrayList<MyTransition> tr) {

		Declarations declaration = Declarations.getInstance();

		String name_comp = splitNameStm(nome);

		ArrayList<ExtendTransition> extend = new ArrayList<ExtendTransition>();

		for (int i = 0; i < tr.size(); i++) {

			ExtendTransition tmp = new ExtendTransition();

			tmp.setComponentName(name_comp);

			tmp.setSource(tr.get(i).getSource());
			tmp.setTarget(tr.get(i).getTarget());

			// verificar se ha guarda

			if (tr.get(i).getGuard().length > 0) {

				if (tr.get(i).getGuard()[0].length() > 0) {

					String[] arrayGuard = new String[1];
					arrayGuard[0] = "internal";
					tmp.setEvent_guard(arrayGuard);
				}
			}

			String[] actionExt = null;

			for (String string : tr.get(i).getAction()) {

				if (string.trim().length() > 0) {

					if ((!string.contains("="))) {
						// separar por ->
						actionExt = arrayconcat(actionExt, splitExt(SplitChannelPort(string, name_comp)));

					} else {
						actionExt = arrayconcat(actionExt, splitExt(SplitSet(string, name_comp)));

					}

				}

			}

			String[] triggerExt = null;

			for (String string : tr.get(i).getTrigger()) {

				if (string.trim().length() > 0) {

					if ((!string.contains("="))) {
						// separar por ->
						triggerExt = arrayconcat(triggerExt, splitExt(SplitChannelPort(string, name_comp)));

					} else {
						triggerExt = arrayconcat(triggerExt, splitExt(SplitSet(string, name_comp)));

					}

				}

			}

			tmp.setEvent_action(actionExt); // action

			tmp.setEvent_trigger(triggerExt);

			extend.add(tmp);

		}

		declaration.setExtendTransition(extend);
	}

	public String[] splitExt(String str) {

		String[] retorno = null;
		if (str.contains("->")) {
			retorno = str.split("->");

		} else {
			retorno = new String[1];
			retorno[0] = str;
		}
		return retorno;
	}

	// remover do array de action o return explicito

	public String[] removeReturn(String[] act) {

		String[] retorno = null;
		ArrayList<String> temp = new ArrayList<String>();
		int j = 0;

		for (int i = 0; i < act.length; i++) {

			if (!act[i].contains("return(")) {

				temp.add(act[i]);
				j = j + 1;

			}
		}

		retorno = temp.toArray(new String[j]);

		return retorno;

	}

/// funcao auxiliar para concatenar arrays 
	public String[] arrayconcat(String[] a, String[] b) {

		String[] result = null;
		if (a != null && b != null) {

			int len_a = a.length;
			int len_b = b.length;
			result = new String[len_a + len_b];

			System.arraycopy(a, 0, result, 0, len_a);
			System.arraycopy(b, 0, result, len_a, len_b);
			System.out.println(Arrays.toString(result));

		} else if (a == null) {

			result = b;
		}

		return result;

	}

	/**
	 * funcao auxiliar que verifica se para uma action associada a uma trigger se ha
	 * retorno
	 * 
	 * 
	 **/

	public String verificaReturn(MyTransition trs) {

		String retorno = "";

		String[] action = trs.getAction();
		String temp;

		for (int i = 0; i < action.length; i++) {

			if (action[i].contains("return")) { // conta
				// tirar os ()
				temp = action[i].replace("return", "");
				temp = temp.replace("(", "");
				temp = temp.replace(")", "");

				if (!temp.isEmpty()) {

					retorno = temp;
					break;
				}

			}
		}

		// verificar se alguma action contem o string return

		return retorno;
	}

}