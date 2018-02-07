package state;

import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import object.Spacecraft;

public class FlyState extends State {
    private double maxRollAngle;
    private double maxPitchAngle;
    private double rollSpeed;
    private double pitchSpeed;

    private double maxSpeed;
    private double minSpeed;

    private double rollAngle;
    private double pitchAngle;
    private double yawAngle;

    private double speed;

    private double rotationFactor;

    public FlyState(Spacecraft spacecraft) {
        super(spacecraft);
        this.maxRollAngle = spacecraft.getMaxRollAngle();
        this.maxPitchAngle = spacecraft.getMaxPitchAngle();
        this.rollSpeed = spacecraft.getRollSpeed();
        this.pitchSpeed = spacecraft.getPitchSpeed();
        this.maxSpeed = spacecraft.getMaxSpeed();
        this.minSpeed = spacecraft.getMinSpeed();
        this.rotationFactor = spacecraft.getRotationFactor();
        speed = 1500;
    }

    @Override
    public void update(double passed, int pitchDirection, int rollDirection, int accelerationDirection) {
        updateSpeed(passed, accelerationDirection);
        pitchAngle = updateAngleValue(passed, pitchDirection, pitchAngle, pitchSpeed, maxPitchAngle);
        rollAngle = updateAngleValue(passed, rollDirection, rollAngle, rollSpeed, maxRollAngle);
        yawAngle = rollAngle / 3;

        double horizontalSpeed = Math.cos(Math.toRadians(pitchAngle)) * speed;
        double verticalSpeed = Math.sin(Math.toRadians(pitchAngle)) * speed;

        double speedX = Math.cos(Math.toRadians(spacecraft.getHorizontalAngle() + 90)) * horizontalSpeed * passed;
        double speedY = Math.sin(Math.toRadians(spacecraft.getHorizontalAngle() + 90)) * horizontalSpeed * passed;
        double speedZ = verticalSpeed * passed;

        spacecraft.getSpeed().set(speedX, speedY, speedZ);
        spacecraft.getPosition().add(speedX, speedY, speedZ);
        spacecraft.setHorizontalAngle(spacecraft.getHorizontalAngle() - yawAngle * rotationFactor * passed * (speed > 0 ? horizontalSpeed / speed : 1));

        spacecraft.getTransforms().setAll(new Translate(spacecraft.getPosition().getX(), spacecraft.getPosition().getY(), spacecraft.getPosition().getZ()));
        spacecraft.getTransforms().addAll(new Rotate(spacecraft.getHorizontalAngle() - yawAngle, Rotate.Z_AXIS), new Rotate(pitchAngle, Rotate.X_AXIS), new Rotate(rollAngle, Rotate.Y_AXIS));
    }

    @Override
    public double getRollAngle() {
        return rollAngle;
    }

    private void updateSpeed(double passed, int acceleration) {
        if (acceleration == 0) return;
        double delta = speed * Math.exp(-(speed / maxSpeed - 1)) * passed;
        if (acceleration == 1) {
            speed = speed == 0 ? 100 : speed;
            speed = speed + delta > maxSpeed ? maxSpeed : speed + delta;
        } else {
            speed = speed - delta < minSpeed ? minSpeed : speed - delta;
        }
    }

    private double updateAngleValue(double passed, int direction, double angle, double angleChangeSpeed, double maxAngle) {
        if (speed == 0) {
            angle = 0;
        } else if (direction == 1 || (direction == 0 && angle < 0)) {
            angle += angleChangeSpeed * passed;
            if (direction == 1 && angle > maxAngle) {
                angle = maxAngle;
            } else if (direction == 0 && angle > 0) {
                angle = 0;
            }
        } else if (direction == -1 || (direction == 0 && angle > 0)) {
            angle -= angleChangeSpeed * passed;
            if (direction == -1 && angle < -maxAngle) {
                angle = -maxAngle;
            } else if (direction == 0 && angle < 0) {
                angle = 0;
            }
        }

        if (speed / maxSpeed < 0.5) {
            if (Math.abs(angle / maxAngle) > (speed / maxSpeed) * 2.) {
                double c = angle > 0 ? 1 : -1;
                angle = c * (speed / maxSpeed) * 2. * maxAngle;
            }
        }

        return angle;
    }
}
