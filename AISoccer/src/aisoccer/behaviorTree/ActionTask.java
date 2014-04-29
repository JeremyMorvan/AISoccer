package aisoccer.behaviorTree;

import aisoccer.Brain;

public abstract class ActionTask implements Task {

	public abstract void DoAction(Brain brain);

	@Override
	public boolean Call(Brain brain) {
		if(checkConditions(brain)){
			DoAction(brain);
			return true;
		}
		return false;		
	}

}
