package aisoccer.behaviorTree;

import java.util.LinkedList;

import aisoccer.Brain;

public abstract class CompositeTask implements Task{

	protected LinkedList<Task> children;
	
	public CompositeTask(){
		this.children = new LinkedList<Task>();
	}
	
	public CompositeTask(LinkedList<Task> children){
		this.children = children;
	}

	@Override
	public abstract boolean Call(Brain brain);
	
	public void addChildren(LinkedList<Task> c){
		this.children = c;
	}
	
	public void addChild(Task c){
		this.children.addLast(c);
	}

}
