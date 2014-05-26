package aisoccer;

import java.lang.Math;

import math.MathFunction;
import math.MathTools;
import math.Vector2D;
import aisoccer.ballcapture.State;
import aisoccer.fullStateInfo.Ball;
import aisoccer.fullStateInfo.FullstateInfo;
import aisoccer.fullStateInfo.Player;
import aisoccer.strategy.Strategy;

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
	private State					 state;
	private Player                   player;       // The player this brain controls
	private Strategy                 strategy;     // Strategy used by this brain

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

	/**
	 * @return the player
	 */
	public Player getPlayer()
	{
		return player;
	}

	/**
	 * @param player the player to set
	 */
	public void setPlayer(Player player)
	{
		this.player = player;
	}

	/**
	 * @return the strategy
	 */
	public Strategy getStrategy()
	{
		return strategy;
	}

	/**
	 * @param strategy the strategy to set
	 */
	public void setStrategy(Strategy strategy)
	{
		this.strategy = strategy;
	}

	/**
	 * @return the actionsQueue
	 */

	/**
	 * @param actionsQueue the actionsQueue to set
	 */

	/**
	 * @return the fullstateInfo
	 */
	public FullstateInfo getFullstateInfo()
	{
		return fullstateInfo;
	}
	
	public State getState(){
		return this.state;
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
		// Before kick off, position the player somewhere in his side.

		int lastTimeStep = 0;
		int currentTimeStep = 0;
		while (true) // TODO: change according to the play mode.
		{
			lastTimeStep = currentTimeStep;
			currentTimeStep = fullstateInfo.getTimeStep();
			if (currentTimeStep == lastTimeStep + 1 || currentTimeStep == 0)
			{

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

	private void actualizeState() {
		this.state = new State(this.fullstateInfo,this.player);
	}

	
	public Vector2D ballPositionPrediction(double steps){
		Ball b = fullstateInfo.getBall();
		double k = SoccerParams.BALL_DECAY;
		double coeff = (1-Math.pow(k, (Double) steps))/(1-k);   
		return b.getPosition().add(b.getVelocity().multiply(coeff));
	}

	///////////////////////////////////////////////////
	/////	 INTERCEPTION METHODS
	public double timeToIntercept(final Player p, final double playerSpeed){
		double time = -1.0;

		MathFunction f = new  MathFunction() {
			public double value(double steps) {	                
				return p.distanceTo(ballPositionPrediction(steps))/playerSpeed - steps;
			}
		};
		
		double finalDist = p.distanceTo(ballPositionPrediction(Double.POSITIVE_INFINITY));
		double distanceMax = Math.max( finalDist, p.distanceTo(fullstateInfo.getBall()) );		
		try {
			time = MathTools.zeroCrossing(f, 0.0, distanceMax/playerSpeed, 0.1);
			//    	  System.out.println("time = "+ time);
		} catch (InvalidArgumentException e) {
			e.printStackTrace();
		}
		return time;		
	}
	
	public double timeToIntercept(final Player p){
		return timeToIntercept(p, SoccerParams.PLAYER_SPEED_MAX*0.6);
	}

	public Vector2D optimumInterceptionPosition(double playerSpeed){
		double time = timeToIntercept(player, playerSpeed);
		return (time>0) ? ballPositionPrediction(time) : null;
	}
	/////////////////////////////////////////////
	

	public double getEffectivePowerRate(){
		double realDistance = player.distanceTo(fullstateInfo.getBall())-SoccerParams.PLAYER_SIZE-SoccerParams.BALL_SIZE;
		realDistance = Math.max(0, realDistance);
		double distCoeff = realDistance/SoccerParams.KICKABLE_MARGIN;
		double dirCoeff = player.angleFromBody(fullstateInfo.getBall())/180;
		return SoccerParams.KICK_POWER_RATE*( 1 - 0.25*(distCoeff + dirCoeff) );
	}
	
	/////////////////////////////////////////////
	////	MARK METHODS
	public boolean checkMarked(Vector2D teammateRP, Vector2D opponentRP){
		if(teammateRP.multiply(opponentRP)<0){
			return true;
		}
		double angle = Math.toRadians(MathTools.normalizeAngle(teammateRP.polarAngle()-opponentRP.polarAngle()));
		if(Math.abs(Math.tan(angle))>SoccerParams.PLAYER_SPEED_MAX/SoccerParams.BALL_SPEED_MAX){
			return true;
		}
		double norm = opponentRP.polarRadius();
		if(norm*(Math.abs(Math.cos(angle))+Math.abs(Math.sin(angle)))>teammateRP.polarRadius()){
			return true;
		}
		return false;		
	}

	public boolean checkMarked(Vector2D teammateRP, Iterable<Vector2D> opponentsRP){
		for(Vector2D oRP : opponentsRP){
			if(!checkMarked(teammateRP,oRP)){
				return false;
			}
		}
		return true;
	}

	public double getXLimOffSide(){
		int p = this.player.isLeftSide() ? 1:-1;		
		Iterable<Player> opponents = this.fullstateInfo.getOpponents(this.player);
		double xmin1 = 0;
		double xmin2 = 0;
		if(this.player.isLeftSide()){
			if(this.fullstateInfo.getBall().getPosition().getX()>0){
				xmin1 = this.fullstateInfo.getBall().getPosition().getX();
				xmin2 = this.fullstateInfo.getBall().getPosition().getX();
			}
		}else{
			if(this.fullstateInfo.getBall().getPosition().getX()<0){
				xmin1 = -this.fullstateInfo.getBall().getPosition().getX();
				xmin2 = -this.fullstateInfo.getBall().getPosition().getX();
			}
		}

		@SuppressWarnings("unused")
		double temp;
		for(Player op : opponents){
			if(p*op.getPosition().getX()>p*xmin1){
				temp = xmin1;
				xmin2 = xmin1;
				xmin1 = op.getPosition().getX();
			}else if(p*op.getPosition().getX()>p*xmin2){
				xmin2 = op.getPosition().getX();
			}
		}
		return xmin2;

	}
}
