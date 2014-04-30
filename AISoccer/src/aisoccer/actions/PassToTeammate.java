package aisoccer.actions;

import java.util.LinkedList;

import aisoccer.Brain;
import aisoccer.behaviorTree.Sequencer;
import aisoccer.behaviorTree.Task;

public class PassToTeammate extends Sequencer {

	
	public PassToTeammate(){
		children = new LinkedList<Task>();
		children.add(new FindUnmarkedTeammate());
		children.add(new Pass());
	}
	
	@Override
	public boolean checkConditions(Brain brain) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void Start(Brain brain) {}

	@Override
	public void End(Brain brain) {}

}
