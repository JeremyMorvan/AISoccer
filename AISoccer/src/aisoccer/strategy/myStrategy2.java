package aisoccer.strategy;

import java.util.LinkedList;

import aisoccer.Brain;
import aisoccer.actions.playModes.BeforeKickOff;
import aisoccer.actions.playModes.PlayOn;
import aisoccer.behaviorTree.Selector;
import aisoccer.behaviorTree.Task;

public class myStrategy2 extends Selector implements Strategy {

	public myStrategy2(int numberOfPlayers,Brain brain) {
		children = new LinkedList<Task>();
		children.add(new BeforeKickOff());
		children.add(new PlayOn());
		Formation442.setMyAreas(numberOfPlayers, brain);
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
