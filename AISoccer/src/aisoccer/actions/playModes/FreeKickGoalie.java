package aisoccer.actions.playModes;

import java.util.LinkedList;

import aisoccer.Brain;
import aisoccer.actions.Clear;
import aisoccer.actions.motion.InterceptBall;
import aisoccer.behaviorTree.Selector;
import aisoccer.behaviorTree.Task;

public class FreeKickGoalie extends Selector {

	public FreeKickGoalie(Brain b) {
		super(b);
		children = new LinkedList<Task>();
		children.add(new InterceptBall(b));
		children.add(new Clear(b));
	}
	
	@Override
	public boolean CheckConditions() {		
		String s = "goalie_catch_ball_"+(brain.getPlayer().isLeftSide() ? "l" : "r");
		String f = "goal_kick_"+(brain.getPlayer().isLeftSide() ? "l" : "r");
		return brain.getFullstateInfo().getPlayMode().equals(s)||brain.getFullstateInfo().getPlayMode().equals(f);
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
