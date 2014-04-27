package aisoccer.behaviorTree;

import aisoccer.Player;
import aisoccer.RobocupClient;
import aisoccer.ballcapture.State;

public interface Task {
	public abstract boolean Call(RobocupClient rc,State s,Player player);
	public abstract boolean checkConditions(State s,Player player);
}
