package aisoccer.strategy;

import java.util.LinkedList;

import aisoccer.Brain;
import aisoccer.actions.playModes.*;
import aisoccer.behaviorTree.Selector;
import aisoccer.behaviorTree.Task;

public class myGoalieStrategyBasic extends Selector implements Strategy {

	public myGoalieStrategyBasic(int numberOfPlayers, Brain b) {
		super(b);
		children = new LinkedList<Task>();
		children.add(new BeforeKickOff(b));
		children.add(new FreeKickGoalie(b));
		children.add(new PlayOnGoalieBasic(b));
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
