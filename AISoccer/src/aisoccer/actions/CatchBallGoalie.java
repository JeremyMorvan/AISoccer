package aisoccer.actions;

import math.Vector2D;
import aisoccer.Brain;
import aisoccer.SoccerParams;

public class CatchBallGoalie extends CatchGoalie {

	public CatchBallGoalie(Brain b) {
		super(b);
	}
	
	@Override
	public boolean CheckConditions() {
		Vector2D myGoal = new Vector2D(brain.getPlayer().isLeftSide() ? -52.5d : 52.5d,0);
		Vector2D pos = brain.getFullstateInfo().getBall().getPosition();
		
		return super.CheckConditions() && 
				brain.getFullstateInfo().LeftGotBall() != brain.getPlayer().isLeftSide()
				&& Math.abs(pos.getX()-myGoal.getX())<SoccerParams.PENALTY_AREA_L
				&& Math.abs(pos.getY())<SoccerParams.PENALTY_AREA_W/2;
	}

}
