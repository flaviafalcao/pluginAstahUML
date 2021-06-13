package plugin;

public class Protocol {
	
	private String processName;
	private String portName;
	
	
	
	
	public Protocol(String processName, String portName) {
	
		this.processName = processName;
		this.portName = portName;
	}
	
	
	
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public String getPortName() {
		return portName;
	}
	public void setPortName(String portName) {
		this.portName = portName;
	}

}
