package aisoccer.actions.motion;

import aisoccer.Brain;
import aisoccer.SoccerParams;
import aisoccer.ballcapture.Action;
import aisoccer.behaviorTree.ActionTask;

public class TurnTo extends ActionTask {

	public static float angleLimit = 5f;
	
	public TurnTo(Brain b){
		super(b);
	}
	
	@Override
	public boolean CheckConditions() {
		return brain.getPlayer().distanceTo(brain.getInterestPos())>SoccerParams.KICKABLE_MARGIN && Math.abs(((float) brain.getPlayer().angleFromBody(brain.getInterestPos()))) > angleLimit;
	}	
	
	@Override
	public void DoAction() {
		brain.doAction(new Action(((float) brain.getPlayer().angleFromBody(brain.getInterestPos())),true));
	}

	@Override
	public void Start() {}

}
