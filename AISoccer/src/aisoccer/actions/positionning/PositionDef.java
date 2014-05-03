package aisoccer.actions.positionning;

import java.util.LinkedList;

import aisoccer.Brain;
import aisoccer.behaviorTree.Selector;
import aisoccer.behaviorTree.Task;

public class PositionDef extends Selector {

	
	public PositionDef(Brain b){
		super(b);
		children = new LinkedList<Task>();
		children.add(new MarkOpponent(b));
	}
	
	
	@Override
	public boolean CheckConditions(){
		return brain.getPlayer().isLeftSide() != brain.getFullstateInfo().LeftGotBall();		
	}

	@Override
	public void Start() {}


	@Override
	public void End() {}

}
