package me.lignum.jvox;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Optional;

final class VoxRootChunk extends VoxChunk {
    private final VoxModel[] models;
    private final int[] palette;
    private final HashMap<Integer, VoxMaterial> materials = new HashMap<>();
    private final HashMap<Integer, VoxOldMaterial> oldMaterials = new HashMap<>();

    VoxRootChunk(InputStream stream, InputStream childrenStream) throws IOException {
        VoxChunk first = VoxChunk.readChunk(childrenStream)
            .orElseThrow(() -> new InvalidVoxException("Root chunk has no children"));

        int modelCount = 1;

        if (first instanceof VoxPackChunk) {
            VoxPackChunk pack = (VoxPackChunk)first;
            modelCount = pack.getModelCount();
            first = null;
        }

        models = new VoxModel[modelCount];

        for (int i = 0; i < modelCount; i++) {
            VoxChunk chunk1;

            if (first != null) {
                // If first != null, then that means that the first chunk was not a PACK chunk,
                // and we've already read a SIZE chunk.
                chunk1 = first;
                first = null;
            } else {
                chunk1 = VoxChunk.readChunk(childrenStream, "SIZE")
                        .orElseThrow(() -> new InvalidVoxException("SIZE chunk expected, got nothing"));
            }

            VoxSizeChunk size = (VoxSizeChunk) chunk1;

            VoxChunk chunk2 = VoxChunk.readChunk(childrenStream, "XYZI")
                .orElseThrow(() -> new InvalidVoxException("XYZI chunk expected, got nothing"));
            VoxXYZIChunk xyzi = (VoxXYZIChunk)chunk2;

            models[i] = new VoxModel(size.getSize(), xyzi.getVoxels());
        }

        int[] pal = VoxRGBAChunk.DEFAULT_PALETTE;

        Optional<VoxChunk> optNext = VoxChunk.readChunk(childrenStream);
        if (optNext.isPresent()) {
            VoxChunk next = optNext.get();

            if (next instanceof VoxRGBAChunk) {
                VoxRGBAChunk rgba = (VoxRGBAChunk)next;
                pal = rgba.getPalette();
            } else {
                processChunk(next);
            }
        }

        palette = pal;

        readRestChunks(childrenStream);
    }

    private void processChunk(VoxChunk chunk) {
        if (chunk instanceof VoxMATLChunk) {
            VoxMaterial mat = ((VoxMATLChunk) chunk).getMaterial();
            materials.put(mat.getID(), mat);
        } else if (chunk instanceof VoxMATTChunk) {
            VoxOldMaterial mat = ((VoxMATTChunk) chunk).getMaterial();
            oldMaterials.put(mat.getID(), mat);
        }
    }

    private void readRestChunks(InputStream stream) throws IOException {
        for (Optional<VoxChunk> optChunk = VoxChunk.readChunk(stream);
             stream.available() > 0; optChunk = VoxChunk.readChunk(stream)) {
            if (optChunk.isPresent()) {
                VoxChunk chunk = optChunk.get();
                processChunk(chunk);
            }
        }
    }

    VoxModel[] getModels() {
        return models;
    }

    int[] getPalette() {
        return palette;
    }

    HashMap<Integer, VoxMaterial> getMaterials() {
        return materials;
    }

    HashMap<Integer, VoxOldMaterial> getOldMaterials() {
        return oldMaterials;
    }
}
