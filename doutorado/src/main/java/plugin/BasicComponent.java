package plugin;

import java.util.ArrayList;

public class BasicComponent {	

	private String name;
	private String stm;
	private String memory;	
	private ArrayList<Attribute> var;	
	private ArrayList<Protocol> protocols = new ArrayList<Protocol>();
	
		
	
	public BasicComponent(String name, String stm,ArrayList<Attribute> var) {		
		this.name = name;
		this.stm = stm;
		this.setVar(var);
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStm() {
		return stm;
	}
	public void setStm(String stm) {
		this.stm = stm;
	}
	public String getMemory() {
		return memory;
	}
	public void setMemory(String memory) {
		this.memory = memory;
	}

	public ArrayList<Attribute> getVar() {
		return var;
	}

	public void setVar(ArrayList<Attribute> var) {
		this.var = var;
	}


	public ArrayList<Protocol> getProtocols() {
		return protocols;
	}


	public void setProtocols(ArrayList<Protocol> protocols) {
		this.protocols = protocols;
	}
}
