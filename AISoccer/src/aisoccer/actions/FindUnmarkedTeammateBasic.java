package aisoccer.actions;

import java.util.ArrayList;
import math.Vector2D;
import aisoccer.Brain;
import aisoccer.behaviorTree.ActionTask;
import aisoccer.fullStateInfo.Player;

public class FindUnmarkedTeammateBasic extends ActionTask {
	
	public FindUnmarkedTeammateBasic(Brain b) {
		super(b);
	}

	@Override
	public boolean CheckConditions() {
		Player me = brain.getPlayer();
		Iterable<Player> teammates = brain.getFullstateInfo().getTeammates(me);
		Iterable<Player> opponents = brain.getFullstateInfo().getOpponents(me);
		Vector2D goal = new Vector2D(brain.getPlayer().isLeftSide() ? 52.5d : -52.5d,0);
		ArrayList<Vector2D> opponentsRP = new ArrayList<Vector2D>();
		for(Player op : opponents){
			opponentsRP.add(op.getPosition().subtract(brain.getFullstateInfo().getBall().getPosition()));
		}
		double dmin = 300;
		if(!brain.getFullstateInfo().getPlayMode().equals("kick_off_l")&&!brain.getFullstateInfo().getPlayMode().equals("kick_off_r")){
			dmin = brain.getPlayer().distanceTo(goal);
		}
		Vector2D bestTeammateP = null;
		for(Player tm : teammates){
			Vector2D tmRP = tm.getPosition().subtract(me.getPosition());
			if(tmRP.polarRadius()<=40 && brain.isFree(tmRP, opponentsRP) && tm.distanceTo(goal)<dmin){
				bestTeammateP = tm.getPosition();				
			}
		}		
		brain.setInterestPos(bestTeammateP);
		
		return brain.getInterestPos() != null;
	}	


	@Override
	public void Start() {		
	}

	@Override
	public void DoAction() {
	}
	

}
