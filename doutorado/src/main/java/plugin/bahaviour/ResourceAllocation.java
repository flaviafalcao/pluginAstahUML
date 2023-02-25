package plugin.bahaviour;

import java.util.ArrayList;

public class ResourceAllocation{
	
	
	String user;
	ArrayList<String> resource = new ArrayList<String>();
	
	public ResourceAllocation(String user,ArrayList<String> resource) {
		this.user = user;
		this.resource = resource;
		
	}
	
	public ResourceAllocation() {
		
	}
	
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public ArrayList<String> getResource() {
		return resource;
	}
	public void setResource(ArrayList<String> resource) {
		this.resource = resource;
	}
	
	//atualiza resource 
	
	public void updateResource(String str) {
		
		this.resource.add(str);
	}
	
	
	public String toString() {
		
		String str ="";
		String temp = "";
		
		str = "USER:" + user ;
		
		for(int i = 0 ; i < resource.size() ;i ++) {
			
			temp = temp + ", " + resource.get(i).toString() ;
		}
		
		str = str + " RESOURCES: " +temp;
		return str;
		
		
	}
	
	
	
	
}	
