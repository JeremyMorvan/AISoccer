package aisoccer.actions;

import java.util.LinkedList;

import aisoccer.Brain;
import aisoccer.SoccerParams;
import aisoccer.actions.motion.AttackTheBall;
import aisoccer.actions.positionning.Position;
import aisoccer.behaviorTree.Selector;
import aisoccer.behaviorTree.Task;

public class WithoutTheBall extends Selector {

	
	public WithoutTheBall(Brain b){
		super(b);
		children = new LinkedList<Task>();
		children.add(new AttackTheBall(b));
		children.add(new Position(b));
	}
	
	@Override
	public boolean CheckConditions() {
		return brain.getPlayer().distanceTo(brain.getFullstateInfo().getBall()) > SoccerParams.KICKABLE_MARGIN;
	}

	@Override
	public void Start() {
		//System.out.println(brain.getPlayer().toString() + " : I don't have the ball !");
	}

	@Override
	public void End() {}

}
