package com.procedural.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cubemap;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.utils.IntIntMap;


/**
 * Created by PWorld on 09/08/2016.
 */
public class PBRShader implements Shader {
    ShaderProgram program;
    Camera camera;
    RenderContext context;
    Cubemap ref;

    int u_worldTrans;
    int u_projTrans;

    int vLight0;

    int albedo;
    int metallic;

    int sCubemapTexture;
    int	vRoughness;

    int ambientOcclusion;

    public Vector3 albedoColor=new Vector3(0.f,0.f,0.9f);
    public float metallicValue=0.5f;
    public float rougness=0.9f;
    public float ambientOcclusionValue=1;

    Mesh currentMesh;

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

        String vert = Gdx.files.internal("Shaders/VS_ShaderPlain.vsh").readString();
        String frag = Gdx.files.internal("Shaders/ShaderPlain.fsh").readString();
        program = new ShaderProgram(vert, frag);
        if (!program.isCompiled()){
            throw new GdxRuntimeException(program.getLog());
        }
        u_worldTrans = program.getUniformLocation("u_worldTrans");
        u_projTrans = program.getUniformLocation("u_projTrans");
        vLight0 = program.getUniformLocation("vLight0");
        albedo = program.getUniformLocation("albedo");
        metallic = program.getUniformLocation("metallic");
        sCubemapTexture = program.getUniformLocation("sCubemapTexture");
        vRoughness = program.getUniformLocation("vRoughness");
        ambientOcclusion = program.getUniformLocation("ambientOcclusion");
    }

    @Override
    public void end() {
        if (currentMesh != null) {
            currentMesh.unbind(program, tempArray.items);
            currentMesh = null;
        }
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
        program.setUniformf(albedo, albedoColor);
        program.setUniformf(metallic, metallicValue);
        program.setUniformf(ambientOcclusion, ambientOcclusionValue);
        program.setUniformf(vLight0, new Vector3(2.00f,-2.00f,-2.00f));

        ref.bind(0);
        program.setUniformi(sCubemapTexture, 0);

        program.setUniformf(vRoughness, new Vector2(rougness,5));

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
