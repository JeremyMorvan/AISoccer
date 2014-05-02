package aisoccer.actions;

import aisoccer.Brain;
import aisoccer.SoccerParams;

public class PositionDef extends GoTo {

	@Override
	public boolean checkConditions(Brain brain) {
		return brain.getPlayer().isLeftSide() == brain.getFullstateInfo().LeftGotBall();			
	}

	@Override
	public void Start(Brain brain) {
		// TODO Auto-generated method stub

	}

}
