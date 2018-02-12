package game;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.scene.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;
import object.powerups.Heart;
import object.spacecrafts.MilleniumFalcon;
import object.spacecrafts.Spacecraft;
import object.spacecrafts.StarDestroyer;
import object.spacecrafts.TieInterceptor;

import java.util.ArrayList;

public class ChooseShip {
    private Main main;
    private Stage primaryStage;
    private Scene scene;
    private PerspectiveCamera camera;

    private ArrayList<Heart> hearts = new ArrayList<>();
    private Text leftText = new Text();
    private Text rightText = new Text();

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

    private SubScene createHealthHeadUp() {
        Group root = new Group();
        SubScene healthHeadUp = new SubScene(root, Main.WINDOW_WIDTH, 100, true, SceneAntialiasing.BALANCED);

        Rectangle rectangle = new Rectangle(Main.WINDOW_WIDTH, 100);
        rectangle.setFill(Color.LIGHTGREEN);
        rectangle.setArcHeight(46.0D);
        rectangle.setArcWidth(60.0D);
        rectangle.setOpacity(0.2D);

        root.getChildren().addAll(rectangle);

        for (int i = 0; i < spacecrafts[0].getHealth(); i++) {
            Heart heart = new Heart();
            heart.stopTimeline();
            double scale = 0.02;
            heart.setScaleX(scale);
            heart.setScaleY(scale);
            heart.setScaleZ(scale);
            heart.getTransforms().add(new Translate(2500 + i * 5000, 2500, -2500));
            heart.getTransforms().add(new Rotate(90, Rotate.X_AXIS));
            root.getChildren().add(heart);
            hearts.add(heart);
        }

        return healthHeadUp;
    }

    private SubScene createMainScene() {
        Group root = new Group();
        SubScene ret = new SubScene(root, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT, true, SceneAntialiasing.BALANCED);
        ret.setFill(Color.BLACK);

        camera = new PerspectiveCamera(true);
        camera.setFieldOfView(60);
        camera.setNearClip(0.1);
        camera.setFarClip(250000);

        camera.getTransforms().addAll(new Rotate(90, Rotate.Y_AXIS), new Translate(0, -500, -1300), new Rotate(-30, Rotate.X_AXIS));

        ret.setCamera(camera);

        root.getChildren().addAll(camera, spacecrafts[0], spacecrafts[1], spacecrafts[2]);

        return ret;
    }

    private SubScene createLeftHeadUp() {
        Group root = new Group();
        final double width = 200;
        final double height = 100;
        SubScene ret = new SubScene(root, width, height, true, SceneAntialiasing.BALANCED);
        ret.setTranslateY(Main.WINDOW_HEIGHT - height);
        ret.setVisible(true);

        Rectangle rectangle = new Rectangle(width, height);
        rectangle.setFill(Color.LIGHTGREEN);
        rectangle.setArcHeight(46.0D);
        rectangle.setArcWidth(60.0D);
        rectangle.setOpacity(0.2D);

        leftText.setText("Speed: 3000\nPitch angle: 30\nRoll angle: 30");
        leftText.setFont(Font.font("Verdana", 16));
        leftText.setStroke(Color.RED);
        leftText.setTranslateX(20);
        leftText.setTranslateY(20);

        root.getChildren().addAll(rectangle, leftText);

        return ret;
    }

    private SubScene createRightHeadUp() {
        Group root = new Group();
        final double width = 200;
        final double height = 100;
        SubScene ret = new SubScene(root, width, height, true, SceneAntialiasing.BALANCED);
        ret.setTranslateX(Main.WINDOW_WIDTH - width);
        ret.setTranslateY(Main.WINDOW_HEIGHT - height);
        ret.setVisible(true);

        Rectangle rectangle = new Rectangle(width, height);
        rectangle.setFill(Color.LIGHTGREEN);
        rectangle.setArcHeight(46.0D);
        rectangle.setArcWidth(60.0D);
        rectangle.setOpacity(0.2D);

        rightText.setText("Firepower: 100%\nPitch speed: 25\nRoll speed: 25");
        rightText.setFont(Font.font("Verdana", 16));
        rightText.setStroke(Color.RED);
        rightText.setTranslateX(20);
        rightText.setTranslateY(20);

        root.getChildren().addAll(rectangle, rightText);

        return ret;
    }

    public void start(Main main, Stage primaryStage) {
        this.main = main;
        this.primaryStage = primaryStage;
        Group root = new Group();

        scene = new Scene(root, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT, true);

        SubScene mainScene = createMainScene();
        SubScene health = createHealthHeadUp();
        SubScene left = createLeftHeadUp();
        SubScene right = createRightHeadUp();

        root.getChildren().addAll(mainScene, health, left, right);

        scene.setOnKeyPressed(this::onKeyPressed);

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
                left.setOnFinished((ev) -> {
                    scene.setOnKeyPressed(this::onKeyPressed);
                    refreshHearts();
                    refreshTexts();
                });
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
                right.setOnFinished((ev) -> {
                    scene.setOnKeyPressed(this::onKeyPressed);
                    refreshHearts();
                    refreshTexts();
                });
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

    private void refreshHearts() {
        for (Heart heart : hearts) {
            heart.setVisible(false);
        }

        for (int i = 0; i < spacecrafts[curr].getHealth(); i++) {
            hearts.get(i).setVisible(true);
        }
    }

    private void refreshTexts() {
        switch (curr) {
            case 0:
                leftText.setText("Speed: 3000\nPitch angle: 30\nRoll angle: 30");
                rightText.setText("Firepower: 100%\nPitch speed: 25\nRoll speed: 25");
                break;
            case 1:
                leftText.setText("Speed: 4500\nPitch angle: 45\nRoll angle: 60");
                rightText.setText("Firepower: 10%\nPitch speed: 100\nRoll speed: 100");
                break;
            case 2:
                leftText.setText("Speed: 5000\nPitch angle: 45\nRoll angle: 45");
                rightText.setText("Firepower: 25%\nPitch speed: 60\nRoll speed: 60");
                break;
        }
    }
}
