package aisoccer.behaviorTree;

import java.util.LinkedList;

public abstract class Selector extends CompositeTask {	
	
	public Selector(LinkedList<Task> children) {
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
				if(child.Call()){
					End();
					return true;
				}
			}
		}
		End();
		return false;
		
	}

}
