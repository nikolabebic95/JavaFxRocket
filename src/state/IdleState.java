package state;

import javafx.scene.transform.Rotate;
import object.Spacecraft;

public class IdleState extends State{

    public IdleState(Spacecraft spacecraft) {
        super(spacecraft);
        spacecraft.getTransforms().setAll(new Rotate(90, Rotate.X_AXIS));
    }

    @Override
    public void spacePressed() {
        spacecraft.setState(new LaunchState(spacecraft));
    }
}
