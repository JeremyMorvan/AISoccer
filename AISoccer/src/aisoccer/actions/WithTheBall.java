package aisoccer.actions;

import java.util.LinkedList;

import aisoccer.Brain;
import aisoccer.SoccerParams;
import aisoccer.behaviorTree.Selector;
import aisoccer.behaviorTree.Task;

public class WithTheBall extends Selector {

	
	public WithTheBall(){
		children = new LinkedList<Task>();
		children.add(new ShootToGoal(true));
		children.add(new PassToTeammate());
		children.add(new Dribble());
	}
	
	@Override
	public boolean checkConditions(Brain brain) {
		return brain.getPlayer().distanceTo(brain.getFullstateInfo().getBall()) <= SoccerParams.KICKABLE_MARGIN;
	}

	@Override
	public void Start(Brain brain) {}

	@Override
	public void End(Brain brain) {}

}
