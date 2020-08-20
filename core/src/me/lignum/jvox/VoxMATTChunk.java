package me.lignum.jvox;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

final class VoxMATTChunk extends VoxChunk {
    private final VoxOldMaterial material;

    VoxMATTChunk(InputStream stream) throws IOException {
        int id = StreamUtils.readIntLE(stream);
        int typeIndex = StreamUtils.readIntLE(stream);
        VoxOldMaterialType type = VoxOldMaterialType.fromIndex(typeIndex)
            .orElseThrow(() -> new InvalidVoxException("Unknown material type " + typeIndex));
        float weight = StreamUtils.readFloatLE(stream);
        int propBits = StreamUtils.readIntLE(stream);
        boolean isTotalPower = VoxOldMaterialProperty.IS_TOTAL_POWER.isSet(propBits);

        int propCount = Integer.bitCount(propBits);

        if (isTotalPower) {
            propCount--; // IS_TOTAL_POWER has no value
        }

        HashMap<VoxOldMaterialProperty, Float> properties = new HashMap<>(propCount);

        for (VoxOldMaterialProperty prop : VoxOldMaterialProperty.values()) {
            if (prop != VoxOldMaterialProperty.IS_TOTAL_POWER && prop.isSet(propBits)) {
                properties.put(prop, StreamUtils.readFloatLE(stream));
            }
        }

        try {
            this.material = new VoxOldMaterial(id, weight, type, properties, isTotalPower);
        } catch (IllegalArgumentException e) {
            throw new InvalidVoxException("Material with ID " + id + " is invalid", e);
        }
    }

    VoxOldMaterial getMaterial() {
        return material;
    }
}
