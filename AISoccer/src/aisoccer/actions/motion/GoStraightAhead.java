package aisoccer.actions.motion;

import aisoccer.Brain;
import aisoccer.SoccerParams;
import aisoccer.ballcapture.Action;
import aisoccer.behaviorTree.ActionTask;

public class GoStraightAhead extends ActionTask {

	public GoStraightAhead(Brain b) {
		super(b);
	}

	@Override
	public boolean CheckConditions() {
		return brain.getInterestPos().distanceTo(brain.getPlayer())>SoccerParams.KICKABLE_MARGIN/2;
	}

	@Override
	public void DoAction() {
		brain.doAction(new Action(100f,false));		
	}

	@Override
	public void Start() {}
	
	

}
