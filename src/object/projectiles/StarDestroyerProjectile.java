package object.projectiles;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import object.spacecrafts.Spacecraft;

public class StarDestroyerProjectile extends Projectile {
    public StarDestroyerProjectile(Spacecraft spacecraft) {
        super(spacecraft);

        Sphere sphere = new Sphere(50);
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.LIGHTGREEN);
        sphere.setMaterial(material);
        getChildren().add(sphere);
    }

    @Override
    public double getStrength() {
        return 100;
    }

    @Override
    protected double getFactor() {
        return 5;
    }
}
