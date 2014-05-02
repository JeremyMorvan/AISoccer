package aisoccer;

import java.lang.Math;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.LinkedList;

import aisoccer.ballcapture.Action;
import aisoccer.ballcapture.State;
import aisoccer.strategy.Strategy;

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
    private Vector2D				 interestPos;
    private Area[]					 allAreas;
    private HashSet<Area>			 myAreas;

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
    		for(int i=9;i>-1;i--){
        		for(int j=9;j>-1;j--){
        			allAreas[j+7*i] = new Area(xmin+i*stepX,xmin+(i+1)*stepX,ymin+j*stepY,ymin+(j+1)*stepY);
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
    
    public void doAction(PlayerAction action){
        this.actionsQueue.addLast(action);
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
            if (currentTimeStep == lastTimeStep + 1)
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
	
	public State getState(){
		return this.state;
	}
	
	public void setInterestPos(Vector2D pos){
		this.interestPos = pos;
	}
	
	public Vector2D getInterestPos(){
		return this.interestPos;
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

	
	public boolean checkMarked(Vector2D teammateRP, Vector2D opponentRP){
		if(teammateRP.multiply(opponentRP)<0){
			return true;
		}
		double angle = Math.toRadians(teammateRP.directionOf(opponentRP));
		if(Math.abs(Math.tan(angle))>SoccerParams.PLAYER_SPEED_MAX/SoccerParams.BALL_SPEED_MAX){
			return true;
		}
		double norm = opponentRP.polarRadius();
		if(norm*(Math.cos(angle)+Math.sin(angle))>teammateRP.polarRadius()){
			return true;
		}
		return false;		
	}
	
	public boolean checkMarked(Vector2D teammateRP, LinkedList<Vector2D> opponentsRP){
		for(Vector2D oRP : opponentsRP){
			if(teammateRP.polarRadius()>40){
				return false;
			}
			if(!checkMarked(teammateRP,oRP)){
				return false;
			}
		}
		return true;
	}
}
