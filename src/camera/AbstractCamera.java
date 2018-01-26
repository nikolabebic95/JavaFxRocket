package camera;

import javafx.scene.PerspectiveCamera;

public abstract class AbstractCamera {
    protected PerspectiveCamera camera;

    public AbstractCamera() {
        camera = new PerspectiveCamera(true);
        camera.setFieldOfView(60);
        camera.setNearClip(0.1);
        camera.setFarClip(250000);
    }

    public PerspectiveCamera getCamera() {
        return camera;
    }

    public abstract void update();
}
