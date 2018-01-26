package object;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

public class FuelPowerUp extends SpaceObject {
    public FuelPowerUp(double radius, Color diffuseColor) {
        position.set(0, 0, 0);
        Image image = new Image("resources/bump.jpg");
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(diffuseColor);
        material.setBumpMap(image);
        Sphere sphere = new Sphere(radius);
        sphere.setMaterial(material);
        getChildren().add(sphere);
    }
}
