package aisoccer.strategy;

import java.util.LinkedList;

import aisoccer.actions.WithTheBall;
import aisoccer.actions.WithoutTheBall;
import aisoccer.behaviorTree.Task;

public class myStrategy2 extends Strategy {

	public myStrategy2() {
		children = new LinkedList<Task>();
		children.add(new WithoutTheBall());
		children.add(new WithTheBall());
	}
	
}
