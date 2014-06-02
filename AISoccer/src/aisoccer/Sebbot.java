/**
 * 
 */
package aisoccer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Date;
import java.util.HashMap;

import aisoccer.training.strategies.TrainingGoalie;
import aisoccer.training.strategies.TrainingPassStrategy;
import aisoccer.strategy.myGoalieStrategy;
import aisoccer.strategy.myStrategy2;


/**
 * @author Sebastien Lentz
 *
 */
public class Sebbot
{
	static HashMap<RobocupClient,Thread> threads;
	static HashMap<RobocupClient,Long> connected;
	static TrainerClient trainerClient;
	static Thread trainerThread;
	static Long tcConnected;

    /**
     * This is the entry point of the application.
     * Launch the soccer client using command line:
     * 
     * Sebbot [-parameter value]
     * 
     * Parameters:
     * 
     * host (default "localhost"):
     * The host name can either be a machine name, such as "java.sun.com"
     * or a string representing its IP address, such as "206.26.48.100."
     *
     * port (default 6000):
     * Port number for the communication with the server
     *
     * team (default Team1):
     * Team name. This name can not contain spaces.
     *
     * 
     * @param args
     * @throws SocketException
     * @throws IOException
     */
    public static void main(String args[]) throws SocketException, IOException
    {
        startAgents(args);
    }

    public static void startAgents(String args[]) throws IOException
    {
        String hostname = "127.0.0.1";
        int port = 6000;
        int portTrainer = 6001;
        String team = "team1";
        @SuppressWarnings("unused")
		String strategy = "Default";

        try
        {
            // First look for parameters
            for (int i = 0; i < args.length; i += 2)
            {
                if (args[i].compareTo("-host") == 0)
                {
                    hostname = args[i + 1];
                }
                else if (args[i].compareTo("-port") == 0)
                {
                    port = Integer.parseInt(args[i + 1]);
                }
                else if (args[i].compareTo("-team") == 0)
                {
                    team = args[i + 1];
                }
                else if (args[i].compareTo("-strategy") == 0)
                {
                    strategy = args[i + 1];
                }
                else
                {
                    throw new InvalidArgumentException(args[i]);
                }
            }
            //initTrainingPass(hostname,port,portTrainer,team);
            initTrainingDribble(hostname,port,portTrainer,team);
            //initTrainingShoot(hostname,port,portTrainer,team);
            //initGame(hostname,port,portTrainer,team,7);
        }
        catch (InvalidArgumentException e)
        {
            System.err.println("");
            System.err.println("USAGE: Sebbot [-parameter value]");
            System.err.println("");
            System.err.println("    Parameters  value          default");
            System.err.println("   ------------------------------------");
            System.err.println("    host        host name      localhost");
            System.err.println("    port        port number    6000");
            System.err.println("    team        team name      team1");
            System.err.println("    strategy    strategy name  Default");
            System.err.println("");
            return;
        }
        
            
//        while(isGameOn()){        	    	
//            System.out.println(" threads sont encore en vie");            
//            if(!isPlayOn()){
//            	for(RobocupClient c : connected.keySet()){
//            		if(!isConnected(c)){
//            			c.reconnect(nbOfPlayers);
//            		}
//            	}            	
//            }            
//        }  
        
    }

    
    public static void notifyConnection(RobocupClient client){
    	connected.put(client, new Date().getTime());
    }
    
    public static boolean isConnected(RobocupClient c){
    	return connected.get(c)>(new Date().getTime()-200);
    }
    
    public static boolean isGameOn(){
    	for(RobocupClient client : connected.keySet()){
    		if(isConnected(client) && client.getBrain().getFullstateInfo().getTimeStep()<6000){
    			return true;
    		}
    	}
    	return false;
    }
    
    public static boolean isPlayOn(){
    	for(RobocupClient client : connected.keySet()){
    		if(isConnected(client) && client.getBrain().getFullstateInfo().equals("play_on")){
    			return true;
    		}
    	}
    	return false;
    }
    
    public static void initTrainingPass(String hostname,int port,int portTrainer,String team) throws IOException
    {   		
        RobocupClient client;
        Brain brain;
        int nbOfPlayers = 9;
        connected = new HashMap<RobocupClient, Long>();
        threads = new HashMap<RobocupClient, Thread>();
        for (int i = 0; i < nbOfPlayers; i++)
		{
		    client = new RobocupClient(InetAddress.getByName(hostname), port, team);
		    client.init(nbOfPlayers, false);

		    brain = client.getBrain();
		    brain.computeAreas();
		    brain.setStrategy(new TrainingPassStrategy(brain));
		    
		    connected.put(client, new Date().getTime());
		    Thread thread = new Thread(client);
		    threads.put(client,thread);
		    thread.start();
		    new Thread(brain).start();
		}
		
		for (int i = 0; i < nbOfPlayers; i++)
		{
		    client = new RobocupClient(InetAddress.getByName(hostname), port, "team2");
		    client.init(nbOfPlayers, false);

		    brain = client.getBrain();
		    brain.computeAreas();
		    brain.setStrategy(new TrainingPassStrategy(brain));

		    connected.put(client, new Date().getTime());
		    Thread thread = new Thread(client);
		    threads.put(client,thread);
		    thread.start();
		    new Thread(brain).start();
		}
		
		TrainerBrain trainerBrain;
		
		trainerClient = new TrainerClient(InetAddress.getByName(hostname),portTrainer);
		trainerClient.init(nbOfPlayers,TrainingType.PASS);
		
		trainerBrain = trainerClient.getBrain();
		tcConnected = new Date().getTime();
		
		trainerThread = new Thread(trainerClient);
		trainerThread.start();
		
		new Thread(trainerBrain).start();
    }
    
