package aisoccer.actions;

import aisoccer.Brain;
import aisoccer.PlayerAction;
import aisoccer.PlayerActionType;
import aisoccer.SoccerParams;
import aisoccer.Vector2D;
import aisoccer.behaviorTree.ActionTask;

public class Shoot extends ActionTask {

	Vector2D acc;
	final double ballMinSpeed = 0.5;
	
	public Shoot(Brain b) {
		super(b);
	}

	@Override
	public boolean CheckConditions() {
		Vector2D ballP = brain.getFullstateInfo().getBall().getPosition();
		Vector2D ballV = brain.getFullstateInfo().getBall().getVelocity();
		System.err.println(ballV.polarRadius());
		Vector2D dir = brain.getInterestPos().subtract(ballP).normalize();
		Vector2D dirComponent = dir.multiply(dir.multiply(ballV));
		Vector2D orthogonalComponent = ballV.subtract( dirComponent );
		double epr = brain.getEffectivePowerRate();

//		double speedNeeded = speedAtTarget + (1-SoccerParams.BALL_DECAY)*target.distanceTo(ballP);
//		Vector2D velocityNeeded = dir.multiply(speedNeeded);
//		Vector2D accNeeded = velocityNeeded.subtract(ballV);
		
		if(orthogonalComponent.polarRadius()>100*epr){
//			return false;
		}
		
		double effectiveAccelerationDirMax = Math.sqrt(Math.pow(100*epr, 2) - Math.pow(orthogonalComponent.polarRadius(), 2));
		if (effectiveAccelerationDirMax + dir.multiply(ballV) < ballMinSpeed){
//			return false;
		}

		// change here to modify ball speed after kick
		acc = orthogonalComponent.multiply(-1.0).add(dir.multiply(effectiveAccelerationDirMax));
		acc = acc.multiply(1/epr);
		return ballV.polarRadius()<0.1;
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
