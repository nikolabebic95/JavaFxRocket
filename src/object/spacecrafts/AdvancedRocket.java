package object.spacecrafts;

import com.interactivemesh.jfx.importer.tds.TdsModelImporter;
import geometry.Vector;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.transform.Rotate;
import object.spacecrafts.Spacecraft;
import state.IdleState;

public class AdvancedRocket extends Spacecraft {

    public AdvancedRocket() {
        position = new Vector(0, 0, 0);
        state = new IdleState(this);
        TdsModelImporter tdsModelImporter = new TdsModelImporter();
        tdsModelImporter.read("models/spacecraft/spacecraft.3ds");
        Node[] tdsMesh = tdsModelImporter.getImport();
        tdsModelImporter.close();

        Group group = new Group(tdsMesh);

        group.setRotationAxis(Rotate.X_AXIS);
        group.setRotate(90);

        group.setScaleX(0.25);
        group.setScaleY(0.25);
        group.setScaleZ(0.25);

        group.setTranslateZ(500);

        getChildren().add(group);
    }

    public void update(double passed) {
        this.state.update(passed, this.pitchDirection, this.rollDirection, this.accelerationDirection);
    }
}
