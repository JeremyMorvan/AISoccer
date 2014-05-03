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
		double myDist = me.distanceTo(brain.getFullstateInfo().getBall());
		for(Player tm : brain.getFullstateInfo().getTeammates(me) ){
			if(tm.distanceTo(brain.getFullstateInfo().getBall())<myDist){
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
