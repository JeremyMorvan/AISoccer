package aisoccer.actions;

import aisoccer.Player;
import aisoccer.PlayerAction;
import aisoccer.RobocupClient;
import aisoccer.ballcapture.Action;
import aisoccer.ballcapture.State;
import aisoccer.behaviorTree.ActionTask;

public class GoToBall extends ActionTask {

	@Override
	public boolean checkConditions(State s,Player player) {
		return true;
	}

	@Override
	public void DoAction(RobocupClient rc,State s,Player player) {
		Action action;
        if (Math.abs(s.getRelativeDirection()) > 36f)
        {
            action = new Action(s.getRelativeDirection(),true);
        }
        else
        {
            action = new Action(100f,false);
        }
		rc.getBrain().getActionsQueue().addLast(new PlayerAction(action, rc));
	}

}
