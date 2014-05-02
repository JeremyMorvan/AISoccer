package aisoccer.strategy;

import java.util.LinkedList;

import aisoccer.Brain;
import aisoccer.actions.ShootToGoal;
import aisoccer.actions.motion.InterceptBall;
import aisoccer.behaviorTree.Selector;
import aisoccer.behaviorTree.Task;

public class myStrategy extends Selector implements Strategy {
	
	public myStrategy() {
		children = new LinkedList<Task>();
		children.add(new InterceptBall());
		children.add(new ShootToGoal(false));
	}

	@Override
	public boolean checkConditions(Brain brain) {
		return true;
	}

	@Override
	public void doAction(Brain brain) {
		Call(brain);		
	}

	@Override
	public void Start(Brain brain) {}

	@Override
	public void End(Brain brain) {}
	

	
}
