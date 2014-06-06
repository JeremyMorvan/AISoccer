package aisoccer.actions.playModes;

import java.util.LinkedList;

import aisoccer.Brain;
import aisoccer.actions.WithTheBall;
import aisoccer.actions.WithTheBallBasic;
import aisoccer.actions.WithoutTheBall;
import aisoccer.behaviorTree.Selector;
import aisoccer.behaviorTree.Task;

public class PlayOnBasic extends Selector {

	public PlayOnBasic(Brain b) {
		super(b);
		children = new LinkedList<Task>();
		children.add(new WithoutTheBall(b));
		children.add(new WithTheBallBasic(b));
	}

	@Override
	public boolean CheckConditions() {
//		return brain.getFullstateInfo().getPlayMode().equals("play_on")||brain.getFullstateInfo().getPlayMode().equals("kick_off_l")||brain.getFullstateInfo().getPlayMode().equals("kick_off_r");
		return true;
	}

	@Override
	public void Start() {}

	@Override
	public void End() {}

}
