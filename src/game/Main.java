package game;

import camera.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import object.*;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.input.MouseEvent;

public class Main extends Application {
    private static final int WINDOW_HEIGHT = 700;
    private static final int WINDOW_WIDTH = 900;

    private Group mainSceneRoot;

    private SubScene mainSubscene;
    private SubScene headUpDisplayScene;

    private Spacecraft spacecraft;
    private LaunchPad launchPad;
    private List<SpaceObject> bubbles = new ArrayList<>();
    private Text coordinates;
    private Text bubblesHitText;
    private int bubblesHit;

    private Circle dot;
    private Polygon arrow;

    private UpdateTimer timer = new UpdateTimer();

    private DefaultCamera defaultCamera = new DefaultCamera();
    private BackCamera backCamera = new BackCamera();
    private OrbitingCamera orbitingCamera = new OrbitingCamera();
    private AbstractCamera camera;

    private double mouseXLeft;
    private double mouseYLeft;
    private double mouseXRight;
    private double mouseYRight;

    private class UpdateTimer extends AnimationTimer {
        private long previous = 0;
        @Override
        public void handle(long now) {
            if (previous == 0)
                previous = now;
            float passed = (now - previous) / 1e9f;
            spacecraft.update(passed);
            refreshHeadUp();
            camera.update();
            checkForCollisions();
            previous = now;
        }
    }

    private void refreshHeadUp() {
        String stringBuilder = "x - " + String.format("%.0f", this.spacecraft.getPosition().getX()) + "\n" +
                "y - " + String.format("%.0f", this.spacecraft.getPosition().getY()) + "\n" +
                "z - " + String.format("%.0f", this.spacecraft.getPosition().getZ()) + "\n";
        this.coordinates.setText(stringBuilder);
        this.bubblesHitText.setText(this.bubblesHit + "");
        this.arrow.setRotate(-this.spacecraft.getHorizontalAngle());
        double offsetX = this.spacecraft.getPosition().getX() / 3000.0D;
        double offsetY = this.spacecraft.getPosition().getY() / 3000.0D;
        this.dot.setTranslateX(150.0D + offsetX);
        this.dot.setTranslateY(116.0D - offsetY);
        if (offsetX <= 75.0D && offsetY <= 75.0D) {
            this.dot.setVisible(true);
        } else {
            this.dot.setVisible(false);
        }
    }

    private void checkForCollisions() {
        for (SpaceObject spaceObject : bubbles) {
            if (spaceObject.getBoundsInParent().intersects(spacecraft.getBoundsInParent())) {
                positionObject(spaceObject);
                bubblesHit++;
            }
        }
    }

    private void drawBubble() {
        SpaceBubble bubble = new SpaceBubble(450, Color.LIGHTGREEN);
        positionObject(bubble);
        bubbles.add(bubble);
        mainSceneRoot.getChildren().add(bubble);
    }

    private void positionObject(SpaceObject object) {
        double x = (Math.random() < 0.5 ? 1f : -1f) * (Math.random() * 20000. + 10000);
        double y = (Math.random() < 0.5 ? 1f : -1f) * (Math.random() * 20000. + 10000);
        double z = (Math.random() < 0.5 ? 1f : -1f) * (Math.random() * 20000. + 10000);
        object.setTranslateX(x);
        object.setTranslateY(y);
        object.setTranslateZ(z);
    }

    private void setUpSpaceObjects() {
        for (int i = 0; i < 12; i++) {
            drawBubble();
        }
    }

