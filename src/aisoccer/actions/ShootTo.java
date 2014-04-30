package aisoccer.actions;

import aisoccer.Brain;
import aisoccer.PlayerAction;
import aisoccer.PlayerActionType;
import aisoccer.behaviorTree.ActionTask;

public class ShootTo extends ActionTask {

	double targetX = 0;
	double targetY = 0;
	
	@Override
	public boolean checkConditions(Brain brain) {
		return true;
	}

	@Override
	public void DoAction(Brain brain) {
        brain.doAction(new PlayerAction(PlayerActionType.KICK,100.0d, brain.getPlayer().angleFromBody(targetX, targetY), brain.getRobocupClient()));
	}

}
