package com.scs.splitscreenfps.game.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btGhostObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject.CollisionFlags;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.scs.basicecs.AbstractEntity;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.CanBeCarriedComponent;
import com.scs.splitscreenfps.game.components.HasDecal;
import com.scs.splitscreenfps.game.components.HasModelComponent;
import com.scs.splitscreenfps.game.components.IsCollectableComponent;
import com.scs.splitscreenfps.game.components.IsPackageComponent;
import com.scs.splitscreenfps.game.components.PhysicsComponent;
import com.scs.splitscreenfps.game.components.PositionComponent;
import com.scs.splitscreenfps.game.systems.CollectableSystem;

import ssmith.libgdx.GraphicsHelper;
import ssmith.libgdx.ShapeHelper;

public class EquipmentEntityFactory {

	public static AbstractEntity createHealthPack(Game game, Vector3 start) {
		AbstractEntity e = new AbstractEntity(game.ecs, "HealthPack");

		e.addComponent(new IsCollectableComponent(CollectableSystem.CollectableType.HealthPack));

		e.addComponent(new PositionComponent());

		HasDecal hasDecal = new HasDecal();
		hasDecal.decal = GraphicsHelper.DecalHelper(game.getTexture("textures/PowerUp/PowerUp_06.png"), 0.4f);
		hasDecal.faceCamera = true;
		hasDecal.dontLockYAxis = true;
		e.addComponent(hasDecal);

		// Add physics
		btBoxShape shape = new btBoxShape(new Vector3(.1f, .1f, .1f));
		btGhostObject body = new btGhostObject();//0, null, shape);
		body.setCollisionFlags(CollisionFlags.CF_NO_CONTACT_RESPONSE);
		body.userData = e;
		body.setCollisionShape(shape);
		Matrix4 mat = new Matrix4();
		mat.setTranslation(start);
		body.setWorldTransform(mat);
		PhysicsComponent pc = new PhysicsComponent(body);
		//pc.disable_gravity = true; Not available for ghost objects
		e.addComponent(pc);

		return e;
	}


	public static AbstractEntity createLootBox(Game game, float posX, float posY, float posZ, CollectableSystem.CollectableType type) {
		AbstractEntity lootbox = new AbstractEntity(game.ecs, "LootBox");

		float w = .5f;
		float h = .5f;
		float d = .5f;

		lootbox.addComponent(new IsCollectableComponent(type));

		TextureRegion tex = game.getTexture("textures/spritesforyou.png", 8, 8, 0, 4);
		Material material = new Material(TextureAttribute.createDiffuse(tex));
		ModelBuilder modelBuilder = game.modelBuilder;
		Model box_model = ShapeHelper.createCube(modelBuilder, w, h, d, material);
		ModelInstance instance = new ModelInstance(box_model, new Vector3(posX, posY, posZ));
		HasModelComponent model = new HasModelComponent(instance, 1f, true);
		lootbox.addComponent(model);

		lootbox.addComponent(new PositionComponent());

		btBoxShape boxShape = new btBoxShape(new Vector3(w/2, h/2, d/2));
		Vector3 local_inertia = new Vector3();
		boxShape.calculateLocalInertia(1f, local_inertia);
		btRigidBody body = new btRigidBody(w*h*d, null, boxShape, local_inertia);
		body.userData = lootbox;
		body.setRestitution(.1f);
		body.setCollisionShape(boxShape);
		body.setWorldTransform(instance.transform);
		lootbox.addComponent(new PhysicsComponent(body));

		return lootbox;
	}


	public static AbstractEntity createPickup(Game game, float posX, float posY, float posZ) {
		AbstractEntity pickup = new AbstractEntity(game.ecs, "Pickup");

		float w = .5f;
		float h = .5f;
		float d = .5f;

		pickup.addComponent(new CanBeCarriedComponent());

		TextureRegion tex = game.getTexture("textures/spritesforyou.png", 8, 8, 0, 4);
		Material black_material = new Material(TextureAttribute.createDiffuse(tex));
		ModelBuilder modelBuilder = game.modelBuilder;

		Model box_model = ShapeHelper.createCube(modelBuilder, w, h, d, black_material);
		ModelInstance instance = new ModelInstance(box_model, new Vector3(posX, posY, posZ));
		HasModelComponent model = new HasModelComponent(instance, 1f, true);
		pickup.addComponent(model);

		pickup.addComponent(new PositionComponent());

		btBoxShape boxShape = new btBoxShape(new Vector3(w/2, h/2, d/2));
		Vector3 local_inertia = new Vector3();
		boxShape.calculateLocalInertia(1f, local_inertia);
		btRigidBody groundObject = new btRigidBody(1, null, boxShape, local_inertia);
		groundObject.userData = pickup;
		groundObject.setRestitution(.5f);
		groundObject.setCollisionShape(boxShape);
		groundObject.setWorldTransform(instance.transform);
		pickup.addComponent(new PhysicsComponent(groundObject));

		return pickup;
	}


	public static AbstractEntity createPackage(Game game, float posX, float posY, float posZ, int type) {
		AbstractEntity pkg = new AbstractEntity(game.ecs, "Package");

		float w = .5f;
		float h = .5f;
		float d = .5f;

		pkg.addComponent(new CanBeCarriedComponent());

		String tex = "";
		switch (type) {
		case 0:
			tex = "colours/red.png"; 
			break;
		case 1:
			tex = "colours/yellow.png"; 
			break;
		case 2:
			tex = "colours/green.png"; 
			break;
		case 3:
			tex = "colours/magenta.png"; 
			break;
		default:
			throw new RuntimeException("Unknown type: " + type);
		}

		Material material = new Material(TextureAttribute.createDiffuse(game.getTexture(tex)));
		ModelBuilder modelBuilder = game.modelBuilder;

		Model box_model = ShapeHelper.createCube(modelBuilder, w, h, d, material);
		ModelInstance instance = new ModelInstance(box_model, new Vector3(posX, posY, posZ));
		HasModelComponent model = new HasModelComponent(instance, 1f, true);
		pkg.addComponent(model);

		pkg.addComponent(new PositionComponent());

		btBoxShape boxShape = new btBoxShape(new Vector3(w/2, h/2, d/2));
		Vector3 local_inertia = new Vector3();
		boxShape.calculateLocalInertia(1f, local_inertia);
		btRigidBody groundObject = new btRigidBody(1, null, boxShape, local_inertia);
		groundObject.userData = pkg;
		groundObject.setRestitution(.5f);
		groundObject.setCollisionShape(boxShape);
		groundObject.setWorldTransform(instance.transform);
		pkg.addComponent(new PhysicsComponent(groundObject));

		pkg.addComponent(new IsPackageComponent(type));

		return pkg;
	}}
