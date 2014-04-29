package aisoccer.behaviorTree;

import aisoccer.Brain;
import aisoccer.Player;
import aisoccer.RobocupClient;
import aisoccer.ballcapture.State;

public abstract class Decorator implements Task {

	Task child;
	
	public Decorator(Task child){
		this.child = child;
	}

	
	public abstract void Start();

	@Override
	public boolean Call(Brain brain){
		if(checkConditions(brain)){
			Start();
			return Operation(child.Call(brain));
		}
		return false;		
	}
	
	public abstract boolean Operation(boolean childResponse);

}
