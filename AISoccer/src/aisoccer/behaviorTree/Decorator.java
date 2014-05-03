package aisoccer.behaviorTree;

import aisoccer.Brain;

public abstract class Decorator extends Task {

	Task child;
	
	public Decorator(Brain b, Task child){
		super(b);
		this.child = child;
	}

	
	public abstract void Start();

	@Override
	public boolean Call(){
		if(CheckConditions()){
			Start();
			return Operation(child.Call());
		}
		return false;		
	}
	
	public abstract boolean Operation(boolean childResponse);

}
