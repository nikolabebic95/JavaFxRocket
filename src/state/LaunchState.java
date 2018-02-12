package state;

import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import object.spacecrafts.Spacecraft;

public class LaunchState extends State {
    private double liftOffSpeed = 200;

    private double rotateAngle = 90;

    public LaunchState(Spacecraft spacecraft) {
        super(spacecraft);
    }

    @Override
    public void update(double passed, int pitchDirection, int rollDirection, int accelerationDirection){
        double speedZ = liftOffSpeed  * rotateAngle / 90.0;
        double speedY = Math.abs(rotateAngle - 90) / 90. * 1500;
        spacecraft.getSpeed().set(0, speedY, speedZ);

        spacecraft.getPosition().setZ(spacecraft.getPosition().getZ() + liftOffSpeed * passed * rotateAngle / 90.);
        spacecraft.getPosition().setY(spacecraft.getPosition().getY() + Math.abs(rotateAngle - 90) / 90. * 1500 * passed);

        if (liftOffSpeed > 1300) {
            if (rotateAngle > 0) rotateAngle -= 30. * passed;
            if (rotateAngle < 0) rotateAngle = 0;
        }

        liftOffSpeed += liftOffSpeed / 2 * passed;

        spacecraft.getTransforms().setAll(new Translate(spacecraft.getPosition().getX(), spacecraft.getPosition().getY(), spacecraft.getPosition().getZ()));
        spacecraft.getTransforms().add(new Rotate(rotateAngle, Rotate.X_AXIS));

        if (rotateAngle == 0) {
            spacecraft.setState(new FlyState(spacecraft));
        }
    }

}
