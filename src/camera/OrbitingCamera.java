package camera;

import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import object.SpaceObject;

public class OrbitingCamera extends AbstractCamera {
    private SpaceObject object;
    private double phi = 45.0D;
    private double theta = 45.0D;
    private double r = 500.0D;

    public OrbitingCamera() {
    }

    public void setObject(SpaceObject object) {
        this.object = object;
    }

    public void update() {
        this.camera.getTransforms().setAll(new Translate(this.object.getPosition().getX(), this.object.getPosition().getY(), this.object.getPosition().getZ()));
        this.camera.getTransforms().addAll(new Rotate(this.phi, Rotate.Z_AXIS));
        this.camera.getTransforms().addAll(new Rotate(-(180.0D - this.theta), Rotate.X_AXIS));
        this.camera.getTransforms().addAll(new Translate(0.0D, 0.0D, -this.r));
    }

    public void updatePosition(double deltaX, double deltaY) {
        this.phi += -deltaX / 5.0D;
        this.theta += deltaY / 5.0D;
        if (this.theta > 180.0D) {
            this.theta = 180.0D;
        } else if (this.theta < 0.0D) {
            this.theta = 0.0D;
        }
    }

    public void updateDistance(double delta) {
        this.r += delta * 8.0D;
        if (this.r < 500.0D) {
            this.r = 500.0D;
        }

        if (this.r > 20000.0D) {
            this.r = 20000.0D;
        }
    }
}
