package object.powerups;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import geometry.Vector;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;
import object.spacecrafts.Spacecraft;
import util.RandomUtility;

import java.util.Random;

public class R2D2 extends PowerUp {
    public R2D2() {
        position = new Vector(0, 0, 0);

        ObjModelImporter modelImporter = new ObjModelImporter();
        modelImporter.read("models/r2d2/Star Wars Rebel r2-d2.obj");
        Node[] mesh = modelImporter.getImport();
        modelImporter.close();

        Group group = new Group(mesh);

        double scale = 10;

        group.setScaleX(scale);
        group.setScaleY(scale);
        group.setScaleZ(scale);

        group.getTransforms().add(new Rotate(-90, Rotate.X_AXIS));
        group.getTransforms().add(new Rotate(90, Rotate.Y_AXIS));

        group.getTransforms().add(new Translate(50, -100, -200));

        Rotate rotation = new Rotate();
        rotation.setAxis(Rotate.Y_AXIS);
        rotation.pivotXProperty().setValue(0);
        rotation.pivotYProperty().setValue(0);
        rotation.pivotZProperty().setValue(181);

        group.getTransforms().add(rotation);

        Random random = RandomUtility.getRandom();
        double duration = 1 + random.nextDouble();
        boolean direction = random.nextBoolean();

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.0D), new KeyValue(rotation.angleProperty(), 0)), new KeyFrame(Duration.seconds(duration), new KeyValue(rotation.angleProperty(), direction ? 360 : -360)));
        timeline.setCycleCount(-1);
        timeline.play();

        getChildren().add(group);
    }

    @Override
    public PowerUp copy() {
        return new R2D2();
    }

    @Override
    public void apply(Spacecraft spacecraft) {
        spacecraft.applyInvincibility(duration());
    }

    @Override
    public double duration() {
        return 10;
    }
}
