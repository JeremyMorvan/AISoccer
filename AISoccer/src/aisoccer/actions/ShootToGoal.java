package aisoccer.actions;

import math.Vector2D;
import aisoccer.Brain;
import aisoccer.SoccerParams;

public class ShootToGoal extends ShootTo {

	public ShootToGoal(Brain b, boolean checkDistance) {
		super(b, checkDistance);
	}
	
	@Override
	public boolean CheckConditions() {
		if(checkDistance){
			if(brain.getPlayer().distanceTo(new Vector2D(brain.getPlayer().isLeftSide() ? 52.5d : -52.5d,0))>40){
				return false;
			}
		}
		return true;
	}

	@Override
	public void Start() {
		Vector2D goal = new Vector2D(brain.getPlayer().isLeftSide() ? 52.5d : -52.5d,0);
		brain.setShootVector(goal.subtract(brain.getFullstateInfo().getBall().getPosition()).normalize().multiply(SoccerParams.BALL_SPEED_MAX));
	}

	@Override
	public void End() {}

}
