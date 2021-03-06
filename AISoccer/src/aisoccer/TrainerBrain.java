package aisoccer;
import java.util.ArrayList;
import java.util.Random;

import math.MathTools;
import math.Vector2D;
import aisoccer.fullStateInfo.FullstateInfo;
import aisoccer.fullStateInfo.Player;
import aisoccer.training.scripts.DribbleTraining;
import aisoccer.training.scripts.PassTraining;
import aisoccer.training.scripts.ShootTraining;

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
	private ShootTraining 	shootTrainer;
	private DribbleTraining dribbleTrainer;
	private TrainingType 	trainingType;
	private Vector2D 	  	currentGoaliePos;
	private Vector2D 	  	currentBallPos;
	private int 		 	count;
	
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
	public TrainerBrain(TrainerClient client, int nbPlayers,TrainingType trainingType)
	{
		this.trainerClient = client;
		this.fullstateInfo = new FullstateInfo(nbPlayers);
		this.trainingType = trainingType;
		this.count = 0;
		this.currentBallPos = null;
		this.currentGoaliePos = null;
		switch(trainingType){
		case PASS :
			this.passTrainer = new PassTraining("../BiasedTrainingPassLogs.txt");
			break;
		case SHOOT :
			this.shootTrainer = new ShootTraining("../TrainingShootLogs.txt");
			break;
		case DRIBBLE : 
			this.dribbleTrainer = new DribbleTraining("../TrainingDribbleLogs.txt");
			break;
		default :
			break;
		}
		
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
			switch(trainingType){
			case PASS :
				passTraining();
				break;
			case SHOOT : 
				shootTraining();
				break;
			case DRIBBLE :
				dribbleTraining();
				break;
			default :
				break;					
			}
			
			
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
	
	private void dribbleTraining() {
		if(fullstateInfo.getPlayMode() != null&&fullstateInfo.getLeftTeam()[0].isConnected()){
			if(!fullstateInfo.getPlayMode().equals("play_on") ){
				movePlayersDribble();				
				randomDribble();								
				setPlayOn();
				return;
			}
			
			int intercepter = -1;
			int j=0;
			for(Player p : fullstateInfo.getEveryBody()){
				if(p.distanceTo(fullstateInfo.getBall())<SoccerParams.KICKABLE_MARGIN){	
					if(intercepter == -1){
						intercepter = j;
					}else{
						setTimeOver();
						return;
					}
				}
				j++;
			}
			if(intercepter != -1){
//				System.out.println("here2");
				// THERE IS AN INTERCEPTER
				dribbleTrainer.notify(intercepter);
				setTimeOver();		
			}				
		}			
	}

	private void shootTraining() {
		if(fullstateInfo.getPlayMode() != null && fullstateInfo.getGoalie(true).isConnected()){
			if(fullstateInfo.getPlayMode().equals("time_over") ){
				//System.out.println("here1");			
				randomShoot();				
				setPlayOn();
				return;			
			}
			if(fullstateInfo.getPlayMode().equals("goalie_catch_ball_l")||fullstateInfo.getPlayMode().equals("free_kick_l")||fullstateInfo.getBall().getVelocity().polarRadius()<1e-6){
				shootTrainer.notify(false);
				setTimeOver();
				return;
			}
			if(fullstateInfo.getBall().getPosition().getX()<-SoccerParams.FIELD_LENGTH/2){
				if(Math.abs(fullstateInfo.getBall().getPosition().getY())<SoccerParams.GOAL_WIDTH/2){
					//System.out.println("goal !!!!!!!!!");
					shootTrainer.notify(true);
					setTimeOver();
					return;
				}else{
					System.out.println("dommage, essaye encore !");
					setTimeOver();
					return;
				}				
			}
			if(!fullstateInfo.getPlayMode().equals("play_on") ){				
				setTimeOver();
				return;	
			}
		}
	}

	public void passTraining(){		
		if(fullstateInfo.getPlayMode() != null){
			if(!fullstateInfo.getPlayMode().equals("play_on") ){
//				System.out.println("here1");
				boolean biased = false;
				if(biased){
					Vector2D[] pos = movePlayers();				
					notSoRandomPass(pos);
				}else{
					movePlayers();				
					randomPass();
				}
								
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
						return;
					}
				}
			}
			if(intercepter != null){
//				System.out.println("here2");
				// THERE IS AN INTERCEPTER
				passTrainer.notifyInterception(intercepter);
				setTimeOver();			
			}				
		}			
	}
	
	private void randomShoot() {
		double ballDir = -1;
		switch(count){
		case 0:
			this.currentBallPos = randomBallPos();
			//System.out.println(this.currentBallPos);
			this.currentGoaliePos = randomGoaliePos(currentBallPos);
			ballDir = this.randomBallDir(0);
			count ++;
			break;
		case 1:
			ballDir = this.randomBallDir(1);
			count ++;
			break;
		case 2:
			ballDir = this.randomBallDir(2);
			count = 0;
			break;
		default :
			break;
		}
		if(currentGoaliePos!=null){
			//System.out.println("here2");
			this.sendShoot(currentGoaliePos, currentBallPos, ballDir);
			shootTrainer.rememberShoot(currentGoaliePos, currentBallPos, ballDir);
		}else{
			System.out.println("error in new shoot, the goalie may be disconnected");
		}
	}
	
	public Vector2D randomGoaliePos(Vector2D ballPos){
		if(!fullstateInfo.getLeftTeam()[0].isConnected()){
			return null;
		}else{
			Random r = new Random();
			double dir = 100;
			double ballDir = ballPos.polarAngle()*Math.PI/180;
			while(dir>Math.PI||dir<0){
				dir = r.nextGaussian()*Math.PI/4 + ballDir;
				//System.out.println("GDir :" + dir);
			}
			double dist = Math.abs(r.nextGaussian()*SoccerParams.GOAL_WIDTH/2);
			return new Vector2D(dist,dir,true);
		}
	}
	
