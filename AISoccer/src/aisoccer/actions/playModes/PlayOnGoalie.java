package aisoccer.actions.playModes;

import java.util.LinkedList;

import aisoccer.Brain;
import aisoccer.actions.CatchBallGoalie;
import aisoccer.actions.CatchGoalie;
import aisoccer.actions.WithTheBall;
import aisoccer.actions.WithoutTheBallGoalie;
import aisoccer.behaviorTree.Selector;
import aisoccer.behaviorTree.Task;

public class PlayOnGoalie extends Selector {

	public PlayOnGoalie(Brain b) {
		super(b);
		children = new LinkedList<Task>();
		children.add(new CatchBallGoalie(b));
		children.add(new WithTheBall(b));
		children.add(new WithoutTheBallGoalie(b));
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
