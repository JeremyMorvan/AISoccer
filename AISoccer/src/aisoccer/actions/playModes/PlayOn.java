package aisoccer.actions.playModes;

import java.util.LinkedList;

import aisoccer.Brain;
import aisoccer.actions.WithTheBall;
import aisoccer.actions.WithoutTheBall;
import aisoccer.behaviorTree.Selector;
import aisoccer.behaviorTree.Task;

public class PlayOn extends Selector {

	public PlayOn(LinkedList<Task> children) {
		super(children);
	}

	public PlayOn() {
		children = new LinkedList<Task>();
		children.add(new WithoutTheBall());
		children.add(new WithTheBall());
	}

	@Override
	public boolean checkConditions(Brain brain) {
//		return brain.getFullstateInfo().getPlayMode().equals("play_on")||brain.getFullstateInfo().getPlayMode().equals("kick_off_l")||brain.getFullstateInfo().getPlayMode().equals("kick_off_r");
		return true;
	}

	@Override
	public void Start(Brain brain) {
		// TODO Auto-generated method stub

	}

	@Override
	public void End(Brain brain) {
		// TODO Auto-generated method stub

	}

}
