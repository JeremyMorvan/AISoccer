package aisoccer.actions.playModes;

import java.util.LinkedList;

import aisoccer.Brain;
import aisoccer.actions.CatchBallGoalie;
import aisoccer.actions.CatchGoalie;
import aisoccer.actions.WithTheBall;
import aisoccer.actions.WithTheBallBasic;
import aisoccer.actions.WithoutTheBallGoalie;
import aisoccer.actions.WithoutTheBallGoalieBasic;
import aisoccer.behaviorTree.Selector;
import aisoccer.behaviorTree.Task;

public class PlayOnGoalieBasic extends Selector {

	public PlayOnGoalieBasic(Brain b) {
		super(b);
		children = new LinkedList<Task>();
		children.add(new CatchBallGoalie(b));
		children.add(new WithTheBallBasic(b));
		children.add(new WithoutTheBallGoalieBasic(b));
	}

	@Override
	public boolean CheckConditions() {
		return true;
	}

	@Override
	public void Start() {
	}

	@Override
	public void End() {
	}

}
