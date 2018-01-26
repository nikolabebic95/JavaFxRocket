package object;

import geometry.Vector;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

public class SpaceBubble extends SpaceObject {
    public SpaceBubble(double radius, Color diffuseColor) {
        this.position = new Vector(0.0D, 0.0D, 0.0D);
        Image image = new Image("resources/bump.jpg");
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(diffuseColor);
        material.setBumpMap(image);
        Sphere sphere = new Sphere(radius);
        sphere.setMaterial(material);

        for(int i = 0; i < 50; ++i) {
            Sphere sph = new Sphere(radius / 30.0D);
            sph.setMaterial(material);
            sph.getTransforms().addAll(new Rotate(360.0D * Math.random(), Rotate.X_AXIS), new Rotate(360.0D * Math.random(), Rotate.Y_AXIS));
            sph.getTransforms().add(new Translate(radius, 0.0D, 0.0D));
            this.getChildren().add(sph);
        }

        Rotate rotation = new Rotate();
        rotation.setAxis(Rotate.Z_AXIS);
        rotation.pivotXProperty().setValue(-radius - radius / 10.0D);
        rotation.pivotYProperty().setValue(0);
        rotation.pivotZProperty().setValue(0);

        for(int i = 0; i < 3; ++i) {
            Sphere freeBubble = new Sphere(radius / 15.0D);
            freeBubble.getTransforms().addAll(new Rotate(Math.random() * 360.0D, Rotate.Y_AXIS), new Translate(radius + radius / 10.0D, 0.0D, 0.0D));
            freeBubble.setMaterial(material);
            freeBubble.getTransforms().add(rotation);
            this.getChildren().addAll(freeBubble);
        }

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.0D), new KeyValue(rotation.angleProperty(), 0)), new KeyFrame(Duration.seconds(1.0D), new KeyValue(rotation.angleProperty(), 360)));
        timeline.setCycleCount(-1);
        timeline.play();
        this.getChildren().addAll(sphere);
    }
}
