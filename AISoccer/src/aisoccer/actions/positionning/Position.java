package aisoccer.actions.positionning;

import java.util.LinkedList;

import aisoccer.Brain;
import aisoccer.behaviorTree.Selector;
import aisoccer.behaviorTree.Task;

public class Position extends Selector {

	public Position(Brain b){
		super(b);
		children = new LinkedList<Task>();
		children.add(new PositionDef(b));
		children.add(new PositionOff(b));
	}
	
	@Override
	public boolean CheckConditions() {
		return true;
	}

	@Override
	public void Start() {}

	@Override
	public void End() {}
	
	

}
