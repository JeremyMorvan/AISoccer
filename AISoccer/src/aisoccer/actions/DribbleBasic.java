package aisoccer.actions;

import java.util.ArrayList;

import math.Vector2D;
import aisoccer.Brain;
import aisoccer.PlayerAction;
import aisoccer.PlayerActionType;
import aisoccer.behaviorTree.ActionTask;
import aisoccer.fullStateInfo.Player;

public class DribbleBasic extends ActionTask {

	public DribbleBasic(Brain b) {
		super(b);
	}

	final static double power = 40.0;
	
	@Override
	public boolean CheckConditions() {
		return true;		
	}

	@Override
	public void Start() {
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
		for(Player op : brain.getFullstateInfo().getOpponents(me)){
			Vector2D opRP = op.getPosition().subtract(me.getPosition());
			if(opRP.multiply(RtargetDribble)<0){
				continue;
			}
			double dist = opRP.polarRadius();
			opRP = opRP.normalize(1);
			modifier.addM(opRP.multiply(1/((1+Math.pow(dist, 2))*(1+Math.pow(opRP.directionOf(RtargetDribble)/10, 2)))));
			i++;
		}
		if(i>0){
			RtargetDribble.addM(modifier.multiply(1/i));
		}
		RtargetDribble.addM(me.getPosition());
		brain.setInterestPos(RtargetDribble);
	}

	@Override
	public void DoAction() {
		System.out.println(brain.getPlayer().toString()+" : I dribble basicly !");
        brain.doAction(new PlayerAction(PlayerActionType.KICK,power, brain.getPlayer().angleFromBody(brain.getInterestPos()), brain.getRobocupClient()));
	}

}
