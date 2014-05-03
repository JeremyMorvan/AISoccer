package aisoccer.actions;

import aisoccer.Brain;

public class Pass extends ShootTo {

	public Pass(Brain b) {
		super(b, false);
	}

	@Override
	public void Start() {}

	@Override
	public boolean CheckConditions() {
		if(brain.getInterestPos()==null){
			return false;
		}
		return true;
	}

}
