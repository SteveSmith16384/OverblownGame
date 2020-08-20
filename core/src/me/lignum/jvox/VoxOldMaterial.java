package me.lignum.jvox;

import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

public final class VoxOldMaterial {
    private final int id;
    private final float weight;
    private final VoxOldMaterialType type;
    private final HashMap<VoxOldMaterialProperty, Float> properties;
    private final boolean isTotalPower;

    public VoxOldMaterial(int id, float weight, VoxOldMaterialType type, HashMap<VoxOldMaterialProperty, Float> properties, boolean isTotalPower) {
        if (type == null) {
            throw new IllegalArgumentException("type must not be null");
        }

        this.type = type;

        if (properties == null) {
            throw new IllegalArgumentException("properties must not be null");
        }

        this.properties = properties;

        if (id < 1 || id > 255) {
            throw new IllegalArgumentException("id must be in [1..255]");
        }

        this.id = id;

        if (type == VoxOldMaterialType.DIFFUSE) {
            if (weight != 1.0) {
                throw new IllegalArgumentException("A diffuse material must have a weight of 1.0");
            }
        } else {
            if (!(weight > 0.0 && weight <= 1.0)) {
                throw new IllegalArgumentException("weight must be in (0.0..1.0]");
            }
        }

        this.weight = weight;
        this.isTotalPower = isTotalPower;
    }

    public Optional<Float> getProperty(VoxOldMaterialProperty property) {
        return Optional.ofNullable(properties.getOrDefault(property, null));
    }

    public boolean isTotalPower() {
        return isTotalPower;
    }

    public int getID() {
        return id;
    }

    public float getWeight() {
        return weight;
    }

    public VoxOldMaterialType getType() {
        return type;
    }

    public HashMap<VoxOldMaterialProperty, Float> getProperties() {
        return properties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VoxOldMaterial that = (VoxOldMaterial) o;
        return id == that.id &&
                Float.compare(that.weight, weight) == 0 &&
                isTotalPower == that.isTotalPower &&
                type == that.type &&
                properties.equals(that.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, weight, type, properties, isTotalPower);
    }
}
