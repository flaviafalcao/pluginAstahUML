package plugin;

public class PartType {
	
	private String name;
	private String type;
	private String owner;
	
		
	public PartType(String name, String  type , String owner) {
		this.name = name;
		this.type = type;
		this.setOwner(owner);
		
		
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


	public String getOwner() {
		return owner;
	}


	public void setOwner(String owner) {
		this.owner = owner;
	}	
		

}
