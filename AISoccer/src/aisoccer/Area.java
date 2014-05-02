package aisoccer;

import java.util.LinkedList;

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
	
	public LinkedList<Vector2D> getInterestingPoints(){
		LinkedList<Vector2D>  answer = new LinkedList<Vector2D>();
		answer.add(new Vector2D((xmin+xmax)/2,(ymin+ymax)/2));
		answer.add(new Vector2D(xmin,ymin));
		answer.add(new Vector2D(xmax,ymax));
		answer.add(new Vector2D(xmin,ymax));
		answer.add(new Vector2D(xmax,ymin));
		return answer;
	}	
}
