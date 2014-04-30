package aisoccer.actions;

import aisoccer.Brain;
import aisoccer.Player;
import aisoccer.PlayerAction;
import aisoccer.RobocupClient;
import aisoccer.ballcapture.Action;
import aisoccer.ballcapture.State;
import aisoccer.behaviorTree.ActionTask;

public class TurnToBall extends ActionTask {

	public static float angleLimit = 5f;
	
	public TurnToBall(){}
	
	@Override
	public boolean checkConditions(Brain brain) {
		return Math.abs(brain.getState().getRelativeDirection()) > angleLimit;
	}	
	

	@Override
	public void DoAction(Brain brain) {
		brain.doAction(new PlayerAction(new Action(brain.getState().getRelativeDirection(),true), brain.getRobocupClient()));
	}

}
