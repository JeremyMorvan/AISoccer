package aisoccer.strategy;
import aisoccer.FullstateInfo;
import aisoccer.Player;
import aisoccer.RobocupClient;


/**
 * @author Sebastien Lentz
 *
 */
public interface Strategy
{
    public void doAction(RobocupClient rcClient, FullstateInfo fsi, Player player);
}
