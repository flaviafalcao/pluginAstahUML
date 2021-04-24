package plugin;

public class Util {
	
	public static String op_I[] = new String[2];
	public static String op_O[] = new String[2];
	public static String id_componente;
	public static String portas;
	public static String processo_stm;
	public static String componente_nm;
	
	
	
	
	//instancia
	
	private static Util instancia;
	
	
	public static synchronized Util getIstance() {
		
		if(instancia ==null) {
			instancia = new Util();
		}
		return instancia;	
	}

	

}

