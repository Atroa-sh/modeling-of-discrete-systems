import java.util.ArrayList;
import java.util.Random;

public class Point {

	public ArrayList<Point> neighbors;
	public static Integer []types ={0,1,2,3};
	public int type;
	public int staticField;
	public boolean isPedestrian;
	public boolean blocked =false;
	private static final Random RNG  = new Random();

	public Point() {
		type=0;
		staticField = 100000;
		neighbors= new ArrayList<Point>();
	}
	
	public void clear() {
		staticField = 100000;
		
	}

	public boolean calcStaticField() {
		int min = 10000000;
		for(int i = 0 ; i < neighbors.size() ; i++){
			if(neighbors.get(i).staticField<=min){
				min=neighbors.get(i).staticField;
			}
		}
		if(this.staticField>min+1){
			this.staticField=min+1;
			return true;
		}
		else return false;
	}
	

	public void move(){
		if(this.isPedestrian && !blocked){
			int min = 1000000;
			ArrayList<Point> moveTo = new ArrayList<>();
			Point destination;
			for(int i = 0 ; i<neighbors.size() ; i++){
				Point current = neighbors.get(i);
				if(!current.isPedestrian && current.staticField<min && current.type!=1){
					min = current.staticField;
					moveTo.clear();
					moveTo.add(current);
				}
				else if(!current.isPedestrian && current.staticField==min){
					moveTo.add(current);

				}
			}
			if(moveTo.size()>=1){
				destination=moveTo.get(RNG.nextInt(moveTo.size()));
				if(destination.type!=2 && destination.type!=1){
					destination.isPedestrian=true;
					destination.type=3;
					destination.blocked=true;
					this.isPedestrian=false;
					this.type=0;
				}
				else if(destination.type==2){
					this.isPedestrian=false;
					this.type=0;
				}
			}



		}
	}

	public void addNeighbor(Point nei) {
		neighbors.add(nei);
	}
}