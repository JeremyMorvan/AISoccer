package aisoccer.actions;

import java.util.ArrayList;

import math.NullVectorException;
import math.Vector2D;
import aisoccer.Brain;
import aisoccer.InvalidArgumentException;
import aisoccer.SoccerParams;
import aisoccer.fullStateInfo.Player;

public class ShootToGoal extends ShootTo {

	Vector2D targetInGoal;
	double scoreForTarget;

	public ShootToGoal(Brain b) {
		super(b);
	}
	
	@Override
	public boolean CheckConditions() {
		Player me = brain.getPlayer();
		Vector2D goal = new Vector2D(brain.getPlayer().isLeftSide() ? 52.5d : -52.5d,0);
		if(me.distanceTo(goal)>40){
			return false;
		}
		int nbSamples = 21;
		double step = SoccerParams.GOAL_WIDTH/(nbSamples-1);
		targetInGoal = null;
		scoreForTarget = 0;
		
		for(int i=0;i!=nbSamples;i++){
			double y = i*step-SoccerParams.GOAL_WIDTH/2;
			System.out.println(y);
			double tScore = brain.evalShoot(y);
			if(tScore>scoreForTarget){
				Vector2D target = (new Vector2D(SoccerParams.FIELD_LENGTH,y)).multiply(me.isLeftSide() ? 1 : -1);
				try {
					double timeToGoal = brain.timeDistanceBall(target.distanceTo(brain.getFullstateInfo().getBall()),SoccerParams.BALL_SPEED_MAX*0.95);
					ArrayList<Player> opponents = brain.getFullstateInfo().getOpponents(me);
					for(Player op : opponents){
						if(brain.timeToIntercept(op)<timeToGoal){
							tScore = 0;
							break;
						}
					}
				} catch (NullVectorException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(tScore>scoreForTarget){
					scoreForTarget = tScore;
					targetInGoal = target;
				}
			}			
		}
		
		if(scoreForTarget>0.8){
			return true;
		}
		return false;
	}

	@Override
	public void Start() {	
		System.out.println("Je tire !!");
		Vector2D shootDirection = targetInGoal.subtract(brain.getPlayer().getPosition()).normalize();
		brain.setShootVector(shootDirection.multiply(SoccerParams.BALL_SPEED_MAX));
	}

	@Override
	public void End() {}

}
