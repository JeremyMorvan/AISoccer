package aisoccer.training;

import aisoccer.Brain;
import aisoccer.actions.motion.InterceptBall;
import aisoccer.behaviorTree.Sequencer;
import aisoccer.fullStateInfo.Player;

public class InterceptCondition extends Sequencer {

	public InterceptCondition(Brain b) {
		super(b);
		children.add(new InterceptBall(b));
	}

	@Override
	public void Start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void End() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean CheckConditions() {
		Player me = brain.getPlayer();
		double myDist = brain.timeToIntercept(me);
		int count = 0;
		int lim = 3;
		for(Player tm : brain.getFullstateInfo().getTeammates(me) ){
			double t = brain.timeToIntercept(tm);
			if(t>0 && t<myDist){
				count++;
				if(count>lim){
					return false;
				}				
			}
		}
		for(Player tm : brain.getFullstateInfo().getOpponents(me) ){
			double t = brain.timeToIntercept(tm);
			if(t>0 && t<myDist){
				count++;
				if(count>lim){
					return false;
				}				
			}
		}
		return true;
	}

}
