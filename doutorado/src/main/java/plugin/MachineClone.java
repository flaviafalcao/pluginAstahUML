package plugin;

import com.change_vision.jude.api.inf.model.IStateMachine;

public class MachineClone {

	
	IStateMachine machine;
	String basicComponentName;	
	
	
	public MachineClone(String basicComponentName, IStateMachine machine) {
		
		this.machine = machine;
		this.basicComponentName = basicComponentName;
	}
	
	


}


