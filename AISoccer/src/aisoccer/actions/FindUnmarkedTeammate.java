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
	public static final double PASS_THRESHOLD_DEF = 0.6;
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
		ArrayList<Vector2D> velocities;
		double standardSpeed, minSpeed, maxSpeed;
		
		for(Player tm : teammates){
			Vector2D dir = tm.getPosition().subtract(me.getPosition()).normalize();
			if(!allowBackward && dir.getX()<0 == me.isLeftSide() ){
				continue;
			}
			
			standardSpeed = tm.distanceTo(brain.getPlayer())*(1-SoccerParams.BALL_DECAY); 
			Vector2D target;
			double intervalWidth;
			if(dir.getX()<0 == me.isLeftSide()){
				target = tm.getPosition();
				intervalWidth = 70; 
				minSpeed = Math.min(0.8*standardSpeed, SoccerParams.BALL_SPEED_MAX);
				maxSpeed = Math.min(1.5*standardSpeed, SoccerParams.BALL_SPEED_MAX);
			}else{
				Vector2D tmDirToGoal = opGoal.subtract(tm.getPosition()).normalize();
				Vector2D behind = tm.getPosition().subtract(tmDirToGoal.multiply(5));
				Vector2D front = tm.getPosition().add(tmDirToGoal.multiply(25));
				
				Vector2D dirBehind = behind.subtract(me.getPosition()).normalize();
				Vector2D dirFront = front.subtract(me.getPosition()).normalize();
				
				double leftAngle = 35;
				double rightAngle = 35;
				if(Math.abs(dir.polarAngle()-dirBehind.polarAngle())>35);{
					if(Vector2D.det(dir,dirBehind)>0){
						leftAngle = Math.abs(dir.polarAngle()-dirBehind.polarAngle());
					}else{
						rightAngle = Math.abs(dir.polarAngle()-dirBehind.polarAngle());					
					}
				}
				if(Math.abs(dir.polarAngle()-dirFront.polarAngle())>35);{
					if(Vector2D.det(dir,dirFront)>0){
						leftAngle = Math.abs(dir.polarAngle()-dirFront.polarAngle());
					}else{
						rightAngle = Math.abs(dir.polarAngle()-dirFront.polarAngle());					
					}
				}
				double finalDirection = (leftAngle-rightAngle)/2 + brain.getPlayer().getBodyDirection();
				Vector2D finalDirectionV = new Vector2D(1, Math.toRadians(finalDirection), true);
				target = me.getPosition().add(finalDirectionV);
				intervalWidth = (leftAngle+rightAngle);

				minSpeed = Math.min(1.2*standardSpeed, SoccerParams.BALL_SPEED_MAX);
				maxSpeed = Math.min(2.5*standardSpeed, SoccerParams.BALL_SPEED_MAX);
			}
						
			minSpeed = Math.min(1*standardSpeed, SoccerParams.BALL_SPEED_MAX);
			maxSpeed = Math.min(2.5*standardSpeed, SoccerParams.BALL_SPEED_MAX);			
			velocities = brain.generateVelocityVectors(me.getPosition(), target, intervalWidth, minSpeed, maxSpeed, 80);

			Vector2D neededAcceleration1, neededAcceleration2;
			double minNeededPowerKick, neededPowerKick1, neededPowerKick2;
			for(Vector2D v : velocities){
				neededAcceleration1 = v.subtract(brain.getFullstateInfo().getBall().getVelocity());
				neededAcceleration2 = v;				
				
				neededPowerKick1 = neededAcceleration1.polarRadius()/brain.getEffectivePowerRate();
				neededPowerKick2 = neededAcceleration2.polarRadius()/(0.8*SoccerParams.KICK_POWER_RATE); // we approximate the effectivePowerRate after control by 0.8
				minNeededPowerKick = Math.min(neededPowerKick1, neededPowerKick2);
//				System.out.println("Needed Acc1 = "+neededAcceleration1);
//				System.out.println("Needed Acc2 = "+neededAcceleration2);
//				System.out.println("Needed Pow1 = "+neededPowerKick1);
//				System.out.println("Needed Pow2 = "+neededPowerKick2);
//				System.out.println("min Needed Pow = "+minNeededPowerKick);
				Vector2D inter = brain.optimumInterceptionPosition(tm, brain.speedEstimation, 0);
				if(Math.abs(inter.getX())>SoccerParams.FIELD_LENGTH/2-5 || Math.abs(inter.getY())>SoccerParams.FIELD_WIDTH/2-5){
					continue;
				}
				Vector2D diff = inter.subtract(tm.getPosition());
				if(!allowBackward && diff.getX()<0 == tm.isLeftSide()){
					continue;
				}
				
				if(minNeededPowerKick<=SoccerParams.POWERMAX){
					double score = 1.0;
					double localScore;
					for(Player op : opponents){
						localScore = (1+brain.evalPass(v, tm.getPosition(), op.getPosition()) )/2;
						if(localScore<SUCCESS_PASS_THRESHOLD){
							score *= localScore;
						}
					}
//					System.out.println("Score de "+v+" : "+ score);
					
					double distanceRatio = Math.abs(brain.getPlayer().getPosition().getX()-myGoal.getX())/SoccerParams.FIELD_LENGTH;
					double threshold = PASS_THRESHOLD_OFF*distanceRatio + PASS_THRESHOLD_DEF*(1-distanceRatio);
					if(score>threshold){
						interestingPasses.add(new Pass(tm.getPosition(), v, score));
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
//			score = Math.pow(p.score,2)*positionScore(p.teamMate, opGoal);
			score = positionScore(p.teamMate, opGoal);
			if(score>bestScore){
				bestScore = score;
				bestPass = p;
			}
		}		
		
		if( allowBackward || bestScore > positionScore(brain.getPlayer().getPosition(), opGoal) ){
			brain.setShootVector(bestPass.ballvelocity);
			if(allowBackward){
				System.out.println("Passe potentiellement en retrait");
			}else{
				System.out.println("Passe offensive");
			}			
			return true;
		}
		brain.setShootVector(null);
		return false;
	}	

	private double positionScore(Vector2D player, Vector2D opGoal){
		return 1/(5+player.distanceTo(opGoal));
	}

	@Override
	public void Start() {			
	}
	
	public class Pass{
		Vector2D teamMate;
		Vector2D ballvelocity;
		double score;
		
		public Pass(Vector2D tm, Vector2D v, double s){
			teamMate = tm;
			ballvelocity = v;
			score = s;
		}
	}

	@Override
	public void DoAction() {
	}
		

}
