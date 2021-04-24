package plugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class OutputCFunction {
	
	//criar funcao C
	
	// criar uma funcao output C para cada tipo de componente
	
	
	//outputsC_FORK1_INTER_FORK2(c) = { x | x <- extensions(c), member(c.x, outputs_FORK1_INTER_FORK2)} 

	
   public String outputFunctionType() {
	   
	    Declarations declaration = Declarations.getInstance();
		ArrayList<Input_Output> io = declaration.getInputoutput();
		HashSet<String> types = new HashSet<String>();
		ArrayList<String> tmp = new ArrayList<String>();
		
		for (int i =0 ; i< io.size(); i ++) {
			
			types.add(io.get(i).getInstance_name());
		}
		
		Iterator<String> it = types.iterator();
		
		while(it.hasNext()) {
			
			tmp.add((String)it.next());	
			
		}
		
		
		ArrayList<String> outputs;
		 
		String  outputs_func ="";
		
		for(int j =0 ; j<tmp.size(); j++) {
			
		outputs = declaration.getOutputSemTeste(tmp.get(j));
			
		if(outputs.size()>0) {	
		outputs_func =  outputs_func  + "outputsC_"  + tmp.get(j) + "  (c) = { x | x <- extensions(c), member(c.x, outputs_"
				 + tmp.get(j) + ")}" + "\n";
		
		}
		
		}
		System.out.println(outputs_func);
		
	   return outputs_func;
   }
	
	// criar um funcao output C para a composicao 
   
   
   public String outputFunctionTypeAll() {
	   
	   	
		String outputs_func =   "outputsC_All (c) = { x | x <- extensions(c), member(c.x, outputs_all)}" + "\n";	
		
		
		System.out.println(outputs_func);
		
	   return outputs_func;
  }


}
