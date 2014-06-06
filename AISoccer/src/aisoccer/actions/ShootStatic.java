package aisoccer.actions;

import math.MathTools;
import math.Vector2D;
import aisoccer.Brain;
import aisoccer.PlayerAction;
import aisoccer.PlayerActionType;
import aisoccer.SoccerParams;
import aisoccer.behaviorTree.ActionTask;

public class ShootStatic extends ActionTask {

	Vector2D acc;
	final double ballMinSpeed = 0.5;
	boolean useShootVector;
	Vector2D kickVector;
	
	public ShootStatic(Brain b) {
		super(b);
		useShootVector = false;
	}
	
	public ShootStatic(Brain b, boolean bool) {
		super(b);
		useShootVector = bool;
	}

	@Override
	public boolean CheckConditions() {
		boolean ok = useShootVector ? brain.getShootVector()!=null : brain.getInterestPos()!=null;
		if(!ok || brain.getFullstateInfo().getBall().getVelocity().polarRadius()>0.1){
			return false;
		}
		if(!useShootVector){
			double speed = (1-SoccerParams.BALL_DECAY)*brain.getFullstateInfo().getBall().distanceTo(brain.getInterestPos()) + 0.8;
			double powerNeeded = speed/brain.getEffectivePowerRate();	
			double angle = Math.toRadians(brain.getPlayer().angleFromBody(brain.getInterestPos()));
			kickVector = new Vector2D(powerNeeded, angle,true);
		}else{
			double angle = Math.toRadians(MathTools.normalizeAngle(brain.getShootVector().polarAngle()-brain.getPlayer().getBodyDirection()));
			kickVector = new Vector2D(brain.getShootVector().polarRadius()/brain.getEffectivePowerRate(), angle, true);
		}	
		
		return true;
	}

	@Override
	public void Start() {}

	@Override
	public void DoAction() {		
		//System.out.println(brain.getPlayer().toString() + " : I am going to shoot ! : " + brain.getInterestPos().toString());
		brain.doAction(new PlayerAction(PlayerActionType.KICK, kickVector.polarRadius(), kickVector.polarAngle(), brain.getRobocupClient()));

	}
	


}
