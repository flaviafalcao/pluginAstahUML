package plugin;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.editor.SequenceDiagramEditor;
import com.change_vision.jude.api.inf.editor.TransactionManager;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.ILifeline;
import com.change_vision.jude.api.inf.model.IMessage;
import com.change_vision.jude.api.inf.model.IModel;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.model.ISequenceDiagram;
import com.change_vision.jude.api.inf.presentation.ILinkPresentation;
import com.change_vision.jude.api.inf.presentation.INodePresentation;
import com.change_vision.jude.api.inf.project.ModelFinder;
import com.change_vision.jude.api.inf.project.ProjectAccessor;

public class CounterExampleSD {
	private ProjectAccessor projectAccessor;
	FdrWrapper wrapper;
	private Map<String, String> lifelinesMap;
	private List<String> lifelineBases = new ArrayList<String>();
	private String[] traceDeadlockArray;

	public CounterExampleSD() {
	}

	public void counterExampleInit(FdrWrapper wrapper) {

		try {
			projectAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
			Declarations declaration = Declarations.getInstance();
			preCreation(wrapper);
			CreateSequenceDiagrama(projectAccessor);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Verifica os contraexemplos e pega o primeiro que eh de deadlock
	 * 
	 * @param wrapper2
	 */
	private void preCreation(FdrWrapper wrapper2) {

		List<Object> counterExamples = wrapper2.getCounterExamples();
		/// verifica se o contraexemplo eh contrea exemplide deadlock
		List<Object> div = wrapper2.deadlockCounterExample(counterExamples);
		String traceDeadlock = wrapper2.traceDeadlock(div.get(0));
		traceDeadlockArray = this.splitContraexemplo(traceDeadlock);

	}

	/**
	 * Cria o diagrama de sequencia
	 * 
	 * 
	 * @param projectAccessor2
	 */

	private void CreateSequenceDiagrama(ProjectAccessor projectAccessor2) {

		IModel project;
		try {
			project = projectAccessor.getProject();
			// create sequence diagram
			TransactionManager.beginTransaction();
			SequenceDiagramEditor de;
			de = projectAccessor.getDiagramEditorFactory().getSequenceDiagramEditor();
			ISequenceDiagram newDgm = de.createSequenceDiagram(project, "CounterExample - " + LocalDate.now());

			// Creates the lifelines and position them properly in the sequence diagram

			List<INodePresentation> myLifelines = CreateLifelines(project, de);

			// create messages, combinedFragment, interactionUse, stateInvariant
		 	CreateMessages(de, myLifelines);

			TransactionManager.endTransaction();
			projectAccessor.save();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
	/**
	 *  retorna uma lista de INodePresentation  - lifeLines
	 * 
	 * @param project
	 * @param de      diagrama de sequencia
	 * @return         Lista de lifeLines
	 * @throws Exception
	 */
	private List<INodePresentation> CreateLifelines(IModel project, SequenceDiagramEditor de) throws Exception {

		List<INodePresentation> myLifelines = new ArrayList<INodePresentation>();

		// lifeline a partir das instancias
		Declarations declaration = Declarations.getInstance();
		HashSet<Instance> instances = declaration.getInstances();

		Iterator it = instances.iterator();

		// verifica ordem da lifeline primeira eh o primeiro evento do contraexemplo
		String msg = traceDeadlockArray[0];
		String semOp[] = msg.split("\\.");
		// saber qual o tipo componente da porta
		String nomeComponente = declaration.getTypeOfPort(semOp[0].trim());
		// o id eh semOp[1]
		int id = Integer.parseInt(semOp[1].trim());
		// qual o nome da instancia
		String lifeline = declaration.getInstanceNameByTypeId(nomeComponente, id);

		lifelineBases.add(lifeline);

		while (it.hasNext()) {

			Instance instance_temp = (Instance) it.next();
			if (instance_temp.getNum_id() != 0) {

				if (!instance_temp.getName().equalsIgnoreCase(lifeline)) {
					lifelineBases.add(instance_temp.getName());
				}
			}
		}

		double position = 0;
		
		INodePresentation objPs2 = de.createLifeline("Enviroment", position);
		ILifeline lifeline2 = (ILifeline) objPs2.getModel();
		//String boundary = declaration.getTypeInstance(lf);

	  //	System.out.println("boundary ------------" + boundary);
		//IClass boundaryclass = findNamedElement(findBasicComponentClass(), boundary, IClass.class);
		// com o nome da instancia recupero o tipo do componente
	    //lifeline1.setBase(new class()); // seta o tipo da lifeLine
		
		lifeline2.setTypeModifier("actor");
		position = position + 200;
		myLifelines.add(objPs2);

		
		
		for (String lf : lifelineBases) {

			System.out.println("lifelineBases---name :   " + lf);

			INodePresentation objPs1 = de.createLifeline(lf, position);
			ILifeline lifeline1 = (ILifeline) objPs1.getModel();
			String boundary = declaration.getTypeInstance(lf);

		  //	System.out.println("boundary ------------" + boundary);
			IClass boundaryclass = findNamedElement(findBasicComponentClass(), boundary, IClass.class);
			// com o nome da instancia recupero o tipo do componente
			lifeline1.setBase(boundaryclass); // seta o tipo da lifeLine
			position = position + 200;
			myLifelines.add(objPs1);
		}


			

		return myLifelines;
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
	 *  filtra apenas basicComponentClass
	 * 
	 * @return
	 * @throws Exception
	 */
	public INamedElement[] findBasicComponentClass() throws Exception {

		INamedElement[] allClass = findClass();
		INamedElement[] retorno = new INamedElement[allClass.length];
		int j = 0;

		for (INamedElement element3 : allClass) {
			IClass temp = (IClass) element3;

			if (temp.getStereotypes().length > 0) {
				if (temp.getStereotypes()[0].equalsIgnoreCase("BasicComponentClass")) {

					retorno[j] = temp;
					j++;
				}
			}

		}

		return retorno;

	}
	
	/**
	 *   Cria menssagens entre lifelines
	 *   
	 * @param de
	 * @param myLifelines
	 * @throws InvalidEditingException
	 */

	private void CreateMessages(SequenceDiagramEditor de, List<INodePresentation> myLifelines)
			throws InvalidEditingException {

		Declarations declaration = Declarations.getInstance();
		ArrayList<SetSinc> setsinc = declaration.getSetsinc(); 
		String lifeLine1 = "";
		String lifeLine2 = "";
		int position = 160;
		int req_prov;

		List<ILinkPresentation> msgs = new ArrayList<ILinkPresentation>();

		int k = 1;
		for (int j = 0; j < traceDeadlockArray.length; j++) {

			String msg = traceDeadlockArray[j];

			// System.out.println(msg);
			// elimina a operacao

			if (msg.trim().length() > 0) {
				String semOp[] = msg.split("\\.");

				// saber qual o tipo componente da porta
				String nomeComponente = declaration.getTypeOfPort(semOp[0].trim());

				// interface provide or required ?
				req_prov = declaration.getIsProvReq(semOp[0].trim());

				// o id eh semOp[1]
				int id = Integer.parseInt(semOp[1].trim());
				// qual o nome da instancia
				String instanceName = declaration.getInstanceNameByTypeId(nomeComponente, id);

				lifeLine1 = instanceName;
				
				String porta_sinc;
				if(semOp.length>2) {
					
					porta_sinc = semOp[0].trim() + "." + semOp[1].trim()+ "." + semOp[2].trim();
					
				}
				else {
				     porta_sinc = semOp[0].trim() + "." + semOp[1].trim();
				}

				String target;
				String instanceNameb = "";
				
				String channel1;
				String str1[];
				String channel2;
				String str2[];
				for (int i = 0; i < setsinc.size(); i++) {
					
					//str1 =setsinc.get(i).getChannel1().split("\\.");						  
				    //channel1 = str1[0] + "."+ str1[1];
				    
				    //str2 =setsinc.get(i).getChannel2().split("\\.");						  
				    //channel2 = str2[0] + "." +str2[1];
					
						 

				  if (porta_sinc.equalsIgnoreCase(setsinc.get(i).getChannel1())) {
					//if (porta_sinc.equalsIgnoreCase(channel1)) {
 
				    
						target = setsinc.get(i).getChannel2();
						//target =channel2;
						String semOp_b[] = target.split("\\.");

						// saber qual o tipo componente da porta
						String nomeComponenteb = declaration.getTypeOfPort(semOp_b[0].trim());
						// o id eh semOp[1]
						int idb = Integer.parseInt(semOp_b[1].trim());
						// qual o nome da instancia
						lifeLine2 = declaration.getInstanceNameByTypeId(nomeComponenteb, idb);
						break;
					}

					
					
   			if (porta_sinc.equalsIgnoreCase(setsinc.get(i).getChannel2())) {
					//if (porta_sinc.equalsIgnoreCase(channel2)) {


						target = setsinc.get(i).getChannel1();
						///target = channel1;
						
						String semOp_b[] = target.split("\\.");

						// saber qual o tipo componente da porta
						String nomeComponenteb = declaration.getTypeOfPort(semOp_b[0].trim());
						// o id eh semOp[1]
						int idb = Integer.parseInt(semOp_b[1].trim());
						// qual o nome da instancia
						lifeLine2 = declaration.getInstanceNameByTypeId(nomeComponenteb, idb);

						break;
					}

				}

				ILifeline life;
				int[] result = { -1, -1 };
				int idl = 0;

				for (INodePresentation lf : myLifelines) {
					life = (ILifeline) lf.getModel();
					if (life.getName().equals(instanceName)) {
						//System.out.println(life.getName() + "-------" + instanceName);
						result[0] = idl;
					}
					if (life.getName().equals(lifeLine2)) {
						//System.out.println(life.getName() + "-------" + lifeLine2);

						result[1] = idl;
					}
					idl++;
				}

				position = position + 50;

				// apresentar apenas as operacoes

				String msg_str[] = msg.split("\\.");
				
				msg = msg_str[2].trim();

				if(msg.length()==1) {
					
					msg = msg_str[3].trim();
				}
				
				// se a op é in ou  out
				
			    
				// se n contem ack
				if ((!msg.contains("_O"))
						&& (req_prov == 1))
				// e eh o componente pertencente eh um required -- ver a porta
				
				{
					String porta_other = declaration.getOtherChannelSinc(porta_sinc);

					// ha mesnagem no trace do contraexmplo

					// retirar o string "_I"

					String msgTemp = msg.replace("_I", "");

					msg = msgTemp;

					String msg_ack = porta_other + "." + msg + "_O";
			
				  
					ILinkPresentation msgSD;
					
					if(result[1] == -1)
					{
						msgSD = de.createMessage(msg, myLifelines.get(result[0]),
								myLifelines.get(0), position);
					}else {
					

					    msgSD = de.createMessage(msg, myLifelines.get(result[0]),
							myLifelines.get(result[1]), position);

					}
					// tem o par da comunicacao

					boolean temack = false;

					// verificar se a mensagem ack faz parte do trace

					for (int m = 0; m < traceDeadlockArray.length; m++) {
						if ((traceDeadlockArray[m].trim()).equalsIgnoreCase(msg_ack)) {

							temack = true;
							break;
						}

					}

					if (temack) {
						de.createReturnMessage("ack", msgSD);
					}
				} //
				
				// se env
				
			    Operation op = declaration.opByName(msg); 
				
			    
				if ( op!=null && op.getModifier().trim().equalsIgnoreCase("env")) {
					
				String porta_other = declaration.getOtherChannelSinc(porta_sinc);

					// ha mesnagem no trace do contraexmplo

					// retirar o string "_I"

					String msgTemp = msg.replace("_I", "");

					msg = msgTemp;

					String msg_ack = porta_other + "." + msg + "_O";
			
				  
					ILinkPresentation msgSD;
					
					//if(result[1] == -1)
					//{
						msgSD = de.createMessage(msg, 
								myLifelines.get(0), myLifelines.get(result[0]), position);
					//}else {
					

					  //  msgSD = de.createMessage(msg, myLifelines.get(result[0]),
						//	myLifelines.get(result[1]), position);

					//}
					// tem o par da comunicacao

					boolean temack = false;

					// verificar se a mensagem ack faz parte do trace

					for (int m = 0; m < traceDeadlockArray.length; m++) {
						if ((traceDeadlockArray[m].trim()).equalsIgnoreCase(msg_ack)) {

							temack = true;
							break;
						}

					}

					if (temack) {
						de.createReturnMessage("ack", msgSD);
					}

					
				}
					

			}

		}

	}
	
	/**
	 * 
	 * @param msgPosition
	 * @param events
	 * @param de
	 * @param myLifelines
	 * @param msgs
	 * @param msgsSpecification
	 * @param msgsImplementation
	 * @param msgType
	 * @param i
	 * @param msgSplit
	 * @return
	 * @throws InvalidEditingException
	 */

	private int BuildMessage(int msgPosition, Map<String, String> events, SequenceDiagramEditor de,
			List<INodePresentation> myLifelines, List<ILinkPresentation> msgs, List<String> msgsSpecification,
			List<String> msgsImplementation, int msgType, int i, String[] msgSplit) throws InvalidEditingException {

		int[] ids;
		// Just consider messages that are not return messages
		if (!msgSplit[1].equals("r")) {
			ids = findLifeline(msgSplit[2], msgSplit[3], myLifelines);
			if (ids[0] != -1 && ids[1] != -1) {
				String[] msgName = msgSplit[4].split("_");
				if (msgName.length >= 1 && !msgSplit[4].contains("_O")) {
					ILinkPresentation msg = de.createMessage(msgName[0], myLifelines.get(ids[0]),
							myLifelines.get(ids[1]), msgPosition);

					if (events.get(0).equals("endInteraction")
							&& !msgsImplementation.get(i).equals(msgsSpecification.get(i))) {
						msg.setProperty("line.color", "#FF0000");
					}

					if (!events.get(0).equals("endInteraction") && i == msgsSpecification.size() - 1) {
						msg.setProperty("line.color", "#FF0000");
					}

					if (msgType == 1) {
						IMessage m = (IMessage) msg.getModel();
						m.setAsynchronous(true);
					} else {
						msgs.add(msg);
					}

					msgPosition = msgPosition + 50;
				} else {
					IMessage message;
					for (ILinkPresentation msg : msgs) {
						message = (IMessage) msg.getModel();
						if (msgName[0].equals(message.getName())) {
							de.createReturnMessage("", msg);
							break;
						}
					}
				}
			}
		}
		return msgPosition;
	}

	private List<String> getMessages(String string) {
		List<String> msgs = new ArrayList<String>();
		String[] split = string.split(", ");
		for (int i = 0; i < split.length; i++) {
			if (!split[i].equals(""))
				msgs.add(split[i]);
		}
		return msgs;
	}

	private int[] findLifeline(String lfsrcID, String lfdestID, List<INodePresentation> myLifelines) {
		String lfName1 = lifelinesMap.get(lfsrcID);
		String lfName2 = lifelinesMap.get(lfdestID);
		String[] split1 = lfName1.split("_");
		String[] split2 = lfName2.split("_");
		int id = 0;
		int[] result = { -1, -1 };
		ILifeline life;

		for (INodePresentation lf : myLifelines) {
			life = (ILifeline) lf.getModel();
			if (life.getName().equals(split1[1])) {
				result[0] = id;
			}
			if (life.getName().equals(split2[1])) {
				result[1] = id;
			}
			id++;
		}
		return result;
	}

	private <T extends INamedElement> T findNamedElement(INamedElement[] children, String name, Class<T> clazz) {
		for (INamedElement child : children) {
			if (clazz.isInstance(child) && child.getName().equals(name)) {
				return clazz.cast(child);
			}
		}
		return null;
	}

	// transforma em array

	public String[] splitContraexemplo(String contra) {

		return contra.split(",");

	}

	class Actor{}
	
	
}
