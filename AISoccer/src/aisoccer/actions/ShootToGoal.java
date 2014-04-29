package aisoccer.actions;

import aisoccer.Brain;
import aisoccer.Vector2D;

public class ShootToGoal extends ShootTo {

	@Override
	public void Start(Brain brain) {
		brain.setInterestPos(new Vector2D(brain.getPlayer().isLeftSide() ? 52.5d : -52.5d,0));	
	}

}
