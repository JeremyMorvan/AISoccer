package aisoccer.actions;

import aisoccer.Player;
import aisoccer.PlayerAction;
import aisoccer.PlayerActionType;
import aisoccer.RobocupClient;
import aisoccer.ballcapture.State;
import aisoccer.behaviorTree.ActionTask;

public class ShootToGoal extends ActionTask {

	@Override
	public boolean checkConditions(State s, Player player) {
		return true;
	}

	@Override
	public void DoAction(RobocupClient rc, State s, Player player) {
		double goalPosX = player.isLeftSide() ? 52.5d : -52.5d;
        rc.getBrain().getActionsQueue().addLast(new PlayerAction(PlayerActionType.KICK,100.0d, player.angleFromBody(goalPosX, 0.0d), rc));
	}

}
