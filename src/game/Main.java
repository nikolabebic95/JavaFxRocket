package game;

import camera.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import object.*;

import java.util.ArrayList;
import javafx.scene.input.MouseEvent;
import object.powerups.Heart;
import object.powerups.PowerUp;
import object.powerups.PowerUpsMap;
import object.projectiles.*;
import object.shootables.*;
import object.spacecrafts.*;
import state.*;

public class Main extends Application {
    private static final int WINDOW_HEIGHT = 700;
    private static final int WINDOW_WIDTH = 900;

    private Group mainSceneRoot;

    private SubScene mainSubscene;
    private SubScene headUpDisplayScene;
    private SubScene altitudeHeadUp;
    private SubScene healthHeadUp;

    private Spacecraft spacecraft;
    private LaunchPad launchPad;
    private ArrayList<ShootableObject> shootables = new ArrayList<>();
    private Text coordinates;
    private Text bubblesHitText;
    private int bubblesHit;

    private ArrayList<Projectile> projectiles = new ArrayList<>();

    private ArrayList<PowerUp> powerUps = new ArrayList<>();

    private Circle dot;
    private Polygon arrow;

    private Polygon speedArrow;

    private Line centralLine;
    private ArrayList<Line> leftLines = new ArrayList<>();
    private ArrayList<Line> rightLines = new ArrayList<>();
    private ArrayList<Text> texts = new ArrayList<>();

    private ArrayList<Heart> health = new ArrayList<>();

    private UpdateTimer timer = new UpdateTimer();

    private DefaultCamera defaultCamera = new DefaultCamera();
    private BackCamera backCamera = new BackCamera();
    private OrbitingCamera orbitingCamera = new OrbitingCamera();
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
            updateProjectiles(passed);
            spacecraft.update(passed);
            refreshHeadUp();
            refreshAltitudeHeadUp();
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

        double speed = spacecraft.getSpeed().getIntensity();
        final double maxSpeed = 5000.0;
        double percentage = speed / maxSpeed;

