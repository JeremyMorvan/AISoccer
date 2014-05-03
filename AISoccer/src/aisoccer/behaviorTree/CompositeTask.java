package aisoccer.behaviorTree;

import java.util.LinkedList;

import aisoccer.Brain;

public abstract class CompositeTask extends Task{

	protected LinkedList<Task> children;
	
	public CompositeTask(Brain b){
		super(b);
		this.children = new LinkedList<Task>();
	}

	public abstract void Start();	
	public abstract void End();	

}