    private void createHeadUpDisplayScene() {
        Group headUpDisplayRoot = new Group();
        this.headUpDisplayScene = new SubScene(headUpDisplayRoot, 300.0D, 233.0D, true, SceneAntialiasing.BALANCED);
        this.headUpDisplayScene.setTranslateX(600.0D);
        this.headUpDisplayScene.setTranslateY(467.0D);
        this.headUpDisplayScene.setVisible(true);
        Rectangle rec = new Rectangle(300.0D, 233.0D);
        rec.setFill(Color.LIGHTGREEN);
        rec.setArcHeight(46.0D);
        rec.setArcWidth(60.0D);
        rec.setOpacity(0.2D);
        headUpDisplayRoot.getChildren().add(rec);
        Font font = new Font("Verdana", 10.0D);
        this.coordinates = new Text();
        this.coordinates.setText(this.coordinates.getText() + this.spacecraft.getPosition().getX() + "\n");
        this.coordinates.setText(this.coordinates.getText() + this.spacecraft.getPosition().getY() + "\n");
        this.coordinates.setText(this.coordinates.getText() + this.spacecraft.getPosition().getZ() + "\n");
        this.coordinates.setFill(Color.RED);
        this.coordinates.setFont(font);
        this.coordinates.setTranslateX(10.0D);
        this.coordinates.setTranslateY(187.0D);
        this.bubblesHitText = new Text();
        this.bubblesHitText.setText(this.bubblesHit + "");
        this.bubblesHitText.setFill(Color.RED);
        this.bubblesHitText.setFont(font);
        this.bubblesHitText.setTranslateX(300.0D - this.bubblesHitText.getLayoutX() - 20.0D);
        this.bubblesHitText.setTranslateY(187.0D);
        headUpDisplayRoot.getChildren().addAll(this.coordinates, this.bubblesHitText);

        for(int i = 0; i < 3; ++i) {
            Circle c = new Circle((double)(i * 300 / 8));
            c.setTranslateX(150.0D);
            c.setTranslateY(116.0D);
            c.setFill(null);
            c.setStroke(Color.WHITE);
            c.setStrokeWidth(2.0D);
            headUpDisplayRoot.getChildren().add(c);
        }

        this.dot = new Circle(5.0D);
        this.dot.setTranslateX(150.0D);
        this.dot.setTranslateY(116.0D);
        this.dot.setFill(Color.RED);
        headUpDisplayRoot.getChildren().add(this.dot);
        Circle compassCircle = new Circle(33.0D);
        compassCircle.setFill(null);
        compassCircle.setStroke(Color.RED);
        compassCircle.setStrokeWidth(3.0D);
        compassCircle.setTranslateY(compassCircle.getRadius() + compassCircle.getRadius() / 6.0D);
        compassCircle.setTranslateX(300.0D - compassCircle.getRadius() - compassCircle.getRadius() / 6.0D);
        headUpDisplayRoot.getChildren().add(compassCircle);
        this.arrow = new Polygon(0.0D, -33.0D, -3.0D, 33.0D, 3.0D, 33.0D);
        this.arrow.setTranslateY(compassCircle.getRadius() + compassCircle.getRadius() / 6.0D);
        this.arrow.setTranslateX(300.0D - compassCircle.getRadius() - compassCircle.getRadius() / 6.0D);
        this.arrow.setFill(Color.WHITE);
        headUpDisplayRoot.getChildren().add(this.arrow);
    }

    private void createMainScene() {
        mainSceneRoot = new Group();
        mainSubscene = new SubScene(mainSceneRoot, WINDOW_WIDTH, WINDOW_HEIGHT, true, SceneAntialiasing.BALANCED);
        mainSubscene.setFill(Color.BLACK);
        spacecraft = new StarDestroyer(); // TODO: Choose rocket
        launchPad = new LaunchPad();
        mainSceneRoot.getChildren().addAll(launchPad, spacecraft);
        setUpSpaceObjects();
        instantiateCameras();
        mainSubscene.setCamera(camera.getCamera());
    }

