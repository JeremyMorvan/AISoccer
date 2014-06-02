package aisoccer.actions;

import java.util.ArrayList;
import math.Vector2D;
import aisoccer.Brain;
import aisoccer.SoccerParams;
import aisoccer.behaviorTree.ActionTask;
import aisoccer.fullStateInfo.Player;

public class FindUnmarkedTeammate extends ActionTask {
	
	boolean allowBackward;
	
	public static final double PASS_THRESHOLD = 0.7;
	private ArrayList<Pass> interestingPasses;

	public FindUnmarkedTeammate(Brain b, boolean a) {
		super(b);
		allowBackward = a;
	}

	@Override
	public boolean CheckConditions() {
		interestingPasses = new ArrayList<Pass>();
		Player me = brain.getPlayer();
		Iterable<Player> teammates = brain.getFullstateInfo().getTeammates(me);
		Iterable<Player> opponents = brain.getFullstateInfo().getOpponents(me);
		Vector2D goal = new Vector2D(brain.getPlayer().isLeftSide() ? 52.5d : -52.5d,0);
		
		for(Player tm : teammates){
			ArrayList<Vector2D> points = brain.generatePointsAround(tm.getPosition(), goal);
			Vector2D neededVelocity, neededAcceleration1, neededAcceleration2;
			double minNeededPowerKick, neededPowerKick1, neededPowerKick2;
			for(Vector2D point : points){
				neededVelocity = brain.computeNeededVelocity(tm.getPosition(), point);
				if(neededVelocity.polarRadius()>SoccerParams.BALL_SPEED_MAX){
					continue;
				}
				
				// neededAcceleration1 = acceleration needed without controlling
				//					 2 = 					 after controlling (supposing vball = 0 after controlling)
				neededAcceleration1 = neededVelocity.subtract(brain.getFullstateInfo().getBall().getVelocity());
				neededAcceleration2 = neededVelocity;				
				
				neededPowerKick1 = neededAcceleration1.polarRadius()/brain.getEffectivePowerRate();
				neededPowerKick2 = neededAcceleration2.polarRadius()/0.8; // we approximate the effectivePowerRate after control by 0.8
				minNeededPowerKick = Math.min(neededPowerKick1, neededPowerKick2);
				
				if(minNeededPowerKick<=SoccerParams.POWERMAX){
					double score = 1.0;
					for(Player op : opponents){
						score *=( 1+brain.evalPass(neededVelocity, tm.getPosition(), op.getPosition()) )/2;
					} 
					
					if(score>PASS_THRESHOLD){
						interestingPasses.add(new Pass(tm.getPosition(), point, neededVelocity, score));
					}
				}				
			}			
		}
		
		if(interestingPasses.isEmpty()){
			return false;
		}
		
		Vector2D bestPassVector = null;
		double bestScore = 0;
		double score;
		for(Pass p : interestingPasses){
			score = Math.pow(p.score,2)/positionScore(p.teamMate, goal);
			if(score>bestScore){
				bestScore = score;
				bestPassVector = p.ballvelocity;
			}
		}		
		
		if( allowBackward || bestScore > 0.9/positionScore(brain.getPlayer().getPosition(), goal) ){
			brain.setShootVector(bestPassVector);
			return true;
		}
		return false;
	}	

	private double positionScore(Vector2D player, Vector2D goal){
		return 5+player.distanceTo(goal);
	}

	@Override
	public void Start() {			
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
