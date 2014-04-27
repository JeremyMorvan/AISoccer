package aisoccer.actions;

import java.util.LinkedList;

import aisoccer.Player;
import aisoccer.SoccerParams;
import aisoccer.ballcapture.State;
import aisoccer.behaviorTree.Selector;
import aisoccer.behaviorTree.Task;

public class GoToBall extends Selector {

	public GoToBall(LinkedList<Task> children) {
		super(children);
	}
	
	public GoToBall(){
		children = new LinkedList<Task>();
		children.add(new TurnToBall());
		children.add(new GoStraightAhead());
	}

	@Override
	public boolean checkConditions(State s,Player player) {
		return player.distanceTo(s.getFsi().getBall()) > SoccerParams.KICKABLE_MARGIN;
	}

	@Override
	public void Start() {
		// TODO Auto-generated method stub		
	}

	@Override
	public void End() {
		// TODO Auto-generated method stub		
	}

}