        double angle = percentage * 240 - 120;
        speedArrow.getTransforms().setAll(new Rotate(angle, 0, 0));
    }

    private void refreshAltitudeHeadUp() {
        int mod = 500;
        double start = 2 + ((int)spacecraft.getPosition().getZ() % mod) * 57.5 / mod;

        int startZ = (int)spacecraft.getPosition().getZ() / mod * mod + 1000;

        double angle = spacecraft.getRollAngle();
        centralLine.getTransforms().setAll(new Rotate(angle, 150, 117, 0));

        for (int i = 0; i < 5; i++) {
            Line line = leftLines.get(i);
            double yCoord = start + i * 57.5;
            line.setStartY(yCoord);
            line.setEndY(yCoord);
            line = rightLines.get(i);
            line.setStartY(yCoord);
            line.setEndY(yCoord);
            Text text = texts.get(i);
            int z = startZ - i * mod;
            text.setText(z + "");
            text.setTranslateX(150 - text.getLayoutBounds().getWidth() / 2.0);
            text.setTranslateY(yCoord + text.getLayoutBounds().getHeight() / 4.0);
        }
    }

    private void refreshHealthHeadUp() {
        int i;
        for (i = 0; i < spacecraft.getHealth(); i++) {
            health.get(i).setVisible(true);
        }

        for (; i < health.size(); i++) {
            health.get(i).setVisible(false);
        }
    }

    private void checkForCollisions() {
        shootables.forEach(shootableObject -> {
            projectiles.forEach(projectile -> {
                if (shootableObject.getBoundsInParent().intersects(projectile.getBoundsInParent())) {
                    shootableObject.shoot(projectile.getStrength());
                    if (shootableObject.isKilled()) {
                        shootableObject.reset();
                        positionObject(shootableObject);
                        bubblesHit++;
                    }

                    projectile.setKilled();
                }
            });

            if (shootableObject.getBoundsInParent().intersects(spacecraft.getBoundsInParent())) {
                shootableObject.kill();
                shootableObject.reset();
                positionObject(shootableObject);
                bubblesHit++;
                spacecraft.collide();
                refreshHealthHeadUp();
            }
        });

        projectiles.forEach(projectile -> {
            if (projectile.isKilled()) {
                mainSceneRoot.getChildren().remove(projectile);
            }
        });

        projectiles.removeIf(Projectile::isKilled);

        powerUps.forEach(powerUp -> {
            if (powerUp.getBoundsInParent().intersects(spacecraft.getBoundsInParent())) {
                powerUp.apply(spacecraft);
                powerUp.kill();
                Platform.runLater(() -> {
                    mainSceneRoot.getChildren().remove(powerUp);
                    drawPowerUp();
                });
                refreshHealthHeadUp();
            }
        });

        powerUps.removeIf(PowerUp::isKilled);
    }

    private void updateProjectiles(double passed) {
        projectiles.forEach(projectile -> projectile.update(passed));
        projectiles.forEach(projectile -> {
            if (projectile.isOutside()) {
                mainSceneRoot.getChildren().remove(projectile);
            }
        });
        projectiles.removeIf(Projectile::isOutside);
    }

    private void drawBubble() {
        SpaceBubble bubble = new SpaceBubble(450);
        //StormtrooperHelmet bubble = new StormtrooperHelmet();
        //Heart bubble = new Heart();
        positionObject(bubble);
        shootables.add(bubble);
        mainSceneRoot.getChildren().add(bubble);
    }

    private void drawPowerUp() {
        PowerUp powerUp = PowerUpsMap.getRandom();
        positionObject(powerUp);
        powerUps.add(powerUp);
        mainSceneRoot.getChildren().add(powerUp);
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

        for (int i = 0; i < 2; i++) {
            drawPowerUp();
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

        Circle speedometer = new Circle(33);
        speedometer.setFill(null);
        speedometer.setStroke(Color.RED);
        speedometer.setStrokeWidth(3);
        speedometer.setTranslateX(speedometer.getRadius() + speedometer.getRadius() / 6);
        speedometer.setTranslateY(speedometer.getRadius() + speedometer.getRadius() / 6);

        double center = speedometer.getRadius() + speedometer.getRadius() / 6;

        Arc arc = new Arc(0, 0, 33, 33, 210, 120);
        arc.setFill(Color.RED);
        arc.setType(ArcType.ROUND);
        arc.setTranslateX(center);
        arc.setTranslateY(center);

        speedArrow = new Polygon(0, -33, -1.5, 7, 1.5, 7);
        speedArrow.setTranslateY(center);
        speedArrow.setTranslateX(center);
        speedArrow.setFill(Color.WHITE);
        speedArrow.getTransforms().setAll(new Rotate(-120, 0, 0));

        headUpDisplayRoot.getChildren().addAll(speedometer, arc, speedArrow);
    }

    private void createAltitudeHeadUp() {
        Group root = new Group();
        altitudeHeadUp = new SubScene(root, 300, 233, true, SceneAntialiasing.BALANCED);
        altitudeHeadUp.setTranslateY(467);
        altitudeHeadUp.setVisible(true);

        Rectangle rectangle = new Rectangle(300, 233);
        rectangle.setFill(Color.LIGHTGREEN);
        rectangle.setArcHeight(46.0D);
        rectangle.setArcWidth(60.0D);
        rectangle.setOpacity(0.2D);

        Line line = new Line(-50, 117, 350, 117);
        line.setStroke(Color.RED);
        line.setStrokeWidth(3);
        centralLine = line;

        root.getChildren().addAll(rectangle, line);

        for (int i = 0; i < 5; i++) {
            double yCoord = 2 + i * 57.5;
            Line left = new Line(50, yCoord, 100, yCoord);
            Line right = new Line(200, yCoord, 250, yCoord);

            int height = 1000 - i * 500;

            Text text = new Text(height + "");
            text.setFont(Font.font("Verdana", 10));
            text.setStroke(Color.WHITE);
            text.setTranslateX(150 - text.getLayoutBounds().getWidth() / 2.0);
            text.setTranslateY(yCoord + text.getLayoutBounds().getHeight() / 4.0);
            left.setStroke(Color.WHITE);
            right.setStroke(Color.WHITE);
            root.getChildren().addAll(left, right);
            root.getChildren().add(text);
            leftLines.add(left);
            rightLines.add(right);
            texts.add(text);
        }
    }

    private void createHealthHeadUp() {
        Group root = new Group();
        healthHeadUp = new SubScene(root, WINDOW_WIDTH, 100, true, SceneAntialiasing.BALANCED);

        Rectangle rectangle = new Rectangle(WINDOW_WIDTH, 100);
        rectangle.setFill(Color.LIGHTGREEN);
        rectangle.setArcHeight(46.0D);
        rectangle.setArcWidth(60.0D);
        rectangle.setOpacity(0.2D);

        root.getChildren().addAll(rectangle);

        for (int i = 0; i < spacecraft.getHealth(); i++) {
            Heart heart = new Heart();
            heart.stopTimeline();
            double scale = 0.02;
            heart.setScaleX(scale);
            heart.setScaleY(scale);
            heart.setScaleZ(scale);
            heart.getTransforms().add(new Translate(2500 + i * 5000, 2500, -2500));
            heart.getTransforms().add(new Rotate(90, Rotate.X_AXIS));
            health.add(heart);
            root.getChildren().add(heart);
        }
    }

    private void createMainScene() {
        mainSceneRoot = new Group();
        mainSubscene = new SubScene(mainSceneRoot, WINDOW_WIDTH, WINDOW_HEIGHT, true, SceneAntialiasing.BALANCED);
        mainSubscene.setFill(Color.BLACK);
        spacecraft = new TieInterceptor(); // TODO: Choose rocket
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
        createAltitudeHeadUp();
        createHealthHeadUp();
        root.getChildren().addAll(mainSubscene, headUpDisplayScene, altitudeHeadUp, healthHeadUp);

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
                    altitudeHeadUp.setVisible(false);
                    healthHeadUp.setVisible(false);
                } else {
                    this.headUpDisplayScene.setVisible(true);
                    altitudeHeadUp.setVisible(true);
                    healthHeadUp.setVisible(true);
                }
                break;
            case B:
                if (spacecraft.getState() instanceof FlyState) {
                    Projectile projectile = spacecraft.shoot();
                    mainSceneRoot.getChildren().add(projectile);
                    projectiles.add(projectile);
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
