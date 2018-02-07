package object;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

class BasicProjectile extends Projectile {
    BasicProjectile(Spacecraft spacecraft) {
        super(spacecraft);

        Sphere sphere = new Sphere(100);
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.RED);
        sphere.setMaterial(material);
        getChildren().add(sphere);
    }
}
