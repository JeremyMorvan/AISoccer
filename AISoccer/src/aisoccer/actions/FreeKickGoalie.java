package aisoccer.actions;

import aisoccer.Brain;

public class FreeKickGoalie extends PassToTeammate {

	public FreeKickGoalie(Brain b) {
		super(b, true);
	}
	
	@Override
	public boolean CheckConditions() {		
		String s = "free_kick_"+(brain.getPlayer().isLeftSide() ? "l" : "r");
		return brain.getFullstateInfo().getPlayMode().equals(s);
	}


}
