package aisoccer.actions;

import java.util.LinkedList;

import aisoccer.Brain;
import aisoccer.SoccerParams;
import aisoccer.behaviorTree.Selector;
import aisoccer.behaviorTree.Task;

public class ShootTo extends Selector {	
	
	public ShootTo(Brain b){
		super(b);
		children = new LinkedList<Task>();
		children.add(new Shoot(brain));
		children.add(new ControlBall(brain));
	}
	

	public boolean CheckConditions() {
		return brain.getPlayer().distanceTo(brain.getFullstateInfo().getBall())<=SoccerParams.KICKABLE_MARGIN;
	}


	@Override
	public void Start() {		
	}


	@Override
	public void End() {	
		brain.setShootVector(null);
		brain.setInterestPos(null);
	}
	


}
