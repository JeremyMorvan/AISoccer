package aisoccer.behaviorTree;

public abstract class ActionTask implements Task {

	public abstract void DoAction();

	@Override
	public boolean Call() {
		DoAction();
		return true;
	}

}
