package aisoccer.strategy;
import java.util.LinkedList;

import aisoccer.FullstateInfo;
import aisoccer.Player;
import aisoccer.RobocupClient;
import aisoccer.ballcapture.State;
import aisoccer.behaviorTree.Task;


/**
 * @author Sebastien Lentz
 *
 */
public abstract class Strategy
{
	LinkedList<Task> children;
	
	public Strategy(){}
	
    public void doAction(RobocupClient rc, FullstateInfo fsi, Player player){
    	State s = new State(fsi,player);
    	for(Task t:children){
    		if(t.Call(rc, s, player)){
    			return;
    		}
    	}
    }
}
