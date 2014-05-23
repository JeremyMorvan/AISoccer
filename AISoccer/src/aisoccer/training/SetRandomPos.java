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
		Vector2D intPos = (new Vector2D(2*SoccerParams.FIELD_LENGTH*(Math.random()-0.5f),2*SoccerParams.FIELD_WIDTH*(Math.random()-0.5f)));
		double mul = Math.random()*SoccerParams.BALL_SPEED_MAX/(1-SoccerParams.BALL_DECAY);
		Vector2D relPos = intPos.subtract(brain.getPlayer().getPosition()).normalize(mul);
		brain.setInterestPos(brain.getPlayer().getPosition().add(relPos));
	}

	@Override
	public boolean CheckConditions() {
		return true;
	}

	@Override
	public void DoAction() {	
	}

}
