package object;

public abstract class ShootableObject extends SpaceObject {
    protected double health;

    protected double getMaxHealth() {
        return 100;
    }

    public ShootableObject() {
        health = getMaxHealth();
    }

    public void shoot(double dHealth) {
        health -= dHealth;
        if (health < 0) health = 0;
    }

    public boolean isKilled() {
        return health == 0;
    }

    public void reset() {
        health = getMaxHealth();
    }

    public void kill() {
        shoot(health);
    }
}
