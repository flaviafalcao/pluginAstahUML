package plugin;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;

/*
 * 
 * mudar o character encoding para UTF-8 
 * o default do eclipse é 1252
 */

import java.util.*;

public class LTS {

	public static List<Object> lista; // = new ArrayList<Node>();
	public static List<no> path;// = new ArrayList<no>();
	public static List<no> pathsemtau;// = new ArrayList<no>();
	public static ArrayList<novisitado> visitados;// = new ArrayList<novisitado>();
	public static String root;
	public FdrWrapper wrapper;
	public static ArrayList<novisitado> visitadosLoop;// = new ArrayList<novisitado>();

	public LTS() {
		lista = new ArrayList<Object>();
		path = new ArrayList<no>();
		pathsemtau = new ArrayList<no>();
		visitados = new ArrayList<novisitado>();
		visitadosLoop = new ArrayList<novisitado>();

	}

//    public static void main(String[] args) {
//       
//		try {
//			
//			System.out.println("aaaaa");
//		
//			System.out.println(fdr.version());
//	        Session session = new Session();
//               // String pathFile = "C:/Users/flavi/OneDrive/Documentos/doutorado/csp/lts_csp.csp";
//           //  String pathFile = "C:/Users/flavi/OneDrive/Documentos/doutorado/csp/testes_protocolo.csp";
//	       // String pathFile = "C:/Users/flavi/OneDrive/Documentos/doutorado/csp/versao_generica.csp";
//	        
//	       // String pathFile = "C:/Users/flavi/OneDrive/Documentos/doutorado/csp/ts_tau_teste.csp";
//	       // String  pathFile =  "C:/Users/flavi/OneDrive/Documentos/doutorado/ringbuffer/teste_dalay.csp";
//	        
//	       // String pathFile = "C:/Users/flavi/OneDrive/Documentos/doutorado/Dropbox/doutorado/teste.csp";
//	        String pathFile = "C:/Users/flavi/eclipse-workspace/ref/teste.csp";
//            session.loadFile(pathFile);
//            
//	         
//	        
//	        
//	        //String processName = "PHIL1";
//	       // String processName = "protocolo_lts";
//	        String processName = "protocolo2";
//	        
//	        MachineEvaluatorResult mEvaluatorResult =  session.evaluateProcess(processName, SemanticModel.Traces, new Canceller());
//            Machine machine = mEvaluatorResult.result();
//	       	        
//	        Node node = machine.rootNode();
//	        
//	        LTS demo = new LTS();
//	        demo.traces(node, machine, session,lista);
//	        
//	        System.out.println("tam lista" + lista.size());
//	        System.out.println(path.size());
//	        
//	        
//	    //  for(int i= 0; i<path.size() ;i++) {
//	        	
//	      //  	System.out.println("(" + path.get(i).origem +"," + path.get(i).evento +","+ path.get(i).destino +")");
//	 	       
//	        //}
//	        
//	        demo.varre();
//	        
//	        Iterator<no> it = pathsemtau.iterator();
//	        no tmp = demo.new no();
//	        String temp = "";
//	        while(it.hasNext()) {
//	           
//	        	tmp = it.next();
//	        	temp = temp + "(" +tmp.origem +"," + tmp.evento +","+ tmp.destino +") \n";
//	        	System.out.println("(" +tmp.origem +"," + tmp.evento +","+ tmp.destino +")");
//	        }
//	        
//	        System.out.println(pathsemtau.size());
//	        
//			//FileWriter arquivo; 
//			//String write = "include \"teste.csp\" \n";
//	        //arquivo = new FileWriter(new File("C:/Users/flavi/OneDrive/Documentos/doutorado/Dropbox/doutorado/Arquivo_protocolo.csp"));  
//	        //write = write + temp;
//	        //arquivo.write(write);
//			//arquivo.close();
//	        destroy();
//	   		}
//	   // catch (InputFileError error) {
//	     //   System.out.println(error);
//	        
//	    //}
//		
//		catch(Exception e) {
//			
//			  System.out.println(e);
//		}
//	   
//	    fdr.libraryExit();
//	    
//        	//for ( int x =0; x< machine.transitions(tmp).size();x++) {
//        	
//        	///System.out.println("transition -->  " + session.uncompileEvent(machine.transitions(tmp).get(x).event()));
//        	//}
//        	//System.out.println("trace");
//        	
//        	//proximo
//        }
//    
	public String root() {

		return root;
	}

	private static void destroy() {

		path = null;
		lista = null;
		pathsemtau = null;
		visitados = null;
	}
	
	

