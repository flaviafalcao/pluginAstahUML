package plugin;

import java.util.Arrays;
import java.util.regex.Pattern;

public class testeSplit {
	
	
	public static void main(String args[]) {
		
		testeSplit teste = new testeSplit();
		
		String[] teestestr =  new String[2];
		
		teestestr[0]= "return";
		String part_action_temp =  teestestr[0].replace("return()", "");
		
		String teste12 = "gggg?z";
		String[] teestestr2 =  teste12.split("[?!]");
		
		
	//	System.out.println("resultado -------->"  +  teestestr2[0]);
	//	System.out.print("resultado ->>" + part_action_temp);
	/*	String teste = "abacaxi -> melao -> laranja"; 
		System.out.println(teste.split("->").toString());
		
		String a = Arrays.toString(teste.split("->"));
		String b = Arrays.toString(teste.split("->"));
		
		//System.out.println(a.concat(b));
		
		String t = new String(a);
		String r = new String(b);
		
		
		System.out.println("ola " +t + r);*/
		/*String x = "abacaxi -> melao -> laranja"; 
		String y = "pera -> melao -> laranja"; */
		
		String[] a= {"port_env"};
		String[] b = {"internal"};
		String[] c = {"return9cache"};
		
		String[] retorno = teste.arrayconcat(a, b,c);
		
		System.out.println(retorno.length); 
		System.out.println( Arrays.toString(retorno));
	}
	
	public String[] arrayconcat(String[] a , String[] b) {
		
		
		int len_a = a.length;
	    int len_b = b.length;
	    String[] result = new String[len_a + len_b];
	    
	    System.arraycopy(a, 0, result, 0, len_a);
	    System.arraycopy(b, 0, result, len_a, len_b);
	    System.out.println(Arrays.toString(result));
		
		
		return result;
		
		
	}
	
	public String[] arrayconcat(String[] a , String[] b,  String[]c){
		
		
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
		return result;

	}

}
