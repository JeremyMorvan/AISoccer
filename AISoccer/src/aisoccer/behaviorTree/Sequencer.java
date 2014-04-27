package aisoccer.behaviorTree;

import java.util.LinkedList;

public abstract class Sequencer extends CompositeTask {

	public Sequencer(LinkedList<Task> children) {
		super(children);
		// TODO Auto-generated constructor stub
	}

	public abstract void Start();

	public abstract void End();

	@Override
	public boolean Call() {
		if(checkConditions()){
			Start();
			for(Task child : this.children){
				if(!child.Call()){
					End();
					return false;
				}
			}
			End();
			return true;
		}
		End();
		return false;		
	}

}
