package com.scs.splitscreenfps.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCylinderShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.BillBoardFPS_Main;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.components.ExplodeAfterTimeComponent;
import com.scs.splitscreenfps.game.components.ExplodeOnContactComponent;
import com.scs.splitscreenfps.game.components.HasDecal;
import com.scs.splitscreenfps.game.components.HasModelComponent;
import com.scs.splitscreenfps.game.components.IsBulletComponent;
import com.scs.splitscreenfps.game.components.PhysicsComponent;
import com.scs.splitscreenfps.game.components.PlayerData;
import com.scs.splitscreenfps.game.components.PositionComponent;
import com.scs.splitscreenfps.game.components.RemoveEntityAfterTimeComponent;
import com.scs.splitscreenfps.game.components.WeaponSettingsComponent;

import ssmith.libgdx.GraphicsHelper;
import ssmith.libgdx.ModelFunctions;
import ssmith.libgdx.ShapeHelper;

public class EntityFactory {

	public static AbstractEntity createBullet(BasicECS ecs, AbstractEntity shooter, Vector3 start, Vector3 dir) {
		AbstractEntity e = new AbstractEntity(ecs, "Bullet");

		e.addComponent(new PositionComponent());

		PlayerData playerData = (PlayerData)shooter.getComponent(PlayerData.class);
		WeaponSettingsComponent settings = (WeaponSettingsComponent)shooter.getComponent(WeaponSettingsComponent.class);

		HasDecal hasDecal = new HasDecal();
		if (playerData.playerIdx == 0) {
			hasDecal.decal = GraphicsHelper.DecalHelper("laser_bolt_red.png", 0.2f);
		} else if (playerData.playerIdx == 1) {
			hasDecal.decal = GraphicsHelper.DecalHelper("laser_bolt_yellow.png", 0.2f);
		} else if (playerData.playerIdx == 2) {
			hasDecal.decal = GraphicsHelper.DecalHelper("laser_bolt_magenta.png", 0.2f);
		} else if (playerData.playerIdx == 3) {
			hasDecal.decal = GraphicsHelper.DecalHelper("laser_bolt_green.png", 0.2f);
		} else {
			throw new RuntimeException("Invalid side: " + playerData.playerIdx);
		}
		hasDecal.decal.setPosition(start);
		hasDecal.faceCamera = true;
		hasDecal.dontLockYAxis = false;
		e.addComponent(hasDecal);

		e.addComponent(new IsBulletComponent(shooter, playerData.playerIdx, start, settings, true));

		// Add physics
		btBoxShape shape = new btBoxShape(new Vector3(.1f, .1f, .1f));
		btRigidBody body = new btRigidBody(.1f, null, shape);
		body.userData = e;
		//body.setFriction(0);
		//body.setRestitution(.9f);
		body.setCollisionShape(shape);
		Matrix4 mat = new Matrix4();
		mat.setTranslation(start);
		body.setWorldTransform(mat);
		//body.applyCentralForce(offset.scl(100));
		//body.applyCentralImpulse(offset.scl(10));
		//body.setGravity(new Vector3());
		PhysicsComponent pc = new PhysicsComponent(body);
		pc.disable_gravity = true;
		pc.force = dir.scl(1.5f);
		e.addComponent(pc);

		BillBoardFPS_Main.audio.play("sfx/Futuristic Shotgun Single Shot.wav");

		return e;
	}


