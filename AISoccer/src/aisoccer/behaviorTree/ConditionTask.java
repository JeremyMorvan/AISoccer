package aisoccer.behaviorTree;

public abstract class ConditionTask implements Task {

	
	public abstract boolean CheckConditions();

	@Override
	public boolean Call() {
		return CheckConditions();
	}

}
