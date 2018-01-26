package object;

import javafx.scene.shape.*;
import javafx.scene.transform.Translate;

public class LaunchPad extends SpaceObject {

    public LaunchPad() {

      Box platform = new Box(1600, 1600, 50);
      platform.setTranslateY(-25);
      this.getChildren().add(platform);

        this.getTransforms().add(new Translate(0, 0, -200));
    }

}
