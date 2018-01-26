package object;

import state.State;

public abstract class Spacecraft extends SpaceObject {
    protected double maxRollAngle = 30;
    protected double maxPitchAngle = 30;
    protected double rollSpeed = 60;
    protected double pitchSpeed = 60;
    protected double rotationFactor = 4;
    protected double maxSpeed = 3000;
    protected double minSpeed = 200;

    protected int pitchDirection;
    protected int rollDirection;
    protected int accelerationDirection;

    protected State state;

    public abstract void update(double passed);

    public final void spacePressed() {
        state.spacePressed();
    }

    public int getPitchDirection() {
        return pitchDirection;
    }

    public void setPitchDirection(int pitchDirection) {
        this.pitchDirection = pitchDirection;
    }

    public int getRollDirection() {
        return rollDirection;
    }

    public void setRollDirection(int rollDirection) {
        this.rollDirection = rollDirection;
    }

    public int getAccelerationDirection() {
        return accelerationDirection;
    }

    public void setAccelerationDirection(int accelerationDirection) {
        this.accelerationDirection = accelerationDirection;
    }

    public void setState(State state) {
        this.state = state;
    }

    public double getMaxRollAngle() {
        return maxRollAngle;
    }

    public double getMaxPitchAngle() {
        return maxPitchAngle;
    }

    public double getRollSpeed() {
        return rollSpeed;
    }

    public double getPitchSpeed() {
        return pitchSpeed;
    }

    public double getRotationFactor() {
        return rotationFactor;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public double getMinSpeed() {
        return minSpeed;
    }

}