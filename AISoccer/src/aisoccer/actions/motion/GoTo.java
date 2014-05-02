package aisoccer.actions.motion;

import java.util.LinkedList;

import aisoccer.Brain;
import aisoccer.Vector2D;
import aisoccer.behaviorTree.Selector;
import aisoccer.behaviorTree.Task;

public abstract class GoTo extends Selector {

	
	public GoTo(){
		children = new LinkedList<Task>();
		children.add(new TurnTo());
		children.add(new GoStraightAhead());
	}
	
	
	@Override
	public abstract boolean checkConditions(Brain brain);

	@Override
	public abstract void Start(Brain brain);

	@Override
	public void End(Brain brain) {
		brain.setInterestPos(new Vector2D(0,0));
	}

}