	public static AbstractEntity createRocket(BasicECS ecs, AbstractEntity shooter, Vector3 start, Vector3 dir) {
		AbstractEntity e = new AbstractEntity(ecs, "Rocket");

		e.addComponent(new PositionComponent());

		PlayerData playerData = (PlayerData)shooter.getComponent(PlayerData.class);
		WeaponSettingsComponent settings = (WeaponSettingsComponent)shooter.getComponent(WeaponSettingsComponent.class);

		HasDecal hasDecal = new HasDecal();
		if (playerData.playerIdx == 0) {
			hasDecal.decal = GraphicsHelper.DecalHelper("laser_bolt_red.png", 0.2f);
		} else if (playerData.playerIdx == 1) {
			hasDecal.decal = GraphicsHelper.DecalHelper("laser_bolt_yellow.png", 0.2f);
		} else if (playerData.playerIdx == 2) {
			hasDecal.decal = GraphicsHelper.DecalHelper("laser_bolt_magenta.png", 0.2f);
		} else if (playerData.playerIdx == 3) {
			hasDecal.decal = GraphicsHelper.DecalHelper("laser_bolt_green.png", 0.2f);
		} else {
			throw new RuntimeException("Invalid side: " + playerData.playerIdx);
		}
		hasDecal.decal.setPosition(start);
		hasDecal.faceCamera = true;
		hasDecal.dontLockYAxis = false;
		e.addComponent(hasDecal);

		e.addComponent(new IsBulletComponent(shooter, playerData.playerIdx, start, settings, true));
		e.addComponent(new ExplodeOnContactComponent());

		// Add physics
		btBoxShape shape = new btBoxShape(new Vector3(.1f, .1f, .1f));
		btRigidBody body = new btRigidBody(.1f, null, shape);
		body.userData = e;
		//body.setFriction(0);
		//body.setRestitution(.9f);
		body.setCollisionShape(shape);
		Matrix4 mat = new Matrix4();
		mat.setTranslation(start);
		body.setWorldTransform(mat);
		//body.applyCentralForce(offset.scl(100));
		//body.applyCentralImpulse(offset.scl(10));
		//body.setGravity(new Vector3());
		PhysicsComponent pc = new PhysicsComponent(body);
		pc.disable_gravity = true;
		pc.force = dir.scl(1f);
		e.addComponent(pc);

		BillBoardFPS_Main.audio.play("sfx/Futuristic Shotgun Single Shot.wav");

		return e;
	}


	public static AbstractEntity createFist(BasicECS ecs, AbstractEntity shooter, Vector3 start, Vector3 dir) {
		AbstractEntity e = new AbstractEntity(ecs, "Punch");

		e.addComponent(new PositionComponent());

		PlayerData playerData = (PlayerData)shooter.getComponent(PlayerData.class);
		WeaponSettingsComponent settings = (WeaponSettingsComponent)shooter.getComponent(WeaponSettingsComponent.class);

		if (Settings.DEBUG_PUNCH) {
			HasDecal hasDecal = new HasDecal();
			if (playerData.playerIdx == 0) {
				hasDecal.decal = GraphicsHelper.DecalHelper("laser_bolt_red.png", 0.2f);
			} else if (playerData.playerIdx == 1) {
				hasDecal.decal = GraphicsHelper.DecalHelper("laser_bolt_yellow.png", 0.2f);
			} else if (playerData.playerIdx == 2) {
				hasDecal.decal = GraphicsHelper.DecalHelper("laser_bolt_magenta.png", 0.2f);
			} else if (playerData.playerIdx == 3) {
				hasDecal.decal = GraphicsHelper.DecalHelper("laser_bolt_green.png", 0.2f);
			} else {
				throw new RuntimeException("Invalid side: " + playerData.playerIdx);
			}
			hasDecal.decal.setPosition(start);
			hasDecal.faceCamera = true;
			hasDecal.dontLockYAxis = false;
			e.addComponent(hasDecal);
		}

		e.addComponent(new IsBulletComponent(shooter, playerData.playerIdx, start, settings, true));

		// Add physics
		btBoxShape shape = new btBoxShape(new Vector3(.1f, .1f, .1f));
		btRigidBody body = new btRigidBody(10f, null, shape);
		body.userData = e;
		//body.setFriction(0);
		//body.setRestitution(.9f);
		body.setCollisionShape(shape);
		Matrix4 mat = new Matrix4();
		mat.setTranslation(start);
		body.setWorldTransform(mat);
		//body.applyCentralForce(offset.scl(100));
		//body.applyCentralImpulse(offset.scl(10));
		//body.setGravity(new Vector3());
		PhysicsComponent pc = new PhysicsComponent(body);
		pc.disable_gravity = true;
		pc.force = dir.scl(100f);
		e.addComponent(pc);

		BillBoardFPS_Main.audio.play("sfx/Futuristic Shotgun Single Shot.wav");

		return e;
	}


