package aisoccer;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import math.Vector2D;
import aisoccer.fullStateInfo.*;

public class TrainingLogs {
	static final String LOGSPATH = "trainingLogs.txt";
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
			
			String string = p.toString();
			try {
				out.write(string, 0, string.length());
				out.newLine();
				System.out.println("la passe : a été écrite dans le fichier");
				out.flush();
			} catch (IOException e) {e.printStackTrace();}
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
				playersPositions.put(p, (Vector2D)p.getPosition().clone());
			}
			for(Player p : fsi.getRightTeam()){
				playersPositions.put(p, (Vector2D)p.getPosition().clone());
			}			
			
			this.ballVelocityAfterKick = null;
		}
	}
		
	
	
	
	public static class Pass{
		KickSnapshot kickSnapshot;
		Player intercepter;
		
		public Pass(KickSnapshot kS, Player p){
			this.kickSnapshot = kS;
			this.intercepter = p;
		}
		
		public String toString(){
			System.out.println("intercepteur : "+intercepter);
			String res = kickSnapshot.ballVelocityAfterKick.getX()+" "+kickSnapshot.ballVelocityAfterKick.getY();
			String others = "";
			Vector2D relPos;
			for(Player p : kickSnapshot.playersPositions.keySet()){
				relPos = kickSnapshot.playersPositions.get(p).subtract(kickSnapshot.ballPosition);
				boolean equals = p.isLeftSide() == intercepter.isLeftSide() && p.getUniformNumber() == intercepter.getUniformNumber();
				if(equals){
					System.out.println("je suis l'intercepteur");
					res +=" "+relPos.getX()+" "+relPos.getY();
				}else{
					others +=" "+relPos.getX()+" "+relPos.getY();
				}
			}
			return res+others;
		}
	}

}
