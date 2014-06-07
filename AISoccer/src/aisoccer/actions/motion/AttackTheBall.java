package aisoccer.actions.motion;

import java.util.LinkedList;

import aisoccer.Brain;
import aisoccer.behaviorTree.Sequencer;
import aisoccer.behaviorTree.Task;
import aisoccer.fullStateInfo.Player;

public class AttackTheBall extends Sequencer {

	
	public AttackTheBall(Brain b){
		super(b);
		children = new LinkedList<Task>();
		children.add(new InterceptBall(b));		
	}
	
	@Override
	public boolean CheckConditions() {
		Player me = brain.getPlayer();
		double myDist = brain.timeToIntercept(me);
		for(Player tm : brain.getFullstateInfo().getTeammates(me) ){
			if(!tm.isGoalie()){
				double t = brain.timeToIntercept(tm);
				if(t>0 && t<myDist){
					return false;
				}
			}			
		}
		return true;
	}

	@Override
	public void Start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void End() {
		// TODO Auto-generated method stub
		
	}


}
