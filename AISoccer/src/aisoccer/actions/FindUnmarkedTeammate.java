package aisoccer.actions;

import java.util.LinkedList;

import aisoccer.Brain;
import aisoccer.Player;
import aisoccer.Vector2D;
import aisoccer.behaviorTree.ActionTask;

public class FindUnmarkedTeammate extends ActionTask {

	@Override
	public void Start(Brain brain) {
		Player me = brain.getPlayer();
		Player[] teammates = brain.getFullstateInfo().getTeammates(me);
		Player[] opponents = brain.getFullstateInfo().getOpponents(me);
		LinkedList<Vector2D> opponentsRP = new LinkedList<Vector2D>();
		for(Player op : opponents){
			opponentsRP.add(op.getPosition().subtract(me.getPosition()));
		}
		Vector2D goal = new Vector2D(brain.getPlayer().isLeftSide() ? 52.5d : -52.5d,0);
		double dmin = 300;
		if(!brain.getFullstateInfo().getPlayMode().equals("kick_off_l")&&!brain.getFullstateInfo().getPlayMode().equals("kick_off_r")){
			dmin = brain.getPlayer().distanceTo(goal);
		}
		Vector2D bestTeammateP = null;
		for(Player tm : teammates){
			Vector2D tmRP = tm.getPosition().subtract(me.getPosition());
			if(tmRP.polarRadius()<=40 && brain.checkMarked(tmRP, opponentsRP) && tm.getPosition().distanceTo(goal)<dmin){
				bestTeammateP = tm.getPosition();				
			}
		}
		brain.setInterestPos(bestTeammateP);	
	}

	@Override
	public boolean checkConditions(Brain brain) {
		return true;
	}

	@Override
	public void DoAction(Brain brain) {	
	}

}