    private void instantiateCameras() {
        camera = defaultCamera;
        backCamera.setObject(spacecraft);
        orbitingCamera.setObject(spacecraft);
    }

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        createMainScene();
        createHeadUpDisplayScene();
        root.getChildren().addAll(mainSubscene, headUpDisplayScene);

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT, true);
        scene.setOnKeyPressed(this::onKeyPressed);
        scene.setOnKeyReleased(this::onKeyReleased);
        scene.setOnMousePressed(this::onMousePressed);
        scene.setOnMouseDragged(this::onMouseDragged);
        primaryStage.setTitle("Rocket");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.show();
        timer.start();
    }

    private void onKeyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case UP :
                spacecraft.setPitchDirection(-1);
                break;
            case DOWN :
                spacecraft.setPitchDirection(1);
                break;
            case LEFT :
                spacecraft.setRollDirection(-1);
                break;
            case RIGHT :
                spacecraft.setRollDirection(1);
                break;
            case C:
                spacecraft.setAccelerationDirection(1);
                break;
            case X:
                spacecraft.setAccelerationDirection(-1);
                break;
            case DIGIT1:
                camera = defaultCamera;
                mainSubscene.setCamera(camera.getCamera());
                break;
            case DIGIT2:
                camera = backCamera;
                mainSubscene.setCamera(camera.getCamera());
                break;
            case DIGIT3:
                camera = orbitingCamera;
                mainSubscene.setCamera(camera.getCamera());
                break;
            case SPACE:
                spacecraft.spacePressed();
                break;
            case ADD:
                if (camera == backCamera) {
                    backCamera.updateHorizontalPosition(-20);
                }
                break;
            case SUBTRACT:
                if (camera == backCamera) {
                    backCamera.updateHorizontalPosition(20);
                }
                break;
            case PAGE_UP:
                if (camera == backCamera) {
                    backCamera.updateVerticalPosition(20);
                }
                break;
            case PAGE_DOWN:
                if (camera == backCamera) {
                    backCamera.updateVerticalPosition(-20);
                }
                break;
            case L:
                PointLight[] lights = this.launchPad.getPointLights();
                if (this.mainSceneRoot.getChildren().contains(lights[0])) {
                    this.mainSceneRoot.getChildren().removeAll(lights);
                } else {
                    this.mainSceneRoot.getChildren().addAll(lights);
                }

                this.launchPad.switchLights();
                break;
            case H:
                if (this.headUpDisplayScene.isVisible()) {
                    this.headUpDisplayScene.setVisible(false);
                } else {
                    this.headUpDisplayScene.setVisible(true);
                }
                break;
            case Q: case ESCAPE:
                Platform.exit();
        }
    }

    private void onKeyReleased(KeyEvent e) {
        switch (e.getCode()) {
            case UP :
                if (spacecraft.getPitchDirection() == -1) spacecraft.setPitchDirection(0);
                break;
            case DOWN :
                if (spacecraft.getPitchDirection() == 1) spacecraft.setPitchDirection(0);
                break;
            case LEFT :
                if (spacecraft.getRollDirection() == -1) spacecraft.setRollDirection(0);
                break;
            case RIGHT :
                if (spacecraft.getRollDirection() == 1) spacecraft.setRollDirection(0);
                break;
            case C:
                if (spacecraft.getAccelerationDirection() == 1) spacecraft.setAccelerationDirection(0);
                break;
            case X:
                if (spacecraft.getAccelerationDirection() == -1) spacecraft.setAccelerationDirection(0);
                break;
        }
    }
    
    private void onMousePressed(MouseEvent e) {
        if (e.getButton().ordinal() == 3) {
            mouseXRight = e.getSceneX();
            mouseYRight = e.getSceneY();
        }
    }

    private void onMouseDragged(MouseEvent e) {
        if (camera == defaultCamera) {
            if (e.getButton().ordinal() == 3) {
                defaultCamera.updatePosition(e.getSceneX() - mouseXRight, e.getSceneY() - mouseYRight);
            }
        }
        mouseXRight = e.getSceneX();
        mouseYRight = e.getSceneY();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
