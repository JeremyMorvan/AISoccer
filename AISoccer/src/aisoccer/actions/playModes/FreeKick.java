package aisoccer.actions.playModes;

import aisoccer.Brain;
import aisoccer.behaviorTree.ActionTask;

public class FreeKick extends ActionTask {

	public FreeKick(Brain b) {
		super(b);
	}

	@Override
	public boolean CheckConditions() {
		//String pm = brain.getFullstateInfo().getPlayMode();
		return false;
//		return pm.equals("free_kick_l") && brain.getPlayer().isLeftSide() || pm.equals("free_kick_r") && !brain.getPlayer().isLeftSide();
	}

	@Override
	public void Start() {}

	@Override
	public void DoAction() {
		// TODO Auto-generated method stub
		
	}


}
