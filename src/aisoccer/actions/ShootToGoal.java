package aisoccer.actions;

import aisoccer.Brain;
import aisoccer.PlayerAction;
import aisoccer.PlayerActionType;
import aisoccer.behaviorTree.ActionTask;

public class ShootToGoal extends ActionTask {

	@Override
	public boolean checkConditions(Brain brain) {
		return true;
	}

	@Override
	public void DoAction(Brain brain) {
		double goalPosX = brain.getPlayer().isLeftSide() ? 52.5d : -52.5d;
        brain.doAction(new PlayerAction(PlayerActionType.KICK,100.0d, brain.getPlayer().angleFromBody(goalPosX, 0.0d), brain.getRobocupClient()));
	}

}
