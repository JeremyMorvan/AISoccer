package aisoccer.strategy;

import java.util.LinkedList;

import aisoccer.FullstateInfo;
import aisoccer.Player;
import aisoccer.RobocupClient;
import aisoccer.actions.GoToBall;
import aisoccer.actions.ShootToGoal;
import aisoccer.behaviorTree.Task;

public class myStrategy extends Strategy {

	
	
	public myStrategy() {
		children = new LinkedList<Task>();
		children.add(new GoToBall());
		children.add(new ShootToGoal());
	}
	
}
