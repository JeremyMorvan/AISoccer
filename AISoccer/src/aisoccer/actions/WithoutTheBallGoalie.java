package aisoccer.actions;

import java.util.LinkedList;

import aisoccer.Brain;
import aisoccer.SoccerParams;
import aisoccer.actions.motion.AttackTheBallGoalie;
import aisoccer.actions.positionning.PositionGoalie;
import aisoccer.actions.positionning.PositionGoalieGeometric;
import aisoccer.behaviorTree.Selector;
import aisoccer.behaviorTree.Task;

public class WithoutTheBallGoalie extends Selector {


	public WithoutTheBallGoalie(Brain b) {
		super(b);
		children = new LinkedList<Task>();
		children.add(new AttackTheBallGoalie(b));
		children.add(new PositionGoalieGeometric(b));
	}

	@Override
	public boolean CheckConditions() {
		return brain.getPlayer().distanceTo(brain.getFullstateInfo().getBall())>SoccerParams.KICKABLE_MARGIN;
	}
	
	@Override
	public void Start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void End() {
		// TODO Auto-generated method stub

	}

}
