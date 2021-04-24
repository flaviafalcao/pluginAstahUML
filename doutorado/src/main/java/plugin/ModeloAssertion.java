package plugin;

import java.util.ArrayList;

public class ModeloAssertion {			
	
	private String fileModel;
	private ArrayList<String> assertivas;	
	
		
	public ModeloAssertion() {
		this.assertivas = new ArrayList<String>();
	}
	
	public String getFileModel() {
		return fileModel;
	}
	public void setFileModel(String fileModel) {
		this.fileModel = fileModel;
	}
	public ArrayList<String> getAssertivas() {
		return assertivas;
	}
	public void setAssertivas(ArrayList<String> assertivas) {
		this.assertivas = assertivas;
	}
	
	
	

}
