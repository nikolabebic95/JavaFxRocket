package object;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import geometry.Vector;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.transform.Rotate;
import state.IdleState;

public class StarDestroyer extends Spacecraft {

    public StarDestroyer() {
        position = new Vector(0, 0, 0);
        state = new IdleState(this);
        ObjModelImporter objModelImporter = new ObjModelImporter();
        objModelImporter.read("models/star destroyer/star_destroyer.obj");
        Node[] tdsMesh = objModelImporter.getImport();
        objModelImporter.close();

        Group group = new Group(tdsMesh);

        group.getTransforms().add(new Rotate(180, Rotate.X_AXIS));
        group.getTransforms().add(new Rotate(-90, Rotate.Z_AXIS));
        group.getTransforms().add(new Rotate(90, Rotate.X_AXIS));

        getChildren().add(group);
    }

    public void update(double passed) {
        this.state.update(passed, this.pitchDirection, this.rollDirection, this.accelerationDirection);
    }
}
