package state;

import object.Spacecraft;

public abstract class State {
    protected Spacecraft spacecraft;

    public State(Spacecraft spacecraft) {
        this.spacecraft = spacecraft;
    }

    public void update(double passed, int pitchDirection, int rollDirection, int accelerationDirection){}
    public void spacePressed(){}

    public double getRollAngle() {
        return 0;
    }
}
