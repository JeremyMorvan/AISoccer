package aisoccer.actions;

import math.Vector2D;
import aisoccer.Brain;
import aisoccer.PlayerAction;
import aisoccer.PlayerActionType;
import aisoccer.SoccerParams;
import aisoccer.behaviorTree.ActionTask;

public class ShootStatic extends ActionTask {

	Vector2D acc;
	final double ballMinSpeed = 0.5;
	
	public ShootStatic(Brain b) {
		super(b);
	}

	@Override
	public boolean CheckConditions() {
		Vector2D ballV = brain.getFullstateInfo().getBall().getVelocity();
		return brain.getInterestPos()!=null && ballV.polarRadius()<0.1;
	}

	@Override
	public void Start() {}

	@Override
	public void DoAction() {		
		//System.out.println(brain.getPlayer().toString() + " : I am going to shoot ! : " + brain.getInterestPos().toString());
		double speed = (1-SoccerParams.BALL_DECAY)*brain.getFullstateInfo().getBall().distanceTo(brain.getInterestPos()) + 0.8;
		double powerNeeded = speed/brain.getEffectivePowerRate();
        brain.doAction(new PlayerAction(PlayerActionType.KICK, powerNeeded, brain.getPlayer().angleFromBody(brain.getInterestPos()), brain.getRobocupClient()));
	}
	


}
