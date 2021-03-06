package aisoccer.actions.positionning;

import java.util.ArrayList;

import math.Vector2D;
import aisoccer.Brain;
import aisoccer.actions.motion.GoTo;
import aisoccer.fullStateInfo.Ball;
import aisoccer.fullStateInfo.Player;

public class MarkClosestOpponent extends GoTo {
	
	public MarkClosestOpponent(Brain b) {
		super(b);
	}

	Player toMark;

	@Override
	public boolean CheckConditions() {
		Player me = brain.getPlayer();	
		Iterable<Player> teammates = brain.getFullstateInfo().getTeammates(me);
		Iterable<Player> opponents = brain.getFullstateInfo().getOpponents(me);
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
			if (opRP.polarRadius()>50){
				continue;
			}
			if(op.distanceTo(me.getPosition())<dist && brain.isFree(opRP, teammatesRP) ){
				toMark = op;
				dist = op.distanceTo(me.getPosition());
			}
		}
		return toMark!=null;
	}

	@Override
	public void defineInterestPosition() {
		Vector2D b = brain.getFullstateInfo().getBall().getPosition();	
		brain.setInterestPos( b.multiply(0.2).add(toMark.getPosition().multiply(0.8)) );		
	}

}
