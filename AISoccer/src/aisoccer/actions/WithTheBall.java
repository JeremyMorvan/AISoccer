package aisoccer.actions;

import java.util.LinkedList;

import aisoccer.Brain;
import aisoccer.SoccerParams;
import aisoccer.behaviorTree.Selector;
import aisoccer.behaviorTree.Task;

public class WithTheBall extends Selector {

	
	public WithTheBall(Brain b){
		super(b);
		children = new LinkedList<Task>();
		children.add(new ShootToGoal(b, true));
		children.add(new PassToTeammate(b));
		children.add(new Dribble(b));
	}
	
	@Override
	public boolean CheckConditions() {
		return brain.getPlayer().distanceTo(brain.getFullstateInfo().getBall()) <= SoccerParams.KICKABLE_MARGIN;
	}

	@Override
	public void Start() {}

	@Override
	public void End() {}

}
