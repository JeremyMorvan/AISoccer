package aisoccer.strategy;

import java.util.LinkedList;

import aisoccer.Brain;
import aisoccer.actions.playModes.BeforeKickOff;
import aisoccer.actions.playModes.FreeKick;
import aisoccer.actions.playModes.KickIn;
import aisoccer.actions.playModes.PlayOn;
import aisoccer.behaviorTree.Selector;
import aisoccer.behaviorTree.Task;

public class myPlayerStrategy extends Selector implements Strategy {

	public myPlayerStrategy(int numberOfPlayers, Brain b) {
		super(b);
		children = new LinkedList<Task>();
		children.add(new BeforeKickOff(b));
		children.add(new FreeKick(b));
		children.add(new KickIn(b));
		children.add(new PlayOn(b));
		Formation442.setMyAreas(numberOfPlayers, b);
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
