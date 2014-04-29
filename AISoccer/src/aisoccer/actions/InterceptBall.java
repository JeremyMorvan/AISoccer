package aisoccer.actions;

import aisoccer.Brain;
import aisoccer.FullstateInfo;
import aisoccer.InvalidArgumentException;
import aisoccer.MathFunction;
import aisoccer.MathTools;
import aisoccer.Player;
import aisoccer.SoccerParams;
import aisoccer.Vector2D;
import aisoccer.ballcapture.Action;
import aisoccer.behaviorTree.ActionTask;

public class InterceptBall extends ActionTask {

	public static float angleLimit = 5f;
	
	public InterceptBall() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean checkConditions(Brain brain) {
		return brain.getPlayer().distanceTo(brain.getFullstateInfo().getBall()) > SoccerParams.KICKABLE_MARGIN;
	}

	@Override
	public void DoAction(Brain brain) {
		FullstateInfo fsi = brain.getFullstateInfo();
		Player player = brain.getPlayer();
		Vector2D target = optimumInterception(fsi.getBall().getPosition(), 
												fsi.getBall().getVelocity(), 
												player.getPosition(), 
												SoccerParams.PLAYER_SPEED_MAX*0.6);

//		System.err.println("ball :" +fsi.getBall().getPosition());
//		System.err.println("target :" +target);
		
		Vector2D diff = target.subtract(player.getPosition());
		double angle = diff.polarAngle() - player.getBodyDirection();
		angle = (angle+180.0)%(360.0)-180.0;
//		System.err.println(angle);
		if(Math.abs(angle) > angleLimit ){
//			System.err.println("left :" +player.isLeftSide()+" : tourne");
			brain.doAction(new Action((float)angle,true));	
		}else{
//			System.err.println("left :" +player.isLeftSide()+" : avance");
			brain.doAction(new Action(100f,false));				
		}
	}
	

	
	private Vector2D ballPositionPrediction(Vector2D ballPos,  Vector2D ballVelocity, double time){
//    	double delta = ((double) SoccerParams.SIMULATOR_STEP)/1000.0; // deltaT in seconds
    	double tau = 1.0/(1.0-SoccerParams.BALL_DECAY);
//    	System.err.println("b decay : "+SoccerParams.BALL_DECAY);
    	double coeff = ( 1.0-Math.exp(-time/tau) )*tau;    	
        Vector2D prediction = ballVelocity.multiply(coeff);
        return prediction.add(ballPos);
	}
	
	
	public Vector2D optimumInterception(final Vector2D ballPos, 
										final Vector2D ballVelocity, 
										final Vector2D playerPos, 
										final double playerSpeed){
	      
	      MathFunction f = new  MathFunction() {
	            public double value(double t) {	                
	                return ballPositionPrediction(ballPos, ballVelocity, t).distanceTo(playerPos)/playerSpeed - t;
	            }
	      };
	      
	      
	      double distanceMax = ballPositionPrediction(ballPos, ballVelocity, Double.POSITIVE_INFINITY).distanceTo(playerPos);
	      distanceMax = Math.max(distanceMax, ballPos.distanceTo(playerPos));
//	      System.out.println("finalPos = "+ ballPositionPrediction(ballPos, ballVelocity, Double.POSITIVE_INFINITY));
//	      System.out.println("distanceMax = "+ distanceMax);
	      double time = -1.0;
	      try {
	    	  time = MathTools.zeroCrossing(f, 0.0, distanceMax/playerSpeed, 0.1);
//	    	  System.out.println("time = "+ time);
	    	  return ballPositionPrediction(ballPos, ballVelocity, time);
	      } catch (InvalidArgumentException e) {
	    	  e.printStackTrace();
	      }
	      return null;
	}

}