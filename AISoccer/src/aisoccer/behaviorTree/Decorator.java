package aisoccer.behaviorTree;

public abstract class Decorator implements Task {

	Task child;
	
	public Decorator(Task child){
		this.child = child;
	}

	
	public abstract void Start();

	@Override
	public boolean Call(){
		if(checkConditions()){
			Start();
			return Operation(child.Call());
		}
		return false;		
	}
	
	public abstract boolean Operation(boolean childResponse);

}
