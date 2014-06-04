package aisoccer.actions.playModes;

import aisoccer.Brain;
import aisoccer.actions.Clear;

public class FreeKickGoalie extends Clear {

	public FreeKickGoalie(Brain b) {
		super(b);
	}
	
	@Override
	public boolean CheckConditions() {		
		String s = "goalie_catch_ball_"+(brain.getPlayer().isLeftSide() ? "l" : "r");
		String f = "goal_kick_"+(brain.getPlayer().isLeftSide() ? "l" : "r");
		return brain.getFullstateInfo().getPlayMode().equals(s)||brain.getFullstateInfo().getPlayMode().equals(f);
	}


}
