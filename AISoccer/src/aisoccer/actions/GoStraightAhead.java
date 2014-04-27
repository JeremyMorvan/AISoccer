package aisoccer.actions;

import aisoccer.Player;
import aisoccer.PlayerAction;
import aisoccer.RobocupClient;
import aisoccer.ballcapture.Action;
import aisoccer.ballcapture.State;
import aisoccer.behaviorTree.ActionTask;

public class GoStraightAhead extends ActionTask {

	@Override
	public boolean checkConditions(State s, Player player) {
		return true;
	}

	@Override
	public void DoAction(RobocupClient rc, State s, Player player) {
		rc.getBrain().getActionsQueue().addLast(new PlayerAction(new Action(100f,false), rc));		
	}

}
