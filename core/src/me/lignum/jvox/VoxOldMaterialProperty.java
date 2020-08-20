package me.lignum.jvox;

public enum VoxOldMaterialProperty {
    PLASTIC,
    ROUGHNESS,
    SPECULAR,
    IOR,
    ATTENUATION,
    POWER,
    GLOW,
    IS_TOTAL_POWER;

    int flag() {
        return 1 << ordinal();
    }

    boolean isSet(int flags) {
        int flag = flag();
        return (flags & flag) == flag;
    }
}
