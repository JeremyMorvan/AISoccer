package aisoccer.actions;

import math.MathTools;
import math.Vector2D;
import aisoccer.Brain;
import aisoccer.PlayerAction;
import aisoccer.PlayerActionType;
import aisoccer.SoccerParams;
import aisoccer.behaviorTree.ActionTask;

public class Shoot extends ActionTask {
	final double SPEED_THRESHOLD = 0.1;
	Vector2D kickVector;
	
	public Shoot(Brain b) {
		super(b);
	}

	@Override
	public boolean CheckConditions() {
		if (brain.getShootVector()==null){
			return false;
		}
		kickVector = null;
		Vector2D ballV = brain.getFullstateInfo().getBall().getVelocity();
		Vector2D neededAcceleration = brain.getShootVector().subtract(ballV);
		double neededPower = neededAcceleration.polarRadius()/brain.getEffectivePowerRate();
		double angleKick = Math.toRadians(MathTools.normalizeAngle(neededAcceleration.polarAngle()-brain.getPlayer().getBodyDirection()));
		if(neededPower<=SoccerParams.POWERMAX){
			kickVector = new Vector2D(neededPower, angleKick, true);
			return true;
		}else if(ballV.polarRadius()<SPEED_THRESHOLD){
			kickVector = new Vector2D(SoccerParams.POWERMAX, angleKick, true);
			return true;		
		}
		//
		return false;
	}

	@Override
	public void Start() {}

	@Override
	public void DoAction() {		
		//System.out.println(brain.getPlayer().toString() + " : I am going to shoot ! : " + brain.getInterestPos().toString());
		brain.doAction(new PlayerAction(PlayerActionType.KICK, kickVector.polarRadius(), kickVector.polarAngle(), brain.getRobocupClient()));
	}
	


}
