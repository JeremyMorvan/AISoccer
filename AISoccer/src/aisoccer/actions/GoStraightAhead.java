package aisoccer.actions;

import aisoccer.Brain;
import aisoccer.Player;
import aisoccer.PlayerAction;
import aisoccer.RobocupClient;
import aisoccer.ballcapture.Action;
import aisoccer.ballcapture.State;
import aisoccer.behaviorTree.ActionTask;

public class GoStraightAhead extends ActionTask {

	@Override
	public boolean checkConditions(Brain brain) {
		return true;
	}

	@Override
	public void DoAction(Brain brain) {
		brain.doAction(new Action(100f,false));		
	}

}
