package object.powerups;

import object.SpaceObject;
import object.spacecrafts.Spacecraft;

public abstract class PowerUp extends SpaceObject {
    private boolean killed = false;

    public void apply(Spacecraft spacecraft) {}

    public double duration() {
        return 0;
    }

    public abstract PowerUp copy();

    public void kill() {
        killed = true;
    }

    public boolean isKilled() {
        return killed;
    }
}
