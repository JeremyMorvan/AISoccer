package aisoccer.actions.playModes;

import java.util.LinkedList;

import aisoccer.Brain;
import aisoccer.actions.PassToTeammate;
import aisoccer.actions.ShootToGoal;
import aisoccer.actions.WithoutTheBall;
import aisoccer.behaviorTree.Selector;
import aisoccer.behaviorTree.Task;

public class FreeKick extends Selector {

	public FreeKick(Brain b) {
		super(b);
		children = new LinkedList<Task>();
		children.add(new WithoutTheBall(b));
		children.add(new ShootToGoal(b));
		children.add(new PassToTeammate(b,true));
	}

	@Override
	public boolean CheckConditions() {
		String pm = brain.getFullstateInfo().getPlayMode();
		boolean condition =  pm.equals("goalie_catch_ball_l") && brain.getPlayer().isLeftSide() || pm.equals("goalie_catch_ball_r") && !brain.getPlayer().isLeftSide();
		return condition;
	}

	@Override
	public void Start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void End() {
		// TODO Auto-generated method stub
		
	}



}
