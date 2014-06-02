package aisoccer;

import java.lang.Math;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Random;

import math.Identity;
import math.MathFunction;
import math.MathTools;
import math.Sigmoide;
import math.Vector2D;
import aisoccer.ballcapture.Action;
import aisoccer.ballcapture.State;
import aisoccer.fullStateInfo.*;
import aisoccer.strategy.Strategy;
import aisoccer.training.scripts.PassTraining;
import ann.Network;

/**
 * 
 * 
 * @author Sebastien Lentz
 *
 */
public class Brain implements Runnable
{
	/*
	 * Private members.
	 */
	private RobocupClient            robocupClient; // For communicating with the server 
	private FullstateInfo            fullstateInfo; // Contains all info about the
	//   current state of the game
	private State					 state;
	private Player                   player;       // The player this brain controls
	private Strategy                 strategy;     // Strategy used by this brain
	private ArrayDeque<PlayerAction> actionsQueue; // Contains the actions to be executed.
	private Area[]					 allAreas;
	private HashSet<Area>			 myAreas;
	private Vector2D 				 posIni;

	private Vector2D				 interestPos;
	private Vector2D 				 shootVector;
	
	private Network 				 passNetwork;
	private Network					 shootNetwork;
	private Network					 dribbleNetwork;
	
	public final double 			speedEstimation = SoccerParams.PLAYER_SPEED_MAX*0.6;

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
	public Brain(RobocupClient robocupClient, boolean leftSide, int playerNumber, int nbPlayers)
	{
		this.robocupClient = robocupClient;
		this.fullstateInfo = new FullstateInfo(nbPlayers);
		this.player = leftSide ? fullstateInfo.getLeftTeam()[playerNumber - 1] : fullstateInfo.getRightTeam()[playerNumber - 1];
		this.actionsQueue = new ArrayDeque<PlayerAction>();
		this.player.setUniformNumber(playerNumber);
		
		MathFunction identity = new Identity();
		MathFunction sigmoide = new Sigmoide();
//		this.passNetwork = Network.load("ANN-passTraining.txt", identity);
//		this.shootNetwork = Network.load("", );
//		this.dribbleNetwork = Network.load("",); 
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
	public RobocupClient getRobocupClient()
	{
		return robocupClient;
	}

	/**
	 * @param robocupClient the robocupClient to set
	 */
	public void setRobocupClient(RobocupClient robocupClient)
	{
		this.robocupClient = robocupClient;
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
	public ArrayDeque<PlayerAction> getActionsQueue()
	{
		return actionsQueue;
	}

	/**
	 * @param actionsQueue the actionsQueue to set
	 */
	public void setActionsQueue(ArrayDeque<PlayerAction> actionsQueue)
	{
		this.actionsQueue = actionsQueue;
	}

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

	public void setInterestPos(Vector2D pos){
		this.interestPos = pos;
	}

	public Vector2D getInterestPos(){
		return this.interestPos;
	}
	
	public void setShootVector(Vector2D v){
		this.shootVector = v;
	}

	public Vector2D getShootVector(){
		return this.shootVector;
	}


	public HashSet<Area> getMyAreas() {
		return myAreas;
	}

	public void setMyAreas(HashSet<Area> myAreas) {
		this.myAreas = myAreas;
	}	

	public Area getArea(int i,int j){
		return this.allAreas[j+i*7];
	}


	public void computeAreas(){
		allAreas = new Area[70];
		double stepX = SoccerParams.FIELD_LENGTH/10;
		double stepY = SoccerParams.FIELD_WIDTH/7;
		double xmin = -SoccerParams.FIELD_LENGTH/2;
		double ymin = -SoccerParams.FIELD_WIDTH/2;
		if(this.getPlayer().isLeftSide()){
			for(int i=0;i<10;i++){
				for(int j=0;j<7;j++){
					allAreas[j+7*i] = new Area(xmin+i*stepX,xmin+(i+1)*stepX,ymin+j*stepY,ymin+(j+1)*stepY);
				}
			}
		}else{
			for(int i=0;i<10;i++){
				for(int j=0;j<7;j++){
					allAreas[69-(j+7*i)] = new Area(xmin+i*stepX,xmin+(i+1)*stepX,ymin+j*stepY,ymin+(j+1)*stepY);
				}
			}
		}

	}

	/**
	 * @param fullstateInfo
	 *            the fullstateInfo to set
	 */
	public void setFullstateInfo(FullstateInfo fullstateInfo)
	{
		this.fullstateInfo = fullstateInfo;
	}

	public void doAction(PlayerAction pAction){
		this.actionsQueue.addLast(pAction);
	}

	public void doAction(Action action){
		this.actionsQueue.addLast(new PlayerAction(action,robocupClient));
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
		robocupClient.move(-Math.random() * 52.5, (2*Math.random()-1) * 34.0);

		int lastTimeStep = 0;
		int currentTimeStep = 0;
		while (true) // TODO: change according to the play mode.
		{
			lastTimeStep = currentTimeStep;
			currentTimeStep = fullstateInfo.getTimeStep();
			if (currentTimeStep == lastTimeStep + 1 || currentTimeStep == 0)
			{
				if (actionsQueue.isEmpty())
				{ // The queue is empty, check if we need to add an action.
					actualizeState();
					strategy.doAction(this);
				}

				if (!actionsQueue.isEmpty())
				{ // An action needs to be executed at this time step, so do it.
					actionsQueue.removeFirst().execute();
				}

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
	
	//////////////////////////////////////////
	// BALL DYNAMICS TOOLS	
	public Vector2D ballPositionPrediction(double steps){
		Ball b = fullstateInfo.getBall();
		double k = SoccerParams.BALL_DECAY;
		double coeff = (1-Math.pow(k, (Double) steps))/(1-k);   
		return b.getPosition().add(b.getVelocity().multiply(coeff));
	}
	
	public double timeDistanceBall(double distance, double speed) throws InvalidArgumentException{
		if(speed<=0 || distance<=0){
			throw new InvalidArgumentException();
		}
		return Math.log(1-(1-SoccerParams.BALL_DECAY)*distance/speed)/Math.log(SoccerParams.BALL_DECAY);
	}
	
	public double timeDistanceBall(double distance) throws InvalidArgumentException{
		return timeDistanceBall(distance,fullstateInfo.getBall().getVelocity().polarRadius());
	}

	///////////////////////////////////////////////////
	/////	 INTERCEPTION METHODS
	public double timeToIntercept(final Player p, final double playerSpeed, final double margin){
		double time = -1.0;
		Vector2D ballP = fullstateInfo.getBall().getPosition();

		MathFunction f = new  MathFunction() {
			public double value(double steps) {	          
				double distance = Math.max(p.distanceTo(ballPositionPrediction(steps)) - margin, 0);
				return distance/playerSpeed - steps;
			}
		};
		
		Vector2D ballFinalPos = ballPositionPrediction(Double.POSITIVE_INFINITY);
		double finalDist = p.distanceTo(ballFinalPos);
		double stepsMax = Math.max( finalDist, p.distanceTo(ballP) )/playerSpeed;
		
		
		Vector2D trajectory = ballFinalPos.subtract(ballP);
		if(trajectory.polarRadius()>0){
			Vector2D relPos = p.getPosition().subtract(ballP);
			double ps = relPos.multiply(trajectory.normalize());
			if(0<ps && ps<trajectory.polarRadius()){
				Vector2D projection = ballP.add(trajectory.normalize().multiply(ps));
				
				double tProj = projection.distanceTo(p)/playerSpeed;
				double distBallTProj = ballPositionPrediction(tProj).distanceTo(ballP);
				if(distBallTProj<projection.distanceTo(ballP)){
					try {
						stepsMax = timeDistanceBall(projection.distanceTo(ballP));
//						System.out.println("projection = "+projection.toString());
//						System.out.println("distBallProj = "+projection.distanceTo(ballP));
//						System.out.println("vitesse Balle = "+fullstateInfo.getBall().getVelocity().polarRadius());
//						System.out.println("stepsMax = "+stepsMax);
//						System.out.println("ballPrediction(stepsMax) = "+ballPositionPrediction(stepsMax));
					} catch (InvalidArgumentException e) {e.printStackTrace();}
				}
			}			
		}
		
		double f0 = f.value(0);
		int counter = 0;
		while(counter<4 && f0*f.value(stepsMax)>0){
			stepsMax *= 2;
			counter++;
		}
		if(f0*f.value(stepsMax)>0){
			System.err.println("Pb in finding interception point");
			return -1.0;
		}
		
		try {
			time = MathTools.zeroCrossing(f, 0.0, stepsMax, 0.1);
		} catch (InvalidArgumentException e) {
			e.printStackTrace();
		}
		return time;		
	}
	
	
	public double timeToIntercept(Player p){
		return timeToIntercept(p, speedEstimation, 0);
	}

	public Vector2D optimumInterceptionPosition(double playerSpeed, double margin){
		double time = timeToIntercept(player, playerSpeed, margin);
		return (time>0) ? ballPositionPrediction(time) : null;
	}
	
	//////////////////////////////////////
	// PASS AND KICK TOOLS	
	public Vector2D computeNeededVelocity(Vector2D teamMate, double speedTM, Vector2D interceptionPoint){
		double time = teamMate.distanceTo(interceptionPoint)/speedTM;
		double dist = interceptionPoint.distanceTo(fullstateInfo.getBall());
		double speed = dist*(1-SoccerParams.BALL_DECAY)/(1-Math.pow(SoccerParams.BALL_DECAY, time));
		if(speed==0){
			return new Vector2D(0, 0);
		}
		return interceptionPoint.subtract(fullstateInfo.getBall().getPosition()).normalize().multiply(speed);
	}
	
	public Vector2D computeNeededVelocity(Vector2D teamMate, Vector2D interceptionPoint){
		return 	computeNeededVelocity(teamMate, speedEstimation, interceptionPoint);
	}

		
	public ArrayList<Vector2D> generatePointsAround(Vector2D player, Vector2D goal){
		int	nbRandomPass = 10;
		double maxRange = SoccerParams.FIELD_LENGTH/6;
		double angleDeviation = Math.PI/8;		
		double angleMax = Math.PI/4;

		ArrayList<Vector2D> points = new ArrayList<Vector2D>(nbRandomPass);
		Random generator = new Random();
		boolean closeToGoal = Math.abs((player.getX()-goal.getX()))<SoccerParams.FIELD_LENGTH/6;
		Vector2D pointRP;
		double angle;
		for(int i=0; i<nbRandomPass; i++){
			angle = generator.nextGaussian()*angleDeviation;
			angle = Math.signum(angle)*Math.min(Math.abs(angle), angleMax);
			pointRP = new Vector2D(generator.nextDouble()*maxRange, angle, true);
			if(closeToGoal){
				pointRP = pointRP.rotate(Math.toRadians(player.directionOf(goal)));
			}
			points.add(player.add(pointRP));
		}
		return null;
		
	}
	
	public ArrayList<Vector2D> generateVelocityVectors(Vector2D player, Vector2D goal){
		
		if(Math.abs((player.getX()-goal.getX()))<SoccerParams.FIELD_LENGTH/6){
			
		}else{
			
		}
	}
	

	public double getEffectivePowerRate(){
		double realDistance = player.distanceTo(fullstateInfo.getBall())-SoccerParams.PLAYER_SIZE-SoccerParams.BALL_SIZE;
		realDistance = Math.max(0, realDistance);
		double distCoeff = realDistance/SoccerParams.KICKABLE_MARGIN;
		double dirCoeff = player.angleFromBody(fullstateInfo.getBall())/180;
		return SoccerParams.KICK_POWER_RATE*( 1 - 0.25*(distCoeff + dirCoeff) );
	}
	
	/////////////////////////////////////////////
	////	MARK METHODS
	public boolean isFree(Vector2D teammateRP, Vector2D opponentRP){
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

	public boolean isFree(Vector2D teammateRP, Iterable<Vector2D> opponentsRP){
		for(Vector2D oRP : opponentsRP){
			if(!isFree(teammateRP,oRP)){
				return false;
			}
		}
		return true;
	}
	
	///////////////////////////////
	// DANGEROSITY OF AN OPPONENT	
	public double evalDangerosity(Player op){
		// The smaller the returned valued, the more dangerous the player is.
		Vector2D myGoal = new Vector2D(player.isLeftSide() ? -52.5d : 52.5d,0);
		return 2*op.distanceTo(myGoal)+op.distanceTo(fullstateInfo.getBall());
	}
	
	public ArrayList<Player> rankDangerousOp(){
		ArrayList<Player> opp = new ArrayList<Player>(fullstateInfo.getOpponents(player));
		Collections.sort(opp, new OpponentComparator());
		Collections.reverse(opp);
		return opp;
	}

	public class OpponentComparator implements Comparator<Player>{
		public int compare(Player o1, Player o2) {
			return (int) Math.signum(evalDangerosity(o2)-evalDangerosity(o1));
		}		
	}
	
	
	///////////////////////////////////
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

	public Vector2D getPosIni() {
		return posIni;
	}

	public void setPosIni(Vector2D posIni) {
		this.posIni = posIni;
	}

	public void engage(){
		this.robocupClient.move(this.posIni.getX(), this.posIni.getY());
	}
	
	///////////////////////////////////////
	// PASS AND SHOOT EVALUATION
	
	public double evalPass(Vector2D ballVelocityAfterKick, Vector2D teamMate, Vector2D opponent){
		Ball b = fullstateInfo.getBall();
		Vector2D stdPosTM = PassTraining.Pass.toStandard(b.getPosition(), ballVelocityAfterKick, teamMate);
		Vector2D stdPosOp = PassTraining.Pass.toStandard(b.getPosition(), ballVelocityAfterKick, opponent);
		double[] input = new double[]{ballVelocityAfterKick.polarRadius(), stdPosTM.getX(), stdPosTM.getY(), stdPosOp.getX(), stdPosOp.getY()};
		return passNetwork.eval(input)[0];
	}

	public double evalShoot(double y){
		Vector2D goaliePos = genPos2relPos(fullstateInfo.getGoalie(player.isLeftSide()).getPosition());
		Vector2D ballPos = genPos2relPos(fullstateInfo.getBall().getPosition());	
		double[] input = new double[]{goaliePos.getX(),goaliePos.getY(),ballPos.getX(),ballPos.getY(),y};
		return (shootNetwork.eval(input)[0]+1.0d)/2.0d;
	}
	
	public double evalDribble(Vector2D ballVelocityAfterKick,Vector2D opponent){
		Ball b = fullstateInfo.getBall();
		Vector2D stdPosOp = PassTraining.Pass.toStandard(b.getPosition(), ballVelocityAfterKick, opponent);
		double[] input = new double[]{ballVelocityAfterKick.polarRadius(), stdPosOp.getX(), stdPosOp.getY()};
		return (dribbleNetwork.eval(input)[0]+1.0d)/2.0d;
	}
	
	private Vector2D genPos2relPos(Vector2D genPos){
		double x = (player.isLeftSide() ? 1 : -1 )*genPos.getY();
		double y = (player.isLeftSide() ? -1 : 1 )*genPos.getX()+SoccerParams.FIELD_LENGTH/2;
		return new Vector2D(x,y);
	}
}
