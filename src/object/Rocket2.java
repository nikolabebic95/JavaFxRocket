package object;

import geometry.Vector;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import state.IdleState;

public class Rocket2 extends Spacecraft {
    public Rocket2() {
        this.position = new Vector(0.0D, 0.0D, 0.0D);
        PhongMaterial material = new PhongMaterial(Color.DARKSLATEGREY);
        Cylinder cylinder = new Cylinder(30.0D, 60.0D);
        cylinder.setMaterial(material);
        cylinder.getTransforms().addAll(new Translate(0.0D, 30.0D, 0.0D));
        MeshView upperPart = this.drawCone(30.0F, 10.0F, 50.0F, 360);
        upperPart.setMaterial(material);
        upperPart.getTransforms().addAll(new Rotate(-90.0D, Rotate.X_AXIS), new Translate(0.0D, 0.0D, 60.0D));
        MeshView lowerPart = this.drawCone(12.5F, 30.0F, 100.0F, 360);
        lowerPart.setMaterial(material);
        lowerPart.getTransforms().addAll(new Rotate(-90.0D, Rotate.X_AXIS), new Translate(0.0D, 0.0D, -100.0D));
        Sphere top = new Sphere(10.0D);
        top.setMaterial(material);
        top.getTransforms().addAll(new Translate(0.0D, 107.5D, 0.0D));
        MeshView centerWing = this.drawWing(6.0F, 100.0F, 50.0F);
        centerWing.setMaterial(material);
        centerWing.setTranslateY(-90.0D);
        MeshView leftWing = this.drawWing(6.0F, 100.0F, 50.0F);
        leftWing.setMaterial(material);
        leftWing.getTransforms().addAll(new Rotate(-90.0D, Rotate.Y_AXIS), new Translate(0.0D, -90.0D, 0.0D));
        MeshView rightWing = this.drawWing(6.0F, 100.0F, 50.0F);
        rightWing.setMaterial(material);
        rightWing.getTransforms().addAll(new Rotate(90.0D, Rotate.Y_AXIS), new Translate(0.0D, -90.0D, 0.0D));
        Cylinder bottom1 = new Cylinder(15.0D, 15.0D);
        bottom1.setMaterial(material);
        bottom1.getTransforms().add(new Translate(0.0D, -107.0D, 0.0D));
        MeshView exhaust = this.drawCone(22.0F, 10.0F, 20.0F, 180);
        exhaust.setMaterial(material);
        exhaust.getTransforms().addAll(new Rotate(-90.0D, Rotate.X_AXIS), new Translate(0.0D, 0.0D, -130.0D));
        Cylinder exhaustTop = new Cylinder(22.0D, 0.1D);
        exhaustTop.setMaterial(material);
        exhaustTop.getTransforms().addAll(new Translate(0.0D, -130.0D, 0.0D));
        this.getChildren().addAll(cylinder, upperPart, lowerPart, top, centerWing, leftWing, rightWing, bottom1, exhaust, exhaustTop);
        this.state = new IdleState(this);
    }

    private MeshView drawWing(float width, float length, float height) {
        float[] points = new float[]{-width / 2.0F, length, 0.0F, -width / 2.0F, 0.0F, 0.0F, -width / 2.0F, 0.0F, height, width / 2.0F, 0.0F, height, width / 2.0F, 0.0F, 0.0F, width / 2.0F, length, 0.0F, -width / 2.0F, 0.0F, 0.0F, width / 2.0F, 0.0F, 0.0F, -width / 2.0F, 0.0F, height, -width / 2.0F, 0.0F, height, width / 2.0F, 0.0F, 0.0F, width / 2.0F, 0.0F, height, -width / 2.0F, 0.0F, height, width / 2.0F, 0.0F, height, -width / 2.0F, length, 0.0F, -width / 2.0F, length, 0.0F, width / 2.0F, 0.0F, height, width / 2.0F, length, 0.0F};
        float[] textCoords = new float[]{0.0F, 0.0F};
        int[] faces = new int[points.length / 3 * 2];

        for(int i = 0; i < faces.length / 2; ++i) {
            faces[i * 2] = i;
            faces[i * 2 + 1] = 0;
        }

        TriangleMesh triangleMesh = new TriangleMesh();
        triangleMesh.getPoints().addAll(points);
        triangleMesh.getTexCoords().addAll(textCoords);
        triangleMesh.getFaces().addAll(faces);
        MeshView meshView = new MeshView();
        meshView.setMesh(triangleMesh);
        return meshView;
    }

    private MeshView drawCone(float bottomRadius, float topRadius, float height, int numberOfTriangles) {
        float[] points = new float[numberOfTriangles * 3 * 3 * 2];
        float[] textCoords = new float[]{0.0F, 0.0F};
        int[] faces = new int[numberOfTriangles * 3 * 2 * 2];

        int i;
        int index;
        for(i = 0; i < numberOfTriangles; ++i) {
            index = i * 18;
            points[index] = (float)Math.cos(Math.toRadians((double)i * 360.0D / (double)((float)numberOfTriangles))) * topRadius;
            points[index + 1] = (float)Math.sin(Math.toRadians((double)i * 360.0D / (double)((float)numberOfTriangles))) * topRadius;
            points[index + 2] = height;
            points[index + 3] = (float)Math.cos(Math.toRadians((double)i * 360.0D / (double)((float)numberOfTriangles))) * bottomRadius;
            points[index + 4] = (float)Math.sin(Math.toRadians((double)i * 360.0D / (double)((float)numberOfTriangles))) * bottomRadius;
            points[index + 5] = 0.0F;
            points[index + 6] = (float)Math.cos(Math.toRadians((double)(i + 1) * 360.0D / (double)((float)numberOfTriangles))) * bottomRadius;
            points[index + 7] = (float)Math.sin(Math.toRadians((double)(i + 1) * 360.0D / (double)((float)numberOfTriangles))) * bottomRadius;
            points[index + 8] = 0.0F;
            points[index + 9] = (float)Math.cos(Math.toRadians((double)i * 360.0D / (double)((float)numberOfTriangles))) * topRadius;
            points[index + 10] = (float)Math.sin(Math.toRadians((double)i * 360.0D / (double)((float)numberOfTriangles))) * topRadius;
            points[index + 11] = height;
            points[index + 12] = (float)Math.cos(Math.toRadians((double)(i + 1) * 360.0D / (double)((float)numberOfTriangles))) * bottomRadius;
            points[index + 13] = (float)Math.sin(Math.toRadians((double)(i + 1) * 360.0D / (double)((float)numberOfTriangles))) * bottomRadius;
            points[index + 14] = 0.0F;
            points[index + 15] = (float)Math.cos(Math.toRadians((double)(i + 1) * 360.0D / (double)((float)numberOfTriangles))) * topRadius;
            points[index + 16] = (float)Math.sin(Math.toRadians((double)(i + 1) * 360.0D / (double)((float)numberOfTriangles))) * topRadius;
            points[index + 17] = height;
        }

        for(i = 0; i < numberOfTriangles * 3 * 2; ++i) {
            index = i * 2;
            faces[index] = i;
            faces[index + 1] = 0;
        }

        TriangleMesh mesh = new TriangleMesh();
        mesh.getPoints().addAll(points);
        mesh.getTexCoords().addAll(textCoords);
        mesh.getFaces().addAll(faces);
        MeshView meshView = new MeshView();
        meshView.setMesh(mesh);
        return meshView;
    }

    public void update(double passed) {
        this.state.update(passed, this.pitchDirection, this.rollDirection, this.accelerationDirection);
    }
}