	public static AbstractEntity createGrenade(BasicECS ecs, AbstractEntity shooter, Vector3 start, Vector3 dir) {
		AbstractEntity e = new AbstractEntity(ecs, "Grenade");

		e.addComponent(new PositionComponent());

		PlayerData playerData = (PlayerData)shooter.getComponent(PlayerData.class);
		WeaponSettingsComponent settings = (WeaponSettingsComponent)shooter.getComponent(WeaponSettingsComponent.class);

		HasDecal hasDecal = new HasDecal();
		if (playerData.playerIdx == 0) {
			hasDecal.decal = GraphicsHelper.DecalHelper("laser_bolt_red.png", 0.2f);
		} else if (playerData.playerIdx == 1) {
			hasDecal.decal = GraphicsHelper.DecalHelper("laser_bolt_yellow.png", 0.2f);
		} else if (playerData.playerIdx == 2) {
			hasDecal.decal = GraphicsHelper.DecalHelper("laser_bolt_magenta.png", 0.2f);
		} else if (playerData.playerIdx == 3) {
			hasDecal.decal = GraphicsHelper.DecalHelper("laser_bolt_green.png", 0.2f);
		} else {
			throw new RuntimeException("Invalid side: " + playerData.playerIdx);
		}

		// scs new hasDecal.decal.setPosition(pos.position);
		hasDecal.faceCamera = true;
		hasDecal.dontLockYAxis = true;
		e.addComponent(hasDecal);

		e.addComponent(new IsBulletComponent(shooter, playerData.playerIdx, start, settings, false));

		e.addComponent(new ExplodeAfterTimeComponent(3000, settings.expl_force));

		// Add physics
		btSphereShape shape = new btSphereShape(.1f);
		btRigidBody body = new btRigidBody(.1f, null, shape);
		body.userData = e;
		body.setFriction(0.9f);
		body.setRestitution(.6f);
		body.setCollisionShape(shape);
		Matrix4 mat = new Matrix4();
		mat.setTranslation(start);
		body.setWorldTransform(mat);
		PhysicsComponent pc = new PhysicsComponent(body);
		pc.force = dir.scl(1f);
		e.addComponent(pc);

		BillBoardFPS_Main.audio.play("sfx/Futuristic Shotgun Single Shot.wav");

		return e;
	}


	public static AbstractEntity createCrate(BasicECS ecs, String tex_filename, float posX, float posY, float posZ, float w, float h, float d) {
		AbstractEntity crate = new AbstractEntity(ecs, "Crate");

		Material black_material = new Material(TextureAttribute.createDiffuse(new Texture(tex_filename)));
		ModelBuilder modelBuilder = new ModelBuilder();
		Model box_model = modelBuilder.createBox(w, h, d, black_material, VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates);

		ModelInstance instance = new ModelInstance(box_model, new Vector3(posX, posY, posZ));

		HasModelComponent model = new HasModelComponent(instance, 1f);
		crate.addComponent(model);

		btBoxShape boxShape = new btBoxShape(new Vector3(w/2, h/2, d/2));
		Vector3 local_inertia = new Vector3();
		boxShape.calculateLocalInertia(1f, local_inertia);
		btRigidBody groundObject = new btRigidBody(w*h*d, null, boxShape, local_inertia);
		groundObject.userData = crate;
		groundObject.setRestitution(.5f);
		groundObject.setCollisionShape(boxShape);
		groundObject.setWorldTransform(instance.transform);
		crate.addComponent(new PhysicsComponent(groundObject));

		return crate;
	}


