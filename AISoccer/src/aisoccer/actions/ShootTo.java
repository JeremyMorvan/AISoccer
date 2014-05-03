package aisoccer.actions;

import aisoccer.Brain;
import aisoccer.PlayerAction;
import aisoccer.PlayerActionType;
import aisoccer.behaviorTree.ActionTask;

public abstract class ShootTo extends ActionTask {
	
	boolean checkDistance;
	
	public ShootTo(Brain b, boolean checkDistance){
		super(b);
		this.checkDistance = checkDistance;
	}
	
	public abstract boolean CheckConditions();

	@Override
	public void DoAction() {
		//System.out.println(brain.getPlayer().toString() + " : I am going to shoot ! : " + brain.getInterestPos().toString());
        brain.doAction(new PlayerAction(PlayerActionType.KICK,100.0d, brain.getPlayer().angleFromBody(brain.getInterestPos()), brain.getRobocupClient()));
	}

	@Override
	public abstract void Start();

}
