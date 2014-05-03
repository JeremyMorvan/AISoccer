package aisoccer.behaviorTree;

import aisoccer.Brain;

public abstract class Sequencer extends CompositeTask {

	public Sequencer(Brain b){
		super(b);
	}

	@Override
	public boolean Call() {
		if(CheckConditions()){
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
