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
		children.add(new ShootToGoalBasic(b));
//		children.add(new ShootToGoal(b));
//		children.add(new PassToTeammate(b,false));
//		children.add(new Dribble(b,false));
		children.add(new PassToTeammate(b,true));
		children.add(new DribbleBasic(b));
//		children.add(new Dribble(b,true));
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
