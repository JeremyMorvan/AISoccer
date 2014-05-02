package aisoccer.actions;

import java.util.ArrayList;
import java.util.LinkedList;

import aisoccer.Ball;
import aisoccer.Brain;
import aisoccer.Player;
import aisoccer.Vector2D;
import aisoccer.behaviorTree.Selector;
import aisoccer.behaviorTree.Task;

public class PositionDef extends Selector {

	
	public PositionDef(){
		children = new LinkedList<Task>();
		children.add(new MarkOpponent());
	}
	
	
	@Override
	public boolean checkConditions(Brain brain){
		return brain.getPlayer().isLeftSide() != brain.getFullstateInfo().LeftGotBall();		
	}

	@Override
	public void Start(Brain brain) {
		Player me = brain.getPlayer();	
		ArrayList<Player> teammates = brain.getFullstateInfo().getTeammates(me);
		Player[] opponents = brain.getFullstateInfo().getOpponents(me);
		Ball b = brain.getFullstateInfo().getBall();	
		
		ArrayList<Vector2D> teammatesRP = new ArrayList<Vector2D>();
		for(Player tm : teammates){
			teammatesRP.add( tm.getPosition().subtract(b.getPosition()) );
		}
		
		Vector2D opRP;
		Player toMark = null;
		double dist = Double.POSITIVE_INFINITY;
		for(Player op : opponents){
			opRP = op.getPosition().subtract(b.getPosition());
			if(op.distanceTo(me.getPosition())<dist && brain.checkMarked(opRP, teammatesRP) ){
				
			}
		}
		
		brain.setInterestPos(brain.getFullstateInfo().getBall().getPosition());
	}


	@Override
	public void End(Brain brain) {
		// TODO Auto-generated method stub
		
	}

}
