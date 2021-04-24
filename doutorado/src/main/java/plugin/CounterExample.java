package plugin;

import java.awt.geom.Point2D;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.editor.BasicModelEditor;
import com.change_vision.jude.api.inf.editor.ModelEditorFactory;
import com.change_vision.jude.api.inf.editor.StateMachineDiagramEditor;
import com.change_vision.jude.api.inf.editor.TransactionManager;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.model.IModel;
import com.change_vision.jude.api.inf.model.IState;
import com.change_vision.jude.api.inf.model.IStateMachine;
import com.change_vision.jude.api.inf.model.IStateMachineDiagram;
import com.change_vision.jude.api.inf.model.ITransition;
import com.change_vision.jude.api.inf.model.IVertex;
import com.change_vision.jude.api.inf.presentation.ILinkPresentation;
import com.change_vision.jude.api.inf.presentation.INodePresentation;
import com.change_vision.jude.api.inf.presentation.PresentationPropertyConstants;
import com.change_vision.jude.api.inf.project.ProjectAccessor;


public class CounterExample {

	private ProjectAccessor projectAccessor;
	FdrWrapper wrapper;
	List<dePara> deParalist = new ArrayList<dePara>();
	int stateid = 0;
	private static ArrayList<ExtendTransition> marcadas = new ArrayList<ExtendTransition>();

	private static ArrayList<ILinkPresentation> array_transitions = new ArrayList<ILinkPresentation>();

	public CounterExample() {
	}

