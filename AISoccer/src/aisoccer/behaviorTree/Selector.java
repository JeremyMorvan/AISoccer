package aisoccer.behaviorTree;

import java.util.LinkedList;

import aisoccer.Brain;

public abstract class Selector extends CompositeTask {	
	
	public Selector(LinkedList<Task> children) {
		super(children);
		// TODO Auto-generated constructor stub
	}
	
	public Selector(){
		
	}

	public abstract void Start();
	
	public abstract void End();
	
	
	@Override
	public boolean Call(Brain brain) {
		if(checkConditions(brain)){
			Start();
			for(Task child : this.children){
				if(child.Call(brain)){
					End();
					return true;
				}
			}
		}
		End();
		return false;
		
	}

}
