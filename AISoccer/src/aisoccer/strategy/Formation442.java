package aisoccer.strategy;

import java.util.HashSet;

import aisoccer.Area;
import aisoccer.Brain;
import aisoccer.Vector2D;

public abstract class Formation442{

	public static void setMyAreas(int numberOfPlayers,Brain brain) {
		HashSet<Area> answer = new HashSet<Area>();
		Vector2D posIni = null;
		System.out.println(brain.getPlayer().getUniformNumber());
		if(numberOfPlayers==6){ //2-2-1
			if(brain.getPlayer().getUniformNumber()==1){//G
				System.out.println("G");
				for(int i=0;i<2;i++){
					for(int j=2;j<5;j++){
						answer.add(brain.getArea(i, j));
					}
				}
				posIni = new Vector2D(-50,0);
			}else if(brain.getPlayer().getUniformNumber()==2){//DefL
				System.out.println("DefL");
				for(int i=0;i<5;i++){
					for(int j=0;j<4;j++){
						answer.add(brain.getArea(i, j));
					}
				}
				posIni = new Vector2D(-30,-15);
			}else if(brain.getPlayer().getUniformNumber()==3){//DefR
				System.out.println("DefR");
				for(int i=0;i<5;i++){
					for(int j=3;j<7;j++){
						answer.add(brain.getArea(i, j));
					}
				}
				posIni = new Vector2D(-30,15);
			}else if(brain.getPlayer().getUniformNumber()==4){//MidL
				System.out.println("MidL");
				for(int i=3;i<8;i++){
					for(int j=0;j<4;j++){
						answer.add(brain.getArea(i, j));
					}
				}
				posIni = new Vector2D(-15,-15);
			}else if(brain.getPlayer().getUniformNumber()==5){//MidR
				System.out.println("MidR");
				for(int i=3;i<8;i++){
					for(int j=3;j<7;j++){
						answer.add(brain.getArea(i, j));
					}
				}
				posIni = new Vector2D(-15,15);
			}else if(brain.getPlayer().getUniformNumber()==6){//Off
				System.out.println("Off");
				for(int i=6;i<10;i++){
					for(int j=0;j<7;j++){
						answer.add(brain.getArea(i, j));
					}
				}
				posIni = new Vector2D(-5,0);
			}						
		}
		brain.setMyAreas(answer);
		brain.setPosIni(posIni);
	}

}