    public static void initTrainingDribble(String hostname,int port,int portTrainer,String team) throws IOException
    {   		
        RobocupClient client;
        Brain brain;
        int nbOfPlayers = 9;
        connected = new HashMap<RobocupClient, Long>();
        threads = new HashMap<RobocupClient, Thread>();
        for (int i = 0; i < nbOfPlayers; i++)
		{
		    client = new RobocupClient(InetAddress.getByName(hostname), port, team);
		    client.init(nbOfPlayers, false);

		    brain = client.getBrain();
		    brain.computeAreas();
		    brain.setStrategy(new TrainingPassStrategy(brain));
		    
		    connected.put(client, new Date().getTime());
		    Thread thread = new Thread(client);
		    threads.put(client,thread);
		    thread.start();
		    new Thread(brain).start();
		}
		
		for (int i = 0; i < nbOfPlayers; i++)
		{
		    client = new RobocupClient(InetAddress.getByName(hostname), port, "team2");
		    client.init(nbOfPlayers, false);

		    brain = client.getBrain();
		    brain.computeAreas();
		    brain.setStrategy(new TrainingPassStrategy(brain));

		    connected.put(client, new Date().getTime());
		    Thread thread = new Thread(client);
		    threads.put(client,thread);
		    thread.start();
		    new Thread(brain).start();
		}
		
		TrainerBrain trainerBrain;
		
		trainerClient = new TrainerClient(InetAddress.getByName(hostname),portTrainer);
		trainerClient.init(nbOfPlayers,TrainingType.DRIBBLE);
		
		trainerBrain = trainerClient.getBrain();
		tcConnected = new Date().getTime();
		
		trainerThread = new Thread(trainerClient);
		trainerThread.start();
		
		new Thread(trainerBrain).start();
    }
    
    public static void initTrainingShoot(String hostname,int port,int portTrainer,String team) throws IOException
    {   		
        RobocupClient client;
        Brain brain;
        connected = new HashMap<RobocupClient, Long>();
        threads = new HashMap<RobocupClient, Thread>();
	    client = new RobocupClient(InetAddress.getByName(hostname), port, team);
	    client.init(1, true);
	    brain = client.getBrain();
	    brain.computeAreas();
	    brain.setStrategy(new TrainingGoalie(brain));	    
	    connected.put(client, new Date().getTime());
	    Thread thread = new Thread(client);
	    threads.put(client,thread);
	    thread.start();
	    new Thread(brain).start();
		
		TrainerBrain trainerBrain;
		
		trainerClient = new TrainerClient(InetAddress.getByName(hostname),portTrainer);
		trainerClient.init(1,TrainingType.SHOOT);
		
		trainerBrain = trainerClient.getBrain();
		tcConnected = new Date().getTime();
		
		trainerThread = new Thread(trainerClient);
		trainerThread.start();
		
		new Thread(trainerBrain).start();
    }
    
    public static void initGame(String hostname,int port,int portTrainer,String team,int nbOfPlayers) throws IOException
    {   		
    	RobocupClient client;
        Brain brain;
        connected = new HashMap<RobocupClient, Long>();
        threads = new HashMap<RobocupClient, Thread>();
        for (int i = 0; i < nbOfPlayers; i++)
		{
		    client = new RobocupClient(InetAddress.getByName(hostname), port, team);
		    client.init(nbOfPlayers, i==0);

		    brain = client.getBrain();
		    brain.computeAreas();
		    if(i==0){
		    	brain.setStrategy(new myGoalieStrategy(nbOfPlayers, brain));
		    }else{
		    	brain.setStrategy(new myStrategy2(nbOfPlayers, brain));
		    }		    
		    connected.put(client, new Date().getTime());
		    Thread thread = new Thread(client);
		    threads.put(client,thread);
		    thread.start();
		    new Thread(brain).start();
		}
		
		for (int i = 0; i < nbOfPlayers; i++)
		{
		    client = new RobocupClient(InetAddress.getByName(hostname), port, "team2");
		    client.init(nbOfPlayers, i==0);

		    brain = client.getBrain();
		    brain.computeAreas();
		    if(i==0){
		    	brain.setStrategy(new myGoalieStrategy(nbOfPlayers, brain));
		    }else{
		    	brain.setStrategy(new myStrategy2(nbOfPlayers, brain));
		    }
		    connected.put(client, new Date().getTime());
		    Thread thread = new Thread(client);
		    threads.put(client,thread);
		    thread.start();
		    new Thread(brain).start();
		}
    }
}
