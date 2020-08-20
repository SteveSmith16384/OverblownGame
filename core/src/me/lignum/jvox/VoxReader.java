package me.lignum.jvox;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Optional;

public class VoxReader implements Closeable {
    private static final byte[] MAGIC_BYTES = new byte[] {
        (byte)'V', (byte)'O', (byte)'X', (byte)' '
    };

    private final InputStream stream;

    public VoxReader(InputStream stream) {
        if (stream == null) {
            throw new IllegalArgumentException("stream must not be null");
        }

        this.stream = stream;
    }

    public VoxFile read() throws IOException {
        byte[] magicBytes = new byte[4];
        if (stream.read(magicBytes) != 4) {
            throw new InvalidVoxException("Could not read magic bytes");
        }

        if (!Arrays.equals(magicBytes, MAGIC_BYTES)) {
            throw new InvalidVoxException("Invalid magic bytes");
        }

        int fileVersion = StreamUtils.readIntLE(stream);

        if (fileVersion < 150) {
            throw new InvalidVoxException("Vox versions older than 150 are not supported");
        }

        Optional<VoxChunk> optChunk = VoxChunk.readChunk(stream);
        if (!optChunk.isPresent()) {
            throw new InvalidVoxException("No root chunk present in the file");
        }

        VoxChunk chunk = optChunk.get();
        if (!(chunk instanceof VoxRootChunk)) {
            throw new InvalidVoxException("First chunk is not of ID \"MAIN\"");
        }

        return new VoxFile(fileVersion, (VoxRootChunk)chunk);
    }

    @Override
    public void close() throws IOException {
        stream.close();
    }
}
