package aisoccer.actions.playModes;

import aisoccer.Brain;
import aisoccer.behaviorTree.ActionTask;

public class FreeKick extends ActionTask {

	public FreeKick() {}

	@Override
	public boolean checkConditions(Brain brain) {
		String pm = brain.getFullstateInfo().getPlayMode();
		return pm.equals("free_kick_l") && brain.getPlayer().isLeftSide() || pm.equals("free_kick_r") && !brain.getPlayer().isLeftSide();
	}

	@Override
	public void Start(Brain brain) {}

	@Override
	public void DoAction(Brain brain) {
		// TODO Auto-generated method stub
		
	}


}
