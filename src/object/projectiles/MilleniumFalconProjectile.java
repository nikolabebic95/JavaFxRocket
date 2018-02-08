package object.projectiles;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import object.spacecrafts.Spacecraft;

public class MilleniumFalconProjectile extends Projectile {

    public MilleniumFalconProjectile(Spacecraft spacecraft) {
        super(spacecraft);

        final double radius = 50;

        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(new Color(1, 0.25, 0.25, 1));

        Group root = new Group();

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
                    if (i == 0 || j == 0 || k == 0) {
                        Sphere sphere = new Sphere(radius);
                        sphere.setMaterial(material);
                        sphere.setTranslateX(i * radius / 2);
                        sphere.setTranslateY(j * radius / 2);
                        sphere.setTranslateZ(k * radius / 2);
                        root.getChildren().add(sphere);
                    }
                }
            }
        }

        root.getTransforms().add(new Rotate(Math.random() * 360, Rotate.X_AXIS));
        root.getTransforms().add(new Rotate(Math.random() * 360, Rotate.Y_AXIS));
        root.getTransforms().add(new Rotate(Math.random() * 360, Rotate.Z_AXIS));

        getChildren().add(root);
    }

    @Override
    protected double getFactor() {
        return 25;
    }

    @Override
    public double getStrength() {
        return 25;
    }
}
