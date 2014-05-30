package aisoccer.actions.motion;

import aisoccer.Brain;

public class GoToBall extends GoTo {

	public GoToBall(Brain b) {
		super(b);
	}

//	@Override
//	public boolean CheckConditions() {
//		return brain.getPlayer().distanceTo(brain.getFullstateInfo().getBall()) > SoccerParams.KICKABLE_MARGIN;
//	}

	@Override
	public void Start() {
		brain.setInterestPos(brain.getFullstateInfo().getBall().getPosition());		
	}

}
