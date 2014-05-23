package aisoccer.actions;

import aisoccer.Brain;

public class Pass extends ShootTo {

	public Pass(Brain b) {
		super(b, false);
	}

	@Override
	public void Start() {}

	@Override
	public void End() {}

	@Override
	public boolean CheckConditions() {
		return brain.getInterestPos() != null;
	}

}
