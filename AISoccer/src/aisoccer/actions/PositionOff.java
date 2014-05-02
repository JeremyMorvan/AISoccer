package aisoccer.actions;

import aisoccer.Brain;

public class PositionOff extends GoTo {

	@Override
	public boolean checkConditions(Brain brain) {
		return brain.getPlayer().isLeftSide() == brain.getFullstateInfo().LeftGotBall();
	}

	@Override
	public void Start(Brain brain) {
		
	}

}
