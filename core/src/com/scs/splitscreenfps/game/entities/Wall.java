package com.scs.splitscreenfps.game.entities;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.procedural.world.PBRTextureAttribute;
import com.scs.basicecs.AbstractEntity;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.HasModelComponent;
import com.scs.splitscreenfps.game.components.PhysicsComponent;
import com.scs.splitscreenfps.game.components.PositionComponent;
import com.scs.splitscreenfps.game.systems.DrawModelSystem;

import ssmith.libgdx.ShapeHelper;

public class Wall extends AbstractEntity {

	private static Map<String,Material> materials=new HashMap<String, Material>();
/*
	static {
		materials.put("Generic", null);
		materials.put("Rough Rock", createMaterial("roughrockface4"));
		materials.put("Bricks", createMaterial("mybricks3"));
		materials.put("Rusted Iron", createMaterial("rustediron-streaks"));
		materials.put("Carved Stone", createMaterial("carvedlimestoneground1"));
		materials.put("Grass", createMaterial("grass1"));
		materials.put("Floor", createMaterial("cavefloor1"));

	}*/

	private static Material createMaterial(String materialName){
		Material material=new Material();
		material.set(PBRTextureAttribute.createAlbedo(new Texture("materials/" + materialName + "_Base_Color.png")));
		material.set(PBRTextureAttribute.createMetallic(new Texture("materials/" + materialName + "_Metallic.png")));
		material.set(PBRTextureAttribute.createRoughness(new Texture("materials/" + materialName + "_Roughness.png")));
		material.set(PBRTextureAttribute.createAmbientOcclusion(new Texture("materials/" + materialName + "_Ambient_Occlusion.png")));
		material.set(PBRTextureAttribute.createHeight(new Texture("materials/" + materialName + "_Height.png")));
		material.set(PBRTextureAttribute.createNormal(new Texture("materials/" + materialName + "_Normal.png")));

		return material;
	}
	
	
	public Wall(Game game, String name, Texture tex, float posX, float posY, float posZ, float w, float h, float d, float mass_pre, boolean tile, boolean cast_shadow) {
		this(game, name, tex, null, posX, posY, posZ, w, h, d, mass_pre, 0, 0, 0, tile, cast_shadow, true);
	}


	// Note that the mass gets multiplied by the size
	// Positions are from the centre
	public Wall(Game game, String name, Texture tex, Color c, float posX, float posY, float posZ, float w, float h, float d, float mass_pre, float degreesX, float degreesY, float degreesZ, boolean tile, boolean cast_shadow, boolean add_physics) {
		super(game.ecs, name);

		Material material = null;//createMaterial("mybricks3");
		if (tex != null) {
			tex.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
			material = new Material(TextureAttribute.createDiffuse(tex));
			//material.set(TextureAttribute.createDiffuse(tex));
		} else {
			material = new Material(ColorAttribute.createDiffuse(c));
		}
		//material.set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)); // Allow transparency, not currently required

		Model box_model = null;
		if (tile) {
			box_model = ShapeHelper.createCube(game.modelBuilder, w, h, d, material);
			Matrix3 mat = new Matrix3();
			float max2 = Math.max(w, h);
			float max = Math.max(max2, d);
			mat.scl(max);
			box_model.meshes.get(0).transformUV(mat);
		} else {
			box_model = ShapeHelper.createCube_AdvancedScaling(game.modelBuilder, w, h, d, material, 2f); // todo - tex scale param
		}
	
		ModelInstance instance = new ModelInstance(box_model, new Vector3(posX, posY, posZ));
		if (degreesX != 0) {
			instance.transform.rotate(Vector3.X, degreesX);
		}
		if (degreesY != 0) {
			instance.transform.rotate(Vector3.Y, degreesY);
		}
		if (degreesZ != 0) {
			instance.transform.rotate(Vector3.Z, degreesZ);
		}

		HasModelComponent model = new HasModelComponent(instance, 1f, cast_shadow);
		//model.shader = DrawModelSystem.pbrSadherTexture;
		this.addComponent(model);

		if (add_physics) {
			//if (mass_pre >= 0) {
			float mass = mass_pre * w * h * d;
			if (mass > 0 && mass < 1f) {
				mass = 1; // Give a minimum mass for (e.g.) thin walls
			}

			btBoxShape boxShape = new btBoxShape(new Vector3(w/2, h/2, d/2));
			Vector3 local_inertia = new Vector3();
			boxShape.calculateLocalInertia(mass, local_inertia);
			btRigidBody body = new btRigidBody(mass, null, boxShape, local_inertia);
			body.userData = this;
			body.setRestitution(.2f);
			body.setDamping(.5f, .5f);
			//groundObject.setFriction(.1f);
			body.setCollisionShape(boxShape);
			body.setWorldTransform(instance.transform);
			this.addComponent(new PhysicsComponent(body));
		}

		this.addComponent(new PositionComponent()); // todo - if no physics, set this position!
	}

}
