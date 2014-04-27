package aisoccer.actions;

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
	public boolean checkConditions(State s, Player player) {
		return Math.abs(s.getRelativeDirection()) > angleLimit;
	}	
	

	@Override
	public void DoAction(RobocupClient rc, State s, Player player) {
		rc.getBrain().getActionsQueue().addLast(new PlayerAction(new Action(s.getRelativeDirection(),true), rc));
	}

}
