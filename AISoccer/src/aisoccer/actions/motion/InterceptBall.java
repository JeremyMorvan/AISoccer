package aisoccer.actions.motion;

import aisoccer.Brain;
import aisoccer.SoccerParams;

public class InterceptBall extends GoTo {

	public InterceptBall(Brain b) {
		super(b);
	}

	public static float angleLimit = 5f;

	@Override
	public boolean CheckConditions() {
		return brain.getPlayer().distanceTo(brain.getFullstateInfo().getBall()) > SoccerParams.KICKABLE_MARGIN;
	}

	@Override
	public void Start() {
//		System.out.println(brain.getPlayer().getUniformNumber()+" intercepts the ball");
		brain.setInterestPos(brain.optimumInterceptionPosition(SoccerParams.PLAYER_SPEED_MAX*0.6));		
	}
}