	public static AbstractEntity createBall(BasicECS ecs, String tex_filename, float posX, float posY, float posZ, float diam, float mass_pre) {
		AbstractEntity ball = new AbstractEntity(ecs, "Ball");

		Material black_material = new Material(TextureAttribute.createDiffuse(new Texture(tex_filename)));
		//Material black_material = new Material(TextureAttribute.createDiffuse(new Texture("textures/neon/tron_yellow.jpg")));
		ModelBuilder modelBuilder = new ModelBuilder();
		Model sphere_model = modelBuilder.createSphere(diam,  diam,  diam, 10, 10, black_material, VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates);

		ModelInstance instance = new ModelInstance(sphere_model, new Vector3(posX, posY, posZ));

		HasModelComponent model = new HasModelComponent(instance, 1f);
		ball.addComponent(model);

		float mass = (float)((4/3) * Math.PI * ((diam/2) * (diam/2) * (diam/2)));

		btSphereShape sphere_shape = new btSphereShape(diam/2);
		Vector3 local_inertia = new Vector3();
		sphere_shape.calculateLocalInertia(1f, local_inertia);
		btRigidBody groundObject = new btRigidBody(mass, null, sphere_shape, local_inertia);
		groundObject.userData = ball;
		groundObject.setRestitution(.5f);
		groundObject.setCollisionShape(sphere_shape);
		groundObject.setWorldTransform(instance.transform);
		ball.addComponent(new PhysicsComponent(groundObject));

		return ball;
	}

	/*
	public static AbstractEntity createDoorway(BasicECS ecs, float posX, float posY, float posZ) {
		AbstractEntity doorway = new AbstractEntity(ecs, "Doorway");

		ModelInstance instance = ModelFunctions.loadModel("models/magicavoxel/doorway.obj", false);

		HasModelComponent hasModel = new HasModelComponent(instance);//, offset, 0, scale);
		doorway.addComponent(hasModel);

		instance.transform.setTranslation(posX, posY, posZ);

		//PositionComponent pos = new PositionComponent(posX, posY, posZ);
		//doorway.addComponent(pos);

		btCollisionShape shape = Bullet.obtainStaticNodeShape(instance.nodes);
		btRigidBody body = new btRigidBody(0, null, shape);
		body.userData = doorway;
		body.setCollisionShape(shape);
		body.setWorldTransform(instance.transform);
		doorway.addComponent(new PhysicsComponent(body));

		return doorway;
	}

/*
	public static AbstractEntity createGun(BasicECS ecs, float posX, float posY, float posZ) {
		AbstractEntity stairs = new AbstractEntity(ecs, "Stairs");

		ModelInstance instance = ModelFunctions.loadModel("models/kenney/machinegun.g3db", false);
		//float scale = 1f;//ModelFunctions.getScaleForHeight(instance, .8f);
		//instance.transform.scl(scale);
		//Vector3 offset = new Vector3();//ModelFunctions.getOrigin(instance);
		//offset.y -= .3f; // Hack since model is too high

		HasModelComponent hasModel = new HasModelComponent(instance);//, offset, 0, scale);
		stairs.addComponent(hasModel);

		instance.transform.setTranslation(posX, posY, posZ);

		//PositionComponent pos = new PositionComponent(posX, posY, posZ);
		//doorway.addComponent(pos);

		btCollisionShape shape = Bullet.obtainStaticNodeShape(instance.nodes);
		btRigidBody body = new btRigidBody(1f, null, shape);
		body.userData = stairs;
		body.setCollisionShape(shape);
		body.setWorldTransform(instance.transform);
		stairs.addComponent(new PhysicsComponent(body));

		return stairs;
	}
	 */


