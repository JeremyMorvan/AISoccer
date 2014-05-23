package aisoccer.training;

import java.util.LinkedList;

import aisoccer.Brain;
import aisoccer.actions.Pass;
import aisoccer.behaviorTree.Sequencer;
import aisoccer.behaviorTree.Task;

public class PassRandom extends Sequencer {

	public PassRandom(Brain b){
		super(b);
		children = new LinkedList<Task>();
		children.add(new SetRandomPos(brain));
		children.add(new Pass(brain));
	}
	
	@Override
	public boolean CheckConditions() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void Start() {
//		System.out.println(brain.getPlayer().toString() + " : Let's find a teammate !");
	}

	@Override
	public void End() {}

}