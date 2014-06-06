package aisoccer.actions;

import math.Vector2D;
import aisoccer.Brain;
import aisoccer.SoccerParams;

public class ShootToGoalGeometric extends ShootTo {

	public ShootToGoalGeometric(Brain b) {
		super(b);
	}
	
	@Override
	public boolean CheckConditions() {	
		Vector2D opGoal = new Vector2D(brain.getPlayer().isLeftSide() ? 52.5d : -52.5d,0);
		return brain.getPlayer().distanceTo(opGoal)<20;
	}

	@Override
	public void Start() {	
		System.out.println("Je tire geometriquement!!");
		Vector2D opGoal = new Vector2D(brain.getPlayer().isLeftSide() ? 52.5d : -52.5d,0);

		Vector2D opGoalie = brain.getFullstateInfo().getGoalie(!brain.getPlayer().isLeftSide()).getPosition();
		Vector2D relativeTarget = new Vector2D(0, SoccerParams.GOAL_WIDTH/2-0.5);
		Vector2D targetInGoal = opGoal.add( relativeTarget.multiply( Math.signum(opGoalie.getY()) ) );
		Vector2D shootDirection = targetInGoal.subtract(brain.getPlayer().getPosition()).normalize();
		brain.setShootVector(shootDirection.multiply(SoccerParams.BALL_SPEED_MAX));
	}

	@Override
	public void End() {}

}
