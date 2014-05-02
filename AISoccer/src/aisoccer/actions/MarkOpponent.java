package aisoccer.actions;

import java.util.ArrayList;
import java.util.LinkedList;

import aisoccer.Ball;
import aisoccer.Brain;
import aisoccer.Player;
import aisoccer.SoccerParams;
import aisoccer.Vector2D;

public class MarkOpponent extends GoTo {
	
	Player toMark;

	@Override
	public boolean checkConditions(Brain brain) {
		Player me = brain.getPlayer();	
		Iterable<Player> teammates = brain.getFullstateInfo().getTeammates(me);
		Player[] opponents = brain.getFullstateInfo().getOpponents(me);
		Ball b = brain.getFullstateInfo().getBall();	
		
		ArrayList<Vector2D> teammatesRP = new ArrayList<Vector2D>();
		for(Player tm : teammates){
			teammatesRP.add( tm.getPosition().subtract(b.getPosition()) );
		}
		
		Vector2D opRP;
		toMark = null;
		double dist = Double.POSITIVE_INFINITY;
		for(Player op : opponents){
			opRP = op.getPosition().subtract(b.getPosition());
			if (opRP.polarRadius()>40){
				continue;
			}
			if(op.distanceTo(me.getPosition())<dist && brain.checkMarked(opRP, teammatesRP) ){
				toMark = op;
				dist = op.distanceTo(me.getPosition());
			}
		}
		return toMark!=null;
	}

	@Override
	public void Start(Brain brain) {	
		Vector2D b = brain.getFullstateInfo().getBall().getPosition();	
		brain.setInterestPos( b.multiply(0.2).add(toMark.getPosition().multiply(0.8)) );
	}

}
