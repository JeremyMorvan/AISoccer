package aisoccer.actions;

import java.util.ArrayList;
import java.util.Collection;

import math.Vector2D;
import aisoccer.Brain;
import aisoccer.SoccerParams;
import aisoccer.behaviorTree.ActionTask;
import aisoccer.fullStateInfo.Player;

public class FindUnmarkedTeammate extends ActionTask {
	
	boolean allowBackward;

	public static final double SUCCESS_PASS_THRESHOLD = 0.8;
	// if evalPass > SUCESS_PASS_THRESHOLD, we consider the given opponent cannot intercept the ball at all
	public static final double PASS_THRESHOLD_DEF = 0.7;
	public static final double PASS_THRESHOLD_OFF = 0.4;
	// we keep only the passes for which the product of evalPass for every Opponent is >PASS_THRESHOLD
	private ArrayList<Pass> interestingPasses;

	public FindUnmarkedTeammate(Brain b, boolean a) {
		super(b);
		allowBackward = a;
	}

	@Override
	public boolean CheckConditions() {
		interestingPasses = new ArrayList<Pass>();
		Player me = brain.getPlayer();
		Collection<Player> teammates = brain.getFullstateInfo().getTeammates(me);
		Collection<Player> opponents = brain.getFullstateInfo().getOpponents(me);
		Vector2D opGoal = new Vector2D(brain.getPlayer().isLeftSide() ? 52.5d : -52.5d,0);
		Vector2D myGoal = new Vector2D(brain.getPlayer().isLeftSide() ? -52.5d : 52.5d,0);
		
		for(Player tm : teammates){
			System.out.println("points autours de "+tm);
			ArrayList<Vector2D> points = brain.generatePointsAround(tm.getPosition(), opGoal);
			Vector2D neededVelocity, neededAcceleration1, neededAcceleration2;
			double minNeededPowerKick, neededPowerKick1, neededPowerKick2;
			for(Vector2D point : points){
				neededVelocity = brain.computeNeededVelocity(tm.getPosition(), point);
				System.out.println("Point : "+point);
				System.out.println("Needed Velocity : "+neededVelocity);
				if(neededVelocity.polarRadius()>SoccerParams.BALL_SPEED_MAX){
					continue;
				}
				
				// neededAcceleration1 = acceleration needed without controlling
				//					 2 = 					 after controlling (supposing vball = 0 after controlling)
				neededAcceleration1 = neededVelocity.subtract(brain.getFullstateInfo().getBall().getVelocity());
				neededAcceleration2 = neededVelocity;				
				
				neededPowerKick1 = neededAcceleration1.polarRadius()/brain.getEffectivePowerRate();
				neededPowerKick2 = neededAcceleration2.polarRadius()/(0.8*SoccerParams.KICK_POWER_RATE); // we approximate the effectivePowerRate after control by 0.8
				minNeededPowerKick = Math.min(neededPowerKick1, neededPowerKick2);
				
				System.out.println("Needed Acc1 = "+neededAcceleration1);
				System.out.println("Needed Acc2 = "+neededAcceleration2);
				System.out.println("Needed Pow1 = "+neededPowerKick1);
				System.out.println("Needed Pow2 = "+neededPowerKick2);
				System.out.println("min Needed Pow = "+minNeededPowerKick);
				
				
				if(minNeededPowerKick<=SoccerParams.POWERMAX){
					double score = 1.0;
					double localScore;
					for(Player op : opponents){
						localScore = (1+brain.evalPass(neededVelocity, tm.getPosition(), op.getPosition()) )/2;
						if(localScore<SUCCESS_PASS_THRESHOLD){
							score *= localScore;
						}
					}
					System.out.println("Score de "+point+" : "+ score);
					
					double distanceRatio = Math.abs(brain.getPlayer().getPosition().getX()-myGoal.getX())/SoccerParams.FIELD_LENGTH;
					double threshold = PASS_THRESHOLD_OFF*distanceRatio + PASS_THRESHOLD_OFF*(1-distanceRatio);
					if(score>threshold){
						interestingPasses.add(new Pass(tm.getPosition(), point, neededVelocity, score));
					}
				}				
			}			
		}
		
		if(interestingPasses.isEmpty()){
			return false;
		}
		
		Pass bestPass = null;
		double bestScore = 0;
		double score;
		for(Pass p : interestingPasses){
			score = Math.pow(p.score,2)*positionScore(p.teamMate, opGoal);
			if(score>bestScore){
				bestScore = score;
				bestPass = p;
			}
		}		
		
		if( allowBackward || bestScore > 0.9*positionScore(brain.getPlayer().getPosition(), opGoal) ){
			brain.setShootVector(bestPass.ballvelocity);
			brain.setInterestPos(bestPass.point);
			System.out.println(brain.getPlayer()+" a choisi de faire une passe vers"+ bestPass.point);
			return true;
		}
		brain.setShootVector(null);
		return false;
	}	

	private double positionScore(Vector2D player, Vector2D goal){
		return 1/(5+player.distanceTo(goal));
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
