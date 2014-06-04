package aisoccer.actions.playModes;

import aisoccer.Brain;
import aisoccer.actions.PassToTeammate;

public class FreeKick extends PassToTeammate {

	public FreeKick(Brain b) {
		super(b,false);
	}

	@Override
	public boolean CheckConditions() {
		String pm = brain.getFullstateInfo().getPlayMode();
		boolean condition =  pm.equals("goalie_catch_ball_l") && brain.getPlayer().isLeftSide() || pm.equals("goalie_catch_ball_r") && !brain.getPlayer().isLeftSide();
		return condition;
	}



}
