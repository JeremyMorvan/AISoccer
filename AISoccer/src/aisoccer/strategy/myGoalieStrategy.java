package aisoccer.strategy;

import java.util.LinkedList;

import aisoccer.Brain;
import aisoccer.actions.CatchGoalie;
import aisoccer.actions.motion.AttackTheBallGoalie;
import aisoccer.behaviorTree.Selector;
import aisoccer.behaviorTree.Task;

public class myGoalieStrategy extends Selector implements Strategy {

	public myGoalieStrategy(Brain b) {
		super(b);
		children = new LinkedList<Task>();
		children.add(new CatchGoalie(b));
		children.add(new AttackTheBallGoalie(b));
	}

	@Override
	public boolean CheckConditions() {
		return true;
	}

	@Override
	public void doAction(Brain b) {
		Call();		
	}

	@Override
	public void Start() {}

	@Override
	public void End() {}

}
