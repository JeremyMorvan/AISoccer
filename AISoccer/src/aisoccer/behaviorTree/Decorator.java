package aisoccer.behaviorTree;

public abstract class Decorator implements Task {

	Task child;
	
	public Decorator(Task child){
		this.child = child;
	}

	
	public abstract void Start();

	@Override
	public boolean Call(){
		Start();
		return Operation(child.Call());
	}
	
	public abstract boolean Operation(boolean childResponse);

}
