package aisoccer.actions;

import aisoccer.Brain;
import aisoccer.Vector2D;

public class ShootToGoal extends ShootTo {

	public ShootToGoal(boolean checkDistance) {
		super(checkDistance);
	}

	@Override
	public void Start(Brain brain) {
		brain.setInterestPos(new Vector2D(brain.getPlayer().isLeftSide() ? 52.5d : -52.5d,0));
	}
	
	public boolean checkConditions(Brain brain) {
		if(checkDistance){
			if(brain.getPlayer().distanceTo(new Vector2D(brain.getPlayer().isLeftSide() ? 52.5d : -52.5d,0))>40){
				return false;
			}
		}
		return true;
	}

}
