package aisoccer.actions;

import aisoccer.Brain;
import aisoccer.PlayerAction;
import aisoccer.PlayerActionType;
import aisoccer.behaviorTree.ActionTask;

public abstract class ShootTo extends ActionTask {
	
	@Override
	public boolean checkConditions(Brain brain) {
		return true;
	}

	@Override
	public void DoAction(Brain brain) {
        brain.doAction(new PlayerAction(PlayerActionType.KICK,100.0d, brain.getPlayer().angleFromBody(brain.getInterestPos()), brain.getRobocupClient()));
	}

	@Override
	public abstract void Start(Brain brain);

}
