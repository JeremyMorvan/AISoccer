package aisoccer.strategy;

import java.util.LinkedList;

import aisoccer.*;
import aisoccer.actions.GoToBall;
import aisoccer.actions.InterceptBall;
import aisoccer.actions.ShootToGoal;
import aisoccer.behaviorTree.Task;

public class myStrategy extends Strategy {

	
	
	public myStrategy() {
		children = new LinkedList<Task>();
		children.add(new InterceptBall());
		children.add(new ShootToGoal());
	}
	

	
}
