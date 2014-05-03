package aisoccer.actions.playModes;

import aisoccer.Brain;
import aisoccer.behaviorTree.ActionTask;

public class KickIn extends ActionTask {

	public KickIn(Brain b) {
		super(b);
	}

	@Override
	public boolean CheckConditions() {
		return false;
	}

	@Override
	public void DoAction() {
		// TODO Auto-generated method stub

	}

	@Override
	public void Start() {}

}
