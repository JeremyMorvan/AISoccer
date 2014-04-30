package aisoccer.actions;

import java.util.LinkedList;

import aisoccer.Brain;
import aisoccer.SoccerParams;
import aisoccer.behaviorTree.Selector;
import aisoccer.behaviorTree.Task;

public class WithoutTheBall extends Selector {

	
	public WithoutTheBall(){
		children = new LinkedList<Task>();
		children.add(new AttackTheBall());
		children.add(new Position());
	}
	
	@Override
	public boolean checkConditions(Brain brain) {
		return brain.getPlayer().distanceTo(brain.getFullstateInfo().getBall()) > SoccerParams.KICKABLE_MARGIN;
	}

	@Override
	public void Start(Brain brain) {
		//System.out.println(brain.getPlayer().toString() + " : I don't have the ball !");
	}

	@Override
	public void End(Brain brain) {}

}
