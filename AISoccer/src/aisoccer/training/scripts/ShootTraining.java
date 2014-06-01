package aisoccer.training.scripts;

import java.io.*;
import java.util.Calendar;

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
			Calendar c = Calendar.getInstance();
			String date = "%"+c.get(Calendar.DAY_OF_MONTH)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.YEAR);
			date +=" at "+c.get(Calendar.HOUR_OF_DAY)+"h"+c.get(Calendar.MINUTE)+"m"+c.get(Calendar.SECOND)+"s";
			out.write("%", 0, 1);
			out.newLine();
			out.write(date, 0, date.length());
			out.newLine();
			out.flush();
			System.out.println("Le writer est cree avec succes");
		} catch (IOException e) {e.printStackTrace();}
	}
		
	public void rememberShoot(Vector2D GoaliePosition, Vector2D ballPosition, double ballPower, double ballDirection){
		currentShootSnapshot = new ShootSnapshot(GoaliePosition,ballPosition,ballPower,ballDirection);
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
				System.out.println("le shoot a été écrit dans le fichier");
				System.out.println("");
				out.flush();
			} catch (IOException e) {e.printStackTrace();}
		}else{
			System.out.println("Warning : notifyInterception was called whereas there was no kick in memory");
		}
		currentShootSnapshot = null;
	}
	
		
	
	
	public class ShootSnapshot{
		Vector2D goaliePosition;
		Vector2D ballPosition;
		double ballPower;
		double ballDirection;
		
		public ShootSnapshot(Vector2D GoaliePosition, Vector2D BallPosition, double BallPower, double BallDirection){
			goaliePosition = GoaliePosition;
			ballPosition = BallPosition;
			ballPower = BallPower;
			ballDirection = BallDirection;
		}
		
		public String toString(){
			return this.goaliePosition.getX() + " " +
					this.goaliePosition.getY() + " " +
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