	public String protocolo(String url, String protocolo, String componentName) {

		String temp = "";
		String retorno = "";
		path = new ArrayList<no>();
		lista = new ArrayList<Object>();

		try {

			String pathFile = url;
			wrapper = FdrWrapper.getInstance();

			wrapper.loadFile(pathFile);
			String processName = protocolo;

			Object machine = wrapper.getMachineEvaluatorResult(processName); // machine
			Object root_tmp = wrapper.getrootNode(machine); // machineroot

			root = wrapper.getHashCode(root_tmp) + "";

			traces(root_tmp, machine, wrapper.getSession(), lista);

			Iterator<no> it = path.iterator();
			no tmp ;

			while (it.hasNext()) {

				tmp = it.next();
				String _str = "";
				_str = tmp.evento;
				if (tmp.evento.equals(new String("τ"))) {
					_str = "tau";
				}

				temp = temp + "(" + tmp.origem + "," + _str + "," + tmp.destino + "),\n";

			}

			if (temp.length() > 0) {
				temp = temp.substring(0, temp.length() - 2);
			}

			String tagName = "tag_" + componentName;

			retorno = temp.replaceAll(tagName, "w");

			while (retorno.contains(tagName)) {
				String retorno1 = retorno.replace(tagName, "w");
				retorno = retorno1;
			}

		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return retorno;

	}

	// retorna o trace de um processo//

	/*
	 * public String protocolo_trace(String url, String protocolo) {
	 * 
	 * String temp = ""; String retorno = "";
	 * 
	 * try {
	 * 
	 * String pathFile = url; wrapper = FdrWrapper.getInstance();
	 * 
	 * wrapper.loadFile(pathFile); String processName = protocolo;
	 * 
	 * Object machine = wrapper.getMachineEvaluatorResult(processName); // machine
	 * Object root_tmp = wrapper.getrootNode(machine); // machineroot
	 * 
	 * root = wrapper.getHashCode(root_tmp) + "";
	 * 
	 * traces(root_tmp, machine, wrapper.getSession(), lista);
	 * 
	 * varre();
	 * 
	 * Iterator<no> it = pathsemtau.iterator(); no tmp = new no();
	 * 
	 * while (it.hasNext()) {
	 * 
	 * tmp = it.next(); String _str = ""; _str = tmp.evento; if
	 * (tmp.evento.equals(new String("τ"))) { _str = "tau"; }
	 * 
	 * temp = temp + "(" + tmp.origem + "," + _str + "," + tmp.destino + "),\n";
	 * 
	 * }
	 * 
	 * if (temp.length() > 0) { temp = temp.substring(0, temp.length() - 2); }
	 * 
	 * retorno = temp.replaceAll("tag", "w");
	 * 
	 * while (retorno.contains("tag")) { String retorno1 = retorno.replace("tag",
	 * "w"); retorno = retorno1; } }
	 * 
	 * catch (Exception e) { e.printStackTrace(); }
	 * 
	 * return retorno; }
	 */

	public void traces(Object root, Object machine, Object session, List lista) {

		Object node = root;
		no n;

		AbstractList transitionList = wrapper.getTransitions(machine, node);

		for (int i = 0; i < transitionList.size(); i++) {

			n = new no();

			n.origem = wrapper.getHashCode(node);

			Object transition = wrapper.getTransitionIndex(transitionList, i);

			Long event = wrapper.getEvent(transition);
			n.evento = wrapper.getUncompileEvent(event).toString();

			Object tmp = wrapper.getDestination(transition);
			
			n.destino = wrapper.getHashCode(tmp);

		////////////////////////
		//	System.out.print("(");
		//	System.out.print(n.origem + ",");
		//	System.out.print(n.evento);
		//	System.out.println("," + n.destino + " ),");

		///////////////////////////////////

			lista.add(node);
			path.add(n);

			if (!lista.contains(tmp)) {
				this.traces(tmp, machine, session, lista);
			}
		}

	}



	class no {

		int origem;
		int destino;
		String evento;

		@Override
		public boolean equals(Object o) {
			if (this.origem == ((no) o).origem && (this.evento).equals(((no) o).evento)
					&& this.destino == ((no) o).destino)
				return true;

			else

				return false;
		}
	}
	
	
	

	class novisitado {
		int origem;
		ArrayList<no> visitou = new ArrayList<no>();

	}

	class noLoop {

		no inicial;
		ArrayList<no> visitou = new ArrayList<no>();

	}

	public void varre() {

		for (int i = 0; i < path.size(); i++) {
			ArrayList<no> primeiro = new ArrayList<no>();
			primeiro.add(path.get(i));
			ehtau(primeiro, path.get(i).origem);
			primeiro = null;

		}

	}

	private ArrayList<no> proximo(no init, List<no> path) {
		ArrayList<no> proximos = new ArrayList<no>();
		// no tn = new no();
		for (int j = 0; j < path.size(); j++) {
			if (init.destino == path.get(j).origem) {
				if (!proximos.contains(path.get(j))) {
					// System.out.println(path.get(j).evento + "' " + path.get(j).destino);
					proximos.add(path.get(j));
				}
			}
		}
		return proximos;
	}

	private void ehtau(ArrayList<no> i, int origem) {

		no e = new no();
		// no temp = new no();
		ArrayList<no> proximos;
		ArrayList<no> visitado_temp;
		if (i != null) {

			for (int j = 0; j < i.size(); j++) {

				visitado_temp = getVisitados(origem);

				/// ver se ha problemas aqui

				if (i.get(j).evento.equalsIgnoreCase(new String("τ"))) {
					// varreu(i.get(j).destino) ||

					ArrayList<noLoop> tmp = new ArrayList<noLoop>();
					if ((i.get(j).origem == i.get(j).destino) || passou(visitado_temp, i.get(j).destino)

					) {
						// nao coloca no array principal

					} else {

						if (!pathsemtau.contains(i.get(j))) {
							pathsemtau.add(i.get(j));

						}

					}

					if (!visitado_temp.contains(i.get(j)) || visitado_temp.isEmpty() || visitado_temp == null)

					{
						addVisitados(origem, i.get(j));
						proximos = proximo(i.get(j), path);
						if (!proximos.isEmpty()) {
							// ehtau(proximos,origem);
							ehtau(proximos, i.get(j).destino);
						}

					}

				}

				else {

					if (!visitado_temp.contains(i.get(j)) || visitado_temp.isEmpty() || visitado_temp == null)

					{
						addVisitados(origem, i.get(j));

						proximos = proximo(i.get(j), path);
						if (!proximos.isEmpty()) {
							// ehtau(proximos,origem);
							ehtau(proximos, i.get(j).destino);

						}

					}

					if (!pathsemtau.contains(i.get(j))) {
						pathsemtau.add(i.get(j));

					}
				}

			}

		}

	}

	private ArrayList<no> getVisitados(int param) {
		ArrayList<no> retorno = new ArrayList<no>();
		for (int j = 0; j < visitados.size(); j++) {

			if (visitados.get(j).origem == param) {
				retorno = visitados.get(j).visitou;
				break;
			}

		}

		return retorno;
	}

	private boolean varreu(int destino) {
		boolean retorno = false;
		for (int i = 0; i < pathsemtau.size(); i++) {

			if (pathsemtau.get(i).origem == destino) {
				retorno = true;
				break;
			}
		}

		return retorno;

	}

	public boolean passou(List<no> path, int noh) {
		boolean retorno = false;
		for (int i = 0; i < path.size(); i++) {

			if (path.get(i).origem == noh) {
				retorno = true;
				break;
			}
		}

		return retorno;

	}

	public void addVisitados(int param, no adiciona) {
		boolean achou = false;
		for (int j = 0; j < visitados.size(); j++) {
			if (visitados.get(j).origem == param) {
				visitados.get(j).visitou.add(adiciona);
				achou = true;
				break;
			}
		}

		if (achou == false) {
			novisitado novo = new novisitado();
			novo.origem = param;
			novo.visitou.add(adiciona);
			visitados.add(novo);

		}

	}

	public ArrayList<no> getArrayloop(no n, ArrayList<noLoop> tmp) {

		ArrayList<no> retorno = new ArrayList<no>();

		for (int i = 0; i < tmp.size(); i++) {

			if (tmp.get(i).inicial.destino == n.destino && tmp.get(i).inicial.origem == n.origem
					&& tmp.get(i).inicial.evento.equalsIgnoreCase(n.evento)) {

				retorno = tmp.get(i).visitou;
				break;

			}
		}
		return retorno;
	}

	public boolean pertenceArray(no n, ArrayList<no> r) {

		boolean retorno = false;

		for (int i = 0; i < r.size(); i++) {

			if (r.get(i).destino == n.destino && r.get(i).origem == n.origem
					&& r.get(i).evento.equalsIgnoreCase(n.evento)) {

				retorno = true;
			}
		}

		return retorno;

	}

}
