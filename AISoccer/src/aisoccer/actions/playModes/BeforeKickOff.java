package aisoccer.actions.playModes;

import java.util.LinkedList;

import aisoccer.Brain;
import aisoccer.behaviorTree.Selector;
import aisoccer.behaviorTree.Task;

public class BeforeKickOff extends Selector {

	public BeforeKickOff(Brain b) {
		super(b);
		children = new LinkedList<Task>();
		children.add(new BeforeKickOffAttack(b));
		children.add(new BeforeKickOffDefence(b));		
	}

	@Override
	public boolean CheckConditions() {
		return true;
	}

	@Override
	public void Start() {}

	@Override
	public void End() {}

}
