package aisoccer;

import java.util.HashSet;

import math.Vector2D;

public class Area {
	private double xmin;
	private double xmax;
	private double ymin;
	private double ymax;
	
	public Area(double xmin, double xmax, double ymin, double ymax) {
		this.xmin = xmin;
		this.xmax = xmax;
		this.ymin = ymin;
		this.ymax = ymax;
	}
	
	public boolean isInArea(Vector2D pos){
		if(pos.getX()<xmin||pos.getX()>xmax||pos.getY()<ymin||pos.getY()>ymax){
			return false;			
		}
		return true;
	}
	
	public Vector2D getCenter(){
		return new Vector2D((xmin+xmax)/2,(ymin+ymax)/2);
	}
	
	public HashSet<Vector2D> getInterestingPoints(){
		HashSet<Vector2D>  answer = new HashSet<Vector2D>();
		answer.add(new Vector2D((xmin+xmax)/2,(ymin+ymax)/2));
		answer.add(new Vector2D(xmin,ymin));
		answer.add(new Vector2D(xmax,ymax));
		answer.add(new Vector2D(xmin,ymax));
		answer.add(new Vector2D(xmax,ymin));
		return answer;
	}	
	
	public HashSet<Vector2D> getInterestingPoints(double xLimOffSide,int parity){
		HashSet<Vector2D>  allPoints = getInterestingPoints();
		HashSet<Vector2D>  answer = new HashSet<Vector2D>();
		for(Vector2D v2 : allPoints){
			if(parity*v2.getX()<parity*xLimOffSide){
				answer.add(v2);
			}
		}
		return answer;		
	}
}
