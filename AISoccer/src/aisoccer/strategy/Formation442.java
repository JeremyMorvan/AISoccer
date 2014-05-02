package aisoccer.strategy;

import java.util.HashSet;

import aisoccer.Area;
import aisoccer.Brain;

public abstract class Formation442{

	public static void setMyAreas(int numberOfPlayers,Brain brain) {
		HashSet<Area> answer = new HashSet<Area>();
		if(numberOfPlayers==6){ //2-2-1
			if(brain.getPlayer().getUniformNumber()==1){//G
				for(int i=0;i<2;i++){
					for(int j=1;j<6;j++){
						answer.add(brain.getArea(i, j));
					}
				}
			}
			if(brain.getPlayer().getUniformNumber()==2){//DefL
				for(int i=0;i<5;i++){
					for(int j=0;j<4;j++){
						answer.add(brain.getArea(i, j));
					}
				}
			}
			if(brain.getPlayer().getUniformNumber()==3){//DefR
				for(int i=0;i<5;i++){
					for(int j=3;j<7;j++){
						answer.add(brain.getArea(i, j));
					}
				}
			}
			if(brain.getPlayer().getUniformNumber()==4){//MidL
				for(int i=3;i<8;i++){
					for(int j=0;j<4;j++){
						answer.add(brain.getArea(i, j));
					}
				}
			}
			if(brain.getPlayer().getUniformNumber()==5){//MidR
				for(int i=3;i<8;i++){
					for(int j=3;j<7;j++){
						answer.add(brain.getArea(i, j));
					}
				}
			}
			if(brain.getPlayer().getUniformNumber()==6){//Off
				for(int i=6;i<10;i++){
					for(int j=0;j<7;j++){
						answer.add(brain.getArea(i, j));
					}
				}
			}
			
		}
		brain.setMyAreas(answer);
	}

}
