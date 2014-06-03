package aisoccer.actions;

import java.util.ArrayList;

import math.Vector2D;
import aisoccer.Brain;
import aisoccer.SoccerParams;
import aisoccer.fullStateInfo.Player;

public class Dribble extends ShootTo {

	boolean backwardPassAuthorized;
	Vector2D bestVelocity;
	public Dribble(Brain b,boolean bPa) {
		super(b);
		backwardPassAuthorized = bPa;
	}
	
	@Override
	public boolean CheckConditions() {
		Player me = brain.getPlayer();
		Vector2D goal = new Vector2D(brain.getPlayer().isLeftSide() ? 52.5d : -52.5d,0);
		ArrayList<Vector2D> velocities = new ArrayList<Vector2D>();
		if(backwardPassAuthorized){
			velocities.addAll(brain.generateVelocityVectors(me.getPosition(), goal, 10000, SoccerParams.PLAYER_SPEED_MAX, 2*SoccerParams.PLAYER_SPEED_MAX, 40));
		}else{
			velocities.addAll(brain.generateVelocityVectors(me.getPosition(), goal, 140, SoccerParams.PLAYER_SPEED_MAX, 2*SoccerParams.PLAYER_SPEED_MAX, 40));
		}
		ArrayList<Player> opponents = brain.getFullstateInfo().getOpponents(me);
		bestVelocity = null;
		double bestScore = 0;
		for(Vector2D vel : velocities){
			double score = 1;
			for(Player op : opponents){
				score *= brain.evalDribble(vel, op.getPosition());
			}
			if(score>bestScore){
				bestScore = score;
				bestVelocity = vel;
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
