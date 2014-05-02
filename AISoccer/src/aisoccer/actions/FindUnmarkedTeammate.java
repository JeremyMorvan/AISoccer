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
		LinkedList<Player> teammates = brain.getFullstateInfo().getTeammates(me);
		LinkedList<Player> opponents = brain.getFullstateInfo().getOpponents(me);
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
			if(brain.checkMarked(tm.getPosition().subtract(me.getPosition()), opponentsRP) && tm.getPosition().distanceTo(goal)<dmin){
				bestTeammateP = tm.getPosition();				
			}
		}
		brain.setInterestPos(bestTeammateP);	
	}

	@Override
	public boolean checkConditions(Brain brain) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void DoAction(Brain brain) {	
	}

}
