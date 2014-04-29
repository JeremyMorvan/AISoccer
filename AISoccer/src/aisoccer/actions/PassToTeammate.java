package aisoccer.actions;

import java.util.LinkedList;

import aisoccer.Brain;
import aisoccer.Player;

public class PassToTeammate extends ShootTo {

	@Override
	public void Start(Brain brain) {
		LinkedList<Player> teammates = brain.getFullstateInfo().getTeammates(brain.getPlayer());
		
	}

}