	// Note that the mass gets multiplied by the size
	public static AbstractEntity createModel(BasicECS ecs, String name, String filename, float posX, float posY, float posZ, float mass) {
		AbstractEntity stairs = new AbstractEntity(ecs, name);

		ModelInstance instance = ModelFunctions.loadModel(filename, false);

		// todo - remove?
		//TextureAttribute textureAttribute1 = TextureAttribute.createDiffuse(new Texture("textures/seamlessTextures2/IMGP5511_seamless.jpg"));
		//TextureAttribute textureAttribute1 = new TextureAttribute(TextureAttribute.Diffuse, new Texture("textures/seamlessTextures2/IMGP5511_seamless.jpg"));
		//Material black_material = new Material(TextureAttribute.createDiffuse(new Texture("textures/seamlessTextures2/IMGP5511_seamless.jpg")));
		//instance.model.materials.get(0).set(black_material);

		instance.transform.setTranslation(posX, posY, posZ);

		/*if (axis != null) {
			instance.transform.rotate(axis, degrees);
		}*/

		HasModelComponent model = new HasModelComponent(instance, 1f);
		stairs.addComponent(model);

		btCollisionShape shape = Bullet.obtainStaticNodeShape(instance.nodes);
		Vector3 local_inertia = new Vector3();
		if (mass > 0) {
			shape.calculateLocalInertia(mass, local_inertia);
		}
		btRigidBody groundObject = new btRigidBody(mass, null, shape, local_inertia);
		groundObject.userData = stairs;
		groundObject.setRestitution(.2f);
		groundObject.setCollisionShape(shape);
		groundObject.setWorldTransform(instance.transform);
		stairs.addComponent(new PhysicsComponent(groundObject));

		return stairs;
	}


	public static AbstractEntity createPillar(BasicECS ecs, String tex_filename, float x, float y, float z, float diam, float length) {
		AbstractEntity pillar = new AbstractEntity(ecs, "Cylinder");

		ModelInstance instance = ShapeHelper.createCylinder(tex_filename, x, y, z, diam, length);

		HasModelComponent model = new HasModelComponent(instance, 1);
		pillar.addComponent(model);

		btCylinderShape boxShape = new btCylinderShape(new Vector3(diam/2, length/2, diam/2));
		Vector3 local_inertia = new Vector3();
		float mass_pre = 1f;
		float mass = (float)(Math.PI * (diam/2) * (diam/2) + length) * mass_pre;
		boxShape.calculateLocalInertia(1f, local_inertia);
		btRigidBody groundObject = new btRigidBody(mass, null, boxShape, local_inertia);
		groundObject.userData = pillar;
		groundObject.setRestitution(.5f);
		groundObject.setCollisionShape(boxShape);
		groundObject.setWorldTransform(instance.transform);
		pillar.addComponent(new PhysicsComponent(groundObject));

		return pillar;
	}


