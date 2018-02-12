package game;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;
import object.spacecrafts.MilleniumFalcon;
import object.spacecrafts.Spacecraft;
import object.spacecrafts.StarDestroyer;
import object.spacecrafts.TieInterceptor;

public class ChooseShip {
    private Main main;
    private Stage primaryStage;
    private Scene scene;
    private PerspectiveCamera camera;

    private Spacecraft[] spacecrafts = new Spacecraft[3];
    private int curr = 0;

    {
        final double radius = 600;

        StarDestroyer starDestroyer = new StarDestroyer();
        starDestroyer.setTranslateX(-radius);
        initSpacecraft(starDestroyer);

        MilleniumFalcon milleniumFalcon = new MilleniumFalcon();
        milleniumFalcon.setTranslateZ(radius * Math.sqrt(3.0) / 2.0);
        milleniumFalcon.setTranslateX(radius / 2.0);
        initSpacecraft(milleniumFalcon);

        TieInterceptor tieInterceptor = new TieInterceptor();
        tieInterceptor.setTranslateZ(-radius * Math.sqrt(3.0) / 2.0);
        tieInterceptor.setTranslateX(radius / 2.0);
        initSpacecraft(tieInterceptor);

        spacecrafts[0] = starDestroyer;
        spacecrafts[1] = tieInterceptor;
        spacecrafts[2] = milleniumFalcon;
    }

    private void initSpacecraft(Spacecraft spacecraft) {
        Rotate rotation = new Rotate();
        rotation.setAxis(Rotate.Z_AXIS);
        rotation.pivotXProperty().setValue(0);
        rotation.pivotYProperty().setValue(0);
        rotation.pivotZProperty().setValue(0);

        spacecraft.getTransforms().add(rotation);

        double duration = 10;

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.0D), new KeyValue(rotation.angleProperty(), 0)), new KeyFrame(Duration.seconds(duration), new KeyValue(rotation.angleProperty(), -360)));
        timeline.setCycleCount(-1);
        timeline.play();
    }

    public void start(Main main, Stage primaryStage) {
        this.main = main;
        this.primaryStage = primaryStage;
        Group root = new Group();

        scene = new Scene(root, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT, true);
        scene.setFill(Color.BLACK);

        camera = new PerspectiveCamera(true);
        camera.setFieldOfView(60);
        camera.setNearClip(0.1);
        camera.setFarClip(250000);

        camera.getTransforms().addAll(new Rotate(90, Rotate.Y_AXIS), new Translate(0, -500, -1300), new Rotate(-30, Rotate.X_AXIS));

        scene.setCamera(camera);

        scene.setOnKeyPressed(this::onKeyPressed);

        root.getChildren().addAll(camera, spacecrafts[0], spacecrafts[1], spacecrafts[2]);

        primaryStage.setTitle("Rocket");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.show();
    }

    private void onKeyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case LEFT:
                curr += 2;
                curr %= 3;
                scene.setOnKeyPressed(null);
                RotateTransition left = new RotateTransition(Duration.seconds(1), camera);
                left.setInterpolator(Interpolator.EASE_BOTH);
                left.setOnFinished((ev) -> scene.setOnKeyPressed(this::onKeyPressed));
                left.setAxis(Rotate.Y_AXIS);
                left.setByAngle(120);
                left.setCycleCount(1);
                left.setAutoReverse(false);
                left.play();
                break;
            case RIGHT:
                curr += 1;
                curr %= 3;
                scene.setOnKeyPressed(null);
                RotateTransition right = new RotateTransition(Duration.seconds(1), camera);
                right.setInterpolator(Interpolator.EASE_BOTH);
                right.setOnFinished((ev) -> scene.setOnKeyPressed(this::onKeyPressed));
                right.setAxis(Rotate.Y_AXIS);
                right.setByAngle(-120);
                right.setCycleCount(1);
                right.setAutoReverse(false);
                right.play();
                break;
            case SPACE:
                main.startScene(primaryStage, curr);
                break;
            case ESCAPE: case Q:
                Platform.exit();
                break;
        }
    }
}
