/**
 * 
 */
package aisoccer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

import aisoccer.ballcapture.DirectPolicySearch;
import aisoccer.strategy.TrainingStrategy;
import aisoccer.strategy.myStrategy2;


/**
 * @author Sebastien Lentz
 *
 */
public class Sebbot
{

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

        RobocupClient client;
        Brain brain;
        int nbOfPlayers = 6;

        for (int i = 0; i < nbOfPlayers; i++)
        {
            client = new RobocupClient(InetAddress.getByName(hostname), port, team);
            client.init(nbOfPlayers);

            brain = client.getBrain();
            brain.computeAreas();
            brain.setStrategy(new TrainingStrategy(brain));

            new Thread(client).start();
            new Thread(brain).start();
        }
        
        for (int i = 0; i < nbOfPlayers; i++)
        {
            client = new RobocupClient(InetAddress.getByName(hostname), port,
                "team2");
            client.init(nbOfPlayers);

            brain = client.getBrain();
            brain.computeAreas();
            brain.setStrategy(new TrainingStrategy(brain));

            new Thread(client).start();
            new Thread(brain).start();
        }
    }

    public static void dpsComputation()
    {
        DirectPolicySearch dps;
        int nbOfBFs = 12;
        for (int i = 0; i < 5; i++)
        {
            dps = new DirectPolicySearch(nbOfBFs, 1, 100);
            dps.run();
            dps = new DirectPolicySearch(nbOfBFs, 2, 100);
            dps.run();
            dps = new DirectPolicySearch(nbOfBFs, 3, 100);
            dps.run();
            nbOfBFs += 4;
        }
    }
    
    public static void loadANNIntercepted(){
    	
    }
}
