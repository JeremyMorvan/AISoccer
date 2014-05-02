package aisoccer.actions.motion;

import aisoccer.Brain;
import aisoccer.ballcapture.Action;
import aisoccer.behaviorTree.ActionTask;

public class GoStraightAhead extends ActionTask {

	@Override
	public boolean checkConditions(Brain brain) {
		return brain.getPlayer().getPosition().distanceTo(brain.getInterestPos())!=0;
	}

	@Override
	public void DoAction(Brain brain) {
		brain.doAction(new Action(100f,false));		
	}

	@Override
	public void Start(Brain brain) {		
	}
	
	

}
