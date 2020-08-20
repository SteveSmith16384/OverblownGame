package me.lignum.jvox;

import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

public final class VoxMaterial {
    private final int id;
    private final HashMap<String, String> properties;

    public VoxMaterial(int id, HashMap<String, String> properties) {
        this.id = id;

        if (properties == null) {
            throw new IllegalArgumentException("properties must not be null");
        }

        this.properties = properties;
    }

    public int getID() {
        return id;
    }

    public Optional<String> getString(String property) {
        return Optional.ofNullable(properties.getOrDefault(property, null));
    }

    public Optional<Integer> getInt(String property) {
        return Optional.ofNullable(properties.getOrDefault(property, null))
            .flatMap(x -> {
                try {
                    return Optional.of(Integer.parseInt(x));
                } catch (NumberFormatException e) {
                    return Optional.empty();
                }
            });
    }

    public Optional<Float> getFloat(String property) {
        return Optional.ofNullable(properties.getOrDefault(property, null))
            .flatMap(x -> {
                try {
                    return Optional.of(Float.parseFloat(x));
                } catch (NumberFormatException e) {
                    return Optional.empty();
                }
            });
    }

    public boolean hasProperty(String property) {
        return properties.containsKey(property);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VoxMaterial that = (VoxMaterial) o;
        return id == that.id &&
                properties.equals(that.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, properties);
    }
}
