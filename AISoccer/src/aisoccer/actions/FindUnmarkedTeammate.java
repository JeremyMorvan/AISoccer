package aisoccer.actions;

import java.util.ArrayList;
import math.Vector2D;
import aisoccer.Brain;
import aisoccer.SoccerParams;
import aisoccer.behaviorTree.ActionTask;
import aisoccer.fullStateInfo.Player;

public class FindUnmarkedTeammate extends ActionTask {
	
	public static final double PASS_THRESHOLD = 0.7;
	private ArrayList<Pass> interestingPasses;

	public FindUnmarkedTeammate(Brain b) {
		super(b);
	}

	@Override
	public boolean CheckConditions() {
		interestingPasses = new ArrayList<Pass>();
		Player me = brain.getPlayer();
		Iterable<Player> teammates = brain.getFullstateInfo().getTeammates(me);
		Iterable<Player> opponents = brain.getFullstateInfo().getOpponents(me);
		Vector2D goal = new Vector2D(brain.getPlayer().isLeftSide() ? 52.5d : -52.5d,0);
		
		ArrayList<Vector2D> kickablePasses = new ArrayList<Vector2D>();
		for(Player tm : teammates){
			ArrayList<Vector2D> points = brain.generatePointsAround(tm.getPosition(), goal);
			Vector2D neededVelocity, neededAcceleration1, neededAcceleration2;
			double neededPowerKick, neededPowerKick1, neededPowerKick2;
			for(Vector2D point : points){
				neededVelocity = brain.computeNeededVelocity(tm.getPosition(), point);
				
				// neededAcceleration1 = acceleration needed without controlling
				//					 2 = 					 after controlling (supposing vball = 0 after controlling)
				neededAcceleration1 = neededVelocity.subtract(brain.getFullstateInfo().getBall().getVelocity());
				neededAcceleration2 = neededVelocity;				
				
				neededPowerKick1 = neededAcceleration1.polarRadius()/brain.getEffectivePowerRate();
				neededPowerKick2 = neededAcceleration2.polarRadius()/0.8; // we approximate the effectivePowerRate after control by 0.8
				neededPowerKick = Math.min(neededPowerKick1, neededPowerKick2);
				
				if(neededPowerKick<=SoccerParams.POWERMAX){
					kickablePasses.add(point);
					double score = 1.0;
					for(Player op : opponents){
						score *=1 + (brain.evalPass(neededVelocity, tm.getPosition(), op.getPosition()))/2;
					} 
					
					if(score>PASS_THRESHOLD){
						interestingPasses.add(new Pass(tm.getPosition(), point, neededVelocity, score));
					}
				}				
			}			
		}
		
		return !interestingPasses.isEmpty();
	}	


	@Override
	public void Start() {	
		Vector2D goal = new Vector2D(brain.getPlayer().isLeftSide() ? 52.5d : -52.5d,0);
		Vector2D bestPassVector = null;
		double bestScore = 0;
		double score;
		for(Pass p : interestingPasses){
			score = Math.pow(p.score,2)/(5+p.point.distanceTo(goal));
			if(score>bestScore){
				bestScore = score;
				bestPassVector = p.ballvelocity;
			}
		}
		
		brain.setShootVector(bestPassVector);		
		
//		double dmin = 300;
//		if(!brain.getFullstateInfo().getPlayMode().equals("kick_off_l")&&!brain.getFullstateInfo().getPlayMode().equals("kick_off_r")){
//			dmin = brain.getPlayer().distanceTo(goal);
//		}
//		Vector2D bestTeammateP = null;
//		for(Player tm : teammates){
//			Vector2D tmRP = tm.getPosition().subtract(me.getPosition());
//			if(tmRP.polarRadius()<=40 && brain.isFree(tmRP, opponentsRP) && tm.distanceTo(goal)<dmin){
//				bestTeammateP = tm.getPosition();				
//			}
//		}
		
//		brain.setInterestPos(bestTeammateP);	
	}
	
	public class Pass{
		Vector2D teamMate;
		Vector2D point;
		Vector2D ballvelocity;
		double score;
		
		public Pass(Vector2D tm, Vector2D pt, Vector2D v, double s){
			teamMate = tm;
			point = pt;
			ballvelocity = v;
			score = s;
		}
	}

	@Override
	public void DoAction() {
	}
	

}
