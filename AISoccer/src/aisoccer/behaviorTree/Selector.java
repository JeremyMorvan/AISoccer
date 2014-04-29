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

	public abstract void Start(Brain brain);
	
	public abstract void End(Brain brain);
	
	
	@Override
	public boolean Call(Brain brain) {
		if(checkConditions(brain)){
			Start(brain);
			for(Task child : this.children){
				if(child.Call(brain)){
					End(brain);
					return true;
				}
			}
		}
		End(brain);
		return false;		
	}

}
