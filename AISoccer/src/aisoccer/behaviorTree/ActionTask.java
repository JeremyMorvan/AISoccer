package aisoccer.behaviorTree;

public abstract class ActionTask implements Task {

	public abstract void DoAction();

	@Override
	public boolean Call() {
		if(checkConditions()){
			DoAction();
			return true;
		}
		return false;		
	}

}
