package aisoccer.strategy;

import java.util.LinkedList;

import aisoccer.Brain;
import aisoccer.behaviorTree.Selector;
import aisoccer.behaviorTree.Task;

public class PlayOnGoalie extends Selector {

	public PlayOnGoalie(Brain b) {
		super(b);
		children = new LinkedList<Task>();
	}

	@Override
	public boolean CheckConditions() {
		return false;
	}

	@Override
	public void Start() {
	}

	@Override
	public void End() {
	}

}
