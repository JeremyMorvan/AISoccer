package aisoccer.behaviorTree;

import aisoccer.Brain;

public abstract class ActionTask extends Task {

	public ActionTask(Brain b) {
		super(b);
	}

	public abstract void DoAction();

	@Override
	public boolean Call() {
		if(CheckConditions()){
			Start();
			DoAction();
			return true;
		}
		return false;		
	}
	
	public abstract void Start();

}
