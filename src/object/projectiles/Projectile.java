package object.projectiles;

import geometry.Vector;
import object.SpaceObject;
import object.spacecrafts.Spacecraft;

public abstract class Projectile extends SpaceObject {
    private Vector speed;
    private boolean killed = false;

    protected double getFactor() {
        return 20;
    }

    public Projectile(Spacecraft spacecraft) {
        position = spacecraft.getPosition().duplicate();
        speed = spacecraft.getSpeed().duplicate().scalarMultiply(getFactor());
        setTranslateX(position.getX());
        setTranslateY(position.getY());
        setTranslateZ(position.getZ());
    }

    public void update(double passed) {
        position.setX(position.getX() + speed.getX() * passed);
        position.setY(position.getY() + speed.getY() * passed);
        position.setZ(position.getZ() + speed.getZ() * passed);
        setTranslateX(position.getX());
        setTranslateY(position.getY());
        setTranslateZ(position.getZ());
    }

    public boolean isOutside() {
        final double OUTSIDE = 100000;
        return position.getIntensity() > OUTSIDE;
    }

    public boolean isKilled() {
        return killed;
    }

    public void setKilled() {
        killed = true;
    }

    public double getStrength() {
        return 10;
    }
}
