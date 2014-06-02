package aisoccer.actions;

import java.util.LinkedList;

import aisoccer.Brain;
import aisoccer.behaviorTree.Selector;
import aisoccer.behaviorTree.Task;

public abstract class ShootTo extends Selector {
	
	
	public ShootTo(Brain b){
		super(b);
		children = new LinkedList<Task>();
		children.add(new Shoot(brain));
		children.add(new ControlBall(brain));
	}
	
	
	


}
