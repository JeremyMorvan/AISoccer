package aisoccer.actions;

import aisoccer.Brain;
import aisoccer.Vector2D;

public class ShootToGoal extends ShootTo {

	public ShootToGoal(Brain b, boolean checkDistance) {
		super(b, checkDistance);
	}
	
	@Override
	public boolean CheckConditions() {
		if(checkDistance){
			if(brain.getPlayer().distanceTo(new Vector2D(brain.getPlayer().isLeftSide() ? 52.5d : -52.5d,0))>40){
				return false;
			}
		}
		return true;
	}

	@Override
	public void Start() {
		brain.setInterestPos(new Vector2D(brain.getPlayer().isLeftSide() ? 52.5d : -52.5d,0));
	}

	@Override
	public void End() {}

}
