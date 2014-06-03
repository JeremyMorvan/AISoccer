package aisoccer.actions.motion;

import aisoccer.Brain;
import aisoccer.SoccerParams;

public class InterceptBall extends GoTo {

	public InterceptBall(Brain b) {
		super(b);
	}

	public static float angleLimit = 5f;
	

	@Override
	public void defineInterestPosition() {
//		System.out.println(brain.getPlayer().getUniformNumber()+" intercepts the ball");
		double margin = (brain.getPlayer().isGoalie()) ? SoccerParams.CATCHABLE_AREA_L*0.8 : 0; 
		brain.setInterestPos(brain.optimumInterceptionPosition(brain.speedEstimation, margin));
	}
	
}
