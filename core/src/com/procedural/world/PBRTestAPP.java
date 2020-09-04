package com.procedural.world;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.UBJsonReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PBRTestAPP extends ApplicationAdapter {
	public CameraInputController camController;
	PerspectiveCamera cam;
	ModelBatch modelBatch;
	Renderable obj;
	PBRShader pbrShader;
	PBRSadherTexture pbrSadherTexture;
	Stage stage;
	Skin skin;
	BitmapFont font;
	SpriteBatch spriteBatch;
	Map<String,Material> materials=new HashMap<String, Material>();
	List<String> matNames;
	int actMaterial=0;

	Material createMaterial(String materialName){
		Material material=new Material();
		material.set(PBRTextureAttribute.createAlbedo(new Texture("materials/" + materialName + "_Base_Color.png")));
		material.set(PBRTextureAttribute.createMetallic(new Texture("materials/" + materialName + "_Metallic.png")));
		material.set(PBRTextureAttribute.createRoughness(new Texture("materials/" + materialName + "_Roughness.png")));
		material.set(PBRTextureAttribute.createAmbientOcclusion(new Texture("materials/" + materialName + "_Ambient_Occlusion.png")));
		material.set(PBRTextureAttribute.createHeight(new Texture("materials/" + materialName + "_Height.png")));
		material.set(PBRTextureAttribute.createNormal(new Texture("materials/" + materialName + "_Normal.png")));

		return material;
	}
	
	@Override
	public void create () {
		pbrShader=new PBRShader();
		pbrShader.init();

		pbrSadherTexture=new PBRSadherTexture();
		pbrSadherTexture.init();

		materials.put("Generic", null);
		materials.put("Rough Rock", createMaterial("roughrockface4"));
		materials.put("Bricks", createMaterial("mybricks3"));
		materials.put("Rusted Iron", createMaterial("rustediron-streaks"));
		materials.put("Carved Stone", createMaterial("carvedlimestoneground1"));
		materials.put("Grass", createMaterial("grass1"));
		materials.put("Floor", createMaterial("cavefloor1"));

		matNames = new ArrayList<String>(materials.keySet());

		actMaterial=matNames.indexOf("Generic");

		modelBatch = new ModelBatch();
		spriteBatch=new SpriteBatch();

		float w=Gdx.graphics.getWidth();
		float h=Gdx.graphics.getHeight();
		float scale=w/1600;

		cam=new PerspectiveCamera(30,w, h);
		cam.position.set(12,14,0);
		cam.lookAt(0,2,0);
		cam.up.set(0,1,0);
		cam.update();
		camController = new CameraInputController(cam);

		font = new BitmapFont(Gdx.files.internal("font/my_font.fnt"),
				Gdx.files.internal("font/my_font.png"), false);

		skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

		CreateStage(w, h, scale);

		G3dModelLoader loader = new G3dModelLoader(new UBJsonReader());
		obj=createRenderableFromMesh(loader.loadModel(Gdx.files.internal("Mesh/teapot.g3db")).meshParts.get(0).mesh,
				new Material(ColorAttribute.createDiffuse(Color.BLUE)), pbrShader, null);
		obj.worldTransform.scl(5);
	}

	private void CreateStage(float w, float h, float scale) {
		if(stage!=null) stage.dispose();

		stage = new Stage();
		addMetallicSlider(w,h,scale);
		addRougnessSlider(w,h,scale);
		addAlbedoRSlider(w,h,scale);
		addAlbedoGSlider(w,h,scale);
		addAlbedoBSlider(w,h,scale);
		addOcclusionlider(w,h,scale);
		addTypeButton(w,h,scale);

		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(stage);
		multiplexer.addProcessor(camController);

		Gdx.input.setInputProcessor(multiplexer);
	}

	private void addTypeButton(float w, float h, float scale) {
		final TextButton btn=new TextButton("Generic",skin);
		Table tbRes = new Table();
		tbRes.add(btn).width(300*scale).height(60*scale);
		tbRes.setPosition(w-300*scale, 60*scale);
		stage.addActor(tbRes);

		btn.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				actMaterial++;
				if(actMaterial>=materials.size()) actMaterial=0;

				String matName=matNames.get(actMaterial);
				btn.setText(matName);

				obj.material=materials.get(matName);
				if(obj.material==null) {
					obj.shader=pbrShader;
					showHideSliders(true);
				} else {
					obj.shader=pbrSadherTexture;
					showHideSliders(false);
				}
			}
		});
	}

	Label lblRoug;
	Slider mroughnessS;
	Label lblOcl;
	Slider occlusionS;
	Label lblAB;
	Slider albedoBS;
	Label lblAG;
	Slider albedoGS;
	Label lblAR;
	Slider albedoRS;
	Label lblMet;
	Slider metallicS;

	private void showHideSliders(boolean show){
		lblRoug.setVisible(show);
		mroughnessS.setVisible(show);
		lblOcl.setVisible(show);
		occlusionS.setVisible(show);
		lblAB.setVisible(show);
		albedoBS.setVisible(show);
		lblAG.setVisible(show);
		albedoGS.setVisible(show);
		lblAR.setVisible(show);
		albedoRS.setVisible(show);
		lblMet.setVisible(show);
		metallicS.setVisible(show);
	}

	private void addMetallicSlider(float w, float h, float scale) {
		lblMet=new Label("Metallic:" , skin);
		lblMet.setFontScale(2.5f*scale);
		Table tbRes = new Table();
		tbRes.add(lblMet).height(60*scale);
		tbRes.setPosition(100*scale, h - 20*scale);
		stage.addActor(tbRes);

		metallicS = new Slider(0, 1, 0.05f, false, skin);
		metallicS.setValue(pbrShader.metallicValue);

		metallicS.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				pbrShader.metallicValue=metallicS.getValue();
			}
		});

		tbRes = new Table();
		tbRes.add(metallicS).width(w-50*scale).height(60*scale);
		tbRes.setPosition(800*scale, h - 50*scale);
		stage.addActor(tbRes);
	}

	private void addAlbedoRSlider(float w, float h, float scale) {
		lblAR=new Label("Albedo-R:" , skin);
		lblAR.setFontScale(2.5f*scale);
		Table tbRes = new Table();
		tbRes.add(lblAR).height(60*scale);
		tbRes.setPosition(115*scale, h - 140*scale);
		stage.addActor(tbRes);

		albedoRS = new Slider(0, 1, 0.05f, false, skin);
		albedoRS.setValue(pbrShader.albedoColor.x);

		albedoRS.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				pbrShader.albedoColor.x=albedoRS.getValue();
			}
		});

		tbRes = new Table();
		tbRes.add(albedoRS).width((w-150*scale)/3f).height(60*scale);
		tbRes.setPosition(800/3*scale, h - 170*scale);
		stage.addActor(tbRes);
	}

	private void addAlbedoGSlider(float w, float h, float scale) {
		lblAG=new Label("Albedo-G:" , skin);
		lblAG.setFontScale(2.5f*scale);
		Table tbRes = new Table();
		tbRes.add(lblAG).height(60);
		tbRes.setPosition(115*scale+(w-150*scale)/3f+50*scale, h - 140*scale);
		stage.addActor(tbRes);

		albedoGS = new Slider(0, 1, 0.05f, false, skin);
		albedoGS.setValue(pbrShader.albedoColor.y);

		albedoGS.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				pbrShader.albedoColor.y=albedoGS.getValue();
			}
		});

		tbRes = new Table();
		tbRes.add(albedoGS).width((w-150*scale)/3f).height(60*scale);
		tbRes.setPosition(800*scale/3+(w-150*scale)/3f+50*scale, h - 170*scale);
		stage.addActor(tbRes);
	}

	private void addAlbedoBSlider(float w, float h, float scale) {
		lblAB=new Label("Albedo-B:" , skin);
		lblAB.setFontScale(2.5f*scale);
		Table tbRes = new Table();
		tbRes.add(lblAB).height(60*scale);
		tbRes.setPosition(115*scale+2*((w-150*scale)/3f+50*scale), h - 140*scale);
		stage.addActor(tbRes);

		albedoBS = new Slider(0, 1, 0.05f, false, skin);
		albedoBS.setValue(pbrShader.albedoColor.z);

		albedoBS.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				pbrShader.albedoColor.z=albedoBS.getValue();
			}
		});

		tbRes = new Table();
		tbRes.add(albedoBS).width((w-150*scale)/3f).height(60*scale);
		tbRes.setPosition(800*scale/3+2*((w-150*scale)/3f+50*scale), h - 170*scale);
		stage.addActor(tbRes);
	}

	private void addOcclusionlider(float w, float h, float scale) {
		lblOcl=new Label("Occlusion:" , skin);
		lblOcl.setFontScale(2.5f*scale);
		Table tbRes = new Table();
		tbRes.add(lblOcl).height(60*scale);
		tbRes.setPosition(120*scale, h - 200*scale);
		stage.addActor(tbRes);

		occlusionS = new Slider(0, 1, 0.05f, false, skin);
		occlusionS.setValue(pbrShader.ambientOcclusionValue);

		occlusionS.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				pbrShader.ambientOcclusionValue=occlusionS.getValue();
			}
		});

		tbRes = new Table();
		tbRes.add(occlusionS).width((w-50*scale)).height(60*scale);
		tbRes.setPosition(800*scale, h - 230*scale);
		stage.addActor(tbRes);
	}

	private void addRougnessSlider(float w, float h, float scale) {
		lblRoug=new Label("Roughness:" , skin);
		lblRoug.setFontScale(2.5f*scale);
		Table tbRes = new Table();
		tbRes.add(lblRoug).height(120*scale);
		tbRes.setPosition(130*scale, h - 80*scale);
		stage.addActor(tbRes);

		mroughnessS = new Slider(0.6f, 1, 0.01f, false, skin);
		mroughnessS.setValue(pbrShader.rougness);

		mroughnessS.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				pbrShader.rougness=mroughnessS.getValue();
			}
		});

		tbRes = new Table();
		tbRes.add(mroughnessS).width((w-50*scale)).height(60*scale);
		tbRes.setPosition(800*scale, h - 110*scale);
		stage.addActor(tbRes);
	}

	public static Renderable createRenderableFromMesh(Mesh mesh, Material material, Shader shader, Environment environment) {
		Renderable outRend=new Renderable();
		outRend.meshPart.mesh=mesh;
		outRend.meshPart.primitiveType=GL20.GL_TRIANGLES;
		if(material!=null) outRend.material=material;
		if(environment!=null) outRend.environment=environment;
		outRend.meshPart.offset=0;
		//strada.shader=elrShader;
		if(shader!=null) outRend.shader=shader;
		outRend.meshPart.size=mesh.getNumIndices();
		return outRend;
	}

	float w=-1;
	float h=-1;

	@Override
	public void render () {
		if(w>0 && Gdx.graphics.getWidth()>0 && (w!=Gdx.graphics.getWidth() || h!=Gdx.graphics.getHeight())){
			w = Gdx.graphics.getWidth();
			h = Gdx.graphics.getHeight();
			CreateStage(w, h, w/1600);
		}
		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();

		camController.update();
		stage.act();

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		modelBatch.begin(cam);
		modelBatch.render(obj);
		modelBatch.end();

		stage.draw();

		spriteBatch.begin();
		font.draw(spriteBatch, "fps: " + Gdx.graphics.getFramesPerSecond(), 20, 30);
		spriteBatch.end();
	}
	
	@Override
	public void dispose () {
		modelBatch.dispose();
	}
}
