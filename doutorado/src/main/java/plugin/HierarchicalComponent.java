package plugin;

import java.util.ArrayList;

public class HierarchicalComponent {
	
	private String name;
	private ArrayList<String> basicComponent;
	
	
	
	
	public HierarchicalComponent(String name) {
		
		this.setName(name);
	}




	public String getName() {
		return name;
	}




	public void setName(String name) {
		this.name = name;
	}




	public ArrayList<String> getBasicComponent() {
		return basicComponent;
	}




	public void setBasicComponent(ArrayList<String> basicComponent) {
		this.basicComponent = basicComponent;
	}
	
	

}
