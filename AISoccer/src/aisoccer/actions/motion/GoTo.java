package aisoccer.actions.motion;

import java.util.LinkedList;

import math.Vector2D;
import aisoccer.Brain;
import aisoccer.behaviorTree.Selector;
import aisoccer.behaviorTree.Task;

public abstract class GoTo extends Selector {
	
	public GoTo(Brain b){
		super(b);
		children = new LinkedList<Task>();
		children.add(new TurnTo(b));
		children.add(new GoStraightAhead(b));
	}
	
	
	@Override
	public abstract boolean CheckConditions();

	@Override
	public abstract void Start();

	@Override
	public void End() {
		brain.setInterestPos(new Vector2D(0,0));
	}

}
