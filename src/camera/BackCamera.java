package camera;

import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import object.SpaceObject;

public class BackCamera extends AbstractCamera {
    private SpaceObject object;
    private int horizontalPosition = -1000;
    private int verticalPosition = 100;

    public BackCamera() {
    }

    public void setObject(SpaceObject object) {
        this.object = object;
    }

    public void update() {
        this.camera.getTransforms().setAll(new Translate(this.object.getPosition().getX(), this.object.getPosition().getY(), this.object.getPosition().getZ()));
        this.camera.getTransforms().addAll(new Rotate(-90.0D, Rotate.X_AXIS));
        this.camera.getTransforms().addAll(new Rotate(-this.object.getHorizontalAngle(), Rotate.Y_AXIS));
        this.camera.getTransforms().addAll(new Translate(0.0D, (double)(-this.verticalPosition), (double)this.horizontalPosition));
    }

    public void updateHorizontalPosition(int delta) {
        this.horizontalPosition += delta;
    }

    public void updateVerticalPosition(int delta) {
        this.verticalPosition += delta;
    }
}
