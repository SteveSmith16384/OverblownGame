package me.lignum.jvox;

import java.util.Optional;

public enum VoxOldMaterialType {
    DIFFUSE,
    METAL,
    GLASS,
    EMISSIVE;

    public int getIndex() {
        return ordinal();
    }

    public static Optional<VoxOldMaterialType> fromIndex(int index) {
        VoxOldMaterialType[] materials = VoxOldMaterialType.values();

        if (index >= 0 && index < materials.length) {
            return Optional.of(materials[index]);
        }

        return Optional.empty();
    }

    public static Optional<VoxOldMaterialType> parse(String material) {
        VoxOldMaterialType mat = null;

        switch (material) {
            case "_diffuse":
                mat = DIFFUSE;
                break;
            case "_metal":
                mat = METAL;
                break;
            case "_glass":
                mat = GLASS;
                break;
            case "_emit":
                mat = EMISSIVE;
                break;
        }

        return Optional.ofNullable(mat);
    }
}
