package aisoccer.actions;

import math.Vector2D;
import aisoccer.Brain;
import aisoccer.PlayerAction;
import aisoccer.PlayerActionType;
import aisoccer.behaviorTree.ActionTask;

public class ControlBall extends ActionTask {

	public ControlBall(Brain b) {
		super(b);
	}

	@Override
	public void DoAction() {	
		Vector2D ballP = brain.getFullstateInfo().getBall().getPosition();
		Vector2D ballV = brain.getFullstateInfo().getBall().getVelocity();
		Vector2D accNeeded = brain.getPlayer().getPosition().subtract(ballP).subtract(ballV);
		Vector2D powerNeeded = accNeeded.multiply(1/brain.getEffectivePowerRate());
//		System.out.println(brain.getPlayer().getUniformNumber() + " : I am going to control");
        brain.doAction(new PlayerAction(PlayerActionType.KICK, powerNeeded.polarRadius(), brain.getPlayer().angleFromBody(brain.getPlayer().getPosition().add(powerNeeded)), brain.getRobocupClient()));
	}

	@Override
	public void Start() {}

	@Override
	public boolean CheckConditions() {
		return true;
	}

}
