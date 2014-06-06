package aisoccer.actions;

import java.util.LinkedList;

import aisoccer.Brain;
import aisoccer.SoccerParams;
import aisoccer.behaviorTree.Selector;
import aisoccer.behaviorTree.Task;

public class WithTheBallBasic extends Selector {

	
	public WithTheBallBasic(Brain b){
		super(b);
		children = new LinkedList<Task>();
		children.add(new ShootToGoalBasic(b));
		children.add(new PassToTeammateBasic(b,false));
		children.add(new DribbleBasic(b));
	}
	
	@Override
	public boolean CheckConditions() {
		return brain.getPlayer().distanceTo(brain.getFullstateInfo().getBall()) <= SoccerParams.KICKABLE_MARGIN;
	}

	@Override
	public void Start() {
//		System.out.println(brain.getPlayer().toString()+" : 'The ball is next to me ! '");
	}

	@Override
	public void End() {}

}
