package aisoccer.actions;

import java.util.LinkedList;

import aisoccer.Brain;
import aisoccer.SoccerParams;
import aisoccer.behaviorTree.Selector;
import aisoccer.behaviorTree.Task;

public class GoToBall extends Selector {

	public GoToBall(LinkedList<Task> children) {
		super(children);
	}
	
	public GoToBall(){
		children = new LinkedList<Task>();
		children.add(new TurnToBall());
		children.add(new GoStraightAhead());
	}

	@Override
	public boolean checkConditions(Brain brain) {
		return brain.getPlayer().distanceTo(brain.getFullstateInfo().getBall()) > SoccerParams.KICKABLE_MARGIN;
	}

	@Override
	public void Start() {
	}

	@Override
	public void End() {	
	}

}
