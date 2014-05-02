package aisoccer.actions;

import java.util.HashSet;
import java.util.LinkedList;

import aisoccer.Area;
import aisoccer.Brain;
import aisoccer.Player;
import aisoccer.Vector2D;

public class PositionOff extends GoTo {

	@Override
	public boolean checkConditions(Brain brain) {
		return brain.getPlayer().isLeftSide() == brain.getFullstateInfo().LeftGotBall();
	}

	@Override
	public void Start(Brain brain) {
		Player me = brain.getPlayer();
		Vector2D ballP = brain.getFullstateInfo().getBall().getPosition();
		Player[] ops = brain.getFullstateInfo().getOpponents(me);
		LinkedList<Vector2D> opRP = new LinkedList<Vector2D>();
		for(Player op : ops){
			opRP.add(op.getPosition().subtract(ballP));
		}
		int p = me.isLeftSide() ? 1:-1;
		double xOffSide = brain.getXLimOffSide();
		HashSet<Vector2D> interestingPoints = new HashSet<Vector2D>();
		HashSet<Vector2D> goodPoints = new HashSet<Vector2D>();
		for(Area a : brain.getMyAreas()){
			interestingPoints.addAll(a.getInterestingPoints(xOffSide, p));
		}
		if(interestingPoints.size()==0){
			if(p*me.getPosition().getX()<p*xOffSide){
				brain.setInterestPos(me.getPosition());
			}else{
				brain.setInterestPos(new Vector2D(-p*52.5,me.getPosition().getY()));
			}
			return;			
		}
		for(Vector2D v2 : interestingPoints){
			if(brain.checkMarked(v2.subtract(ballP), opRP)){
				goodPoints.add(v2);
			}
		}
		if(goodPoints.size()==0){
			if(p*me.getPosition().getX()<p*xOffSide){
				brain.setInterestPos(me.getPosition());
			}else{
				brain.setInterestPos(new Vector2D(-p*52.5,me.getPosition().getY()));
			}
			return;			
		}
		double minDist = 300;
		Vector2D best = null;
		Vector2D opGoal = new Vector2D(me.isLeftSide() ? 52.5:-52.5,0);
		for(Vector2D v2 : goodPoints){
			if(v2.distanceTo(opGoal)<minDist){
				minDist = v2.distanceTo(opGoal);
				best = v2;
			}
		}
		brain.setInterestPos(best);
	}

}
