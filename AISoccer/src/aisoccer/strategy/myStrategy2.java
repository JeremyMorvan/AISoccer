package aisoccer.strategy;

import java.util.LinkedList;

import aisoccer.Brain;
import aisoccer.actions.playModes.BeforeKickOff;
import aisoccer.actions.playModes.FreeKick;
import aisoccer.actions.playModes.KickIn;
import aisoccer.actions.playModes.PlayOn;
import aisoccer.behaviorTree.Selector;
import aisoccer.behaviorTree.Task;

public class myStrategy2 extends Selector implements Strategy {

	public myStrategy2(int numberOfPlayers,Brain b) {
		super(b);
		children = new LinkedList<Task>();
		children.add(new BeforeKickOff(brain));
		children.add(new FreeKick(brain));
		children.add(new KickIn(brain));
		children.add(new PlayOn(brain));
		Formation442.setMyAreas(numberOfPlayers, brain);
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
