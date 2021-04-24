package plugin;

import com.change_vision.jude.api.inf.model.IConnector;

public class SetSinc {
	
	private IConnector conn;
	private String channel1;
	private String channel2;			
	
	
	public SetSinc(String c1, String c2) {
				
		this.channel1 = c1;
		this.channel2 = c2;
		
	}	
	
		
   public SetSinc(IConnector conn, String c1, String c2) {
		this.setConn(conn);
		this.channel1 = c1;
		this.channel2 = c2;
	}
	public String getChannel1() {
		return channel1;
	}
	public void setChannel1(String channel1) {
		this.channel1 = channel1;
	}
	public String getChannel2() {
		return channel2;
	}
	public void setChannel2(String channel2) {
		this.channel2 = channel2;
	}

	public IConnector getConn() {
		return conn;
	}

	public void setConn(IConnector conn) {
		this.conn = conn;
	}
	
	
	

}
