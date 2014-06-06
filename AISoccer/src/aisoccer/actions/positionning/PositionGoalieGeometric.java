package aisoccer.actions.positionning;

import math.Vector2D;
import aisoccer.Brain;
import aisoccer.SoccerParams;
import aisoccer.actions.motion.GoTo;

public class PositionGoalieGeometric extends GoTo {

	public PositionGoalieGeometric(Brain b) {
		super(b);
	}

	@Override
	public void defineInterestPosition() {
		Vector2D myGoal = new Vector2D(brain.getPlayer().isLeftSide() ? -52.5d : 52.5d,0);
		Vector2D dir = brain.getFullstateInfo().getBall().getPosition().subtract(myGoal).normalize();
		double coeff = SoccerParams.GOAL_WIDTH*(1-Math.cos(dir.polarAngle('r'))/2);
		brain.setInterestPos( myGoal.add( dir.multiply(coeff) ) );		
	}

}