//	public double randomBallPow(){
//		Random r = new Random();
//		double pow = -1;
//		while(pow<0){
//			pow = SoccerParams.BALL_SPEED_MAX*((3+r.nextGaussian())/4);
//			//System.out.println("pow :" + pow);
//		}
//		return pow;
//	}
	
	public double randomBallDir(int section){
		Random r = new Random();
		double a = 100;
		while(Math.abs(a)>SoccerParams.GOAL_WIDTH/6){
			a = r.nextGaussian()*SoccerParams.GOAL_WIDTH/18;
			//System.out.println("dir :" + a);
		}
		a = a+(section-1)*SoccerParams.GOAL_WIDTH/3;
		return a;
	}
	
	public Vector2D randomBallPos(){
		Random r = new Random();
		double dir = 100;
		while(dir>Math.PI||dir<0){
			dir = Math.PI*r.nextGaussian()/4+Math.PI/2;
			//System.out.println("dirPos :" + dir);
		}
		double dist = Math.random()*34+10;
		return new Vector2D(dist,dir,true);		
	}
	
	public void sendShoot(Vector2D gPos,Vector2D bPos, double bDir){
		//System.out.println("here3");
		ArrayList<Player> everybody = fullstateInfo.getEveryBody();
		if(everybody.size()>2){
			System.out.println("More than 1 player in the field");
		}else{
			trainerClient.movePlayer(everybody.get(0), relPos2genPos(gPos));
		}
		trainerClient.moveBall(relPos2genPos(bPos), new Vector2D(SoccerParams.BALL_SPEED_MAX*0.95,Math.toRadians(relPos2genPos(bPos).directionOf(new Vector2D(-SoccerParams.FIELD_LENGTH/2,-bDir))),true));		
	}
	
	public Vector2D[] movePlayers(){
		// MOVE THE PLAYERS FOR THE NEXT PASS SIMULATION
		ArrayList<Player> everybody = fullstateInfo.getEveryBody();
		Vector2D[] pos = new Vector2D[everybody.size()];
		int i=0;
		double x;
		double y;
		for(Player p : everybody){
			x = SoccerParams.FIELD_LENGTH*(Math.random()-0.5);
			y = SoccerParams.FIELD_WIDTH*(Math.random()-0.5);
			trainerClient.movePlayer(p,x,y);
			Vector2D v2 = new Vector2D(x,y);
			p.setPosition(v2);
			pos[i] = v2;
			i++;
		}
		return pos;
	}
	
	public Vector2D[] movePlayersDribble(){
		// MOVE THE PLAYERS FOR THE NEXT PASS SIMULATION
		ArrayList<Player> everybody = fullstateInfo.getEveryBody();
		Vector2D[] pos = new Vector2D[everybody.size()];
		int i=0;
		double x1 = SoccerParams.FIELD_LENGTH*(Math.random()-0.5);
		double y1 = SoccerParams.FIELD_WIDTH*(Math.random()-0.5);
		Random r = new Random();
		double x;
		double y;
		for(Player p : everybody){
			if(p.isLeftSide()&&p.getUniformNumber()==1){
				x = x1;
				y = y1;
			}else{
				x = x1+r.nextGaussian()*10;
				y = y1+r.nextGaussian()*10;
			}
			trainerClient.movePlayer(p,x,y);
			Vector2D v2 = new Vector2D(x,y);
			p.setPosition(v2);
			pos[i] = v2;
			i++;
		}
		return pos;
	}
	
	public void randomPass(){
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
			max = max*(1-SoccerParams.BALL_DECAY);
			max = Math.min(max, SoccerParams.BALL_SPEED_MAX);
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
	
	public void notSoRandomPass(Vector2D[] pos){
		Vector2D newBallP = null;
		Vector2D newBallV = null;
		
		
		// HERE : COMPUTE newBalP AND newBallV
		double x = 100;
		double y = 100;
		while(newBallP == null){
			Vector2D shooter = pos[(int)Math.floor(Math.random()*pos.length)];
			Random r = new Random();
			x = shooter.getX()+r.nextGaussian()*3;
			y = shooter.getY()+r.nextGaussian()*3;
			//System.out.println(x + "    " + y);
			if(Math.abs(x)<SoccerParams.FIELD_LENGTH/2&&Math.abs(y)<SoccerParams.FIELD_WIDTH/2){
				newBallP = new Vector2D(x,y);	
			}					
		}
		
		
		
		double min;
		double max;
		
		
		while(newBallV == null){	
//			System.out.println("I come here 2 - One time");
			Vector2D target = pos[(int)Math.floor(Math.random()*pos.length)];
//			System.out.println(target);
			double direction = MathTools.positiveAngle(Math.toRadians(newBallP.directionOf(target)));
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
			//System.out.println("direction : " + direction + "; max : " + max + "; pos : " + newBallP);
			max = max*(1-SoccerParams.BALL_DECAY);
			max = Math.min(max, SoccerParams.BALL_SPEED_MAX);
//			if(max>3){
//				System.out.println("////////////////////////////////////////////////////");
//				System.out.println("direction : " + direction + "; max : " + max + "; pos : " + newBallP);
//				System.out.println(Math.cos(direction));
//				System.out.println(Math.sin(direction));
//				System.out.println("////////////////////////////////////////////////////");
//			}
			if(max>min){
				double dist = newBallP.distanceTo(target)*(1-SoccerParams.BALL_DECAY);
				double pow = -1;
				Random r = new Random();
//				System.out.println(dist);
//				System.out.println(min);
//				System.out.println(max);
				if(dist>1.2*max||dist<min/1.2){
					dist = (max+min)/2;
				}
				while(pow>max||pow<min){
					pow = r.nextGaussian()*0.1+dist;
				}
				newBallV = new Vector2D(pow,direction,true);
			}
		}
		trainerClient.moveBall(newBallP, newBallV);
		passTrainer.rememberKick(fullstateInfo, newBallP, newBallV);
	}
	
	private void randomDribble() {
		Vector2D newBallP = null;
		Vector2D newBallV = null;
		
		// HERE : COMPUTE newBalP AND newBallV
		newBallP = fullstateInfo.getLeftTeam()[0].getPosition();
		
		double direction;
		double min;
		double max;
		
		direction = 2*Math.random()*Math.PI;			
		min = 0;
		max = 2*SoccerParams.PLAYER_SPEED_MAX;
		newBallV = new Vector2D(Math.random()*(max-min)+min,direction,true);
		
		trainerClient.moveBall(newBallP, newBallV);
		dribbleTrainer.rememberDribble(newBallP,newBallV,fullstateInfo);		
	}
	
	private Vector2D relPos2genPos(Vector2D relPos){
		return new Vector2D(relPos.getY()-SoccerParams.FIELD_LENGTH/2,-relPos.getX());
	}
}
