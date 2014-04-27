package aisoccer.behaviorTree;

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
	public boolean Call(RobocupClient rc,State s,Player player){
		if(checkConditions(s,player)){
			Start();
			return Operation(child.Call(rc,s,player));
		}
		return false;		
	}
	
	public abstract boolean Operation(boolean childResponse);

}
