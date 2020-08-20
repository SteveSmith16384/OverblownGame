package me.lignum.jvox;

import java.util.Objects;

public final class Voxel {
    private final Vector3<Byte> position;
    private final byte colourIndex;

    public Voxel(Vector3<Byte> position, byte colourIndex) {
        this.position = position;
        this.colourIndex = colourIndex;
    }

    public Vector3<Byte> getPosition() {
        return position;
    }

    public int getColourIndex() {
        return colourIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Voxel voxel = (Voxel) o;
        return colourIndex == voxel.colourIndex &&
                Objects.equals(position, voxel.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, colourIndex);
    }

    @Override
    public String toString() {
        return "(" + position.toString() + ", " + Byte.toUnsignedInt(colourIndex) + ")";
    }
}
