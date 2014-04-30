package aisoccer.actions;

import java.util.LinkedList;

import aisoccer.Brain;
import aisoccer.Player;
import aisoccer.SoccerParams;
import aisoccer.Vector2D;
import aisoccer.behaviorTree.ActionTask;

public class FindUnmarkedTeammate extends ActionTask {

	@Override
	public void Start(Brain brain) {}

	@Override
	public boolean checkConditions(Brain brain) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void DoAction(Brain brain) {
		Player me = brain.getPlayer();
		LinkedList<Player> teammates = brain.getFullstateInfo().getTeammates(me);
		LinkedList<Player> opponents = brain.getFullstateInfo().getOpponents(me);
		LinkedList<Vector2D> opponentsRP = new LinkedList<Vector2D>();
		for(Player op : opponents){
			opponentsRP.add(op.getPosition().subtract(me.getPosition()));
		}
		Vector2D goal = new Vector2D(brain.getPlayer().isLeftSide() ? 52.5d : -52.5d,0);
		double dmin = 300;
		Vector2D bestTeammateP = null;
		for(Player tm : teammates){
			if(checkMarked(tm.getPosition().subtract(me.getPosition()), opponentsRP)&&tm.getPosition().distanceTo(goal)<dmin){
				bestTeammateP = tm.getPosition();				
			}
		}
		brain.setInterestPos(bestTeammateP);		
	}
	
	public boolean checkMarked(Vector2D teammateRP, Vector2D opponentRP){
		if(teammateRP.multiply(opponentRP)<0){
			return true;
		}
		double angle = Math.toRadians(teammateRP.directionOf(opponentRP));
		if(Math.abs(Math.tan(angle))>SoccerParams.PLAYER_SPEED_MAX/SoccerParams.BALL_SPEED_MAX){
			return true;
		}
		double norm = opponentRP.polarRadius();
		if(norm*(Math.cos(angle)+Math.sin(angle))>teammateRP.polarRadius()){
			return true;
		}
		return false;		
	}
	
	public boolean checkMarked(Vector2D teammateRP, LinkedList<Vector2D> opponentsRP){
		for(Vector2D oRP : opponentsRP){
			if(!checkMarked(teammateRP,oRP)){
				return false;
			}
		}
		return true;
	}

}
