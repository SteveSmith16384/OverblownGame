package com.procedural.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Cubemap;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.IntIntMap;

/**
 * Created by pWorld on 12/08/2016.
 */
public class PBRSadherTexture implements Shader {
    ShaderProgram program;
    Camera camera;
    RenderContext context;
    Cubemap ref;

    int u_worldTrans;
    int u_projTrans;

    int vLight0;

    int albedoTexture;
    int metallicTexture;

    int normalTexture;
    int	heightTexture;

    int sCubemapTexture;
    int	roughnessTexture;

    int ambientOcclusionTexture;

    Mesh currentMesh;
    Material currentMaterial;

    public void loadReflection(String refl){
        ref=new Cubemap(Gdx.files.internal("cubemaps/" + refl + "_c00.bmp"), Gdx.files.internal("cubemaps/" + refl + "_c01.bmp"),
                Gdx.files.internal("cubemaps/" + refl + "_c02.bmp"), Gdx.files.internal("cubemaps/" + refl + "_c03.bmp"),
                Gdx.files.internal("cubemaps/" + refl + "_c04.bmp"), Gdx.files.internal("cubemaps/" + refl + "_c05.bmp"));
        ref.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        ref.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
    }

    @Override
    public void init() {
        loadReflection("rnl_phong_m00");

        String vert = Gdx.files.internal("Shaders/VS_PBR_ShaderTexture.vsh").readString();
        String frag = Gdx.files.internal("Shaders/PBR_ShaderTexture.fsh").readString();
        program = new ShaderProgram(vert, frag);
        if (!program.isCompiled()){
            throw new GdxRuntimeException(program.getLog());
        }
        u_worldTrans = program.getUniformLocation("u_worldTrans");
        u_projTrans = program.getUniformLocation("u_projTrans");
        vLight0 = program.getUniformLocation("vLight0");
        albedoTexture = program.getUniformLocation("albedoTexture");
        metallicTexture = program.getUniformLocation("metallicTexture");
        sCubemapTexture = program.getUniformLocation("sCubemapTexture");
        roughnessTexture = program.getUniformLocation("roughnessTexture");
        ambientOcclusionTexture = program.getUniformLocation("ambientOcclusionTexture");
        normalTexture = program.getUniformLocation("normalTexture");
        heightTexture = program.getUniformLocation("heightTexture");
    }

    @Override
    public void end() {
        if (currentMesh != null) {
            currentMesh.unbind(program, tempArray.items);
            currentMesh = null;
        }
        currentMaterial=null;
        program.end();
    }

    @Override
    public boolean canRender(Renderable instance) {
        return true;
    }

    @Override
    public void begin(Camera camera, RenderContext context) {
        this.camera=camera;
        this.context=context;

        program.begin();
        program.setUniformMatrix(u_projTrans, camera.combined);

        context.setDepthTest(GL20.GL_LEQUAL); /* context.setDepthTest(0) per disabilitare */
        context.setCullFace(GL20.GL_BACK);
    }

    @Override
    public int compareTo(Shader other) {
        return 0;
    }

    protected final IntArray tempArray = new IntArray();
    protected final int[] getAttributeLocations(Renderable renderable) {
        final IntIntMap attributes = new IntIntMap();
        final VertexAttributes attrs = renderable.meshPart.mesh.getVertexAttributes();
        final int c = attrs.size();
        for (int i = 0; i < c; i++) {
            final VertexAttribute attr = attrs.get(i);
            final int location = program.getAttributeLocation(attr.alias);
            if (location >= 0)
                attributes.put(attr.getKey(), location);
        }

        tempArray.clear();
        final int n = attrs.size();
        for (int i = 0; i < n; i++) {
            tempArray.add(attributes.get(attrs.get(i).getKey(), -1));
        }
        return tempArray.items;
    }

    @Override
    public void render(Renderable renderable) {
        program.setUniformMatrix(u_worldTrans, renderable.worldTransform);

        if(currentMaterial!=renderable.material){
            currentMaterial=renderable.material;

            ((PBRTextureAttribute)currentMaterial.get(PBRTextureAttribute.Albedo)).textureDescription.texture.bind(1);
            program.setUniformi(albedoTexture, 1);
            ((PBRTextureAttribute)currentMaterial.get(PBRTextureAttribute.Metallic)).textureDescription.texture.bind(2);
            program.setUniformi(metallicTexture, 2);
            ((PBRTextureAttribute)currentMaterial.get(PBRTextureAttribute.AmbientOcclusion)).textureDescription.texture.bind(3);
            program.setUniformi(ambientOcclusionTexture, 3);
            ((PBRTextureAttribute)currentMaterial.get(PBRTextureAttribute.Roughness)).textureDescription.texture.bind(4);
            program.setUniformi(roughnessTexture, 4);
            ((PBRTextureAttribute)currentMaterial.get(PBRTextureAttribute.Normal)).textureDescription.texture.bind(5);
            program.setUniformi(normalTexture, 5);
            ((PBRTextureAttribute)currentMaterial.get(PBRTextureAttribute.Height)).textureDescription.texture.bind(6);
            program.setUniformi(heightTexture, 6);
        }


        program.setUniformf(vLight0, new Vector3(2.00f,-2.00f,-2.00f));

        ref.bind(0);
        program.setUniformi(sCubemapTexture, 0);

        if (currentMesh != renderable.meshPart.mesh) {
            if (currentMesh != null)
                currentMesh.unbind(program, tempArray.items);
            currentMesh = renderable.meshPart.mesh;
            currentMesh.bind(program, getAttributeLocations(renderable));
        }

        renderable.meshPart.mesh.render(program,
                renderable.meshPart.primitiveType,
                renderable.meshPart.offset,
                renderable.meshPart.size,false);
    }

    @Override
    public void dispose() {

    }
}
