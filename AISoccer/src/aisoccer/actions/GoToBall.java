package aisoccer.actions;

import aisoccer.Brain;
import aisoccer.SoccerParams;

public class GoToBall extends GoTo {

	@Override
	public boolean checkConditions(Brain brain) {
		return brain.getPlayer().distanceTo(brain.getFullstateInfo().getBall()) > SoccerParams.KICKABLE_MARGIN;
	}

	@Override
	public void Start(Brain brain) {
		brain.setInterestPos(brain.getFullstateInfo().getBall().getPosition());		
	}

}
