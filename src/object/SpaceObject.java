package object;

import geometry.Vector;
import javafx.scene.Group;

public abstract class SpaceObject extends Group {
    protected Vector position;
    protected double horizontalAngle;

    public Vector getPosition() {
        return position;
    }

    public double getHorizontalAngle() {
        return horizontalAngle;
    }

    public void setHorizontalAngle(double horizontalAngle) {
        this.horizontalAngle = horizontalAngle;
    }
}
