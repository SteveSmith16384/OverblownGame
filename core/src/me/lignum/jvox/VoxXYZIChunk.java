package me.lignum.jvox;

import java.io.IOException;
import java.io.InputStream;

final class VoxXYZIChunk extends VoxChunk {
    private final Voxel[] voxels;

    VoxXYZIChunk(InputStream stream) throws IOException {
        int voxelCount = StreamUtils.readIntLE(stream);
        voxels = new Voxel[voxelCount];

        for (int i = 0; i < voxelCount; i++) {
            voxels[i] = new Voxel(StreamUtils.readVector3b(stream), (byte)stream.read());
        }
    }

    Voxel[] getVoxels() {
        return voxels;
    }
}
