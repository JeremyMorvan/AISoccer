package aisoccer;

import aisoccer.MathFunction;
import aisoccer.MathTools;
import aisoccer.Vector2D;
import aisoccer.strategy.*;

public class Tests {
	
	public static void main(String args[]) throws InvalidArgumentException{
		  myStrategy s = new myStrategy();
		  Vector2D ballPos = new Vector2D(5.0, 5.0);
		  Vector2D ballVelocity = new Vector2D(-1.0, 0.0);
		  Vector2D playerPos = new Vector2D(0.0, 0.0);
		  double playerSpeed = 10.0;

	      System.out.println("hello");
	      System.out.println(s.optimumInterception(ballPos, ballVelocity, playerPos, playerSpeed));	      
	      
	
	}
}
