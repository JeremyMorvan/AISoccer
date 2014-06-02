package aisoccer.actions.positionning;

import aisoccer.Brain;
import aisoccer.behaviorTree.ActionTask;

public class PositionGoalie extends ActionTask {

	public PositionGoalie(Brain b) {
		super(b);
	}

	@Override
	public boolean CheckConditions() {
		return true;
	}

	@Override
	public void DoAction() {
	}

	@Override
	public void Start() {}

}
