package aisoccer.actions;

import java.util.LinkedList;

import aisoccer.Brain;
import aisoccer.SoccerParams;
import aisoccer.behaviorTree.Sequencer;
import aisoccer.behaviorTree.Task;

public class PassToTeammateBasic extends Sequencer {
	
	public PassToTeammateBasic(Brain b, boolean allowBackward){
		super(b);
		children = new LinkedList<Task>();
		children.add(new FindUnmarkedTeammateBasic(brain, allowBackward));
		children.add(new ShootToBasic(brain));
	}
	
	@Override
	public boolean CheckConditions() {
		return brain.getPlayer().distanceTo(brain.getFullstateInfo().getBall())<=SoccerParams.KICKABLE_MARGIN;
	}
	
	@Override
	public void Start() {
//		System.out.println(brain.getPlayer().toString() + " : Let's find a teammate !");
	}

	@Override
	public void End() {}


}
