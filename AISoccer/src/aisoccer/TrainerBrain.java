package aisoccer;
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
		int count = 0;
		
		while (true) // TODO: change according to the play mode.
		{
			if(fullstateInfo.getPlayMode() != null && fullstateInfo.getPlayMode().equals("time_over")){
				count++;
				System.out.println(count);
				if(count == 40){
					trainerClient.move(fullstateInfo.getLeftTeam()[0], new Vector2D(40,0));
					trainerClient.moveBall(new Vector2D(-40,0), new Vector2D(2,0));
				}
				if(count == 100){
					setPlayOn();
					count = 0;
				}
			}
			//passTraining();
			
			lastTimeStep = currentTimeStep;
			currentTimeStep = fullstateInfo.getTimeStep();
			if (currentTimeStep == lastTimeStep + 1 || currentTimeStep == 0)
			{
				//System.out.println(currentTimeStep);
				if(currentTimeStep > 0 && currentTimeStep % 100 == 0){
					System.out.println("hehe, c'est l'heure  : " + currentTimeStep);
					setTimeOver();
				}
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
		if(!fullstateInfo.getPlayMode().equals("play_on")){
			movePlayers();
			trainerClient.changeMode("play_on");
			randomShoot();
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
					passTrainer.clearMemory();
					movePlayers();
					randomShoot();
					return;
				}
			}
		}
		
		if(intercepter != null){
			// THERE IS AN INTERCEPTER
			passTrainer.notifyInterception(intercepter);
			movePlayers();
			randomShoot();
		}		
	}
	
	public void movePlayers(){
		// MOVE THE PLAYERS FOR THE NEXT PASS SIMULATION
		
	}
	
	public void randomShoot(){
		Vector2D newBallP = null;
		Vector2D newBallV = null;
		
		// HERE : COMPUTE newBalP AND newBallV
		
		trainerClient.moveBall(newBallP, newBallV);
		passTrainer.rememberKick(fullstateInfo, newBallP, newBallV);		
	}
}
