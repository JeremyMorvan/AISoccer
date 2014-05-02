package aisoccer.actions;

import java.util.LinkedList;

import aisoccer.Brain;
import aisoccer.behaviorTree.Selector;
import aisoccer.behaviorTree.Task;

public class Position extends Selector {

	public Position(){
		children = new LinkedList<Task>();
		children.add(new PositionDef());
		children.add(new PositionOff());
	}
	
	@Override
	public boolean checkConditions(Brain brain) {
		return true;
	}

	@Override
	public void Start(Brain brain) {}

	@Override
	public void End(Brain brain) {}
	
	

}
