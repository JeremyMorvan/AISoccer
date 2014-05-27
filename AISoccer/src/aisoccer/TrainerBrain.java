package aisoccer;
import java.util.ArrayList;

import math.Vector2D;
import aisoccer.fullStateInfo.FullstateInfo;
import aisoccer.fullStateInfo.Player;

/**
 * 
 * 
 * @author Sebastien Lentz
 *
 */
public class TrainerBrain implements Runnable
{
	/*
	 * Private members.
	 */
	private TrainerClient 	trainerClient; // For communicating with the server 
	private FullstateInfo	fullstateInfo; // Contains all info about the
	
	private PassTraining	passTrainer;
	//   current state of the game

	/*
	 * =========================================================================
	 * 
	 *                     Constructors and destructors
	 * 
	 * =========================================================================
	 */
	/**
	 * Constructor.
	 * 
	 * @param robocupClient
	 * @param teamSide
	 * @param playerNumber
	 * @param strategy
	 */
	public TrainerBrain(TrainerClient client, int nbPlayers)
	{
		this.trainerClient = client;
		this.fullstateInfo = new FullstateInfo(nbPlayers);
		
		this.passTrainer = new PassTraining("../TrainingLogs.txt");
	}

	/*
	 * =========================================================================
	 * 
	 *                      Getters and Setters
	 * 
	 * =========================================================================
	 */
	/**
	 * @return the robocupClient
	 */
	public TrainerClient getTrainerClient()
	{
		return trainerClient;
	}

	/**
	 * @param robocupClient the robocupClient to set
	 */
	public void setTrainerClient(TrainerClient trainerClient)
	{
		this.trainerClient = trainerClient;
	}
	
	public void setTimeOver(){
		trainerClient.changeMode("time_over");
	}
	
	public void setKickOff(char side){
		trainerClient.changeMode("kick_off_"+side);
	}
	
	public void setPlayOn(){
		trainerClient.changeMode("play_on");
	}

	/**
	 * @return the fullstateInfo
	 */
	public FullstateInfo getFullstateInfo()
	{
		return fullstateInfo;
	}

	/**
	 * @param fullstateInfo
	 *            the fullstateInfo to set
	 */
	public void setFullstateInfo(FullstateInfo fullstateInfo)
	{
		this.fullstateInfo = fullstateInfo;
	}

	/*
	 * =========================================================================
	 * 
	 *                          Other methods
	 * 
	 * =========================================================================
	 */
	/**
	 * This is the main function of the Brain.
	 */
	public void run()
	{

		int lastTimeStep = 0;
		int currentTimeStep = 0;
		trainerClient.getTeamNames();
		trainerClient.eyeOn();
		trainerClient.earOn();
		
		while (true) // TODO: change according to the play mode.
		{
			passTraining();
			
			lastTimeStep = currentTimeStep;
			currentTimeStep = fullstateInfo.getTimeStep();
			if (currentTimeStep == lastTimeStep + 1 || currentTimeStep == 0)
			{
				//System.out.println(currentTimeStep);
				//                System.out.println(fullstateInfo.getTimeStep() + ": " + player + " " + fullstateInfo.getBall());
				//                System.out.println("Next position: " + player.nextPosition(100.0d));
				//                System.out.println("Next velocity: " + player.nextVelocity(100.0d));
			}
			else if (currentTimeStep != lastTimeStep)
			{
				System.out.println("A time step has been skipped:");
				System.out.println("Last time step: " + lastTimeStep);
				System.out.println("Current time step: " + currentTimeStep);
			}

			// Wait for next cycle before sending another command.
			try
			{
				Thread.sleep(SoccerParams.SIMULATOR_STEP / 5);

			}
			catch (Exception e){System.err.println(e);}
		}	
	}
	
	
	
