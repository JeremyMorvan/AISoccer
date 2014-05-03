package aisoccer.strategy;

import aisoccer.PlayerAction;
import aisoccer.RobocupClient;
import aisoccer.ballcapture.Action;
import aisoccer.ballcapture.State;
import aisoccer.fullStateInfo.FullstateInfo;
import aisoccer.fullStateInfo.Player;

/**
 * @author Sebastien Lentz
 *
 */
public class GoToBallAndShoot
{
    
    private aisoccer.ballcapture.Policy ballCaptureAlgorithm;
    

    /*
     * =========================================================================
     * 
     *                     Constructors and destructors
     * 
     * =========================================================================
     */
    /**
     * @param ballCaptureAlgorithm
     */
    public GoToBallAndShoot(aisoccer.ballcapture.Policy ballCaptureAlgorithm)
    {
        this.ballCaptureAlgorithm = ballCaptureAlgorithm;
    }
    
    /**
     * 
     */
    public GoToBallAndShoot()
    {
        /*this(new aisoccer.ballcapture.HandCodedPolicy());*/
    }
    
    /*
     * =========================================================================
     * 
     *                      Getters and Setters
     * 
     * =========================================================================
     */
    /**
     * @return the ballCaptureAlgorithm
     */
    public aisoccer.ballcapture.Policy getBallCaptureAlgorithm()
    {
        return ballCaptureAlgorithm;
    }

    /**
     * @param ballCaptureAlgorithm the ballCaptureAlgorithm to set
     */
    public void setBallCaptureAlgorithm(aisoccer.ballcapture.Policy ballCaptureAlgorithm)
    {
        this.ballCaptureAlgorithm = ballCaptureAlgorithm;
    }
    
    /*
     * =========================================================================
     * 
     *                      Main methods
     * 
     * =========================================================================
     */
    /* (non-Javadoc)
     * @see sebbot.strategy.Strategy#doAction(sebbot.RobocupClient, sebbot.FullstateInfo, sebbot.Player)
     */
    public void doAction(RobocupClient rcClient, FullstateInfo fsi,
                         Player player)
    {
        if (!CommonStrategies.shootToGoal(rcClient, fsi, player))
        {
            State state = new State(fsi, player);
            Action action = ballCaptureAlgorithm.chooseAction(state);

            rcClient.getBrain().getActionsQueue().addLast(new PlayerAction(action, rcClient));
        }
    }
}
