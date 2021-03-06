package aisoccer.actions.motion;

import java.util.LinkedList;

import aisoccer.Brain;
import aisoccer.behaviorTree.Sequencer;
import aisoccer.behaviorTree.Task;
import aisoccer.fullStateInfo.Player;

public class AttackTheBallGoalie extends Sequencer {

	
	public AttackTheBallGoalie(Brain b){
		super(b);
		children = new LinkedList<Task>();
		children.add(new InterceptBall(b));		
	}
	
	@Override
	public boolean CheckConditions() {
		Player me = brain.getPlayer();
		double myDist = brain.timeToIntercept(me);
		for(Player tm : brain.getFullstateInfo().getEveryBody() ){
			double t = brain.timeToIntercept(tm);
			if(t>0 && t<myDist){
				return false;
			}
		}
		return true;
	}

	@Override
	public void Start() {
	}

	@Override
	public void End() {}

}
