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
	boolean useShootVector;
	Vector2D kickVector;
	
	public Shoot(Brain b) {
		super(b);
		useShootVector = true;
	}
	
	public Shoot(Brain b, boolean bool) {
		super(b);
		useShootVector = bool;
	}

	@Override
	public boolean CheckConditions() {
		kickVector = null;
		
		boolean ok = useShootVector ? brain.getShootVector()!=null : brain.getInterestPos()!=null;
		if(!ok){
			return false;
		}

		if(!useShootVector){
//			if(brain.getPlayer().getUniformNumber() == 6){
//				brain.setInterestPos(new Vector2D(50,20));
//			}
//			System.out.println("Je veux shooter vers "+brain.getInterestPos());
			double speed = (1-SoccerParams.BALL_DECAY)*brain.getFullstateInfo().getBall().distanceTo(brain.getInterestPos()) + 0.8;
			double angle = Math.toRadians(brain.getFullstateInfo().getBall().getPosition().directionOf(brain.getInterestPos()));
			brain.setShootVector(new Vector2D(speed, angle,true));
		}
//		System.out.println("ShootVector = "+brain.getShootVector());
//		System.out.println("ballV = "+ballV);

		Vector2D ballV = brain.getFullstateInfo().getBall().getVelocity();		
		Vector2D neededAcceleration = brain.getShootVector().subtract(ballV);
		double neededPower = neededAcceleration.polarRadius()/brain.getEffectivePowerRate();
		double angleKick = Math.toRadians(MathTools.normalizeAngle(neededAcceleration.polarAngle()-brain.getPlayer().getBodyDirection()));
		if(neededPower<=SoccerParams.POWERMAX){
			kickVector = new Vector2D(neededPower, angleKick, true);
//			System.out.println("Kick Vector1 = "+kickVector);
			return true;
		}else if(ballV.polarRadius()<SPEED_THRESHOLD){
			kickVector = new Vector2D(SoccerParams.POWERMAX, angleKick, true);
//			System.out.println("Kick Vector2 = "+kickVector);
			return true;		
		}
		//
//		System.out.println("Vitesse de la balle : "+ballV.polarRadius());
		return false;
	}

	@Override
	public void Start() {
		System.out.println(brain.getShootVector());
	}

	@Override
	public void DoAction() {		
		//System.out.println(brain.getPlayer().toString() + " : I am going to shoot ! : " + brain.getInterestPos().toString());
		brain.doAction(new PlayerAction(PlayerActionType.KICK, kickVector.polarRadius(), kickVector.polarAngle(), brain.getRobocupClient()));
	}
	


}
