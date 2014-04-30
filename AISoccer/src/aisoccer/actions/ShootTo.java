package aisoccer.actions;

import aisoccer.Brain;
import aisoccer.PlayerAction;
import aisoccer.PlayerActionType;
import aisoccer.behaviorTree.ActionTask;

public abstract class ShootTo extends ActionTask {
	
	boolean checkDistance;
	
	public ShootTo(boolean checkDistance){
		super();
		this.checkDistance = checkDistance;
	}
	
	@Override
	public boolean checkConditions(Brain brain) {
		if(brain.getInterestPos()==null){
			return false;
		}
		if(checkDistance){
			if(brain.getPlayer().distanceTo((brain.getInterestPos()))>40){
				return false;
			}
		}
		return true;
	}

	@Override
	public void DoAction(Brain brain) {
		System.out.println(brain.getPlayer().toString() + " : I am going to shoot ! : " + brain.getInterestPos().toString());
        brain.doAction(new PlayerAction(PlayerActionType.KICK,100.0d, brain.getPlayer().angleFromBody(brain.getInterestPos()), brain.getRobocupClient()));
	}

	@Override
	public abstract void Start(Brain brain);

}
