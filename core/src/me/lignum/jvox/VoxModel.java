package me.lignum.jvox;

import java.util.Arrays;
import java.util.Objects;

public final class VoxModel {
    private final Vector3<Integer> size;
    private final Voxel[] voxels;

    public VoxModel(Vector3<Integer> size, Voxel[] voxels) {
        if (size == null || voxels == null) {
            throw new IllegalArgumentException("Both size and voxels must be non-null");
        }

        this.size = size;
        this.voxels = voxels;
    }

    public Vector3<Integer> getSize() {
        return size;
    }

    public Voxel[] getVoxels() {
        return voxels;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VoxModel voxModel = (VoxModel) o;
        return size.equals(voxModel.size) &&
                Arrays.equals(voxels, voxModel.voxels);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(size);
        result = 31 * result + Arrays.hashCode(voxels);
        return result;
    }
}
