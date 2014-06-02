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
	
	public void notify(int intercepter){
		if(currentDribbleSnapshot != null){
			Dribble s = new Dribble(currentDribbleSnapshot, intercepter);
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
		ArrayList<Vector2D> everybodyConnected;
		ArrayList<Vector2D> everybody;
		
		public DribbleSnapshot(Vector2D ballPos, Vector2D ballVelocity, FullstateInfo fsi){
			this.ballVelocity = ballVelocity.polarRadius();
			everybody = new ArrayList<Vector2D>();
			everybodyConnected = new ArrayList<Vector2D>();
			for(Player p : fsi.getEveryBody()){
				if (!(p.isLeftSide()&&p.getUniformNumber()==1)){
					Vector2D v2 = DribbleTraining.toStandard(ballPos, ballVelocity, (Vector2D)p.getPosition().clone());
					if(p.isConnected()){
						everybodyConnected.add(v2);
					}
					everybody.add(v2);										
				}
			}
		}
		
		public String toString(int intercepter){
			String output = "" + this.ballVelocity;
			if(intercepter==0){				
				for(Vector2D v2 : everybodyConnected){
					output += " " + v2.getX() + " " + v2.getY();
				}
			}else{
				output += " " + everybody.get(intercepter-1).getX() + " " + everybody.get(intercepter-1).getY();
			}
			return output;			
		}	
	}
		
	public class Dribble{
		DribbleSnapshot dribbleSnapshot;
		boolean goal;
		int intercepter;
		
		public Dribble(DribbleSnapshot sS, int intercepter){
			this.dribbleSnapshot = sS;
			this.intercepter = intercepter;
		}
		
		public String toString(){
			return this.dribbleSnapshot.toString(intercepter);
		}
	}
	
	public static Vector2D toStandard(Vector2D pBall, Vector2D vBall, Vector2D p){
		Vector2D res = p.subtract(pBall);
		res = res.rotate(-vBall.polarAngle('r'));
		res.setY(Math.abs(res.getY()));
		return res;
	}

}
