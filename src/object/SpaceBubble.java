package object;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

public class SpaceBubble extends SpaceObject {

    public SpaceBubble(double radius, Color diffuseColor) {
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(diffuseColor);

        Sphere sphere = new Sphere(radius);
        sphere.setMaterial(material);

        this.getChildren().addAll(sphere);

    }

}
