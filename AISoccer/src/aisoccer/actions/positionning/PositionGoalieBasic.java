package aisoccer.actions.positionning;

import math.Vector2D;
import aisoccer.Brain;
import aisoccer.actions.motion.GoTo;

public class PositionGoalieBasic extends GoTo {

	public PositionGoalieBasic(Brain b) {
		super(b);
	}

	@Override
	public void defineInterestPosition() {		
		brain.setInterestPos(new Vector2D(brain.getPlayer().isLeftSide() ? -52.5d : 52.5d,0));
	}

}
