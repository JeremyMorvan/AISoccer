package aisoccer.behaviorTree;

import aisoccer.Brain;
import aisoccer.Player;
import aisoccer.RobocupClient;
import aisoccer.ballcapture.State;

public abstract class ActionTask implements Task {

	public abstract void DoAction(Brain brain);

	@Override
	public boolean Call(Brain brain) {
		if(checkConditions(brain)){
			DoAction(brain);
			return true;
		}
		return false;		
	}

}
