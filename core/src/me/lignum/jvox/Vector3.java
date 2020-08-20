package me.lignum.jvox;

import java.util.Objects;

public final class Vector3<T extends Number> {
    private final T x, y, z;

    public Vector3(T x, T y, T z) {
        if (x == null || y == null || z == null) {
            throw new IllegalArgumentException("No component may be null");
        }

        this.x = x;
        this.y = y;
        this.z = z;
    }

    public T getX() {
        return x;
    }

    public T getY() {
        return y;
    }

    public T getZ() {
        return z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector3<?> vector3 = (Vector3<?>) o;
        return x.equals(vector3.x) &&
                y.equals(vector3.y) &&
                z.equals(vector3.z);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
}
