package me.lignum.jvox;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

final class ChunkFactory {
    static Optional<VoxChunk> createChunk(String id, InputStream stream, InputStream childrenStream) throws IOException {
        VoxChunk chunk = null;

        switch (id) {
            case "MAIN":
                chunk = new VoxRootChunk(stream, childrenStream);
                break;
            case "SIZE":
                chunk = new VoxSizeChunk(stream);
                break;
            case "XYZI":
                chunk = new VoxXYZIChunk(stream);
                break;
            case "RGBA":
                chunk = new VoxRGBAChunk(stream);
                break;
            case "MATT":
                chunk = new VoxMATTChunk(stream);
                break;
            case "MATL":
                chunk = new VoxMATLChunk(stream);
                break;
            // These chunks are unsupported and simply skipped.
            case "nTRN":
            case "nGRP":
            case "nSHP":
            case "LAYR":
            case "rOBJ":
                chunk = new VoxDummyChunk();
                break;
        }

        return Optional.ofNullable(chunk);
    }
}
