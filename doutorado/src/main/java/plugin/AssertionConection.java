package plugin;

import com.change_vision.jude.api.inf.model.IConnector;


public class AssertionConection {
	
	private String file;
	private IConnector conn;
	
	
	public AssertionConection(String file,IConnector conn) {
		
		this.file = file;
		this.conn = conn;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public IConnector getConn() {
		return conn;
	}
	public void setConn(IConnector conn) {
		this.conn = conn;
	}
	
}
