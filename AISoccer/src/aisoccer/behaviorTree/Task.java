package aisoccer.behaviorTree;

import aisoccer.Brain;

public abstract class Task {
	protected Brain brain;
	
	public Task(){}
	
	public Task(Brain b){
		setBrain(b);
	}
	
	public Brain getBrain() {
		return brain;
	}

	public void setBrain(Brain brain) {
		this.brain = brain;
	}

	public abstract boolean Call();
	public abstract boolean CheckConditions();
}
