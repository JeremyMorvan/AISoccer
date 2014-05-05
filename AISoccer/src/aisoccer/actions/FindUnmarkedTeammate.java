package aisoccer.actions;

import java.util.ArrayList;
import java.util.LinkedList;

import aisoccer.Brain;
import aisoccer.Vector2D;
import aisoccer.behaviorTree.ActionTask;
import aisoccer.fullStateInfo.Player;

public class FindUnmarkedTeammate extends ActionTask {

	public FindUnmarkedTeammate(Brain b) {
		super(b);
	}

	@Override
	public void Start() {
		Player me = brain.getPlayer();
		ArrayList<Player> teammates = brain.getFullstateInfo().getTeammates(me);
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
			if(tmRP.polarRadius()<=40 && brain.checkMarked(tmRP, opponentsRP) && tm.distanceTo(goal)<dmin){
				bestTeammateP = tm.getPosition();				
			}
		}
		brain.setInterestPos(bestTeammateP);	
	}

	@Override
	public boolean CheckConditions() {
		return true;
	}

	@Override
	public void DoAction() {	
	}

}
