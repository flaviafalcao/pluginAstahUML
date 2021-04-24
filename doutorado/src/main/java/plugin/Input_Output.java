package plugin;

import java.util.ArrayList;

public class Input_Output {
	
	
	private String instance_name; //tipo
	private String inputs_name;
	private String outputs_name;	
	private String instance;		
	//lista de inputs e outputs
	private ArrayList<String> channels;
	
		
	
	public Input_Output(String instance_name, String input , String output) {
		this.instance_name = instance_name;
		this.outputs_name = output;
		this.inputs_name = input;
	}		
	
	public String getInstance_name() {
		return instance_name;
	}	
	
	public void setInstance_name(String instance_name) {
		this.instance_name = instance_name;
	}
	
	public String getInputs_name() {
		return inputs_name;
	}
	public void setInputs_name(String inputs_name) {
		this.inputs_name = inputs_name;
	}
	public String getOutputs_name() {
		return outputs_name;
	}
	public void setOutputs_name(String outputs_name) {
		this.outputs_name = outputs_name;
	}

	
	public ArrayList<String> getChannels() {
		return channels;
	}

	public void setChannels(ArrayList<String> channels) {
		this.channels = channels;
	}

	public String getInstance() {
		return instance;
	}

	public void setInstance(String instance) {
		this.instance = instance;
	}
	

}
