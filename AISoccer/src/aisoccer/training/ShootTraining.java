package aisoccer.training;

import java.io.*;

import math.Vector2D;

public class ShootTraining {
	String logsPath;
	ShootSnapshot currentShootSnapshot;
	static BufferedWriter out;
	
	// just to avoid 
	
	
	public ShootTraining(String path){	
		logsPath = path;
		currentShootSnapshot = null;
		try { 
			out = new BufferedWriter(new FileWriter(logsPath, true));
			System.out.println("Le writer est cree avec succes");
		} catch (IOException e) {e.printStackTrace();}
	}
		
	public void rememberShoot(double GoalieDirection, Vector2D ballPosition, double ballPower, double ballDirection){
		currentShootSnapshot = new ShootSnapshot(GoalieDirection,ballPosition,ballPower,ballDirection);
	}
	
	public void clearMemory(){
		currentShootSnapshot = null;
	}
	
	public void notify(boolean goal){
		if(currentShootSnapshot != null){
			Shoot s = new Shoot(currentShootSnapshot, goal);
			String string = s.toString();
			try {
				out.write(string, 0, string.length());
				out.newLine();
				System.out.println("le shoot a �t� �crit dans le fichier");
				System.out.println("");
				out.flush();
			} catch (IOException e) {e.printStackTrace();}
		}else{
			System.out.println("Warning : notifyInterception was called whereas there was no kick in memory");
		}
		currentShootSnapshot = null;
	}
	
		
	
	
	public class ShootSnapshot{
		double goalieDirection;
		Vector2D ballPosition;
		double ballPower;
		double ballDirection;
		
		public ShootSnapshot(double GoalieDirection, Vector2D BallPosition, double BallPower, double BallDirection){
			goalieDirection = GoalieDirection;
			ballPosition = BallPosition;
			ballPower = BallPower;
			ballDirection = BallDirection;
		}
		
		public String toString(){
			return this.goalieDirection + " " +
					this.ballPosition.getX() + " " + 
					this.ballPosition.getY() + " " +
					this.ballPower + " " + 
					this.ballDirection;
		}	
	}
		
	
	
	
	public class Shoot{
		ShootSnapshot shootSnapshot;
		boolean goal;
		
		public Shoot(ShootSnapshot sS, boolean goal){
			this.shootSnapshot = sS;
			this.goal = goal;
		}
		
		public String toString(){
			return this.shootSnapshot.toString() + " " + (goal ? 1 : 0);
		}		
	}

}
