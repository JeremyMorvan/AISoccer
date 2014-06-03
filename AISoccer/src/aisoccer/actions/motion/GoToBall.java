package aisoccer.actions.motion;

import aisoccer.Brain;

public class GoToBall extends GoTo {

	public GoToBall(Brain b) {
		super(b);
	}


	@Override
	public void Start() {	
	}


	@Override
	public void defineInterestPosition() {
		brain.setInterestPos(brain.getFullstateInfo().getBall().getPosition());			
	}

}
