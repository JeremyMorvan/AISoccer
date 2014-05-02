package aisoccer.actions.playModes;

import aisoccer.Brain;
import aisoccer.behaviorTree.ActionTask;

public class BeforeKickOffAttack extends ActionTask {

	public BeforeKickOffAttack() {}

	@Override
	public boolean checkConditions(Brain brain) {
		String pm = brain.getFullstateInfo().getPlayMode();
		boolean left = brain.getPlayer().isLeftSide();
		boolean firstHalf = brain.getFullstateInfo().getTimeStep() == 0;
		
		boolean condition = pm.equals("goal_r") && left || pm.equals("goal_l") && !left;
		return condition || pm.equals("before_kick_off") && (left==firstHalf);		
	}

	@Override
	public void DoAction(Brain brain) {
		// TODO Auto-generated method stub

	}

	@Override
	public void Start(Brain brain) {
		// TODO Auto-generated method stub

	}

}
