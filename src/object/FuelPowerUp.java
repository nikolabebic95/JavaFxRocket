package object;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import com.interactivemesh.jfx.importer.tds.TdsModelImporter;
import com.sun.javafx.scene.paint.GradientUtils;
import geometry.Vector;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

public class FuelPowerUp extends SpaceObject {
    public FuelPowerUp() {
        position = new Vector(0, 0, 0);

        ObjModelImporter modelImporter = new ObjModelImporter();
        modelImporter.read("models/stoormtrooper/Stormtrooper Imperial Issue helmet.obj");
        Node[] mesh = modelImporter.getImport();
        modelImporter.close();

        Group group = new Group(mesh);

        double scale = 0.1;

        group.setScaleX(scale);
        group.setScaleY(scale);
        group.setScaleZ(scale);

        getChildren().add(group);
    }
}
