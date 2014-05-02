package aisoccer.actions;

import aisoccer.Brain;

public class Pass extends ShootTo {

	public Pass() {
		super(false);
	}

	@Override
	public void Start(Brain brain) {}

	@Override
	public boolean checkConditions(Brain brain) {
		if(brain.getInterestPos()==null){
			return false;
		}
		return true;
	}

}
