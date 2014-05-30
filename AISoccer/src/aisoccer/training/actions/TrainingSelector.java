package aisoccer.training.actions;

import java.util.LinkedList;

import aisoccer.Brain;
import aisoccer.SoccerParams;
import aisoccer.actions.positionning.PositionOff;
import aisoccer.behaviorTree.Selector;
import aisoccer.behaviorTree.Task;

public class TrainingSelector extends Selector {

	public TrainingSelector(Brain b) {
		super(b);
		children = new LinkedList<Task>();
		children.add(new InterceptCondition(b));
		children.add(new PositionOff(b));
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
		return brain.getPlayer().distanceTo(brain.getFullstateInfo().getBall()) > SoccerParams.KICKABLE_MARGIN;
	}

}
