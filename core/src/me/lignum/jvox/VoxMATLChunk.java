package me.lignum.jvox;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

final class VoxMATLChunk extends VoxChunk {
    private final VoxMaterial material;

    VoxMATLChunk(InputStream stream) throws IOException {
        int id = StreamUtils.readIntLE(stream);
        HashMap<String, String> props = StreamUtils.readDictionary(stream);
        material = new VoxMaterial(id, props);
    }

    VoxMaterial getMaterial() {
        return material;
    }
}
