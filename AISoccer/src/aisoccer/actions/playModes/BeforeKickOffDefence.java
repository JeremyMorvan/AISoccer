package aisoccer.actions.playModes;

import aisoccer.Brain;
import aisoccer.behaviorTree.ActionTask;

public class BeforeKickOffDefence extends ActionTask {

	public BeforeKickOffDefence(Brain b) {
		super(b);
	}

	@Override
	public boolean CheckConditions() {
		String pm = brain.getFullstateInfo().getPlayMode();
		boolean left = brain.getPlayer().isLeftSide();
		boolean firstHalf = brain.getFullstateInfo().getTimeStep() == 0;
		
		boolean condition = pm.equals("goal_r") && !left || pm.equals("goal_l") && left;
		return condition || pm.equals("before_kick_off") && (left!=firstHalf);	
	}

	@Override
	public void DoAction() {
		brain.engage();
	}

	@Override
	public void Start() {
		// TODO Auto-generated method stub

	}

}
