package object.starwars;

import com.interactivemesh.jfx.importer.tds.TdsModelImporter;
import geometry.Vector;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import object.Spacecraft;
import state.IdleState;

public class MilleniumFalcon extends Spacecraft {
    public MilleniumFalcon() {
        position = new Vector(0, 0, 0);
        state = new IdleState(this);
        TdsModelImporter modelImporter = new TdsModelImporter();
        modelImporter.read("models/millennium falcon/millenium-falcon.3DS");
        Node[] mesh = modelImporter.getImport();
        modelImporter.close();

        Group group = new Group(mesh);

        group.getTransforms().add(new Rotate(-90, Rotate.X_AXIS));
        group.getTransforms().add(new Scale(0.65, 0.65, 0.65));
        group.getTransforms().add(new Translate(85, 0, -2550));

        getChildren().add(group);
    }

    @Override
    public void update(double passed) {
        this.state.update(passed, this.pitchDirection, this.rollDirection, this.accelerationDirection);
    }
}
