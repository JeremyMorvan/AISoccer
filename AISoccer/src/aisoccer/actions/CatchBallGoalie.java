package aisoccer.actions;

import math.Vector2D;
import aisoccer.Brain;
import aisoccer.SoccerParams;

public class CatchBallGoalie extends CatchGoalie {

	public CatchBallGoalie(Brain b) {
		super(b);
	}
	
	@Override
	public boolean CheckConditions() {
		Vector2D myGoal = new Vector2D(brain.getPlayer().isLeftSide() ? -52.5d : 52.5d,0);
		Vector2D pos = brain.getFullstateInfo().getBall().getPosition();

		
		boolean inBox = Math.abs(pos.getX()-myGoal.getX())<SoccerParams.PENALTY_AREA_L
				&& Math.abs(pos.getY())<SoccerParams.PENALTY_AREA_W/2;
		
//		if(brain.getPlayer().isLeftSide()){
//			System.out.println("my team has the boal : "+brain.getFullstateInfo().LeftGotBall());
//		}
		if(!inBox){
			return false;
		}
		
		boolean b = super.CheckConditions(); 		
		boolean ok = brain.getFullstateInfo().LeftGotBall() != brain.getPlayer().isLeftSide();		 
		 return b && ok && inBox;
//			System.out.println("Le gardien dit : ");
//			System.out.println("la balle est dans ma penalty area");
//			System.out.println("la balle est dans ma catchable area : "+b);
//			System.out.println("la balle ne vient pas de ma team : "+ok);
	}

}
