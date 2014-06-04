package aisoccer.actions;

import java.util.ArrayList;

import math.Vector2D;
import aisoccer.Brain;
import aisoccer.SoccerParams;
import aisoccer.fullStateInfo.Player;

public class Clear extends ShootTo {

	public Clear(Brain b) {
		super(b);
	}
	
	public void Start() {
		Player me = brain.getPlayer();
		Vector2D meP = me.getPosition();
		ArrayList<Player> opponents = brain.getFullstateInfo().getOpponents(me);
		ArrayList<Player> teammates = brain.getFullstateInfo().getTeammates(me);
		Vector2D goal = new Vector2D(brain.getPlayer().isLeftSide() ? 52.5d : -52.5d,0);
		ArrayList<Vector2D> velocities = brain.generateVelocityVectors(meP, goal, 140, 2.9, 3, 20);
		double bestScore = -1;
		Vector2D bestVel = null;
		for(Vector2D vel : velocities){
			double score = 0;
			for(Player t : teammates){
				double tscore = 1;
				for(Player o : opponents){
					tscore *= brain.evalPass(vel, t.getPosition(), o.getPosition());
				}
				score += tscore;
			}
			if(score>bestScore){
				bestScore = score;
				bestVel = vel;
			}
		}
		brain.setShootVector(bestVel.normalize().multiply(SoccerParams.BALL_SPEED_MAX));
	}

}
