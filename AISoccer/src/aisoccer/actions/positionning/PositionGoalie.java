package aisoccer.actions.positionning;

import java.util.ArrayList;

import math.Vector2D;
import aisoccer.Brain;
import aisoccer.SoccerParams;
import aisoccer.actions.motion.GoTo;

public class PositionGoalie extends GoTo {

	public PositionGoalie(Brain b) {
		super(b);
	}

	@Override
	public boolean CheckConditions() {
		return true;
	}

	@Override
	public void defineInterestPosition() {
		if(brain.getPlayer().getPosition().distanceTo(brain.getFullstateInfo().getBall())>40){
			brain.setInterestPos(brain.getPosIni().rotate(brain.getPlayer().isLeftSide() ? 0 : Math.PI));
			return;
		}
		int nbPoints = 50;
		ArrayList<Vector2D> points = brain.generateClosePoints(10, nbPoints);
		Vector2D ballPos = brain.getFullstateInfo().getBall().getPosition();
		double scMax = 2;
		Vector2D bestPos = null;
		for(Vector2D p : points){
			double sc = brain.evalGoal(ballPos, p);
			if(sc<scMax){
				scMax = sc;
				bestPos = p;
			}
		}
		if(bestPos==null){
			System.out.println("error in positionningGoalie");
			brain.setInterestPos(brain.getPlayer().getPosition());
			return;
		}
		brain.setInterestPos(bestPos);
		
	}

}
