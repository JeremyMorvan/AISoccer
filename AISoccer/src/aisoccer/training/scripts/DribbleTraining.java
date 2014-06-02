package aisoccer.training.scripts;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;

import aisoccer.fullStateInfo.FullstateInfo;
import aisoccer.fullStateInfo.Player;

import math.Vector2D;

public class DribbleTraining {
	String logsPath;
	DribbleSnapshot currentDribbleSnapshot;
	static BufferedWriter out;
	
	// just to avoid 
	
	
	public DribbleTraining(String path){	
		logsPath = path;
		currentDribbleSnapshot = null;
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
		
	public void rememberDribble(Vector2D ballP, Vector2D ballV, FullstateInfo fsi){
		currentDribbleSnapshot = new DribbleSnapshot(ballP,ballV,fsi);
	}
	
	public void clearMemory(){
		currentDribbleSnapshot = null;
	}
	
	public void notify(boolean goal){
		if(currentDribbleSnapshot != null){
			Dribble s = new Dribble(currentDribbleSnapshot, goal);
			String string = s.toString();
			try {
				out.write(string, 0, string.length());
				out.newLine();
				System.out.println("le dribble a été écrit dans le fichier");
				System.out.println("");
				out.flush();
			} catch (IOException e) {e.printStackTrace();}
		}else{
			System.out.println("Warning : notifyInterception was called whereas there was no kick in memory");
		}
		currentDribbleSnapshot = null;
	}
	
		
	
	
	public class DribbleSnapshot{
		double ballVelocity;
		ArrayList<Vector2D> everybody;
		
		public DribbleSnapshot(Vector2D ballPos, Vector2D ballVelocity, FullstateInfo fsi){
			this.ballVelocity = ballVelocity.polarRadius();
			everybody = new ArrayList<Vector2D>();
			for(Player p : fsi.getEveryBody()){
				if (!(p.isLeftSide()&&p.getUniformNumber()==1)&&p.isConnected()){
					everybody.add(DribbleTraining.toStandard(ballPos, ballVelocity, (Vector2D)p.getPosition().clone()));					
				}
			}
		}
		
		public String toString(){
			String output = "" + this.ballVelocity;
			for(Vector2D v2 : everybody){
				output = " " + v2.getX() + " " + v2.getY();
			}
			return output;
		}	
	}
		
	
	
	
	public class Dribble{
		DribbleSnapshot dribbleSnapshot;
		boolean goal;
		
		public Dribble(DribbleSnapshot sS, boolean goal){
			this.dribbleSnapshot = sS;
			this.goal = goal;
		}
		
		public String toString(){
			return this.dribbleSnapshot.toString() + " " + (goal ? 1 : 0);
		}		
	}
	
	public static Vector2D toStandard(Vector2D pBall, Vector2D vBall, Vector2D p){
		Vector2D res = p.subtract(pBall);
		res = res.rotate(-vBall.polarAngle('r'));
		res.setY(Math.abs(res.getY()));
		return res;
	}

}
