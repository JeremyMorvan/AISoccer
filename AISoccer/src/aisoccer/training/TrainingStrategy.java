package aisoccer.training;

import java.util.LinkedList;

import aisoccer.Brain;
import aisoccer.actions.motion.InterceptBall;
import aisoccer.behaviorTree.Selector;
import aisoccer.behaviorTree.Task;
import aisoccer.strategy.Strategy;

public class TrainingStrategy extends Selector implements Strategy {

	public TrainingStrategy(Brain b) {
		super(b);
		children = new LinkedList<Task>();
		children.add(new InterceptBall(brain));
//		children.add(new TrainingSelector(brain));
//		children.add(new PassRandom(brain));
//		Formation442.setMyAreas(6, b);
	}

	@Override
	public boolean CheckConditions() {
		return brain.getFullstateInfo().getPlayMode()!= null && brain.getFullstateInfo().getPlayMode().equals("play_on");
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
