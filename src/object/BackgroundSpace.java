package object;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.Sphere;

public class BackgroundSpace extends Group {
    {
        Image image = new Image("resources/bulb.png");
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseMap(image);

        Sphere sphere = new Sphere(100000);
        sphere.setCullFace(CullFace.NONE);
        sphere.setMaterial(material);
        getChildren().add(sphere);
    }
}
