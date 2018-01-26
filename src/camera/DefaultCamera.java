package camera;

import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class DefaultCamera extends AbstractCamera {

    private double yawAngle;
    private double tiltAngle = -45;

    public DefaultCamera() {
        camera.getTransforms().addAll(new Rotate(-90, Rotate.Y_AXIS), new Translate(0, -10000, -10000), new Rotate(-45, Rotate.X_AXIS));
    }

    public void updatePosition(double deltaX, double deltaY) {
        tiltAngle -= deltaY / 5;
        yawAngle += deltaX / 5;
    }

    @Override
    public void update() {
        camera.getTransforms().setAll(new Rotate(-90, Rotate.X_AXIS));
        camera.getTransforms().addAll(new Rotate(-90, Rotate.Y_AXIS), new Translate(0, -10000, -10000), new Rotate(yawAngle, Rotate.Y_AXIS), new Rotate(tiltAngle, Rotate.X_AXIS));
    }
}
