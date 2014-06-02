package aisoccer.actions;

import aisoccer.Brain;
import aisoccer.SoccerParams;

public class FreeKickGoalie extends PassToTeammate {

	public FreeKickGoalie(Brain b) {
		super(b, true);
	}
	
	@Override
	public boolean CheckConditions() {
		boolean a = super.CheckConditions();
		
		String s = "free_kick_"+(brain.getPlayer().isLeftSide() ? "l" : "r");
		
		return a && brain.getFullstateInfo().getPlayMode().equals(s);
	}


}
