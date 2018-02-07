package object.projectiles;

import geometry.Vector;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import object.spacecrafts.Spacecraft;

public class TieInterceptorProjectile extends Projectile {

    public TieInterceptorProjectile(Spacecraft spacecraft) {
        super(spacecraft);

        final double radius = 10;
        final double height = 500;

        Cylinder topLeft = new Cylinder(radius, height);
        Cylinder topRight = new Cylinder(radius, height);
        Cylinder bottomLeft = new Cylinder(radius, height);
        Cylinder bottomRight = new Cylinder(radius, height);

        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.LIGHTGREEN);

        topLeft.setMaterial(material);
        topRight.setMaterial(material);
        bottomLeft.setMaterial(material);
        bottomRight.setMaterial(material);

        Vector angle = spacecraft.getAngle();

        final double horizontalOffset = 175;
        final double verticalOffset = 50;

        topLeft.getTransforms().addAll(new Rotate(angle.getZ(), Rotate.Z_AXIS), new Rotate(angle.getX(), Rotate.X_AXIS), new Rotate(angle.getY(), Rotate.Y_AXIS));
        topRight.getTransforms().addAll(new Rotate(angle.getZ(), Rotate.Z_AXIS), new Rotate(angle.getX(), Rotate.X_AXIS), new Rotate(angle.getY(), Rotate.Y_AXIS));
        bottomLeft.getTransforms().addAll(new Rotate(angle.getZ(), Rotate.Z_AXIS), new Rotate(angle.getX(), Rotate.X_AXIS), new Rotate(angle.getY(), Rotate.Y_AXIS));
        bottomRight.getTransforms().addAll(new Rotate(angle.getZ(), Rotate.Z_AXIS), new Rotate(angle.getX(), Rotate.X_AXIS), new Rotate(angle.getY(), Rotate.Y_AXIS));

        topLeft.getTransforms().addAll(new Translate(-horizontalOffset, 0, verticalOffset));
        topRight.getTransforms().addAll(new Translate(horizontalOffset, 0, verticalOffset));
        bottomLeft.getTransforms().addAll(new Translate(-horizontalOffset, 0, -verticalOffset));
        bottomRight.getTransforms().addAll(new Translate(horizontalOffset, 0, -verticalOffset));

        getChildren().addAll(topLeft, topRight, bottomLeft, bottomRight);
    }

    @Override
    protected double getFactor() {
        return 20;
    }

    @Override
    public double getStrength() {
        return 10;
    }
}
