package object.spacecrafts;

import geometry.Vector;
import object.SpaceObject;
import object.projectiles.BasicProjectile;
import object.projectiles.Projectile;
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

    protected int health;
    protected int numOfProjectiles;

    protected boolean invincible = false;
    protected double invincibleDuration = 0;

    protected int getMaxHealth() {
        return 9;
    }
    protected int getMaxNumOfProjectiles() {
        return 100;
    }

    protected int getProjectilesUpdate() {
        return 10;
    }

    private Vector speed = new Vector(0, 0, 0);
    private Vector angle = new Vector(0, 0, 0);

    public Spacecraft() {
        health = getMaxHealth();
        numOfProjectiles = getMaxNumOfProjectiles();
    }

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

    public double getRollAngle() {
        return state.getRollAngle();
    }

    public Vector getSpeed() {
        return speed;
    }

    public Vector getAngle() {
        return angle;
    }

    public Projectile shoot() {
        if (numOfProjectiles == 0) return null;
        numOfProjectiles--;
        return createProjectile();
    }

    protected Projectile createProjectile() {
        return new BasicProjectile(this);
    }

    public int getNumOfProjectiles() {
        return numOfProjectiles;
    }

    public void collide() {
        if (!invincible) health--;
    }

    public int getHealth() {
        return health;
    }

    public State getState() {
        return state;
    }

    public void increaseHealth() {
        if (health != getMaxHealth()) {
            health++;
        }
    }

    public void increaseNumOfProjectiles() {
        numOfProjectiles += getProjectilesUpdate();
        if (numOfProjectiles > getMaxNumOfProjectiles()) numOfProjectiles = getMaxNumOfProjectiles();
    }

    public void applyInvincibility(double duration) {
        invincibleDuration = duration;
        invincible = true;
    }

    public void updateInvincibility(double passed) {
        if (invincible) {
            invincibleDuration -= passed;
            if (invincibleDuration < 0) invincible = false;
        }
    }
}
