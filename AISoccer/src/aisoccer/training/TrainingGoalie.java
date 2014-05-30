package aisoccer.training;

import java.util.LinkedList;

import aisoccer.Brain;
import aisoccer.actions.CatchGoalie;
import aisoccer.actions.motion.AttackTheBallGoalie;
import aisoccer.behaviorTree.Selector;
import aisoccer.behaviorTree.Task;
import aisoccer.strategy.Strategy;

public class TrainingGoalie extends Selector implements Strategy {

	public TrainingGoalie(Brain b) {
		super(b);
		children = new LinkedList<Task>();
		children.add(new CatchGoalie(b));
		children.add(new AttackTheBallGoalie(b));
	}

	@Override
	public void doAction(Brain brain) {
		Call();
	}

	@Override
	public void Start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void End() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean CheckConditions() {
		return brain.getFullstateInfo().getPlayMode()!= null && brain.getFullstateInfo().getPlayMode().equals("play_on");
	}

}
