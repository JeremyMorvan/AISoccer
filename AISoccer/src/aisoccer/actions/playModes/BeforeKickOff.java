package aisoccer.actions.playModes;

import java.util.LinkedList;

import aisoccer.Brain;
import aisoccer.behaviorTree.Selector;
import aisoccer.behaviorTree.Task;

public class BeforeKickOff extends Selector {

	public BeforeKickOff() {
		children = new LinkedList<Task>();
		children.add(new BeforeKickOffAttack());
		children.add(new BeforeKickOffDefence());		
	}

	@Override
	public boolean checkConditions(Brain brain) {
		return true;
	}

	@Override
	public void Start(Brain brain) {}

	@Override
	public void End(Brain brain) {}

}
