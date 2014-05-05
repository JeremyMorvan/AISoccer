package aisoccer.actions;

import java.util.LinkedList;

import aisoccer.Brain;
import aisoccer.behaviorTree.Selector;
import aisoccer.behaviorTree.Task;

public abstract class ShootTo extends Selector {
	
	boolean checkDistance;
	
	public ShootTo(Brain b, boolean checkDistance){
		super(b);
		this.checkDistance = checkDistance;
		children = new LinkedList<Task>();
		children.add(new Shoot(brain));
		children.add(new ControlBall(brain));
	}
	
	
	


}
