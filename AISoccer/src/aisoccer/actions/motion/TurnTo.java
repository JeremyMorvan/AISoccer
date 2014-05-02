package aisoccer.actions.motion;

import aisoccer.Brain;
import aisoccer.ballcapture.Action;
import aisoccer.behaviorTree.ActionTask;

public class TurnTo extends ActionTask {

	public static float angleLimit = 5f;
	
	public TurnTo(){}
	
	@Override
	public boolean checkConditions(Brain brain) {
		return Math.abs(((float) brain.getPlayer().angleFromBody(brain.getInterestPos()))) > angleLimit;
	}	
	

	@Override
	public void DoAction(Brain brain) {
		brain.doAction(new Action(((float) brain.getPlayer().angleFromBody(brain.getInterestPos())),true));
	}

	@Override
	public void Start(Brain brain) {}

}
