package aisoccer.actions.playModes;

import aisoccer.Brain;
import aisoccer.actions.ShootTo;

public class KickOff extends ShootTo {

	@Override
	public boolean CheckConditions() {
		return true;		
	}
	
	public KickOff(Brain b) {
		super(b);
		// TODO Auto-generated constructor stub
	}

}
