package aisoccer.strategy;

import java.util.LinkedList;

import aisoccer.Brain;
import aisoccer.actions.CatchGoalie;
import aisoccer.actions.motion.AttackTheBallGoalie;
import aisoccer.actions.playModes.BeforeKickOff;
import aisoccer.actions.playModes.FreeKickGoalie;
import aisoccer.actions.playModes.PlayOn;
import aisoccer.actions.playModes.PlayOnGoalie;
import aisoccer.behaviorTree.Selector;
import aisoccer.behaviorTree.Task;

public class myGoalieStrategy extends Selector implements Strategy {

	public myGoalieStrategy(int numberOfPlayers, Brain b) {
		super(b);
		children = new LinkedList<Task>();
		children.add(new BeforeKickOff(b));
		children.add(new FreeKickGoalie(b));
		children.add(new PlayOnGoalie(b));
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
