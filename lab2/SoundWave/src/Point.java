import java.lang.Math;

public class Point {

	public Point nNeighbor;
	public Point wNeighbor;
	public Point eNeighbor;
	public Point sNeighbor;
	/*
	public Point neNeighbor;
	public Point nwNeighbor;
	public Point seNeighbor;
	public Point swNeighbor;
	 */
	public float nVel;
	public float eVel;
	public float wVel;
	public float sVel;
	public float pressure;
	public int type;
	public int sinInput=90;
	public static Integer []types ={0,1,2};


	public Point() {
		clear();
		type = 0;
	}

	public void clicked() {
		pressure = 1;
	}
	
	public void clear() {
		// TODO: clear velocity and pressure
		nVel=0;
		eVel=0;
		sVel=0;
		wVel=0;
		pressure=0;
	}

	public void updateSin(){
		sinInput+=90;

	}

	public void updateVelocity() {
		// TODO: velocity update
		if(type==0) {
			nVel = nVel - (nNeighbor.getPressure() - pressure);
			eVel = eVel - (eNeighbor.getPressure() - pressure);
			sVel = sVel - (sNeighbor.getPressure() - pressure);
			wVel = wVel - (wNeighbor.getPressure() - pressure);
		}
	}


	public void updatePresure() {
		// TODO: pressure update
		if(type==0)pressure = pressure - ((float) 0.5)*(nVel + eVel + sVel + wVel);
		else if(type==2){
			double radians = Math.toRadians(sinInput);
			pressure = (float) (Math.sin(radians));
		}
	}

	public float getPressure() {
		return pressure;
	}
}