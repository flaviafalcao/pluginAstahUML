package plugin.bahaviour;

import java.util.ArrayList;

public class UserResource {
	
	private String resource;
	private ArrayList<String> user = new ArrayList<String>();
	
	public UserResource(String resource ,ArrayList<String> user) {
		this.user = user;
		this.setResource(resource);
		
	}
	
	public UserResource() {
		
	}
	
	public void setUser(ArrayList<String> user) {
		this.user = user;
	}
	
	public  ArrayList<String> getUser(){
		
		return this.user;
	}
		
	//atualiza resource 
	
	public void updateUser(String str) {
		
		this.user.add(str);
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}
	

}
