package aisoccer.behaviorTree;

import aisoccer.Brain;

public interface Task {
	public abstract boolean Call(Brain brain);
	public abstract boolean checkConditions(Brain brain);
}
