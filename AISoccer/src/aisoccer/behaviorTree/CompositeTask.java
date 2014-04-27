package aisoccer.behaviorTree;

import java.util.LinkedList;

import aisoccer.Player;
import aisoccer.RobocupClient;
import aisoccer.ballcapture.State;

public abstract class CompositeTask implements Task{

	protected LinkedList<Task> children;
	
	public CompositeTask(){
		this.children = new LinkedList<Task>();
	}
	
	public CompositeTask(LinkedList<Task> children){
		this.children = children;
	}

	@Override
	public abstract boolean Call(RobocupClient rc,State s,Player player);
	
	public void addChildren(LinkedList<Task> c){
		this.children = c;
	}
	
	public void addChild(Task c){
		this.children.addLast(c);
	}

}
