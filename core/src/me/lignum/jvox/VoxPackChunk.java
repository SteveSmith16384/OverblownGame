package me.lignum.jvox;

import java.io.IOException;
import java.io.InputStream;

final class VoxPackChunk extends VoxChunk {
    private final int modelCount;

    VoxPackChunk(InputStream stream) throws IOException {
        this.modelCount = StreamUtils.readIntLE(stream);
    }

    int getModelCount() {
        return modelCount;
    }
}
