package me.lignum.jvox;

import java.util.HashMap;

public final class VoxFile {
    private final int version;
    private final VoxRootChunk root;

    VoxFile(int version, VoxRootChunk root) {
        this.version = version;
        this.root = root;
    }

    public VoxModel[] getModels() {
        return root.getModels();
    }

    public int[] getPalette() {
        return root.getPalette();
    }

    public HashMap<Integer, VoxMaterial> getMaterials() {
        return root.getMaterials();
    }

    @Deprecated
    public HashMap<Integer, VoxOldMaterial> getOldMaterials() {
        return root.getOldMaterials();
    }

    public int getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "VoxFile{version=" + version + "}";
    }
}
