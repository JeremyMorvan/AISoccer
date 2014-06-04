package aisoccer.actions;

import java.util.ArrayList;

import math.Vector2D;
import aisoccer.Brain;
import aisoccer.SoccerParams;
import aisoccer.fullStateInfo.Player;

public class Dribble extends ShootTo {

	boolean backwardDribbleAuthorized;
	Vector2D bestVelocity;
	public Dribble(Brain b,boolean bDa) {
		super(b);
		backwardDribbleAuthorized = bDa;
	}
	
	@Override
	public boolean CheckConditions() {
		Player me = brain.getPlayer();
		double x = me.getPosition().getX();
		double y = me.getPosition().getY();
		Vector2D goal = new Vector2D(brain.getPlayer().isLeftSide() ? 52.5d : -52.5d,0);
		ArrayList<Vector2D> velocities = new ArrayList<Vector2D>();
		if(backwardDribbleAuthorized){
			velocities.addAll(brain.generateVelocityVectors(me.getPosition(), goal, 10000, SoccerParams.PLAYER_SPEED_MAX, 2*SoccerParams.PLAYER_SPEED_MAX, 40));
		}else{
			velocities.addAll(brain.generateVelocityVectors(me.getPosition(), goal, 140, SoccerParams.PLAYER_SPEED_MAX, 2*SoccerParams.PLAYER_SPEED_MAX, 40));
		}
		ArrayList<Player> opponents = brain.getFullstateInfo().getOpponents(me);
		bestVelocity = null;
		double bestScore = 0;
		for(Vector2D vel : velocities){
			if(((Math.abs(x)<SoccerParams.FIELD_LENGTH/2-5)||(x*vel.getX()<0))&&((Math.abs(y)<SoccerParams.FIELD_WIDTH/2-5)||(y*vel.getY()<0))){
				double score = 1;
				for(Player op : opponents){
					score *= brain.evalDribble(vel, op.getPosition());
				}
				if(!backwardDribbleAuthorized){
					score *= Math.pow(Math.cos(Math.toRadians(me.getPosition().directionOf(goal)-vel.polarAngle())),1.0d/2.0d);
				}
				if(score>bestScore){
					bestScore = score;
					bestVelocity = vel;
				}
			}			
		}
		if(bestScore>0.6){
			return true;
		}
		return false;
	}

	@Override
	public void Start() {
		System.out.println(brain.getPlayer()+ " : Je veux dribbler");
		System.out.println(bestVelocity);
		brain.setShootVector(bestVelocity);
	}

	@Override
	public void End() {
		// TODO Auto-generated method stub
		
	}

}
