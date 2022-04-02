public class Point {
    private int velocity = 0;
    private CellState  state =CellState.FREE;
    public void setState(CellState newState) {
        this.state = newState;
    }

    public CellState getState(){
        return state;
    }
    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    public void clicked() {
        if(this.state==CellState.FREE)
        this.setState(CellState.OCCUPIED);
    }

    public void clear() {
        this.setState(CellState.FREE);
    }

    public int getVelocity() {
        return  this.velocity;
    }


}