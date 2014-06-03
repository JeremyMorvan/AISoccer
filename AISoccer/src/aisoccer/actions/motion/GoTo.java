package aisoccer.actions.motion;

import java.util.LinkedList;

import aisoccer.Brain;
import aisoccer.behaviorTree.Selector;
import aisoccer.behaviorTree.Task;

public abstract class GoTo extends Selector {
	
	public GoTo(Brain b){
		super(b);
		children = new LinkedList<Task>();
		children.add(new TurnTo(b));
		children.add(new GoStraightAhead(b));
		children.add(new Stay(b));
	}
	
	
	@Override
	public boolean CheckConditions(){
		return true;
	}
	
	public abstract void defineInterestPosition();

	@Override
	public void Start(){	
		defineInterestPosition();
	}

	@Override
	public void End() {
		brain.setInterestPos(null);
	}

}
