package aisoccer.training.scripts;

import java.io.*;
import java.util.Calendar;
import java.util.HashMap;

import math.MathTools;
import math.Vector2D;
import aisoccer.fullStateInfo.*;

public class PassTraining {
	String logsPath;
	KickSnapshot currentKickSnapshot;
	static BufferedWriter out;
	
	// just to avoid 
	
	
	public PassTraining(String path){	
		logsPath = path;
		currentKickSnapshot = null;
		try { 
			out = new BufferedWriter(new FileWriter(logsPath, true));
			Calendar c = Calendar.getInstance();
			String date = "%"+c.get(Calendar.DAY_OF_MONTH)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.YEAR);
			date +=" at "+c.get(Calendar.HOUR_OF_DAY)+"h"+c.get(Calendar.MINUTE)+"m"+c.get(Calendar.SECOND)+"s";			out.write("%", 0, 1);
			out.newLine();
			out.write(date, 0, date.length());
			out.newLine();
			out.flush();
			System.out.println("Le writer est cree avec succes");
		} catch (IOException e) {e.printStackTrace();}
	}
		
	
	public void rememberKick(FullstateInfo fsi, Vector2D ballPosition, Vector2D ballVelocty){
		currentKickSnapshot = new KickSnapshot(fsi, ballPosition, ballVelocty);
	}
	
	public void clearMemory(){
		currentKickSnapshot = null;
	}
	
	public void notifyInterception(Player intercepter){
		if(currentKickSnapshot != null){
			Pass p = new Pass(currentKickSnapshot, intercepter);
			if(p.isValid()){
				String string = p.toString();
				try {
					out.write(string, 0, string.length());
					out.newLine();
					System.out.println("la passe : a été écrite dans le fichier");
					System.out.println("");
					out.flush();
				} catch (IOException e) {e.printStackTrace();}
			}
		}else{
			System.out.println("Warning : notifyInterception was called whereas there was no kick in memory");
		}
		currentKickSnapshot = null;
	}		
	
	
	public class KickSnapshot{
		Vector2D ballPosition;
		Vector2D ballVelocity;
		HashMap<Player,Vector2D> playersPositions = new HashMap<Player,Vector2D>();
		
		public KickSnapshot(FullstateInfo fsi, Vector2D ballP, Vector2D ballV){
			ballPosition = ballP;
			ballVelocity = ballV;
			for(Player p : fsi.getEveryBody()){
				if (p.isConnected()){
					playersPositions.put(p, (Vector2D)p.getPosition().clone());					
				}
			}
//			System.out.println("nb of players in kickSnapShoot : "+playersPositions.size());
		}
	}
		
	
	
	
	public static class Pass{
		KickSnapshot kickSnapshot;
		Player intercepter;
		
		public Pass(KickSnapshot kS, Player p){
			this.kickSnapshot = kS;
			this.intercepter = p;
		}
		
		public boolean isValid(){
//			if(kickSnapshot.ballVelocity.polarRadius()<0.5){
//				System.err.println("Pass too low !");
//				System.out.println("");
//				return false;
//			}
			for(Player p : kickSnapshot.playersPositions.keySet()){				
				if(p.equals(intercepter)){
					return true;
				}
			}
			System.err.println("Invalid pass !");
			System.out.println("");
			return false;
		}
		
		public String toString(){
			String res = ""+kickSnapshot.ballVelocity.polarRadius();
			String others = "";
			Vector2D standPos;
			for(Player p : kickSnapshot.playersPositions.keySet()){
				standPos = MathTools.toPassStandard(kickSnapshot.ballPosition, kickSnapshot.ballVelocity, kickSnapshot.playersPositions.get(p));
				if(p.equals(intercepter)){
//					System.out.println("je suis l'intercepteur : "+p);
					res +=" "+standPos.getX()+" "+standPos.getY();
				}else{
					others +=" "+standPos.getX()+" "+standPos.getY();
				}
			}
			return res+others;
		}
		
	}

}
