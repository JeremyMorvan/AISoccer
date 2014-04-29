package aisoccer.behaviorTree;

import aisoccer.Brain;
import aisoccer.Player;
import aisoccer.RobocupClient;
import aisoccer.ballcapture.State;

public interface Task {
	public abstract boolean Call(Brain brain);
	public abstract boolean checkConditions(Brain brain);
}
