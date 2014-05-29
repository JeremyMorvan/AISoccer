package aisoccer.actions;

import aisoccer.Brain;
import aisoccer.PlayerAction;
import aisoccer.PlayerActionType;
import aisoccer.SoccerParams;
import aisoccer.behaviorTree.ActionTask;

public class CatchGoalie extends ActionTask {

	public CatchGoalie(Brain b) {
		super(b);
	}

	@Override
	public void DoAction() {
		// TODO Auto-generated method stub
		brain.doAction(new PlayerAction(PlayerActionType.CATCH,0,brain.getPlayer().angleFromBody(brain.getFullstateInfo().getBall()),brain.getRobocupClient()));
	}

	@Override
	public void Start() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean CheckConditions() {
		return brain.getPlayer().distanceTo(brain.getFullstateInfo().getBall()) <= SoccerParams.CATCHABLE_AREA_L;
	}

}
