package aisoccer.training;

import math.Vector2D;
import aisoccer.Brain;
import aisoccer.SoccerParams;
import aisoccer.behaviorTree.ActionTask;

public class SetRandomPos extends ActionTask {

	public SetRandomPos(Brain b) {
		super(b);
	}

	@Override
	public void Start() {
		Vector2D intPos = (new Vector2D(Math.random()-0.5f,Math.random()-0.5f));
		Vector2D relPos = intPos.subtract(brain.getPlayer().getPosition());
		
		brain.setInterestPos((new Vector2D(Math.random()-0.5f,Math.random()-0.5f)).multiply(2*SoccerParams.BALL_SPEED_MAX/(1-SoccerParams.BALL_DECAY)));
	}

	@Override
	public boolean CheckConditions() {
		return true;
	}

	@Override
	public void DoAction() {	
	}

}
