package aisoccer.behaviorTree;

import aisoccer.Player;
import aisoccer.RobocupClient;
import aisoccer.ballcapture.State;

public abstract class ActionTask implements Task {

	public abstract void DoAction(RobocupClient rc,State s,Player player);

	@Override
	public boolean Call(RobocupClient rc,State s,Player player) {
		if(checkConditions(s,player)){
			DoAction(rc,s,player);
			return true;
		}
		return false;		
	}

}
