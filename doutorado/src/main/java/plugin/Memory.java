package plugin;

public class Memory {
	
	private String type;  //nome do componente
	private String name;  // nome do internal
	private String str_guard;  // expressao booleana
	private String exp; // expressao guard + internal   ou expressao guard + trigger 
	private String target;	// target da transicao 
	private String[] trigger;  // caso a guarda tiver uma trigger associada 
	private int id_trs;
	private int id;
	
	
	
	public Memory(String type, String name,  String  str_guard, String exp ,String target , int id_trs, int id , String[] trigger) {
		this.type = type;
		this.name = name;
		this.setStr_guard(str_guard);
		this.exp = exp;
		this.target = target;
		this.setId_trs(id_trs);
		this.setId(id);
		this.setTrigger(trigger);
	}
				
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getExp() {
		return exp;
	}
	public void setExp(String exp) {
		this.exp = exp;
	}


	public String getTarget() {
		return target;
	}


	public void setTarget(String target) {
		this.target = target;
	}

	public String getStr_guard() {
		return str_guard;
	}

	public void setStr_guard(String str_guard) {
		this.str_guard = str_guard;
	}

	public String[] getTrigger() {
		return trigger;
	}

	public void setTrigger(String[] trigger) {
		this.trigger = trigger;
	}

	public int getId_trs() {
		return id_trs;
	}

	public void setId_trs(int id_trs) {
		this.id_trs = id_trs;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
	

}
