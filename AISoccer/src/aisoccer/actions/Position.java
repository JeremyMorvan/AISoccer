package aisoccer.actions;

import aisoccer.Brain;
import aisoccer.behaviorTree.ActionTask;

public class Position extends ActionTask {

	@Override
	public boolean checkConditions(Brain brain) {
		return true;
	}

	@Override
	public void DoAction(Brain brain) {
		//System.out.println(brain.getPlayer().toString() + " : I am moving to my position !");
	}

	@Override
	public void Start(Brain brain) {

	}

}
