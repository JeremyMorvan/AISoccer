package aisoccer;

import java.io.*;
import java.util.HashMap;

import math.Vector2D;
import aisoccer.fullStateInfo.*;

public class TrainingLogs {
	static final String LOGSPATH = "../trainingLogs.txt";
	static KickSnapshot currentKickSnapshot;
	static boolean ready = false;
	
	static BufferedWriter out;
	
	
	public static void init(){
		currentKickSnapshot = null;

		try { 
			out = new BufferedWriter(new FileWriter(LOGSPATH, true));
			System.out.println("Le writer est cree avec succes");
		} catch (IOException e) {
			e.printStackTrace();
		}
		ready = true;
	}
		
	public static void notifyKick(Player intercepter, FullstateInfo fsi){
		if(!ready){
			init();
		}
		if(fsi.getPlayMode().equals("play_on") && currentKickSnapshot.ballVelocityAfterKick!=null){
			// this is a successful pass
			Pass p = new Pass(currentKickSnapshot, intercepter);
			if(p.isValid()){
				String string = p.toString();
				try {
					out.write(string, 0, string.length());
					out.newLine();
					System.out.println("la passe : a �t� �crite dans le fichier");
					System.out.println("");
					out.flush();
				} catch (IOException e) {e.printStackTrace();}				
			}else{
			}
		}
		currentKickSnapshot = new KickSnapshot(fsi);
	}
	
	
	public static void takeFSI(FullstateInfo f){
		if(currentKickSnapshot!=null && f.getTimeStep() == currentKickSnapshot.timeStep+1 && currentKickSnapshot.ballVelocityAfterKick == null){
			currentKickSnapshot.ballVelocityAfterKick = f.getBall().getVelocity().multiply(1.0/SoccerParams.BALL_DECAY);
		}
	}
	
	
	
	public static class KickSnapshot{
		int timeStep;
		Vector2D ballPosition;
		HashMap<Player,Vector2D> playersPositions = new HashMap<Player,Vector2D>();
		Vector2D ballVelocityAfterKick;
		
		public KickSnapshot(FullstateInfo fsi){
			timeStep = fsi.getTimeStep();
			ballPosition = (Vector2D) fsi.getBall().getPosition().clone();
			for(Player p : fsi.getLeftTeam()){
				if (p.isConnected()){
					playersPositions.put(p, (Vector2D)p.getPosition().clone());					
				}
			}
			for(Player p : fsi.getRightTeam()){
				if (p.isConnected()){
					playersPositions.put(p, (Vector2D)p.getPosition().clone());			
				}			
			}
			this.ballVelocityAfterKick = null;
			//System.out.println("nb of players in kickSnapShoot : "+playersPositions.size());
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
			boolean equals;
			if(kickSnapshot.ballVelocityAfterKick.polarRadius()<0.5){
				//System.err.println("Pass too low !");
				//System.out.println("");
				return false;
			}
			for(Player p : kickSnapshot.playersPositions.keySet()){				
				equals = p.isLeftSide() == intercepter.isLeftSide() && p.getUniformNumber() == intercepter.getUniformNumber();
				if(equals){
					return true;
				}
			}
			//System.err.println("Invalid pass !");
			//System.out.println("");
			return false;
		}
		
		public String toString(){
			String res = ""+kickSnapshot.ballVelocityAfterKick.polarRadius();
			String others = "";
			Vector2D standPos;
			for(Player p : kickSnapshot.playersPositions.keySet()){
				standPos = toStandard(kickSnapshot.ballPosition, kickSnapshot.ballVelocityAfterKick, kickSnapshot.playersPositions.get(p));
				boolean equals = p.isLeftSide() == intercepter.isLeftSide() && p.getUniformNumber() == intercepter.getUniformNumber();
				if(equals){
					//System.out.println("je suis l'intercepteur : "+p);
					res +=" "+standPos.getX()+" "+standPos.getY();
				}else{
					others +=" "+standPos.getX()+" "+standPos.getY();
				}
			}
			return res+others;
		}
		
		
		
		public static Vector2D toStandard(Vector2D pBall, Vector2D vBall, Vector2D p){
			Vector2D res = p.subtract(pBall);
			res = res.rotate(-vBall.polarAngle('r'));
			res.setY(Math.abs(res.getY()));
			return res;
		}
		
	}

}
