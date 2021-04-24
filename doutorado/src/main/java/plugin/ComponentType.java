package plugin;

public class ComponentType {

	private String type;
	private int count;	
		
	
	public ComponentType(String type, int count) {		
		this.type = type;
		this.setCount(count);			
	}
		
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
