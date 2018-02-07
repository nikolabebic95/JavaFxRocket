package object.spacecrafts;

import com.interactivemesh.jfx.importer.tds.TdsModelImporter;
import geometry.Vector;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import object.spacecrafts.Spacecraft;
import state.IdleState;

public class TieInterceptor extends Spacecraft {
    public TieInterceptor() {
        position = new Vector(0, 0, 0);
        state = new IdleState(this);
        TdsModelImporter modelImporter = new TdsModelImporter();
        modelImporter.read("models/tie interceptor/tie-intercept.3DS");
        Node[] mesh = modelImporter.getImport();
        modelImporter.close();

        Group group = new Group(mesh);

        group.getTransforms().add(new Rotate(90, Rotate.X_AXIS));
        group.getTransforms().add(new Translate(1200, 0, 2500));
        group.getTransforms().add(new Scale(25, 25, 25));

        getChildren().add(group);
    }

    @Override
    public void update(double passed) {
        this.state.update(passed, this.pitchDirection, this.rollDirection, this.accelerationDirection);
    }
}
