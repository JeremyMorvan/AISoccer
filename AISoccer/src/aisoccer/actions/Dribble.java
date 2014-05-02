package aisoccer.actions;

import java.util.LinkedList;

import aisoccer.Brain;
import aisoccer.Player;
import aisoccer.PlayerAction;
import aisoccer.PlayerActionType;
import aisoccer.Vector2D;
import aisoccer.behaviorTree.ActionTask;

public class Dribble extends ActionTask {

	final static double power = 40.0;
	
	@Override
	public boolean checkConditions(Brain brain) {
		return true;
	}

	@Override
	public void DoAction(Brain brain) {
		System.out.println(" I am moving with the ball ! : " + brain.getFullstateInfo().getPlayMode());
        brain.doAction(new PlayerAction(PlayerActionType.KICK,power, brain.getPlayer().angleFromBody(brain.getInterestPos()), brain.getRobocupClient()));
	}

	@Override
	public void Start(Brain brain) {
		Player me = brain.getPlayer();
		Vector2D RtargetDribble;
		if(me.isLeftSide()){
			if(me.getPosition().getX()<22.5){
				RtargetDribble = (new Vector2D(22.5,me.getPosition().getY())).subtract(me.getPosition());
			}else{
				RtargetDribble = (new Vector2D(52.5,0)).subtract(me.getPosition());
			}
		}else{
			if(me.getPosition().getX()>-22.5){
				RtargetDribble = (new Vector2D(-22.5,me.getPosition().getY())).subtract(me.getPosition());
			}else{
				RtargetDribble = (new Vector2D(-52.5,0)).subtract(me.getPosition());
			}
		}
		RtargetDribble = RtargetDribble.normalize(1);
		Vector2D modifier = new Vector2D(0,0);
		int i = 0;
		LinkedList<Player> opponents = brain.getFullstateInfo().getOpponents(me);
		for(Player op : opponents){
			Vector2D opRP = op.getPosition().subtract(me.getPosition());
			if(opRP.multiply(RtargetDribble)<0){
				continue;
			}
			double dist = opRP.polarRadius();
			opRP = opRP.normalize(1);
			modifier = modifier.add(opRP.multiply(1/((1+Math.pow(dist, 2))*(1+Math.pow(opRP.directionOf(RtargetDribble)/10, 2)))));
			i++;
		}
		if(i>0){
			RtargetDribble.add(modifier.multiply(1/i));
		}
		brain.setInterestPos(RtargetDribble.add(me.getPosition()));
	}

}
