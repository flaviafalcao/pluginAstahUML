package plugin;

import java.io.File;
import java.io.FileWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.editor.TransactionManager;
import com.change_vision.jude.api.inf.model.*;

import com.change_vision.jude.api.inf.presentation.*;
import com.change_vision.jude.api.inf.project.*;

public class Principal {

	// variavel global nome do modelo

	public static String nomeModelo;
	public static FdrWrapper wrapper;
	boolean condition1 = false;
	boolean allconditions;

	VerifyWellFormednessConditions conditions = new VerifyWellFormednessConditions();

//-----------------------------------------------------------------------
//-----------------------------------------------------------------------

	public boolean valida() {

		FileWriter arquivo;
		Declarations declaration = Declarations.getInstance();
		String write = "";
		boolean check = true;
		boolean checkCTR = true;
		String path ="";

		try {

			conditions.resetMessage(); // reseta todas as mensagens

			ProjectAccessor projectAccessor = AstahAPI.getAstahAPI().getProjectAccessor();

			// arquivo modeloX.csp
			String filemodelo = "modelo" + declaration.getNum() + ".csp";

			nomeModelo = filemodelo;
		
			arquivo = new FileWriter(
					new File("C:/Users/flavi/eclipse-workspace/CorTeste/src/main/resources/" + filemodelo));
					
			    

			ModeloAssertion modelocsp = new ModeloAssertion();

			modelocsp.setFileModel(filemodelo);
			declaration.addAssertionModel(modelocsp);

			/* classes do diagrama */

			ClassView classv;
			classv = new ClassView();

			String op = classv.to_stringOp();

			String opI = classv.to_StringOpI();

			String opO = classv.to_StringOpO();

			// operacoes de ambiente
			String op_env = "";

			String channelGetSet = classv.getChannelGetSet();

			String variaveis = classv.getVariaveis();

			/* maquina de estados */

			STMView stm;
			stm = new STMView();

			int i = 0;
			ArrayList<IStateMachineDiagram> stateMachineDiagram = new ArrayList<IStateMachineDiagram>();

			// identify STMs
			INamedElement[] foundElements = stm.findStateMachine();
			for (INamedElement element : foundElements) {
				stateMachineDiagram.add(stm.castStateMachineDiagaram(element));
				i = i + 1;
			}

			// loop nas maquinas de estado
			IStateMachine machine1;
			String machines = "";
			for (int st = 0; st < stateMachineDiagram.size(); st++) {

				condition1 = conditions.isSTMEmpty(stateMachineDiagram.get(st));

				machine1 = (IStateMachine) stateMachineDiagram.get(st).getStateMachine();
				machines = machines + stm.showStateMachine(machine1) + "\n";

			}

			int num_internal = declaration.getMemory_num();

			String channelInternal;

			if (num_internal > 1) {
				channelInternal = "t_id = {0.." + num_internal + "} \n";
			} else {
				channelInternal = "t_id = {0..1} \n";
			}

			channelInternal = channelInternal + "channel internal: t_id \n";

			/*****************************************************************************/

			// construir componentes
			Composition comp = new Composition();
			/// criar basic component
			comp.createBasicComponent();
			String temp_basic = comp.printBasicComponent();

			/* composite */
			CompositeView c = new CompositeView();
			write = "-------------canais das portas-----------------" + "\n";

			String channelPort = c.getChannelPort();

			String contrato_str = c.getContrato();

			// cria instancia teste
			String intanciaTeste = c.montaInstanciaTeste(); // para cada componente basico

			c.findPart(); // instancias

			String instancias = c.MontaInstancia(); // monta a instancia

			String range_tipo = c.rangeTipo();

			INamedElement[] foundElements3 = null;

			foundElements3 = c.findConnector();

			String getSetProcess = c.memoryProcess(); // opcional

			for (INamedElement element3 : foundElements3) {
				IConnector temp = (IConnector) element3;
				comp.firstConection(temp);
			}

			String inputComponent = comp.getInputComponent();

			String outputComponent = comp.getOutputComponent();

			String doComponent = "";
			// se ha instancias
			if (instancias.length() > 0) {

				doComponent = comp.doComposition();

				System.out.println(doComponent);
			}

			String tagProtocol = comp.tagProtocol();
			String dual = comp.getDualProtocol();

			////

			write = write + " \n include \"function_aux.csp\" \n";

			write = write + "--------------------canais set e gets--------------" + "\n";

			write = write + range_tipo + "\n" + channelPort + "\n" + variaveis + "\n" + "" + channelGetSet
					+ "-----instancias teste-----------" + "\n" + intanciaTeste + "-----instancias real-----------"
					+ "\n" + instancias + "\n" + "\n" + "" + "\n" + contrato_str + " \n";

			write = write + op + "\n" + opI + "\n" + opO + "\n" + op_env + "\n" + "\n";

			write = write + channelInternal + "\n" + getSetProcess + "\n" + machines + "\n";

			write = write + temp_basic + "\n";

			write = write + "\n" + inputComponent + "\n" + outputComponent + "\n" + "-- do componente \n" + doComponent
					+ "\n" + "-- funcDefault \n"
					// + funcDefaut +"\n"
					+ tagProtocol + "" + "\n";
			// dual + "\n";

			InputTypeDeclaration input = new InputTypeDeclaration();

			String inputType = input.inputType();
			String outputType = input.outputType();

			String inputAll = "";
			String outputAll = "";

			// apenas se ha instancias
			if (instancias.length() > 0) {

				inputAll = input.inputAll();
				outputAll = input.outputAll();

				write = write + "\n" + inputType + "\n" + outputType + "\n" + inputAll + "\n" + outputAll + "\n";

				OutputCFunction cFunction = new OutputCFunction();
				write = write + cFunction.outputFunctionType();
				write = write + cFunction.outputFunctionTypeAll();

				// ha buffer se ha composicao

				Buffer bf = new Buffer();

				write = write + bf.bufferAll();
				write = write + bf.BufferIO();

			}

			String assertivaCtr = "";
			write = write + "\n " + assertivaCtr + "\n";

			HashSet<Instance> instancias_name = declaration.getInstances();
			Iterator it = instancias_name.iterator();

			while (it.hasNext()) {

				String temp = "";

				Instance instance_temp = (Instance) it.next();
				//
				write = write + "\n include \"auxiliar_" + instance_temp.getName() + ".csp\" \n";

			}

			arquivo.write(write);
			arquivo.flush();
			arquivo.close();

			// --------------------------------------------------------------------------------------------------------

			String map =

					"-------------------------------------------------------------------------------" + "\n"
							+ "-- mapping an LTS to (traces) equivalent CSP process" + "\n"
							+ "--------------------------------------------------------------------------------" + "\n"
							+ "-- Let p = (Q, Li, Lo, T, q0) be an LTS(Li,Lo)" + "\n"

							+ "available(q,T) = { ev | (q1,ev,q2)<-T, q1==q } \n "
							+ "next(q,e,T) = { q2 | (q1,ev,q2)<-T, q1==q, ev==e } \n "
							+ "M(q1,T) = [] ev : available(q1,T)  \n"
							+ "         @ ( [] q2 : next(q1,ev,T) @ ev -> M(q2,T) ) \n " + "channel tau \n"
							+ "MFDR(q1,T) = M(q1,T) \\ {tau}  \n "
							+ "-----------------------------------------------------------------------------------------"
							+ "------------------------------------------------------------------------------------------"
							+ "--------mapping an LTS to dual protocol ---------------------------------------------------" +"\n"
							+ "MDUAL(q1,T) = |~| ev : available(q1,T)  \n"
							+ "         @ ( |~| q2 : next(q1,ev,T) @ ev -> MDUAL(q2,T) ) \n " + "\n"
							+ "MFDRDUAL(q1,T) = MDUAL(q1,T) \\ {tau}  \n " + "\n"
							+ "---------------------------------------------------------------" +"\n";

			ArrayList<ComponentType> type = declaration.getComponentType();
			String protocolName = "";

			// ----------------------------------------------------------------------------------------------------------------------------//
			// --------------------- fazer verificacoes apenas se o modelo estiver ok
			// ---------------------------------------------------- //
			// ---------------------------------------------------------------------------------------------------------------------------
			// //

			// verificar se todos os basic components tem pelo menos uma porta.

			boolean condition2 = conditions.HasNoPort();
			boolean condition3 = conditions.portWithoutName();
			boolean condition4 = conditions.portWithoutInterface();
			boolean condition5 = conditions.StmPortInvalid();
			boolean condition6 = conditions.stmOpInvalid();

			//
			IModel iCurrentProject = projectAccessor.getProject();

			HashSet<IClass> classeList = new HashSet<IClass>();
			classv.getAllClasses(iCurrentProject, classeList);

			INamedElement[] pacotes = classv.findPackage(projectAccessor);

			boolean condition7 = conditions.NameBasicComponentInvalid(pacotes, classeList);
			boolean condition8 = conditions.NameHierarchicalComponentInvalid(pacotes, classeList);
			System.out.println(classeList.size());

			conditions.partsInvalid();

			allconditions = (condition1 || condition2 || condition3 || condition4 || condition5 || condition6
					|| condition7 || condition8);

			// --------------------------------------------------------------------------------------------------------------------//

			if (!allconditions) { // SE MODELO ESTA CORRETAMENTE DESENHADO

				// se os diagramas estao escritos de forma correta
				// verificar se formam contratos validos
				// ----------------------------------------------------------------------------------------------------//
				// criar um arquivo para verificacao se contrato valido
				// ---------------------------------------------//
				// ----------------------------------------------------------------------------------------------------//
				String verificaCTR = "verificaCTR" + declaration.getNum() + ".csp";
				FileWriter arquivoCTR = new FileWriter(
						new File("C:/Users/flavi/eclipse-workspace/CorTeste/src/main/resources/" + verificaCTR));

				VerificationContract contrato = new VerificationContract();
				assertivaCtr = contrato.ContractCSP();

				String include = "include \"" + filemodelo + "\" \n";
				assertivaCtr = include + assertivaCtr;

				arquivoCTR.write(assertivaCtr);
				arquivoCTR.flush();
				arquivoCTR.close();

				// -------------------------------------------------------------
				// -- checa se eh um IO Process ----------------------------------
				// --------------------------------------------------------------

				checkCTR = checkRefinementIO(
						"C:/Users/flavi/eclipse-workspace/CorTeste/src/main/resources/" + verificaCTR);
						

			 

				wrapper = FdrWrapper.getInstance();

				int count = wrapper.getCounterExamples().size();

				if (count > 0) {

					CounterExample ce = new CounterExample();
					ce.counterExampleInit(wrapper);

				}

				// ----------------------------------------------------------------------------------------------------//
				// criar um arquivo para verificacao se contrato valido
				// ---------------------------------FIM ------------//
				// ----------------------------------------------------------------------------------------------------//

				else {

					LTS lts = new LTS();

					String trace_lts = "";
					String protocolo = "";

					for (int k = 0; k < type.size(); k++) {

						// a lista de protocolos do tipo

						String temp = type.get(k).getType();
						HashSet<String> ports_comp;
						ports_comp = declaration.getPortComponentNotEnv(temp);

						Iterator iterator = ports_comp.iterator();
						String porta = "";
						
						BasicComponent basic = declaration.getBasicComponentbyName(temp);
						ArrayList<String> protocolNames = basic.getProtocolNames();


						while (iterator.hasNext()) {

							porta = (String) iterator.next();

						}

						
						for (int l = 0; l < protocolNames.size(); l++) {

							protocolName = protocolNames.get(l);

							trace_lts = lts.protocolo(
									   "C:/Users/flavi/eclipse-workspace/CorTeste/src/main/resources/" + filemodelo,
									protocolName, porta);

							String protocolId = "";

							protocolId = protocolName.replaceAll("[^0-9.]", "");

							protocolo = protocolo + "wb_protocolo_" + type.get(k).getType() + protocolId + "= MFDR( "
									+ lts.root() + ",{" + trace_lts + "}) \n";
							
							protocolo = protocolo  + "dual_wb_protocolo_" + type.get(k).getType() + protocolId + "= MFDRDUAL( "
									+ lts.root() + ",{" + trace_lts + "}) \n"; ;

						}

					}

					FileWriter arquivo2;
					String write2 = "include \"" + filemodelo + "\" \n";

					write2 = write2 + "\n" + dual + "\n";

					write2 = write2 + "channel w : operation" + "\n";

					String fileName = "arquivo_protocolo" + declaration.getNum();

					arquivo2 = new FileWriter(new File(
							"C:/Users/flavi/eclipse-workspace/CorTeste/src/main/resources/" + fileName + ".csp"));
							 // "C:/Users/flavi/git/pluginAstahUML/doutorado/src/main/resources/" + fileName + ".csp"));
					write2 = write2 + map;
					// monta protocolo

					write2 = write2 + protocolo + "\n";// + protocolo2 ;

					arquivo2.write(write2);
					arquivo2.flush();
					arquivo2.close();

					IConnector conn = null;

					ArrayList<String> assertions = declaration.assertionsModel(nomeModelo);
					assertions.add(filemodelo);
					boolean check1;

					for (String assertion : assertions) {

						check = checkRefinement(
								"C:/Users/flavi/eclipse-workspace/CorTeste/src/main/resources/" + assertion);
							
						if (!check) {
							if (declaration.getIConnector(assertion + "") != null) {
								conn = declaration.getIConnector(assertion + "");
								break;
							}
						}
					}

					///
					// rever este codigo para gerar contra exemplo

					if (!check) {
						// TransactionManager.beginTransaction();
						// IPresentation cp = conn.getPresentations()[0];
						// cp.setProperty(PresentationPropertyConstants.Key.LINE_COLOR, "#FF0000");
						// TransactionManager.endTransaction();

						// criar diagrama de sequencia

						wrapper = FdrWrapper.getInstance();
						CounterExampleSD SD = new CounterExampleSD();
						SD.counterExampleInit(wrapper);
					}

				}

			} else {

				return !allconditions;
			}

		}

		catch (Exception e) {

			System.out.println(e.toString());
			e.printStackTrace();

		}

		int num = declaration.getNum();
		declaration.setNum(num + 1);

		check = !(allconditions) & check & checkCTR;

		return check;

	}

	// verifica assertions
	public boolean checkRefinementIO(String filename) {
		boolean retorno = true;
		wrapper = FdrWrapper.getInstance();
		wrapper.loadFile(filename);

		List<Object> assertions = FdrWrapper.getInstance().getAssertions();
		if (assertions.size() > 0) {
			System.out.println("Numero de assertions : " + assertions.size());

			retorno = wrapper.executeAssertionsIO(assertions);
			System.out.println(" numero de contra exemplos" + wrapper.getCounterExamples().size());
			System.out.println("retorno" + retorno);

		}
		return retorno;
	}

	// verifica assertions
	public boolean checkRefinement(String filename) {
		boolean retorno = true;
		wrapper = FdrWrapper.getInstance();
		wrapper.loadFile(filename);

		List<Object> assertions = FdrWrapper.getInstance().getAssertions();
		if (assertions.size() > 0) {
			System.out.println("Numero de assertions : " + assertions.size());

			retorno = wrapper.executeAssertions(assertions);
			System.out.println(" numero de contra exemplos" + wrapper.getCounterExamples().size());
			System.out.println("retorno" + retorno);

		}
		return retorno;
	}

}
