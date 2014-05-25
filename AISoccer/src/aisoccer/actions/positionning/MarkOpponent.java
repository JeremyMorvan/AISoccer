package aisoccer.actions.positionning;

import java.util.ArrayList;

import math.Vector2D;
import aisoccer.Brain;
import aisoccer.actions.motion.GoTo;
import aisoccer.fullStateInfo.Player;

public class MarkOpponent extends GoTo {
	
	public MarkOpponent(Brain b) {
		super(b);
	}
	
	Vector2D target;

	@Override
	public boolean CheckConditions() {
		target = null;
		Player me = brain.getPlayer();	
		ArrayList<Player> opponents = brain.rankDangerousOp();
		ArrayList<Player> teamMates = brain.getFullstateInfo().getTeammates(me);
		ArrayList<Player> availableTM = new ArrayList<Player>(teamMates);
		availableTM.add(me);
		
		Vector2D oppMarkingPosition;
		double alpha = 0.9;
		Player opponent;
		Player closestTM;
		double dist=Double.POSITIVE_INFINITY;
		for(int i=0; i<opponents.size();i++){
			opponent = opponents.get(i);
			oppMarkingPosition = opponent.getPosition().multiply(alpha);
			oppMarkingPosition = oppMarkingPosition.add(brain.getFullstateInfo().getBall().getPosition().multiply(1.0-alpha));
			
			closestTM = null;
			for(Player tm : availableTM){
				if(closestTM == null || tm.distanceTo(oppMarkingPosition)<dist){
					closestTM = tm;
					dist = tm.distanceTo(oppMarkingPosition);
				}
			}
			availableTM.remove(closestTM);
			
			if(closestTM == me){
				if(me.isLeftSide() && me.getUniformNumber()==4){
					System.out.println(me.getUniformNumber()+" va marquer "+opponent.getUniformNumber());
				}
				target = oppMarkingPosition;
				return true;
			}
		}
		
		return target != null;
	}

	@Override
	public void Start() {			
		brain.setInterestPos(target);
		Player me = brain.getPlayer();	

		if(me.isLeftSide() && me.getUniformNumber()==4){
			System.out.println("Je me dirige vers "+ target.toString());
		}
	}

}
