package object;

import geometry.Vector;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import state.IdleState;

public class Rocket extends Spacecraft {

    public Rocket() {
        position = new Vector(0, 0, 0);
        PhongMaterial material = new PhongMaterial(Color.DARKGRAY);
        PhongMaterial material1 = new PhongMaterial(Color.DARKGRAY);
        Image image1 = new Image("resources/t1.jpg");
        Image image3 = new Image("resources/t3.jpg");
        material.setDiffuseMap(image1);
        material1.setDiffuseMap(image3);

        Cylinder mainBody = new Cylinder(50, 200);
        mainBody.setMaterial(material);
        mainBody.setRotationAxis(Rotate.Y_AXIS);
        mainBody.setRotate(180);

        Cylinder c1 = new Cylinder(30, 20);
        c1.setMaterial(material);
        c1.setTranslateY(-110);
        this.getChildren().addAll(c1);

        Group cone1 = makeCone(14, 50, material1);
        cone1.setTranslateY(-120);
        cone1.setTranslateX(-16);
        cone1.setTranslateZ(-9);
        cone1.setRotationAxis(Rotate.X_AXIS);
        cone1.setRotate(-90);
        this.getChildren().addAll(cone1);

        Group cone2 = makeCone(14, 50, material1);
        cone2.setTranslateY(-120);
        cone2.setTranslateX(16);
        cone2.setTranslateZ(-9);
        cone2.setRotationAxis(Rotate.X_AXIS);
        cone2.setRotate(-90);
        this.getChildren().addAll(cone2);

        Group cone3 = makeCone(14, 50, material1);
        cone3.setTranslateY(-120);
        cone3.setTranslateZ(16);
        cone3.setRotationAxis(Rotate.X_AXIS);
        cone3.setRotate(-90);
        this.getChildren().addAll(cone3);

        Sphere sphere = new Sphere(50);
        sphere.setMaterial(material);
        sphere.setTranslateY(100);
        sphere.setRotationAxis(Rotate.X_AXIS);
        sphere.setRotate(90);

        Group wing1 = makeWing(material);
        Group wing2 = makeWing(material);
        Group wing3 = makeWing(material);

        wing1.getTransforms().add(new Translate(50, -60, 0));
        wing2.getTransforms().addAll(new Scale(-1, 1, 1), new Translate(50, -60, 0));
        wing3.getTransforms().addAll(new Rotate(-90, Rotate.Y_AXIS), new Translate(50, -60, 0));

        this.getChildren().addAll(mainBody, sphere, wing1, wing2, wing3);

        state = new IdleState(this);

        this.getTransforms().addAll(new Translate(position.getX(), position.getY(), position.getZ()));
    }

    public void update(double passed) {
        state.update(passed, pitchDirection, rollDirection, accelerationDirection);
    }

    private Group makeCone(float radius, float height, PhongMaterial material) {
        Group cone = new Group();

        int numberOfTriangles = 360;

        float[] points = new float[numberOfTriangles * 3 * 3];
        float[] textCoords = {
                0.5f, 0,
                0, 1,
                1, 1
        };
        int[] faces = new int[numberOfTriangles * 3 * 2];

        for (int i = 0; i < numberOfTriangles; i++) {
            int index = i * 9;
            points[index] = 0;
            points[index + 1] = 0;
            points[index + 2] = height / 2;
            points[index + 3] = (float)Math.cos(Math.toRadians(i)) * radius;
            points[index + 4] = (float)Math.sin(Math.toRadians(i)) * radius;
            points[index + 5] = - height / 2;
            points[index + 6] = (float)Math.cos(Math.toRadians(i + 1)) * radius;
            points[index + 7] = (float)Math.sin(Math.toRadians(i + 1)) * radius;
            points[index + 8] = - height / 2;
        }

        populateFaces(numberOfTriangles, faces);

        TriangleMesh mesh = new TriangleMesh();
        mesh.getPoints().addAll(points);
        mesh.getTexCoords().addAll(textCoords);
        mesh.getFaces().addAll(faces);

        MeshView meshView = new MeshView();
        meshView.setMesh(mesh);
        meshView.setMaterial(material);

        Cylinder circle = new Cylinder(radius, 0.1);
        circle.setMaterial(material);
        circle.setTranslateZ(- height / 2);
        circle.setRotationAxis(Rotate.X_AXIS);
        circle.setRotate(90);


        cone.getChildren().addAll(meshView, circle);

        return cone;
    }

