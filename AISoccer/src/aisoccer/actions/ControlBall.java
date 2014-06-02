package aisoccer.actions;

import math.MathTools;
import math.Vector2D;
import aisoccer.Brain;
import aisoccer.PlayerAction;
import aisoccer.PlayerActionType;
import aisoccer.SoccerParams;
import aisoccer.behaviorTree.ActionTask;

public class ControlBall extends ActionTask {

	public ControlBall(Brain b) {
		super(b);
	}

	@Override
	public void DoAction() {	
		Vector2D ballP = brain.getFullstateInfo().getBall().getPosition();
		Vector2D ballV = brain.getFullstateInfo().getBall().getVelocity();
		
//		Vector2D deltaP = brain.getPlayer().getPosition().subtract(ballP);
//		double deplacement = deltaP.polarRadius()-(SoccerParams.PLAYER_SIZE+SoccerParams.BALL_SIZE)/2;
//		Vector2D deplacementWanted = deltaP.normalize().multiply(deplacement);		
//		Vector2D accNeeded = deplacementWanted.subtract(ballV);
		
		Vector2D accNeeded = ballV.multiply(-1);
		Vector2D powerNeeded = accNeeded.multiply(1/brain.getEffectivePowerRate());
		double angleKick = MathTools.normalizeAngle(accNeeded.polarAngle()-brain.getPlayer().getBodyDirection());
//		System.out.println(brain.getPlayer().getUniformNumber() + " : I am going to control");
        brain.doAction(new PlayerAction(PlayerActionType.KICK, powerNeeded.polarRadius(), angleKick, brain.getRobocupClient()));
	}

	@Override
	public void Start() {}

	@Override
	public boolean CheckConditions() {
		return true;
	}

}
