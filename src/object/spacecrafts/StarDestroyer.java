package object.spacecrafts;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import geometry.Vector;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import object.projectiles.Projectile;
import object.projectiles.StarDestroyerProjectile;
import object.spacecrafts.Spacecraft;
import state.IdleState;

public class StarDestroyer extends Spacecraft {

    public StarDestroyer() {
        position = new Vector(0, 0, 0);
        state = new IdleState(this);
        ObjModelImporter objModelImporter = new ObjModelImporter();
        objModelImporter.read("models/star destroyer/star_destroyer.obj");
        Node[] mesh = objModelImporter.getImport();
        objModelImporter.close();

        Group group = new Group(mesh);

        group.getTransforms().add(new Rotate(180, Rotate.X_AXIS));
        group.getTransforms().add(new Rotate(-90, Rotate.Z_AXIS));
        group.getTransforms().add(new Rotate(90, Rotate.X_AXIS));

        group.getTransforms().add(new Translate(0, 0, 12));

        getChildren().add(group);

        /*
        protected double maxRollAngle = 30;
        protected double maxPitchAngle = 30;
        protected double rollSpeed = 60;
        protected double pitchSpeed = 60;
        protected double rotationFactor = 4;
        protected double maxSpeed = 3000;
        protected double minSpeed = 200;
         */

        rollSpeed = 25;
        pitchSpeed = 25;
    }

    @Override
    public void update(double passed) {
        this.state.update(passed, this.pitchDirection, this.rollDirection, this.accelerationDirection);
    }

    @Override
    public Projectile createProjectile() {
        return new StarDestroyerProjectile(this);
    }

    @Override
    protected int getMaxHealth() {
        return 9;
    }

    @Override
    protected int getMaxNumOfProjectiles() {
        return 20;
    }
}
