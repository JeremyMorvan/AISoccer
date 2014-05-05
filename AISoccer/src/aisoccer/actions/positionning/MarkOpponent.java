package aisoccer.actions.positionning;

import java.util.ArrayList;

import aisoccer.Brain;
import aisoccer.Vector2D;
import aisoccer.actions.motion.GoTo;
import aisoccer.fullStateInfo.Ball;
import aisoccer.fullStateInfo.Player;

public class MarkOpponent extends GoTo {
	
	public MarkOpponent(Brain b) {
		super(b);
	}

	Player toMark;

	@Override
	public boolean CheckConditions() {
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
			if (opRP.polarRadius()>50){
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
	public void Start() {	
		Vector2D b = brain.getFullstateInfo().getBall().getPosition();	
		brain.setInterestPos( b.multiply(0.2).add(toMark.getPosition().multiply(0.8)) );
	}

}
