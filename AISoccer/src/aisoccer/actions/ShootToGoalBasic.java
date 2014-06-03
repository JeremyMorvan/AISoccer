package aisoccer.actions;

import java.util.ArrayList;

import math.NullVectorException;
import math.Vector2D;
import aisoccer.Brain;
import aisoccer.InvalidArgumentException;
import aisoccer.SoccerParams;
import aisoccer.fullStateInfo.Player;

public class ShootToGoalBasic extends ShootTo {

	public ShootToGoalBasic(Brain b) {
		super(b);
	}
	
	@Override
	public boolean CheckConditions() {
		brain.setInterestPos(null);
		Vector2D opGoal = new Vector2D(brain.getPlayer().isLeftSide() ? 52.5d : -52.5d,0);
		if(brain.getPlayer().distanceTo(opGoal)<40){
			brain.setInterestPos(opGoal);
			return true;
		}
		return false;
	}

	@Override
	public void Start() {		
		System.out.println("I am going to Shoot to the goal basicly");
		Vector2D shootDirection = brain.getInterestPos().subtract(brain.getFullstateInfo().getBall().getPosition()).normalize();
		brain.setShootVector(shootDirection.multiply(SoccerParams.BALL_SPEED_MAX));
	}

	@Override
	public void End() {}

}
