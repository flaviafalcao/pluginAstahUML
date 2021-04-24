package plugin;

public class ExtendTransition {
	
	
	private String componentName;
	private String source;
	private String[] event_action; 
	private String[] event_trigger;
	private String target;
	private String[] event_guard; // pode ser uma lista de guardas		
	
	
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String[] getEvent_action() {
		return event_action;
	}
	public void setEvent_action(String[] event_action) {
		this.event_action = event_action;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String[] getEvent_guard() {
		return event_guard;
	}
	public void setEvent_guard(String[] event_guard) {
		this.event_guard = event_guard;
	}
	
	public void setComponentName(String name) {
		this.componentName = name;
	}
	
	public String getComponentName() {
		return this.componentName;
	}
	public String[] getEvent_trigger() {
		return event_trigger;
	}
	public void setEvent_trigger(String[] event_trigger) {
		this.event_trigger = event_trigger;
	}


}