	public void passTraining(){
		if(fullstateInfo.getPlayMode() != null){
			if(!fullstateInfo.getPlayMode().equals("play_on")){
				System.out.println("here1");
				movePlayers();				
				randomShoot();
				setPlayOn();
				return;			
			}
			
			Player intercepter = null;
			for(Player p : fullstateInfo.getEveryBody()){
				if(p.distanceTo(fullstateInfo.getBall())<SoccerParams.KICKABLE_MARGIN){	
					if(intercepter == null){
						intercepter = p;
					}else{
						// There are at least two players who intercept the ball
						// So we discard this pass
						setTimeOver();
//						passTrainer.clearMemory();
//						movePlayers();
//						randomShoot();
						return;
					}
				}
			}
			if(intercepter != null){
				System.out.println("here2");
				// THERE IS AN INTERCEPTER
				passTrainer.notifyInterception(intercepter);
				setTimeOver();
//				movePlayers();
//				randomShoot();
			}	
		}			
	}
	
	public void movePlayers(){
		// MOVE THE PLAYERS FOR THE NEXT PASS SIMULATION
		ArrayList<Player> everybody = fullstateInfo.getEveryBody();
		double x;
		double y;
		for(Player p : everybody){
			x = SoccerParams.FIELD_LENGTH*(Math.random()-0.5);
			y = SoccerParams.FIELD_WIDTH*(Math.random()-0.5);
			trainerClient.move(p,x,y);
		}
	}
	
	public void randomShoot(){
		Vector2D newBallP = null;
		Vector2D newBallV = null;
		
		// HERE : COMPUTE newBalP AND newBallV
		double x = SoccerParams.FIELD_LENGTH*(Math.random()-0.5);
		double y = SoccerParams.FIELD_WIDTH*(Math.random()-0.5);
		newBallP = new Vector2D(x,y);
		
		double direction;
		double min;
		double max;
		
		while(newBallV == null){
			direction = 2*Math.random()*Math.PI;
			
			min = 0.5;
			max = 0;
			if(direction<=Math.PI/4||direction>7*Math.PI/4){
				max = ((SoccerParams.FIELD_LENGTH/2)-x)/Math.cos(direction);
//				System.out.println("case 1");
//				System.out.println((SoccerParams.FIELD_LENGTH/2)-x);
//				System.out.println(Math.cos(direction));
			}
			else if(direction>Math.PI/4&&direction<=3*Math.PI/4){
				max = ((SoccerParams.FIELD_WIDTH/2)-y)/Math.sin(direction);
//				System.out.println("case 2");
//				System.out.println((SoccerParams.FIELD_WIDTH/2)-y);
//				System.out.println(Math.sin(direction));
			}
			else if(direction>3*Math.PI/4&&direction<=5*Math.PI/4){
				max = -((SoccerParams.FIELD_LENGTH/2)+x)/Math.cos(direction);
//				System.out.println("case 3");
//				System.out.println((SoccerParams.FIELD_LENGTH/2)+x);
//				System.out.println(Math.cos(direction));
			}
			else if(direction>5*Math.PI/4&&direction<=7*Math.PI/4){
				max = -((SoccerParams.FIELD_WIDTH/2)+y)/Math.sin(direction);
//				System.out.println("case 4");
//				System.out.println((SoccerParams.FIELD_WIDTH/2)+y);
//				System.out.println(Math.sin(direction));
			}
			else{
				System.out.println("the direction couldn't be classified : " + direction);
			}
//			System.out.println("direction : " + direction + "; max : " + max + "; pos : " + newBallP);
			max = max*(1-SoccerParams.BALL_DECAY)/SoccerParams.BALL_SPEED_MAX;
//			if(max>3){
//				System.out.println("////////////////////////////////////////////////////");
//				System.out.println("direction : " + direction + "; max : " + max + "; pos : " + newBallP);
//				System.out.println(Math.cos(direction));
//				System.out.println(Math.sin(direction));
//				System.out.println("////////////////////////////////////////////////////");
//			}
			if(max>min){
				newBallV = new Vector2D(Math.random()*(max-min)+min,direction,true);
			}
		}
		trainerClient.moveBall(newBallP, newBallV);
		passTrainer.rememberKick(fullstateInfo, newBallP, newBallV);		
	}
}
