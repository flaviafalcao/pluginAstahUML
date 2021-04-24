package plugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class Buffer {
	
	//classe que cria buffer
	
	// um buffer de interleave
	
	
	
	public String bufferInterleave() { // cria um buffer por tipo de instancia
		
		String buffer ="";
		
		
		 Declarations declaration = Declarations.getInstance();
			ArrayList<Input_Output> io = declaration.getInputoutput();
			HashSet<String> types = new HashSet<String>();
		//	String outputs_str = "";
			ArrayList<String> tmp = new ArrayList<String>();
			
			for (int i =0 ; i< io.size(); i ++) {
				
				types.add(io.get(i).getInstance_name());
			}			
			Iterator<String> it = types.iterator();
			
			while(it.hasNext()) {
				tmp.add((String)it.next());	
			}
			
			
			for(int j =0 ; j<tmp.size(); j++) {
			
//				BUFFER_FORK1_INTER_FORK2(ci,co, n) =
		//				  let
	//					    B(<>) = [] x : outputsC_FORK1_INTER_FORK2(ci) @ ci.x -> B(<x>)
			//			    B(s)  =    (co!head(s) -> B(tail(s)))
				//		              [] (#s<n & [] x : outputsC_FORK1_INTER_FORK2(ci) @ ci.x -> B(s^<x>))
					//	  within B(<>)
				
				
				
			buffer = buffer + "BUFFER_" + tmp.get(j) + "(ci,co, n) = \n" +
			        "let \n"+
					"B(<>) = [] x : outputsC_" +  tmp.get(j) + "(ci) @ ci.x -> B(<x>) \n"+
			        " B(s)  =    (co!head(s) -> B(tail(s))) \n"+
			        "[] (#s<n & [] x : outputsC_"+  tmp.get(j)+ "(ci) @ ci.x -> B(s^<x>)) \n"+
			        "within B(<>) \n  \n";

			
			
			}
		
			System.out.println(buffer);
	return buffer;
	}
	
   ////buffer comunicacao
	
	/// a cada conexao compor o buffer
	
	//buffer generico//
	
	//BUFFER_FORK1_INTER_FORK2_COMM_PHIL1_INTER_PHIL2_FEED_FEED(ci,co, n) =
		//	  let
			//    B(<>) = [] x : outputsC_FORK1_INTER_FORK2_COMM_PHIL1_INTER_PHIL2_FEED_FEED(ci) @ ci.x -> B(<x>)
			  //  B(s)  =    (co!head(s) -> B(tail(s)))
			    //          [] (#s<n & [] x : outputsC_FORK1_INTER_FORK2_COMM_PHIL1_INTER_PHIL2_FEED_FEED(ci) @ ci.x -> B(s^<x>))
			  // within B(<>)
	
	public String bufferAll() {
		String buffer ="BUFFER_All(ci,co, n) = \n "+
	         "let \n"+
			 "B(<>) = [] x : outputsC_All(ci) @ ci.x -> B(<x>) \n" +
	         "B(s)  =    (co!head(s) -> B(tail(s))) \n" +
			 " [] (#s<n & [] x : outputsC_All(ci) @ ci.x -> B(s^<x>)) \n" +
	         "within B(<>) \n \n";
	         
		
		System.out.println(buffer);
		return buffer;
	}
	
	
	
	
	/**
	 * 
	 *  BUFFER Comunicacao
	 * 
	 * 
	 * @return
	 */
	
	public String BufferIO() {
		
		String buffer ="";
		
		 Declarations declaration = Declarations.getInstance();
			ArrayList<Input_Output> io = declaration.getInputoutput();
			
			HashSet<String> types = new HashSet<String>();
		
			
			for (int i =0 ; i< io.size(); i ++) {
				
				types.add(io.get(i).getInstance_name());
			}			
			
		
			buffer ="BFIO_INIT(c1,c2) = BUFFER_All"  + "(c1,c2,1) \n" +
			  "||| BUFFER_All"  +"(c2,c1,1) \n \n";
	          //  }
	
	System.out.println(buffer);
	return buffer;
	}
	
}
