package aisoccer.strategy;
import java.util.LinkedList;

import aisoccer.Brain;
import aisoccer.behaviorTree.Task;


/**
 * @author Sebastien Lentz
 *
 */
public abstract class Strategy
{
	LinkedList<Task> children;
	
	public Strategy(){}
	
    public void doAction(Brain brain){
    	for(Task t:children){
    		if(t.Call(brain)){
    			return;
    		}
    	}
    }
}
