package aisoccer.actions.positionning;

import java.util.ArrayList;

import math.Vector2D;
import aisoccer.Brain;
import aisoccer.behaviorTree.ActionTask;

public class PositionGoalie extends ActionTask {

	public PositionGoalie(Brain b) {
		super(b);
	}

	@Override
	public boolean CheckConditions() {
		return true;
	}

	@Override
	public void DoAction() {
		int nbPoints = 20;
		ArrayList<Vector2D> points = brain.generateClosePoints(2, nbPoints);
		Vector2D ballPos = brain.getFullstateInfo().getBall().getPosition();
		double scMax = -1;
		Vector2D bestPos = null;
		for(Vector2D p : points){
			double sc = brain.evalGoal(ballPos, p);
			if(sc>scMax){
				scMax = sc;
				bestPos = p;
			}
		}
		if(bestPos==null){
			System.out.println("error in positionningGoalie");
			return;
		}
		
	}

	@Override
	public void Start() {}

}
