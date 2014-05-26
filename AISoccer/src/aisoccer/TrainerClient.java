package aisoccer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.charset.Charset;

import aisoccer.fullStateInfo.Ball;
import aisoccer.fullStateInfo.Player;

import math.Vector2D;


/**
 * This class implements the commands of the Robocup Soccer Simulation 2D
 * interface. It contains the client-server communication functions.
 * 
 * @author Sebastien Lentz
 * 
 */
public class TrainerClient implements Runnable
{

    private final int      MSG_SIZE = 4096; // Size of the socket buffer
    final static String TEAMNAMES_PATTERN  = "\\(ok team_names (team l [a-zA-Z_]*) (team r [a-zA-Z_]*)\\)";

    private DatagramSocket socket;         // Socket to communicate with the server
    private InetAddress    host;           // Server address
    private int            port;           // Server port
    private TrainerBrain   brain;          // Actions deciding module
    private String 		   nameLeft;
    private String 	 	   nameRight;

    /*
     * =========================================================================
     * 
     *                     Constructors and destructors
     * 
     * =========================================================================
     */
    /**
     * @param host
     * @param port
     * @param teamName
     * @throws SocketException
     */
    public TrainerClient(InetAddress host, int port) throws SocketException
    {
        this.socket = new DatagramSocket();
        this.host = host;
        this.port = port;
    }

    /**
     * This destructor closes the communication socket.
     */
    public void finalize()
    {
        send("(bye)");
        socket.close();
    }

    /*
     * =========================================================================
     * 
     *                      Getters and Setters
     * 
     * =========================================================================
     */
    /**
     * @return the brain
     */
    public TrainerBrain getBrain()
    {
        return brain;
    }

    /**
     * @param brain the brain to set
     */
    public void setBrain(TrainerBrain brain)
    {
        this.brain = brain;
    }

    /*
     * =========================================================================
     * 
     *                          Socket communication
     * 
     * =========================================================================
     */
    /**
     * Send a message to the server.
     * 
     * @param message
     */
    private void send(String message)
    {
        //System.out.println("Sending: " + message);

        byte[] buffer = message.getBytes(Charset.defaultCharset());

        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, host,
            port);

        try
        {
            socket.send(packet);
        }
        catch (IOException e)
        {
            System.err.println("socket sending error " + e);
        }
    }

    /**
     * Wait for a new message from the server.
     */
    private String receive()
    {
        byte[] buffer = new byte[MSG_SIZE];
        DatagramPacket packet = new DatagramPacket(buffer, MSG_SIZE);
        try
        {
            socket.receive(packet);
        }
        catch (IOException e)
        {
            System.err.println("socket receiving error " + e);
        }
        return new String(buffer, Charset.defaultCharset());
    }

    /*
     * =========================================================================
     * 
     *                  Implementation of client commands
     * 
     * =========================================================================
     */
    
    /**
     * @param play_mode
     */
    public void changeMode(String play_mode){
    	send("(change_mode " + play_mode + ")");
    }
    
    /**
     * @param object
     * @param position
     * @param facingDirection
     * @param velocity
     */
    
    public void move(String objName, Vector2D position, double facingDirection, Vector2D velocity){
    	send("(move objName "+ position.getX()+" "+position.getY()+" "+	facingDirection+" "+
    												velocity.getX()+" "+velocity.getY()+")");
    }
    
    public void move(Player player, Vector2D position, double facingDirection, Vector2D velocity){
    	String objName = "(p " + (player.isLeftSide() ? nameLeft : nameRight) + player.getUniformNumber() + ")";
    	move(objName,position,facingDirection,velocity);
    }
    
    public void moveBall(Vector2D position, Vector2D velocity){
    	String objName = "(b)";
    	move(objName,position, 0,velocity);
    }
    
    public void getTeamNames(){
    	send("(team_names)");
    }
    
    /**
     * 
     */
    
    public void checkBall(){
    	send("(check_ball)");
    }

    /*
     * =========================================================================
     * 
     *                      Message parsing methods
     * 
     * =========================================================================
     */
    /**
     * @param message
     */
    private void parseServerMsg(String message)
    {
        // Check the kind of information first
        if (message.charAt(1) == 'f')
        { // Fullstate information
            brain.getFullstateInfo().setFullstateMsg(message);
            brain.getFullstateInfo().parse();
        }
        
        else if (message.charAt(1) == 'o'){
        	Pattern pattern = Pattern.compile(TEAMNAMES_PATTERN);
    		Matcher matcher = pattern.matcher(message);
        	if(matcher.find()){
        		nameLeft = matcher.group(1);
        		nameRight = matcher.group(2);
        	}
        }

        else if (message.charAt(1) == 'e')
            System.out.println(message);

    }

    /**
     * This function sends the init message to the server and parse its answer.
     * Once the response of the server has been parsed, the brain is initialized
     * with the given strategy.
     * 
     * (init Side Unum PlayMode)
     * 
     * Side ::= l | r
     * Unum ::= 1 ~ 11
     * PlayMode ::= one of play modes
     * 
     * @param strategy the strategy the brain will use.
     * 
     * @throws IOException if the init message could not be parsed.
     */
    protected void init(int nbPlayers) throws IOException
    {
        byte[] buffer = new byte[MSG_SIZE];
        DatagramPacket packet = new DatagramPacket(buffer, MSG_SIZE);

        // First we need to initialize the connection to the server
        send("(init (version 14))");
        socket.receive(packet);
        port = packet.getPort();

        String initMsg = new String(buffer, Charset.defaultCharset());
        final String initPattern = "\\(init ok\\)";

        Pattern pattern = Pattern.compile(initPattern);
        Matcher matcher = pattern.matcher(initMsg);

        if (matcher.find())
        {

            brain = new TrainerBrain(this,nbPlayers);
        }
        else
        {
            throw new IOException(initMsg);
        }

    }

    
    /** 
     * This methods will just keep waiting for server messages
     * to arrive on the socket then parse them.
     */
    public void run()
    {
        while (true)
        {
        	//System.out.println("thread de "+teamName+ " "+port+" est encore en vie");
            parseServerMsg(receive());
            TrainingLogs.takeFSI(brain.getFullstateInfo());
        }
    }

}
