package aisoccer.actions;

import java.util.LinkedList;

import aisoccer.Brain;
import aisoccer.Player;
import aisoccer.behaviorTree.Sequencer;
import aisoccer.behaviorTree.Task;

public class AttackTheBall extends Sequencer {

	
	public AttackTheBall(){
		children = new LinkedList<Task>();
		children.add(new InterceptBall());		
	}
	
	@Override
	public boolean checkConditions(Brain brain) {
		Player me = brain.getPlayer();
		double myDist = me.distanceTo(brain.getFullstateInfo().getBall());
		LinkedList<Player> teammates = brain.getFullstateInfo().getTeammates(me);
		for(Player tm : teammates){
			if(tm.distanceTo(brain.getFullstateInfo().getBall())<myDist){
				return false;
			}
		}
		return true;
	}

	@Override
	public void Start(Brain brain) {}

	@Override
	public void End(Brain brain) {}

}