	public void counterExampleInit(FdrWrapper wrapper) {

		try {
			projectAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
			Declarations declaration = Declarations.getInstance();
			showCounterExample(wrapper);
			CreateStateMachineD(projectAccessor);
			declaration.setArray_counter(this.getMarcados());
			// color();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void showCounterExample(FdrWrapper wrapper) {

		List<Object> counterExamples = wrapper.getCounterExamples();
		/// verifica se o contraexemplo eh de divergencia
		List<Object> div = wrapper.divCounterExample(counterExamples);

		String path = wrapper.traceDiv(div.get(0));

		// vejo as transicoes extendidas ( trs de uma maquina de estados)
		Declarations declaration = Declarations.getInstance();		
		ArrayList<ExtendTransition> ext = declaration.getExtendTransition();  

		ExtendTransition primeiro = null;
		// retorna o primeiro no

		for (int i = 0; i < ext.size(); i++) {

			if (ext.get(i).getSource().contains("Initial")) {
				primeiro = ext.get(i);
				marcadas.add(primeiro);
				break;
			}
		}

		// ver nas transicoes quais o target do primeiro
		ArrayList<ExtendTransition> s = getMesmaSource(primeiro.getTarget());

		// transforma contra exemplo em array

		String[] contraexemplo = splitContraexemplo(path);

		ArrayList<ExtendTransition> ts = new ArrayList<ExtendTransition>();
		ts.add(s.get(0));

		// retira o ultimo tau do contra exemplo
		int tam = contraexemplo.length - 2;
		String[] array = subarray(contraexemplo, 0, tam);

		verificaTransicao(array, ts, marcadas);

	}

	
	
	
	public void verifica(String[] contraexemplo, ExtendTransition ts, ArrayList<ExtendTransition> marcadas) {

		boolean verifica = true;

		String[] concat = arrayconcat(ts.getEvent_trigger(),ts.getEvent_guard(), ts.getEvent_action());

		String[] concatau = this.transforma(concat);

		System.out.println(Arrays.toString(concatau));

		// se tamanho dos eventos da transicao forem menores que o do contraexemplo
		// verifica == falso

		if (contraexemplo.length < concatau.length) {

			verifica = false;

		}

		else

		{
			// transforma as guardas e sets e gets
			int tam = concat.length;

			// extrair do path contraexemplo o tamanho do int

			String[] array = subarray(contraexemplo, 0, tam);

			for (int i = 0; i < array.length; i++) {

				if (!(concatau[i].trim()).equalsIgnoreCase(array[i].trim())) {

					verifica = false;
					break;
				}
			}

			System.out.println(verifica);

			if (verifica == true) {

				marcadas.add(ts);

				// ver o proximo
				// ver nas transicoes quais o target do primeiro
				ArrayList<ExtendTransition> s = getMesmaSource(ts.getTarget());

				String[] array_tmp = subarray(contraexemplo, tam, contraexemplo.length);
				verificaTransicao(array_tmp, s, marcadas);

			}

		}

		// return verifica;

	}

	public void verificaTransicao(String[] contraexemplo, ArrayList<ExtendTransition> ts,
			ArrayList<ExtendTransition> marcadas) {

		for (int k = 0; k < ts.size(); k++) {

			verifica(contraexemplo, ts.get(k), marcadas);

		}

		/*
		 * boolean verifica = true;
		 * 
		 * String[] concat = arrayconcat(ts.getEvent_guard(),ts.getEvent_action());
		 * 
		 * String[] concatau = this.transforma(concat);
		 * 
		 * System.out.println(Arrays.toString(concatau));
		 * 
		 * //transforma as guardas e sets e gets
		 * 
		 * 
		 * int tam = concat.length;
		 * 
		 * // extrair do path contraexemplo o tamanho do int
		 * 
		 * String[] array = subarray(contraexemplo,0,tam);
		 * 
		 * 
		 * for(int i =0; i <array.length ;i++) {
		 * 
		 * if(!(concatau[i].trim()).equalsIgnoreCase(array[i].trim())) {
		 * 
		 * verifica = false; break; } }
		 * 
		 * 
		 * System.out.println(verifica);
		 * 
		 * if(verifica = true) {
		 * 
		 * marcadas.add(ts);
		 * 
		 * // ver o proximo //ver nas transicoes quais o target do primeiro
		 * ArrayList<ExtendTransition> s = getMesmaSource( ts.getTarget());
		 * 
		 * String[] array_tmp = subarray(contraexemplo,tam ,contraexemplo.length -1);
		 * verificaTransicao(array_tmp, s.get(0), marcadas );
		 * 
		 * }
		 * 
		 * 
		 * 
		 * // return verifica;
		 * 
		 */
	}

	/// retorna as transicoes com a mesma origem

	public ArrayList<ExtendTransition> getMesmaSource(String name) {

		Declarations declaration = Declarations.getInstance();
		ArrayList<ExtendTransition> ext = declaration.getExtendTransition();
		ArrayList<ExtendTransition> retorno = new ArrayList<ExtendTransition>();

		for (int i = 0; i < ext.size(); i++) {

			if (ext.get(i).getSource().equalsIgnoreCase(name)) {

				retorno.add(ext.get(i));
			}

		}
		return retorno;

	}

	public void CreateStateMachineD(ProjectAccessor projectAccessor) {

		try {

			projectAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
			String name = Principal.nomeModelo;
			// create sequence diagram
			IModel project = projectAccessor.getProject();

			//
			Declarations declaration = Declarations.getInstance();
			ArrayList<MachineClone> clones = declaration.getMachineclones();

			MachineClone clone = clones.get(0);

			IStateMachine machine = clone.machine;

			String manchineName = machine.getName().trim().split("STM_")[1];
			
			

			Calendar calendar = Calendar.getInstance();
			
			
			
			//System.out.println("HOJE : " + calendar.get(Calendar.YEAR) + calendar.get(Calendar.MONTH)
				//	+ calendar.get(Calendar.DAY_OF_MONTH) + calendar.get(Calendar.HOUR_OF_DAY));

			

		
			// metodo para formatar

			IState[] states = machine.getStates();
			
			
	
					
					
			ITransition[] transitions = machine.getTransitions();
			
			IVertex[] vertices = machine.getVertexes();
			
			

			TransactionManager.beginTransaction();

			StateMachineDiagramEditor de = projectAccessor.getDiagramEditorFactory().getStateMachineDiagramEditor();

			IStateMachineDiagram newDgm = de.createStatemachineDiagram(project, "CounterExemplo" + calendar.get(Calendar.YEAR) + calendar.get(Calendar.MONTH)
			+ calendar.get(Calendar.DAY_OF_MONTH) + calendar.get(Calendar.HOUR_OF_DAY) + manchineName);
						
			de.setDiagram(newDgm);
			

			
			
		 

			// There are only two kinds of presentation APIs in Astah.
			// INodePresentation is for those presentations whose shape are like rectangle
			// like block and port.
			// ILinkPresentation is for those presentations whose shape are like line like
			// connector and association.
		    
			INodePresentation initialPseudostate = de.createInitialPseudostate(null, new Point2D.Double(0, 0));

			

		     ArrayList<INodePresentation> states_temp = new ArrayList<INodePresentation>();

		    states_temp = builStates(vertices, de);
		    
	       	states_temp.add(initialPseudostate);

			buildTransitions(states_temp, transitions, de);
//
			TransactionManager.endTransaction();
			projectAccessor.save();

		}

		catch (Exception e) {
			
			System.out.println("Excecao");
			e.printStackTrace();
			
		}

	}
	
	
	

	public void color() {

		try {

			projectAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
			// create sequence diagram
			IModel project = projectAccessor.getProject();

			TransactionManager.beginTransaction();

			// recuperar

			for (int i = 0; i < array_transitions.size(); i++) {

				for (int k = 0; k < marcadas.size(); k++) {

					if ((marcadas.get(k).getSource()).toString()
							.equalsIgnoreCase(array_transitions.get(i).getSource().toString())
							& (marcadas.get(k).getTarget()).toString()
									.equalsIgnoreCase(array_transitions.get(i).getTarget().toString())) {

						array_transitions.get(i).setProperty(PresentationPropertyConstants.Key.LINE_COLOR, "#FF0000");
					}

				}
			}

			TransactionManager.endTransaction();
			projectAccessor.save();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public ArrayList<ILinkPresentation> getMarcados() {

		ArrayList<ILinkPresentation> array = new ArrayList<ILinkPresentation>();

		for (int i = 0; i < array_transitions.size(); i++) {

		//	System.out.println("---->>" + array_transitions.get(i).getSource().toString());
		//	if (array_transitions.get(i).getSource().toString().startsWith("Initial")) {
		//		array.add(array_transitions.get(i));

		//	}

			for (int k = 0; k < marcadas.size(); k++) {

				if ((marcadas.get(k).getSource()).toString()
						.equalsIgnoreCase(array_transitions.get(i).getSource().toString())
						& (marcadas.get(k).getTarget()).toString()
								.equalsIgnoreCase(array_transitions.get(i).getTarget().toString())) {

					array.add(array_transitions.get(i));
					// array_transitions.get(i).setProperty(PresentationPropertyConstants.Key.LINE_COLOR,
					// "#FF0000");
				}

			}
		}

		return array;
	}

	/// metodo que formata em estados
	public ArrayList<INodePresentation> builStates(IVertex[] vertices, StateMachineDiagramEditor de) {

		double x = 200;
		double y = 0;
		ArrayList<INodePresentation> states_temp = new ArrayList<INodePresentation>();
		Map.Entry me2  = null;
		
		

		try {
			for (int i = 0; i < vertices.length; i++) {
				if (!vertices[i].getName().startsWith("Initial")) {
					
					
				System.out.println("vertices[i].getContainer().getId() -----> " + ((INodePresentation) vertices[i].getPresentations()[0]).getLocation());
					
				Point2D point = ((INodePresentation) vertices[i].getPresentations()[0]).getLocation();
				double width =  ((INodePresentation) vertices[i].getPresentations()[0]).getWidth();
				double height = ((INodePresentation) vertices[i].getPresentations()[0]).getHeight();
				
				      
				x = point.getX();
				y = point.getY();
			
					
					
				//	System.out.println("name.point.x" +  vertices[i].getPresentations()[0].getProperty("name.point.x"));
				//	System.out.println("------------------------------------------------------");
				//			vertices[i].getPresentations().length + "-----------------------------");
			//		System.out.println("------------------------------------------------------");
				
				
				
				 
					
									
					
					// There are only two kinds of presentation APIs in Astah.
					// INodePresentation is for those presentations whose shape are like rectangle like block and port.
					// ILinkPresentation is for those presentations whose shape are like line like connector and association.
					
					INodePresentation tmp = de.createState(vertices[i].getName(), null, new Point2D.Double(x, y)); 					
					
				    tmp.setProperty("action_visibility", "true");
				    
				    
				//	Iterator iterator0 = ((INodePresentation)vertices[i].getPresentations()[0]).getProperties().entrySet().iterator();
					
				    Iterator iterator0 = 	tmp.getProperties().entrySet().iterator();
				    
					while (iterator0.hasNext()) {
						  me2 = (Map.Entry) iterator0.next();
				          System.out.println("Key: "+me2.getKey() + " & Value: " + me2.getValue());	
				    }
				    
				    
				//   tmp.setHeight(height);
                //    tmp.setWidth(width);
                    
				 	tmp.setProperty(me2.getKey().toString(), me2.getValue().toString());
					
					
					
					
					states_temp.add(tmp);
					System.out.println("aqui");
					

				}
			}
			
	
		} catch (Exception e) {
			
			e.printStackTrace();

		}
		return states_temp;

	}

	public void buildTransitions(ArrayList<INodePresentation> states_temp, ITransition[] transitions,
			StateMachineDiagramEditor de) {
		
		 Map.Entry me2 = null;

		try {

			for (int i = 0; i < transitions.length; i++) {

				int s = getIndex(states_temp, transitions[i].getSource().getName());
				int t = getIndex(states_temp, transitions[i].getTarget().getName());
				
				Point2D[]  points = ((ILinkPresentation) transitions[i].getPresentations()[0]).getAllPoints();
				
				Point2D[]  pointsX = ((ILinkPresentation) transitions[i].getPresentations()[0]).getPoints();
				
				ILinkPresentation transition = de.createTransition(states_temp.get(s), states_temp.get(t));
				
			
             //  transition.setAllPoints(points);				
			
				
			
	            		
						
				//Iterator iterator0 = transitions[i].getPresentations()[0].getProperties().entrySet().iterator();
				
			 //	System.out.println("-------" + transitions[i].getSource().getName()  + "---------------------------------");
			 //	System.out.println("-------" + transitions[i].getTarget().getName() + "---------------------------------");
		    //		while (iterator0.hasNext()) {
			//           me2 = (Map.Entry) iterator0.next();
			//        System.out.println("Key: "+me2.getKey() + " & Value: " + me2.getValue());
			    //    } 
				
			//	System.out.println("-------------------------------------------------------");
			
				
			
				if (!transitions[i].getSource().getName().startsWith("Initial")) {
				
					
					String label0 = transitions[i].getEvent() ;
					String label1 = "[" + transitions[i].getGuard() + "]";
					String label2 = "/" +  transitions[i].getAction();
					
					transition.setLabel(label0 + label1 + "" + label2);
			 
					
				//	Point2D[]  points2 = transition.getAllPoints();
				//	System.out.println("-------------------" +  i   + "------------------------------------");
				//	System.out.println("-------" + transitions[i].getSource().getName()  + "---------------------------------");
				//	System.out.println("-------" + transitions[i].getTarget().getName() + "---------------------------------");
				
						
						Point2D[]  points3 =  new Point2D[points.length];
						
						for(int h =0 ; h <pointsX.length ; h++) {
							
							
							Point2D  temp =	(new Point2D.Double(points[0].getX() , points[0].getY() ));
							
							points3[h] = temp;
						}
						
						
											
						
						//transition.setAllPoints(points3);
					//	transition.setAllPoints(arg0);
					
					
					
						
				//	System.out.println(points2[0].getX());
				//	System.out.println(points2[0].getY());
					
				//	System.out.println(points2[1].getX());
				//	System.out.println(points2[1].getY());
					
				//	System.out.println("-------------------------------------------------------");
					 
					
					
				//	transition.setProperty(me2.getKey().toString(),  me2.getValue().toString());
					
				//	Iterator iterator = transition.getProperties().entrySet().iterator();
					
				//	while (iterator.hasNext()) {
			    //       Map.Entry me1= (Map.Entry) iterator.next();
			  //        System.out.println("Key: ->>>>> "+me1.getKey() + " & Value:  ---->" + me1.getValue());
			   //     } 

				}
				array_transitions.add(transition);

			}
			
			
	
			
			
			

		} 
		 
		
		catch (Exception e) {
		
			
		e.printStackTrace();
		}
		
		

	}

	public void buildCounterExample(ProjectAccessor projectAccessor) {

		try {
			projectAccessor = AstahAPI.getAstahAPI().getProjectAccessor();

			String name = Principal.nomeModelo;
			// create sequence diagram
			IModel project = projectAccessor.getProject();
			// System.out.println(entrada.size());

			TransactionManager.beginTransaction();
			StateMachineDiagramEditor de = projectAccessor.getDiagramEditorFactory().getStateMachineDiagramEditor();
			IStateMachineDiagram newDgm = de.createStatemachineDiagram(project, "DivergenceCounterExample" + name);
			de.setDiagram(newDgm);

			// There are only two kinds of presentation APIs in Astah.
			// INodePresentation is for those presentations whose shape are like rectangle
			// like block and port.
			// ILinkPresentation is for those presentations whose shape are like line like
			// connector and association.
			INodePresentation initialPseudostate = de.createInitialPseudostate(null, new Point2D.Double(0, 0));

			// criar os nos
			// elementos dintintos no deParalist
			// uso um HashSet

			ArrayList<INodePresentation> states = new ArrayList<INodePresentation>();
			boolean achou = false;

			int x = 200;
			int y = 0;

			for (int i = 0; i < deParalist.size(); i++) {

				achou = false;
				for (int j = 0; j < states.size(); j++) {

					if (// deParalist.get(i).n_destino.equalsIgnoreCase(states.get(j).getLabel()) ||
					deParalist.get(i).n_origem.equalsIgnoreCase(states.get(j).getLabel())) {

						achou = true;
						// break;
					}
				}

				if (achou == false) {

					INodePresentation tmp = de.createState(deParalist.get(i).n_origem, null, new Point2D.Double(x, y)); // (X,Y)
					tmp.setProperty("action_visibility", "true");
					y = y + 115;
					x = x + 100;

					states.add(tmp);

				}

			}

			///// ----

			for (int i = 0; i < deParalist.size(); i++) {

				achou = false;
				for (int j = 0; j < states.size(); j++) {

					if (deParalist.get(i).n_destino.equalsIgnoreCase(states.get(j).getLabel()) // ||
					// deParalist.get(i).n_origem.equalsIgnoreCase(states.get(j).getLabel())
					) {

						achou = true;
						// break;
					}
				}

				if (achou == false) {

					INodePresentation tmp = de.createState(deParalist.get(i).n_destino, null, new Point2D.Double(x, y)); // (X,Y)
					tmp.setProperty("action_visibility", "true");
					y = y + 115;
					x = x + 100;

					states.add(tmp);

				}

			}

			// transicoes

			ILinkPresentation transitionUM = de.createTransition(initialPseudostate, states.get(0));

			int indice_o;
			int indice_d;

			for (int i = 0; i < deParalist.size(); i++) {

				// funcao q retona o indice do stado
				indice_o = getIndex(states, deParalist.get(i).n_origem);
				indice_d = getIndex(states, deParalist.get(i).n_destino);

				ILinkPresentation transition = de.createTransition(states.get(indice_o), states.get(indice_d));
				transition.setLabel(deParalist.get(i).evento);
				transition.setProperty("line.shape", "line_right_angle"); //
			}
			TransactionManager.endTransaction();
			projectAccessor.save();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public Map<Integer, List<String>> describeCounterExample(FdrWrapper wrapper) {

		List<Object> counterExamples = wrapper.getCounterExamples();
		Map<Integer, List<String>> result = new HashMap<Integer, List<String>>();

		for (int i = 0; i < counterExamples.size(); i++) {
			buildCounterExample(counterExamples.get(i), wrapper);
			System.out.println("Preencheu no id " + i);
		}
		return result;
	}

	private void buildCounterExample(Object counterExample, FdrWrapper wrapper) {
		List<no> path = new ArrayList<no>();
		try {
			wrapper.getDivergence(counterExample);
			path = wrapper.getpath();
			createState(path);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// criar nome dos estados da maquina
	// fazer um loop

	private void createState(List<no> pre) {

		// se o stado ja tem nome

		// numero - nome - evento - numero- nome

		no tmp;
		for (int i = 0; i < pre.size(); i++) {

			tmp = pre.get(i);
			String tmp_name_o = "";
			String tmp_name_d = "";

			// verifico se ja tem nome

			if (getName(tmp.origem).length() > 0) {
				tmp_name_o = getName(tmp.origem);
			} else {
				tmp_name_o = geraNome(); // proximo nome de estado
			}

			if (getName(tmp.destino).length() > 0) {
				tmp_name_d = getName(tmp.destino);
			} else {
				tmp_name_d = geraNome();
			}

			dePara e = new dePara();
			e.origem = tmp.origem;
			e.n_origem = tmp_name_o;
			e.destino = tmp.destino;
			e.n_destino = tmp_name_d;
			e.evento = tmp.evento;

			// System.out.println(e.origem );
			System.out.println(e.n_origem);
			System.out.println(e.evento);
			// System.out.println(e.destino );
			System.out.println(e.n_destino);

			deParalist.add(e);

		}

		// caso nao

	}

	public String geraNome() {

		int index = stateid + 1;
		stateid = index;
		return "state_" + index;

	}

	// se um dado numero ja tem nome

	public String getName(long od) {

		String retorno = "";

		for (int i = 0; i < deParalist.size(); i++) {

			if (deParalist.get(i).destino == od) {
				retorno = deParalist.get(i).n_destino;
				break;
			}
			if (deParalist.get(i).origem == od) {
				retorno = deParalist.get(i).n_origem;
				break;
			}

		}

		return retorno;
	}

	public int getIndex(ArrayList<INodePresentation> states, String name) {
		int retorno = -1;

		for (int i = 0; i < states.size(); i++) {

			if (states.get(i).getLabel().equals(name)) {
				retorno = i;
				break;
			}
		}

		return retorno;
	}

	/// funcao auxiliar para concatenar arrays
	// 
	public String[] arrayconcat(String[] a, String[] b, String[] c ) {

		String[] result = null;
		int len_a;
		int len_b;
		int len_c;
		
		if (a != null && b != null &&  c!= null) {
			
			len_a = a.length;
			len_b = b.length;
			len_c = c.length;			

			
			result = new String[len_a + len_b + len_c];

			System.arraycopy(a, 0, result, 0, len_a);
			System.arraycopy(b, 0, result, len_a, len_b);
			System.arraycopy(c, 0, result, len_a + len_b , len_c);
			System.out.println("------------ARRAY---------" + Arrays.toString(result) );
		}
		
		  else if (a == null && c== null) {

			   result = b;
			
		    }
		
		  else if (b== null  && c==null) {
		    	
		    	result =a;
		    }
		
		  else if( a== null & b ==null) {
		    	
		    	result =c;
		    }
		  
		    
		    else if (b==null && a!=null && c!=null)
		    {   
		    	len_a = a.length;
				len_c = c.length;			

		    	
		    	result = new String[len_a + len_c];
		    	System.arraycopy(a, 0, result, 0, len_a);
				System.arraycopy(c, 0, result, len_a, len_c);
		    	
		    }
		
		    else if (a==null && b!=null && c!=null)
		    {   
		    	
				len_b = b.length;
				len_c = c.length;			

		    	result = new String[len_b + len_c];
		    	System.arraycopy(b, 0, result, 0, len_b);
				System.arraycopy(c, 0, result, len_b, len_c);
		    	
		    }
		
		    else if (c==null && a!=null && b!=null)
		    {   
		    	len_a = a.length;
				len_b = b.length;

		    	result = new String[len_a + len_b];
		    	System.arraycopy(a, 0, result, 0, len_a);
				System.arraycopy(b, 0, result, len_a, len_b);
		    	
		    }
		
		
		 
		
		System.out.println("------------ARRAY---------" + Arrays.toString(result) );
		
		
		return result;

	}

	// separar por virgula

	public String[] splitContraexemplo(String contra) {

		return contra.split(",");

	}

	// subarray
	public String[] subarray(String[] array, int beg, int end) {

		return Arrays.copyOfRange(array, beg, end);
	}

	// trabsformar os eventos em tau
	// e coloca 0 onde for id

	public String[] transforma(String[] contraexemplo) {

		String[] retorno = new String[contraexemplo.length];

		for (int i = 0; i < contraexemplo.length; i++) {

			if (contraexemplo[i].contains("set_") || contraexemplo[i].contains("internal")) {

				retorno[i] = new String("τ");
			} else {
				retorno[i] = contraexemplo[i];
			}

		}

		// se guarda igual a tau

		// se set ou get igual a tau

		// replace id por 0

		for (int j = 0; j < retorno.length; j++) {

			retorno[j] = retorno[j].replaceAll("id", "0");
		}

		return retorno;
	}

	class dePara {

		int origem;
		String n_origem;
		String evento;
		int destino;
		String n_destino;

	}

}
