package object.powerups;

import com.interactivemesh.jfx.importer.tds.TdsModelImporter;
import geometry.Vector;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import object.spacecrafts.Spacecraft;
import util.RandomUtility;

import java.util.Random;

public class Heart extends PowerUp {
    private Timeline timeline;

    public Heart() {
        position = new Vector(0, 0, 0);

        TdsModelImporter modelImporter = new TdsModelImporter();
        modelImporter.read("models/heart/Heart.3ds");
        Node[] mesh = modelImporter.getImport();
        modelImporter.close();

        Group group = new Group(mesh);

        double scale = 1;

        group.setScaleX(scale);
        group.setScaleY(scale);
        group.setScaleZ(scale);

        group.getTransforms().add(new Rotate(-90, Rotate.X_AXIS));
        group.getTransforms().add(new Rotate(90, Rotate.Y_AXIS));

        Rotate rotation = new Rotate();
        rotation.setAxis(Rotate.Y_AXIS);
        rotation.pivotXProperty().setValue(0);
        rotation.pivotYProperty().setValue(0);
        rotation.pivotZProperty().setValue(0);

        group.getTransforms().add(rotation);

        Random random = RandomUtility.getRandom();
        double duration = 1 + random.nextDouble();
        boolean direction = random.nextBoolean();

        timeline = new Timeline(new KeyFrame(Duration.seconds(0.0D), new KeyValue(rotation.angleProperty(), 0)), new KeyFrame(Duration.seconds(duration), new KeyValue(rotation.angleProperty(), direction ? 360 : -360)));
        timeline.setCycleCount(-1);
        timeline.play();

        getChildren().add(group);
    }

    public void stopTimeline() {
        timeline.stop();
    }

    @Override
    public PowerUp copy() {
        return new Heart();
    }

    @Override
    public void apply(Spacecraft spacecraft) {
        spacecraft.increaseHealth();
    }
}
