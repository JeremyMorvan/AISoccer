package aisoccer.behaviorTree;

import java.util.LinkedList;

import aisoccer.Player;
import aisoccer.RobocupClient;
import aisoccer.ballcapture.State;

public abstract class Selector extends CompositeTask {	
	
	public Selector(LinkedList<Task> children) {
		super(children);
		// TODO Auto-generated constructor stub
	}

	public abstract void Start();
	
	public abstract void End();
	
	
	@Override
	public boolean Call(RobocupClient rc,State s,Player player) {
		if(checkConditions(s,player)){
			Start();
			for(Task child : this.children){
				if(child.Call(rc,s,player)){
					End();
					return true;
				}
			}
		}
		End();
		return false;
		
	}

}
