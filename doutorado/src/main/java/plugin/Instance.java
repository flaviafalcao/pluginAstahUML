package plugin;

import java.util.ArrayList;

public class Instance {
	
	private String name;
	private String type;
	private int num_id;
	private String input_name;
	private String output_name;	
	private ArrayList<String> portas;
	
	
	public Instance(String name, String type, int num_id) {
		this.name = name;
		this.type = type;
		this.num_id = num_id;		
	}		
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getNum_id() {
		return num_id;
	}
	public void setNum_id(int num_id) {
		this.num_id = num_id;
	}
	
	public String getInput_name() {
		return input_name;
	}
	public void setInput_name(String input_name) {
		this.input_name = input_name;
	}
	public String getOutput_name() {
		return output_name;
	}
	public void setOutput_name(String output_name) {
		this.output_name = output_name;
	}


	public ArrayList<String> getPortas() {
		return portas;
	}


	public void setPortas(ArrayList<String> portas) {
		this.portas = portas;
	}
	
	
}
