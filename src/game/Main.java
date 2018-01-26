package game;

import camera.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
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

    private Spacecraft spacecraft;
    private List<SpaceObject> bubbles = new ArrayList<>();
    private int bubblesHit;

    private UpdateTimer timer = new UpdateTimer();

    private DefaultCamera defaultCamera = new DefaultCamera();
    private BackCamera backCamera = new BackCamera();
    private AbstractCamera camera;
    
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
            camera.update();
            checkForCollisions();
            previous = now;
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

    private void createMainScene() {
        mainSceneRoot = new Group();
        mainSubscene = new SubScene(mainSceneRoot, WINDOW_WIDTH, WINDOW_HEIGHT, true, SceneAntialiasing.BALANCED);
        mainSubscene.setFill(Color.BLACK);
        spacecraft = new Rocket();
        LaunchPad launchPad = new LaunchPad();
        mainSceneRoot.getChildren().addAll(launchPad, spacecraft);
        setUpSpaceObjects();
        instantiateCameras();
        mainSubscene.setCamera(camera.getCamera());
    }

    private void instantiateCameras() {
        camera = defaultCamera;
        backCamera.setObject(spacecraft);
    }

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        createMainScene();
        root.getChildren().addAll(mainSubscene);

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
