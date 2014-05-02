package aisoccer.actions.positionning;

import java.util.LinkedList;

import aisoccer.Brain;
import aisoccer.behaviorTree.Selector;
import aisoccer.behaviorTree.Task;

public class PositionDef extends Selector {

	
	public PositionDef(){
		children = new LinkedList<Task>();
		children.add(new MarkOpponent());
	}
	
	
	@Override
	public boolean checkConditions(Brain brain){
		return brain.getPlayer().isLeftSide() != brain.getFullstateInfo().LeftGotBall();		
	}

	@Override
	public void Start(Brain brain) {}


	@Override
	public void End(Brain brain) {}

}
