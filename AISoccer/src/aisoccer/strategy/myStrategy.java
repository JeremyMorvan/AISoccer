package aisoccer.strategy;

import java.util.LinkedList;

import aisoccer.*;
import aisoccer.Player;
import aisoccer.RobocupClient;
import aisoccer.actions.GoToBall;
import aisoccer.actions.ShootToGoal;
import aisoccer.behaviorTree.Task;

public class myStrategy extends Strategy {

	
	
	public myStrategy() {
		children = new LinkedList<Task>();
		children.add(new GoToBall());
		children.add(new ShootToGoal());
	}
	

	
	private Vector2D ballPositionPrediction(Vector2D ballPos,  Vector2D ballVelocity, double time){
    	double delta = ((double) SoccerParams.SIMULATOR_STEP)/1000.0; // deltaT in seconds
    	double tau = delta/(1-SoccerParams.BALL_DECAY);
    	double coeff = ( 1-Math.exp(-time/tau) )*tau;
    	
        Vector2D prediction = ballVelocity.multiply(coeff);
        return prediction.add(ballPos);
	}
	
	public Vector2D optimumInterception(final Vector2D ballPos, final Vector2D ballVelocity, final Vector2D playerPos, final double playerSpeed){
	      
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