	public static AbstractEntity createCannonball(BasicECS ecs, AbstractEntity shooter, Vector3 start, Vector3 dir) {
		AbstractEntity e = new AbstractEntity(ecs, "Cannonball");

		e.addComponent(new PositionComponent());

		PlayerData playerData = (PlayerData)shooter.getComponent(PlayerData.class);
		WeaponSettingsComponent settings = (WeaponSettingsComponent)shooter.getComponent(WeaponSettingsComponent.class);

		HasDecal hasDecal = new HasDecal();
		if (playerData.playerIdx == 0) {
			hasDecal.decal = GraphicsHelper.DecalHelper("laser_bolt_red.png", 0.2f);
		} else if (playerData.playerIdx == 1) {
			hasDecal.decal = GraphicsHelper.DecalHelper("laser_bolt_yellow.png", 0.2f);
		} else if (playerData.playerIdx == 2) {
			hasDecal.decal = GraphicsHelper.DecalHelper("laser_bolt_magenta.png", 0.2f);
		} else if (playerData.playerIdx == 3) {
			hasDecal.decal = GraphicsHelper.DecalHelper("laser_bolt_green.png", 0.2f);
		} else {
			throw new RuntimeException("Invalid side: " + playerData.playerIdx);
		}

		// scs new hasDecal.decal.setPosition(pos.position);
		hasDecal.faceCamera = true;
		hasDecal.dontLockYAxis = true;
		e.addComponent(hasDecal);

		e.addComponent(new IsBulletComponent(shooter, playerData.playerIdx, start, settings, false));

		e.addComponent(new RemoveEntityAfterTimeComponent(4));

		// Add physics
		btSphereShape shape = new btSphereShape(.2f);
		btRigidBody body = new btRigidBody(.2f, null, shape);
		body.userData = e;
		body.setFriction(0.6f);
		body.setRestitution(.8f);
		body.setCollisionShape(shape);
		Matrix4 mat = new Matrix4();
		mat.setTranslation(start);
		body.setWorldTransform(mat);
		PhysicsComponent pc = new PhysicsComponent(body);
		pc.force = dir.scl(4f);
		e.addComponent(pc);

		BillBoardFPS_Main.audio.play("sfx/Futuristic Shotgun Single Shot.wav");

		return e;
	}


/*
	public static AbstractEntity playersWeapon(BasicECS ecs, AbstractEntity player) {
		AbstractEntity weapon = new AbstractEntity(ecs, "PlayersWeapon");

		PositionComponent pos = new PositionComponent();
		pos.angle_x_degrees = 90;
		weapon.addComponent(pos);

		ModelInstance instance = ModelFunctions.loadModel("models/kenney/machinegun.g3db", false);
		//ModelInstance instance = ShapeHelper.createCylinder("textures/set3_example_1.png", 0, 0, 0, .2f, 1f);
		//instance.transform.rotate(Vector3.Z, 90);
		//instance.transform.rotate(Vector3.X, 90);

		HasModelComponent model = new HasModelComponent(instance);
		model.always_draw = true;
		//float scale = ModelFunctions.getScaleForHeight(instance, .8f);
		//model.scale = 40;//scale;

		PlayerData playerData = (PlayerData)player.getComponent(PlayerData.class);
		model.onlyDrawInViewId = playerData.playerIdx;
		weapon.addComponent(model);

		PlayersWeaponComponent wep = new PlayersWeaponComponent(player);
		weapon.addComponent(wep);


		return weapon;
	}

	 */
	
	
	// Note that the mass gets multiplied by the size
	public static AbstractEntity createModelAndPhysicsBox(BasicECS ecs, String name, String filename, float posX, float posY, float posZ, int rotYDegrees, float mass_pre) {
		AbstractEntity entity = new AbstractEntity(ecs, name);

		ModelInstance instance = ModelFunctions.loadModel(filename, true);

		float scale = ModelFunctions.getScaleForWidth(instance, 1f);
		instance.transform.scale(scale, scale, scale);
		
		/* todo
		if (axis != null) {
			instance.transform.rotate(axis, degrees);
		}*/

		HasModelComponent hasModel = new HasModelComponent(instance, scale);
		hasModel.positionOffsetToOrigin = ModelFunctions.getOrigin(instance).scl(-1);
		entity.addComponent(hasModel);
		
		instance.transform.setTranslation(posX, posY, posZ); // Must be AFTER we've got the origin!

		entity.addComponent(new PositionComponent());

		// Calc BB for physics box
		BoundingBox bb = new BoundingBox();
		instance.calculateBoundingBox(bb);
		bb.mul(instance.transform);

		btBoxShape boxShape = new btBoxShape(new Vector3(bb.getWidth()/2, bb.getHeight()/2, bb.getDepth()/2));
		Vector3 local_inertia = new Vector3();
		float mass = mass_pre * bb.getWidth() * bb.getHeight() * bb.getDepth();
		if (mass > 0) {
			boxShape.calculateLocalInertia(mass, local_inertia);
		}
		btRigidBody groundObject = new btRigidBody(mass, null, boxShape, local_inertia);
		groundObject.userData = entity;
		groundObject.setRestitution(.2f);
		groundObject.setCollisionShape(boxShape);
		groundObject.setWorldTransform(instance.transform);
		entity.addComponent(new PhysicsComponent(groundObject));
		return entity;
	}



}
