public class Point {
    public static final int maxSpeed = 5;
    public static final double chanceToSlowDown = 0.25;
    private int type;
    private int velocity;
    public boolean moved;


    // TODO
    public Point(){
        this.clear();

    }


    public void speedUp(){
        if(type!=0 && velocity<maxSpeed)velocity++;
    }

    public void slowingDown(int emptySpace){
        if(type!=0){
            if(this.velocity>emptySpace){
                velocity=emptySpace;
            }
        }
    }

    public void randomSlowing(){
        if(type!=0) {
            if (Math.random() <= chanceToSlowDown && velocity > 0) {
                velocity--;
            }
        }
    }


    public void clicked() {
        // TODO
        if(type!=1){
            type=1;
        }

    }

    public void clear() {
        // TODO
        type=0;
        velocity=0;
    }

    public void moveCar(int velocity){
        type=1;
        this.velocity=velocity;
        this.moved=true;
    }

    public int getType() {
        return type;
    }

    public int getVelocity() {
        return velocity;
    }
}