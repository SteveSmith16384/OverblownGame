package com.scs.splitscreenfps.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.BillBoardFPS_Main;
import com.scs.splitscreenfps.game.components.AffectedByExplosionComponent;
import com.scs.splitscreenfps.game.components.ExplodeAfterTimeComponent;
import com.scs.splitscreenfps.game.components.ExplodeOnContactComponent;
import com.scs.splitscreenfps.game.components.HasDecal;
import com.scs.splitscreenfps.game.components.HasModelComponent;
import com.scs.splitscreenfps.game.components.IsBulletComponent;
import com.scs.splitscreenfps.game.components.PhysicsComponent;
import com.scs.splitscreenfps.game.components.PlayerData;
import com.scs.splitscreenfps.game.components.PositionComponent;
import com.scs.splitscreenfps.game.components.RemoveOnContactComponent;

import ssmith.libgdx.GraphicsHelper;

public class EntityFactory {

	public static AbstractEntity createBullet(BasicECS ecs, AbstractEntity shooter, Vector3 start, Vector3 dir) {
		AbstractEntity e = new AbstractEntity(ecs, "Bullet");

		PositionComponent pos = new PositionComponent(start);
		e.addComponent(pos);

		PlayerData playerData = (PlayerData)shooter.getComponent(PlayerData.class);

		HasDecal hasDecal = new HasDecal();
		if (playerData.side == 0) {
			if (playerData.health > 0) {
				hasDecal.decal = GraphicsHelper.DecalHelper("laser_bolt_red.png", 0.2f);
			} else {
				hasDecal.decal = GraphicsHelper.DecalHelper("laser_bolt_red_desync.png", 0.2f);
			}
		} else if (playerData.side == 1) {
			if (playerData.health > 0) {
				hasDecal.decal = GraphicsHelper.DecalHelper("laser_bolt_blue.png", 0.2f);
			} else {
				hasDecal.decal = GraphicsHelper.DecalHelper("laser_bolt_blue_desync.png", 0.2f);
			}
		} else {
			throw new RuntimeException("Invalid side: " + playerData.side);
		}
		hasDecal.decal.setPosition(pos.position);
		hasDecal.faceCamera = true;
		hasDecal.dontLockYAxis = false;
		e.addComponent(hasDecal);

		e.addComponent(new RemoveOnContactComponent());

		e.addComponent(new IsBulletComponent(shooter, playerData.side));

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

		PositionComponent pos = new PositionComponent(start);
		e.addComponent(pos);

		PlayerData playerData = (PlayerData)shooter.getComponent(PlayerData.class);

		HasDecal hasDecal = new HasDecal();
		if (playerData.side == 0) {
			if (playerData.health > 0) {
				hasDecal.decal = GraphicsHelper.DecalHelper("laser_bolt_red.png", 0.2f);
			} else {
				hasDecal.decal = GraphicsHelper.DecalHelper("laser_bolt_red_desync.png", 0.2f);
			}
		} else if (playerData.side == 1) {
			if (playerData.health > 0) {
				hasDecal.decal = GraphicsHelper.DecalHelper("laser_bolt_blue.png", 0.2f);
			} else {
				hasDecal.decal = GraphicsHelper.DecalHelper("laser_bolt_blue_desync.png", 0.2f);
			}
		} else {
			throw new RuntimeException("Invalid side: " + playerData.side);
		}
		hasDecal.decal.setPosition(pos.position);
		hasDecal.faceCamera = true;
		hasDecal.dontLockYAxis = false;
		e.addComponent(hasDecal);

		e.addComponent(new IsBulletComponent(shooter, playerData.side));
		e.addComponent(new RemoveOnContactComponent());
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


	public static AbstractEntity createGrenade(BasicECS ecs, AbstractEntity shooter, Vector3 start, Vector3 dir) {
		AbstractEntity e = new AbstractEntity(ecs, "Grenade");

		PositionComponent pos = new PositionComponent(start);
		e.addComponent(pos);

		PlayerData playerData = (PlayerData)shooter.getComponent(PlayerData.class);

		HasDecal hasDecal = new HasDecal();
		if (playerData.side == 0) {
			if (playerData.health > 0) {
				hasDecal.decal = GraphicsHelper.DecalHelper("laser_bolt_red.png", 0.2f);
			} else {
				hasDecal.decal = GraphicsHelper.DecalHelper("laser_bolt_red_desync.png", 0.2f);
			}
		} else if (playerData.side == 1) {
			if (playerData.health > 0) {
				hasDecal.decal = GraphicsHelper.DecalHelper("laser_bolt_blue.png", 0.2f);
			} else {
				hasDecal.decal = GraphicsHelper.DecalHelper("laser_bolt_blue_desync.png", 0.2f);
			}
		} else {
			throw new RuntimeException("Invalid side: " + playerData.side);
		}
		
		hasDecal.decal.setPosition(pos.position);
		hasDecal.faceCamera = true;
		hasDecal.dontLockYAxis = true;
		e.addComponent(hasDecal);

		e.addComponent(new IsBulletComponent(shooter, playerData.side));

		e.addComponent(new ExplodeAfterTimeComponent(3000));

		// Add physics
		btSphereShape shape = new btSphereShape(.1f);//new Vector3(.1f, .1f, .1f));
		btRigidBody body = new btRigidBody(.1f, null, shape);
		body.userData = e;
		body.setFriction(0.9f);
		body.setRestitution(.6f);
		body.setCollisionShape(shape);
		Matrix4 mat = new Matrix4();
		mat.setTranslation(start);
		body.setWorldTransform(mat);
		//body.applyCentralForce(offset.scl(100));
		//body.applyCentralImpulse(offset.scl(10));
		//body.setGravity(new Vector3());
		PhysicsComponent pc = new PhysicsComponent(body);
		//pc.disable_gravity = true;
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

		ModelInstance instance = new ModelInstance(box_model, new Vector3(posX+(w/2), posY+(h/2), posZ+(d/2)));
		//ModelInstance instance = new ModelInstance(box_model, new Vector3(posX, posY, posZ));
		//instance.transform.rotate(Vector3.Z, 90); // Position textures upright

		HasModelComponent model = new HasModelComponent("Crate", instance);
		crate.addComponent(model);

		btBoxShape boxShape = new btBoxShape(new Vector3(w/2, h/2, d/2));
		Vector3 local_inertia = new Vector3();
		boxShape.calculateLocalInertia(1f, local_inertia);
		btRigidBody groundObject = new btRigidBody(.7f, null, boxShape, local_inertia);
		groundObject.userData = crate;
		groundObject.setRestitution(.5f);
		groundObject.setCollisionShape(boxShape);
		groundObject.setWorldTransform(instance.transform);
		crate.addComponent(new PhysicsComponent(groundObject));
		
		crate.addComponent(new AffectedByExplosionComponent());
		
		return crate;
	}

}
