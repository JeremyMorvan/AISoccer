package aisoccer.behaviorTree;

import aisoccer.Brain;

public abstract class Selector extends CompositeTask {	
	
	public Selector(Brain b){
		super(b);
	}
	
	@Override
	public boolean Call() {
		if(CheckConditions()){
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