    private static void populateFaces(int numberOfTriangles, int[] faces) {
        for (int i = 0; i < numberOfTriangles; i++) {
            int index = i * 6;
            faces[index] = i * 3;
            faces[index + 1] = 0;
            faces[index + 2] = i * 3 + 1;
            faces[index + 3] = 1;
            faces[index + 4] = i * 3 + 2;
            faces[index + 5] = 2;
        }
    }

    private Group makeWing(PhongMaterial material) {
        Group wing = new Group();

        MeshView side1 = wingMesh(material, 360);
        MeshView side2 = wingMesh(material, 360);
        side1.setTranslateZ(5);
        side1.setScaleZ(150);
        side2.setScaleZ(-1);
        side2.setTranslateZ(-5);

        Box rec1 = new Box(50, 10, 0.1);
        rec1.setMaterial(material);
        rec1.getTransforms().addAll(new Translate(25, 0, 0), new Rotate(90, Rotate.X_AXIS));

        MeshView cSide = circularSide((TriangleMesh) side1.getMesh(), material, 360);

        wing.getChildren().addAll(rec1, side1, side2, cSide);

        return wing;
    }

    private MeshView circularSide(TriangleMesh circleMesh, PhongMaterial material, int numberOfTriangles) {
        float[] points = new float[numberOfTriangles * 2 * 3 * 3];
        float[] textCoords = {
                0.5f, 0,
                0, 1,
                1, 1
        };
        int[] faces = new int[numberOfTriangles * 2 * 3 * 2];

        for (int i = 0; i < numberOfTriangles; i++) {
            int index = i * 9;
            float x1 = circleMesh.getPoints().get(index + 3);
            float y1 = circleMesh.getPoints().get(index + 4);
            float x2 = circleMesh.getPoints().get(index + 6);
            float y2 = circleMesh.getPoints().get(index + 7);

            int index2 = i * 18;
            points[index2] = x1;
            points[index2 + 1] = y1;
            points[index2 + 2] = 5;
            points[index2 + 3] = x1;
            points[index2 + 4] = y1;
            points[index2 + 5] = -5;
            points[index2 + 6] = x2;
            points[index2 + 7] = y2;
            points[index2 + 8] = -5;

            points[index2 + 9] = x2;
            points[index2 + 10] = y2;
            points[index2 + 11] = -5;
            points[index2 + 12] = x2;
            points[index2 + 13] = y2;
            points[index2 + 14] = 5;
            points[index2 + 15] = x1;
            points[index2 + 16] = y1;
            points[index2 + 17] = 5;
        }

        populateFaces(numberOfTriangles, faces);

        TriangleMesh mesh = new TriangleMesh();
        mesh.getPoints().addAll(points);
        mesh.getTexCoords().addAll(textCoords);
        mesh.getFaces().addAll(faces);

        MeshView meshView = new MeshView();
        meshView.setMesh(mesh);
        meshView.setMaterial(material);
        meshView.setCullFace(CullFace.NONE);

        return meshView;
    }

    private MeshView wingMesh(PhongMaterial material, int numberOfTriangles) {
        float diff = (float)90 / numberOfTriangles;

        float[] points = new float[numberOfTriangles * 3 * 3];
        float[] textCoords = {
                0.5f, 0,
                0, 1,
                1, 1
        };
        int[] faces = new int[numberOfTriangles * 3 * 2];

        for (int i = 0; i < numberOfTriangles; i++) {
            int index = i * 9;
            points[index] = 0;
            points[index + 1] = 0;
            points[index + 2] = 0;
            float x1 = (float)Math.cos(Math.toRadians(i * diff)) * 50;
            float y1 = (float)Math.sin(Math.toRadians(i * diff)) * 50;
            float z1 = 0;
            float x2 = (float)Math.cos(Math.toRadians((i + 1) * diff)) * 50;
            float y2 = (float)Math.sin(Math.toRadians((i + 1) * diff)) * 50;
            float z2 = 0;

            points[index + 3] = x1;
            points[index + 4] = y1;
            points[index + 5] = z1;
            points[index + 6] = x2;
            points[index + 7] = y2;
            points[index + 8] = z2;
        }

        populateFaces(numberOfTriangles, faces);

        TriangleMesh mesh = new TriangleMesh();
        mesh.getPoints().addAll(points);
        mesh.getTexCoords().addAll(textCoords);
        mesh.getFaces().addAll(faces);

        MeshView meshView = new MeshView();
        meshView.setMesh(mesh);
        meshView.setMaterial(material);
        meshView.setCullFace(CullFace.NONE);

        return meshView;
    }

}
