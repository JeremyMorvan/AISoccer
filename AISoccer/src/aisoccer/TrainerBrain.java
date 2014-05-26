package aisoccer;
import math.Vector2D;
import aisoccer.fullStateInfo.FullstateInfo;

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
	private TrainerClient            trainerClient; // For communicating with the server 
	private FullstateInfo            fullstateInfo; // Contains all info about the
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
				if(count == 20){
					trainerClient.move(fullstateInfo.getLeftTeam()[0], new Vector2D(0,0), 0, new Vector2D(0,0));
					setPlayOn();
					count = 0;
				}
			}
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
			catch (Exception e)
			{
				System.err.println(e);
			}
		}

	}
}
