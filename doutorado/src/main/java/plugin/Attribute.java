package plugin;

public class Attribute {
	
	private String name;
	private String initial_value;
	private String type;
		
	
	public Attribute(String name,String type) {
		this.name = name;
		this.setType(type);
	}
	
	
	public Attribute(String name,String initial_value, String type) {
		this.name = name;
		this.setType(type);
		this.setInitial_value(initial_value);
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

	public String getInitial_value() {
		return initial_value;
	}

	public void setInitial_value(String initial_value) {
		this.initial_value = initial_value;
	}
	
	
	

}
