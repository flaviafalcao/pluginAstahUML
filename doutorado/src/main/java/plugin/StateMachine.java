package plugin;

public class StateMachine {
	
	private String stm_str;
	private String component_name;
			
	public StateMachine(String stm, String comp_name) {
		this.stm_str =stm;
		this.component_name = comp_name;
	}
	
	public String getStm_str() {
		return stm_str;
	}
	public void setStm_str(String stm_str) {
		this.stm_str = stm_str;
	}
	public String getComponent_name() {
		return component_name;
	}
	public void setComponent_name(String component_name) {
		this.component_name = component_name;
	}	
		
	
}


