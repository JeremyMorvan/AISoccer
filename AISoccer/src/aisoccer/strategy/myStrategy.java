package aisoccer.strategy;

import java.util.LinkedList;

import aisoccer.Brain;
import aisoccer.actions.ShootToGoal;
import aisoccer.actions.motion.InterceptBall;
import aisoccer.behaviorTree.Selector;
import aisoccer.behaviorTree.Task;

public class myStrategy extends Selector implements Strategy {
	
	public myStrategy(Brain b) {
		super(b);
		children = new LinkedList<Task>();
		children.add(new InterceptBall(brain));
		children.add(new ShootToGoal(brain));
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
