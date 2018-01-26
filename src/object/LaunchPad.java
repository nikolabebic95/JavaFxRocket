package object;

import javafx.scene.Group;
import javafx.scene.PointLight;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class LaunchPad extends SpaceObject {
    private PointLight[] pointLights;
    private Sphere[] bulbs;
    private PhongMaterial bulbMaterial;

    public LaunchPad() {
        MeshView platform = this.drawPlatform(1600.0F);
        platform.setTranslateZ(-3.0D);
        Image image = new Image("resources/launchpad.jpg");
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseMap(image);
        platform.setMaterial(material);
        Group support = this.drawSupport();
        support.setTranslateY(150.0D);
        Group lights = this.drawLights(1600.0D);
        this.getChildren().addAll(platform, support, lights);
        this.getChildren().addAll(this.bulbs);

        for (PointLight pointLight : this.pointLights) {
            pointLight.getScope().addAll(lights, platform, support);
            pointLight.getScope().addAll(this.bulbs);
        }

        this.getTransforms().add(new Translate(0.0D, 0.0D, -200.0D));
    }

    public void switchLights() {
        for (Sphere bulb : this.bulbs) {
            if (bulb.getMaterial() != null) {
                bulb.setMaterial(null);
            } else {
                bulb.setMaterial(this.bulbMaterial);
            }
        }

    }

    private Group drawLights(double size) {
        Group lights = new Group();
        this.pointLights = new PointLight[3];
        this.bulbs = new Sphere[3];
        Image image = new Image("resources/bulb.png");
        this.bulbMaterial = new PhongMaterial();
        this.bulbMaterial.setSelfIlluminationMap(image);

        for(int i = 0; i < 3; ++i) {
            Box box = new Box(5.0D, 5.0D, 150.0D);
            box.getTransforms().addAll(new Rotate((double)(i * 90 + 180), Rotate.Z_AXIS), new Translate(size / 2.0D, 0.0D, 75.0D));
            Sphere bulb = new Sphere(15.0D);
            bulb.getTransforms().addAll(new Rotate((double)(i * 90 + 180), Rotate.Z_AXIS), new Translate(size / 2.0D, 0.0D, 150.0D));
            this.bulbs[i] = bulb;
            lights.getChildren().addAll(box);
            this.pointLights[i] = new PointLight(Color.WHITE);
            this.pointLights[i].getTransforms().addAll(new Rotate((double)(i * 90 + 180), Rotate.Z_AXIS), new Translate(size / 2.0D, 0.0D, 75.0D));
        }

        return lights;
    }

    private Group drawSupport() {
        Group support = new Group();
        PhongMaterial material = new PhongMaterial(Color.DARKGRAY);
        double width = 80.0D;
        double length = 80.0D;
        double height = 500.0D;

        int i;
        Box holder2;
        for(i = 0; i < 4; ++i) {
            holder2 = new Box(5.0D, 5.0D, height);
            holder2.setMaterial(material);
            support.getChildren().add(holder2);
            holder2.setTranslateX((double)(i < 2 ? 1 : -1) * length / 2.0D);
            holder2.setTranslateY((double)(i % 2 == 0 ? 1 : -1) * width / 2.0D);
            holder2.setTranslateZ(height / 2.0D);
        }

        Box box2;
        for(i = 0; (double)i < height / 50.0D + 1.0D; ++i) {
            holder2 = new Box(length, 5.0D, 5.0D);
            holder2.getTransforms().add(new Translate(0.0D, -width / 2.0D, (double)(i * 50)));
            holder2.setMaterial(material);
            box2 = new Box(length, 5.0D, 5.0D);
            box2.getTransforms().add(new Translate(0.0D, width / 2.0D, (double)(i * 50)));
            box2.setMaterial(material);
            Box box3 = new Box(5.0D, width, 5.0D);
            box3.getTransforms().add(new Translate(length / 2.0D, 0.0D, (double)(i * 50)));
            box3.setMaterial(material);
            Box box4 = new Box(5.0D, width, 5.0D);
            box4.getTransforms().add(new Translate(-length / 2.0D, 0.0D, (double)(i * 50)));
            box4.setMaterial(material);
            support.getChildren().addAll(holder2, box2, box3, box4);
        }

        for(i = 0; (double)i < height / 50.0D; ++i) {
            holder2 = new Box(Math.sqrt(length * length + 2500.0D), 5.0D, 5.0D);
            holder2.setTranslateY(-width / 2.0D);
            holder2.setTranslateZ((double)(i * 50 + 25));
            holder2.setMaterial(material);
            holder2.setRotationAxis(Rotate.Y_AXIS);
            holder2.setRotate(Math.toDegrees(Math.atan(50.0D / length)));
            box2 = new Box(Math.sqrt(length * length + 2500.0D), 5.0D, 5.0D);
            box2.setTranslateY(width / 2.0D);
            box2.setTranslateZ((double)(i * 50 + 25));
            box2.setMaterial(material);
            box2.setRotationAxis(Rotate.Y_AXIS);
            box2.setRotate(Math.toDegrees(Math.atan(50.0D / length)));
            support.getChildren().addAll(holder2, box2);
        }

        for(i = 0; i < 4; ++i) {
            holder2 = new Box(5.0D, 5.0D, 100.0D * Math.sqrt(2.0D));
            holder2.setMaterial(material);
            holder2.getTransforms().addAll(new Rotate((double)(45 + i * 90), Rotate.Z_AXIS), new Translate(50.0D + width * Math.sqrt(2.0D) / 2.0D, 0.0D, 50.0D), new Rotate(-45.0D, Rotate.Y_AXIS));
            support.getChildren().add(holder2);
        }

        Box holder1 = new Box(5.0D, 80.0D, 5.0D);
        holder1.setTranslateY(-width / 2.0D - 40.0D);
        holder1.setTranslateZ(height / 2.0D);
        holder1.setMaterial(material);
        holder2 = new Box(5.0D, 80.0D, 5.0D);
        holder2.setTranslateY(-width / 2.0D - 40.0D);
        holder2.setTranslateZ(-50.0D + height / 2.0D);
        holder2.setMaterial(material);
        support.getChildren().addAll(holder1, holder2);
        return support;
    }

    private MeshView drawPlatform(float size) {
        float[] points = new float[]{0.0F, -size / 2.0F, 0.0F, size / 2.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -size / 2.0F, 0.0F, 0.0F, 0.0F, 0.0F, -size / 2.0F, 0.0F, 0.0F, size / 2.0F, 0.0F, 0.0F, 0.0F, size / 2.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, size / 2.0F, 0.0F, -size / 2.0F, 0.0F, 0.0F};
        float[] textCoords = new float[]{0.5F, 0.0F, 0.0F, 0.5F, 1.0F, 0.5F, 0.5F, 1.0F, 0.5F, 0.5F};
        int[] faces = new int[]{0, 0, 1, 1, 2, 4, 3, 0, 4, 4, 5, 2, 6, 1, 7, 3, 8, 4, 9, 4, 10, 3, 11, 2};
        TriangleMesh triangleMesh = new TriangleMesh();
        triangleMesh.getPoints().addAll(points);
        triangleMesh.getTexCoords().addAll(textCoords);
        triangleMesh.getFaces().addAll(faces);
        MeshView meshView = new MeshView();
        meshView.setMesh(triangleMesh);
        meshView.setCullFace(CullFace.NONE);
        return meshView;
    }

    public PointLight[] getPointLights() {
        return this.pointLights;
    }
}
