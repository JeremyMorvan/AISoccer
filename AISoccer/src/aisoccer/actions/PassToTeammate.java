package aisoccer.actions;

import java.util.LinkedList;

import aisoccer.Brain;
import aisoccer.behaviorTree.Sequencer;
import aisoccer.behaviorTree.Task;

public class PassToTeammate extends Sequencer {
	
	public PassToTeammate(Brain b, boolean allowBackward){
		super(b);
		children = new LinkedList<Task>();
		children.add(new FindUnmarkedTeammate(brain, allowBackward));
		children.add(new Pass(brain));
	}
	
	@Override
	public boolean CheckConditions() {
		return true;
	}

	@Override
	public void Start() {
//		System.out.println(brain.getPlayer().toString() + " : Let's find a teammate !");
	}

	@Override
	public void End() {}

}
