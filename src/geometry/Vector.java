package geometry;

public class Vector {
    private double x;
    private double y;
    private double z;

    public Vector() {}

    public Vector(double _x, double _y, double _z) {
        x = _x;
        y = _y;
        z = _z;
    }

    public Vector(Vector other) {
        this(other.x, other.y, other.z);
    }

    public Vector duplicate() {
        return new Vector(x, y, z);
    }

    public Vector scalarMultiply(double value) {
        x *= value;
        y *= value;
        z *= value;
        return this;
    }

    public Vector add(double dx, double dy, double dz) {
        x += dx;
        y += dy;
        z += dz;
        return this;
    }

    public double getIntensity() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public Vector add(Vector v) {
        return add(v.getX(), v.getY(), v.getZ());
    }

    public Vector set(double _x, double _y, double _z) {
        x = _x;
        y = _y;
        z = _z;
        return this;
    }

    public Vector set(Vector v) {
        return set(v.getX(), v.getY(), v.getZ());
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }
}
