package aisoccer.actions.positionning;

import java.util.HashSet;
import java.util.LinkedList;

import math.Vector2D;
import aisoccer.Area;
import aisoccer.Brain;
import aisoccer.actions.motion.GoTo;
import aisoccer.fullStateInfo.Player;

public class PositionOff extends GoTo {

	public PositionOff(Brain b) {
		super(b);
	}

	@Override
	public boolean CheckConditions() {
//		return brain.getPlayer().isLeftSide() == brain.getFullstateInfo().LeftGotBall();
		return true;
	}

	@Override
	public void defineInterestPosition() {
		Player me = brain.getPlayer();
		Vector2D ballP = brain.getFullstateInfo().getBall().getPosition();
		Iterable<Player> ops = brain.getFullstateInfo().getOpponents(me);
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
//			if(me.isLeftSide()&&me.getUniformNumber()==1){
//				System.out.println("No interesting point");
//			}
			
			if(p*me.getPosition().getX()<p*xOffSide){
				for(Area a : brain.getMyAreas()){
					
					if(a.isInArea(me.getPosition())){
						brain.setInterestPos(me.getPosition());
						return;
					}
					interestingPoints.addAll(a.getInterestingPoints());
				}
				double 	minDist = 300;
				Vector2D best = null;
				for(Vector2D v2 : interestingPoints){
					if(me.getPosition().distanceTo(v2)<minDist){
						minDist = me.getPosition().distanceTo(v2);
						best = v2;
					}
				}
				brain.setInterestPos(best);
			}else{
				Vector2D pos = new Vector2D(-p*52.5,me.getPosition().getY());
				brain.setInterestPos(pos);
			}
			return;			
		}
		for(Vector2D v2 : interestingPoints){	
			if(brain.isFree(v2.subtract(ballP), opRP)){
				goodPoints.add(v2);
			}
		}
		if(goodPoints.size()==0){
//			if(me.isLeftSide()&&me.getUniformNumber()==1){
//				System.out.println("No good point");
//			}
			if(p*me.getPosition().getX()<p*xOffSide){
				for(Area a : brain.getMyAreas()){
					if(a.isInArea(me.getPosition())){
						brain.setInterestPos(me.getPosition());
						return;
					}
				}
				double 	minDist = 300;
				Vector2D best = null;
				for(Vector2D v2 : interestingPoints){
					if(me.getPosition().distanceTo(v2)<minDist){
						minDist = me.getPosition().distanceTo(v2);
						best = v2;
					}
				}
				brain.setInterestPos(best);

			}else{
				brain.setInterestPos(new Vector2D(-p*52.5,me.getPosition().getY()));
			}
			return;			
		}
		Vector2D best = new Vector2D(0,0);
		int count = 0;
//		System.out.println(goodPoints.size());
		for(Vector2D v2 : goodPoints){
			best.addM(v2);
			count++;
		}
		best.multiplyM(1/((double)count));
		best.addM(new Vector2D((me.isLeftSide() ? 1 : -1)*Math.abs(ballP.getX()/5.0),ballP.getY()/3.0));
		brain.setInterestPos(best);
	}



}
