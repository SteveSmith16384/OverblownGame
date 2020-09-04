package com.procedural.world;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.TextureDescriptor;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.NumberUtils;

/**
 * Created by fabio on 12/08/2016.
 */
public class PBRTextureAttribute extends Attribute {
    public final static String AlbedoAlias = "albedoTexture";
    public final static long Albedo = register(AlbedoAlias);
    public final static String MetallicAlias = "metallicTexture";
    public final static long Metallic = register(MetallicAlias);
    public final static String RoughnessAlias = "roughness";
    public final static long Roughness = register(RoughnessAlias);
    public final static String AmbientOcclusionAlias = "ambientOcclusionTexture";
    public final static long AmbientOcclusion = register(AmbientOcclusionAlias);
    public final static String NormalAlias = "normalTexture";
    public final static long Normal = register(NormalAlias);
    public final static String HeightAlias = "heightTexture";
    public final static long Height = register(HeightAlias);

    protected static long Mask = Albedo | Metallic | Roughness | AmbientOcclusion | Normal | Height;

    public final static boolean is (final long mask) {
        return (mask & Mask) != 0;
    }

    public final TextureDescriptor<Texture> textureDescription;
    public float offsetU = 0;
    public float offsetV = 0;
    public float scaleU = 1;
    public float scaleV = 1;
    /** The index of the texture coordinate vertex attribute to use for this TextureAttribute. Whether this value is used, depends
     * on the shader and {@link Attribute#type} value. For basic (model specific) types (e.g. {@link #Diffuse}, {@link #Normal},
     * etc.), this value is usually ignored and the first texture coordinate vertex attribute is used. */
    public int uvIndex = 0;

    public PBRTextureAttribute (final long type) {
        super(type);
        if (!is(type)) throw new GdxRuntimeException("Invalid type specified");
        textureDescription = new TextureDescriptor<Texture>();
    }

    public <T extends Texture> PBRTextureAttribute (final long type, final TextureDescriptor<T> textureDescription) {
        this(type);
        this.textureDescription.set(textureDescription);
    }

    public <T extends Texture> PBRTextureAttribute (final long type, final TextureDescriptor<T> textureDescription, float offsetU,
                                                 float offsetV, float scaleU, float scaleV, int uvIndex) {
        this(type, textureDescription);
        this.offsetU = offsetU;
        this.offsetV = offsetV;
        this.scaleU = scaleU;
        this.scaleV = scaleV;
        this.uvIndex = uvIndex;
    }

    public <T extends Texture> PBRTextureAttribute (final long type, final TextureDescriptor<T> textureDescription, float offsetU,
                                                 float offsetV, float scaleU, float scaleV) {
        this(type, textureDescription, offsetU, offsetV, scaleU, scaleV, 0);
    }

    public PBRTextureAttribute (final long type, final Texture texture) {
        this(type);
        textureDescription.texture = texture;
    }

    public PBRTextureAttribute (final long type, final TextureRegion region) {
        this(type);
        set(region);
    }

    public PBRTextureAttribute (final PBRTextureAttribute copyFrom) {
        this(copyFrom.type, copyFrom.textureDescription, copyFrom.offsetU, copyFrom.offsetV, copyFrom.scaleU, copyFrom.scaleV,
                copyFrom.uvIndex);
    }

    public void set (final TextureRegion region) {
        textureDescription.texture = region.getTexture();
        offsetU = region.getU();
        offsetV = region.getV();
        scaleU = region.getU2() - offsetU;
        scaleV = region.getV2() - offsetV;
    }

    @Override
    public Attribute copy () {
        return new PBRTextureAttribute(this);
    }

    @Override
    public int hashCode () {
        int result = super.hashCode();
        result = 991 * result + textureDescription.hashCode();
        result = 991 * result + NumberUtils.floatToRawIntBits(offsetU);
        result = 991 * result + NumberUtils.floatToRawIntBits(offsetV);
        result = 991 * result + NumberUtils.floatToRawIntBits(scaleU);
        result = 991 * result + NumberUtils.floatToRawIntBits(scaleV);
        result = 991 * result + uvIndex;
        return result;
    }

    @Override
    public int compareTo (Attribute o) {
        if (type != o.type) return type < o.type ? -1 : 1;
        TextureAttribute other = (TextureAttribute)o;
        final int c = textureDescription.compareTo(other.textureDescription);
        if (c != 0) return c;
        if (uvIndex != other.uvIndex) return uvIndex - other.uvIndex;
        if (!MathUtils.isEqual(scaleU, other.scaleU)) return scaleU > other.scaleU ? 1 : -1;
        if (!MathUtils.isEqual(scaleV, other.scaleV)) return scaleV > other.scaleV ? 1 : -1;
        if (!MathUtils.isEqual(offsetU, other.offsetU)) return offsetU > other.offsetU ? 1 : -1;
        if (!MathUtils.isEqual(offsetV, other.offsetV)) return offsetV > other.offsetV ? 1 : -1;
        return 0;
    }

    public static PBRTextureAttribute createAlbedo (final Texture texture) {
        return new PBRTextureAttribute(Albedo, texture);
    }

    public static PBRTextureAttribute createAlbedo (final TextureRegion region) {
        return new PBRTextureAttribute(Albedo, region);
    }

    public static PBRTextureAttribute createMetallic (final Texture texture) {
        return new PBRTextureAttribute(Metallic, texture);
    }

    public static PBRTextureAttribute createMetallic (final TextureRegion region) {
        return new PBRTextureAttribute(Metallic, region);
    }

    public static PBRTextureAttribute createRoughness (final Texture texture) {
        return new PBRTextureAttribute(Roughness, texture);
    }

    public static PBRTextureAttribute createRoughness (final TextureRegion region) {
        return new PBRTextureAttribute(Roughness, region);
    }

    public static PBRTextureAttribute createAmbientOcclusion (final Texture texture) {
        return new PBRTextureAttribute(AmbientOcclusion, texture);
    }

    public static PBRTextureAttribute createAmbientOcclusion (final TextureRegion region) {
        return new PBRTextureAttribute(AmbientOcclusion, region);
    }

    public static PBRTextureAttribute createNormal (final Texture texture) {
        return new PBRTextureAttribute(Normal, texture);
    }

    public static PBRTextureAttribute createNormal (final TextureRegion region) {
        return new PBRTextureAttribute(Normal, region);
    }

    public static PBRTextureAttribute createHeight (final Texture texture) {
        return new PBRTextureAttribute(Height, texture);
    }

    public static PBRTextureAttribute createHeight (final TextureRegion region) {
        return new PBRTextureAttribute(Height, region);
    }
}
