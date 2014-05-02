package aisoccer.behaviorTree;

import aisoccer.Brain;

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
