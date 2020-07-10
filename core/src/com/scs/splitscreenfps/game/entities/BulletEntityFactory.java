package com.scs.splitscreenfps.game.entities;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.scs.basicecs.AbstractEntity;
import com.scs.splitscreenfps.BillBoardFPS_Main;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.ExplodeAfterTimeComponent;
import com.scs.splitscreenfps.game.components.ExplodeOnContactComponent;
import com.scs.splitscreenfps.game.components.HasDecal;
import com.scs.splitscreenfps.game.components.IsBulletComponent;
import com.scs.splitscreenfps.game.components.PhysicsComponent;
import com.scs.splitscreenfps.game.components.PlayerData;
import com.scs.splitscreenfps.game.components.PositionComponent;
import com.scs.splitscreenfps.game.components.RemoveEntityAfterTimeComponent;
import com.scs.splitscreenfps.game.components.WeaponSettingsComponent;

import ssmith.libgdx.GraphicsHelper;

public class BulletEntityFactory {

	public static AbstractEntity createBullet(Game game, AbstractEntity shooter, Vector3 start, Vector3 dir) {
		AbstractEntity e = new AbstractEntity(game.ecs, "Bullet");

		e.addComponent(new PositionComponent());

		PlayerData playerData = (PlayerData)shooter.getComponent(PlayerData.class);
		WeaponSettingsComponent settings = (WeaponSettingsComponent)shooter.getComponent(WeaponSettingsComponent.class);

		HasDecal hasDecal = new HasDecal();
		if (playerData.playerIdx == 0) {
			hasDecal.decal = GraphicsHelper.DecalHelper(game.getTexture("laser_bolt_red.png"), 0.2f);
		} else if (playerData.playerIdx == 1) {
			hasDecal.decal = GraphicsHelper.DecalHelper(game.getTexture("laser_bolt_yellow.png"), 0.2f);
		} else if (playerData.playerIdx == 2) {
			hasDecal.decal = GraphicsHelper.DecalHelper(game.getTexture("laser_bolt_magenta.png"), 0.2f);
		} else if (playerData.playerIdx == 3) {
			hasDecal.decal = GraphicsHelper.DecalHelper(game.getTexture("laser_bolt_green.png"), 0.2f);
		} else {
			throw new RuntimeException("Invalid side: " + playerData.playerIdx);
		}
		hasDecal.decal.setPosition(start);
		hasDecal.faceCamera = true;
		hasDecal.dontLockYAxis = true;
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


	public static AbstractEntity createRocket(Game game, AbstractEntity shooter, Vector3 start, Vector3 dir) {
		AbstractEntity e = new AbstractEntity(game.ecs, "Rocket");

		e.addComponent(new PositionComponent());

		PlayerData playerData = (PlayerData)shooter.getComponent(PlayerData.class);
		WeaponSettingsComponent settings = (WeaponSettingsComponent)shooter.getComponent(WeaponSettingsComponent.class);

		HasDecal hasDecal = new HasDecal();
		if (playerData.playerIdx == 0) {
			hasDecal.decal = GraphicsHelper.DecalHelper(game.getTexture("laser_bolt_red.png"), 0.2f);
		} else if (playerData.playerIdx == 1) {
			hasDecal.decal = GraphicsHelper.DecalHelper(game.getTexture("laser_bolt_yellow.png"), 0.2f);
		} else if (playerData.playerIdx == 2) {
			hasDecal.decal = GraphicsHelper.DecalHelper(game.getTexture("laser_bolt_magenta.png"), 0.2f);
		} else if (playerData.playerIdx == 3) {
			hasDecal.decal = GraphicsHelper.DecalHelper(game.getTexture("laser_bolt_green.png"), 0.2f);
		} else {
			throw new RuntimeException("Invalid side: " + playerData.playerIdx);
		}
		hasDecal.decal.setPosition(start);
		hasDecal.faceCamera = true;
		hasDecal.dontLockYAxis = true;
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

		BillBoardFPS_Main.audio.play("sfx/launches/iceball.wav");

		return e;
	}


	public static AbstractEntity createFist(Game game, AbstractEntity shooter, Vector3 start, Vector3 dir) {
		AbstractEntity e = new AbstractEntity(game.ecs, "Punch");

		e.addComponent(new PositionComponent());

		PlayerData playerData = (PlayerData)shooter.getComponent(PlayerData.class);
		WeaponSettingsComponent settings = (WeaponSettingsComponent)shooter.getComponent(WeaponSettingsComponent.class);

		if (Settings.DEBUG_PUNCH) {
			HasDecal hasDecal = new HasDecal();
			if (playerData.playerIdx == 0) {
				hasDecal.decal = GraphicsHelper.DecalHelper(game.getTexture("laser_bolt_red.png"), 0.2f);
			} else if (playerData.playerIdx == 1) {
				hasDecal.decal = GraphicsHelper.DecalHelper(game.getTexture("laser_bolt_yellow.png"), 0.2f);
			} else if (playerData.playerIdx == 2) {
				hasDecal.decal = GraphicsHelper.DecalHelper(game.getTexture("laser_bolt_magenta.png"), 0.2f);
			} else if (playerData.playerIdx == 3) {
				hasDecal.decal = GraphicsHelper.DecalHelper(game.getTexture("laser_bolt_green.png"), 0.2f);
			} else {
				throw new RuntimeException("Invalid side: " + playerData.playerIdx);
			}
			hasDecal.decal.setPosition(start);
			hasDecal.faceCamera = true;
			hasDecal.dontLockYAxis = true;
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

		BillBoardFPS_Main.audio.play("sfx/punch.mp3");

		return e;
	}


	public static AbstractEntity createGrenade(Game game, AbstractEntity shooter, Vector3 start, Vector3 dir) {
		AbstractEntity e = new AbstractEntity(game.ecs, "Grenade");

		e.addComponent(new PositionComponent());

		PlayerData playerData = (PlayerData)shooter.getComponent(PlayerData.class);
		WeaponSettingsComponent settings = (WeaponSettingsComponent)shooter.getComponent(WeaponSettingsComponent.class);

		HasDecal hasDecal = new HasDecal();
		if (playerData.playerIdx == 0) {
			hasDecal.decal = GraphicsHelper.DecalHelper(game.getTexture("laser_bolt_red.png"), 0.2f);
		} else if (playerData.playerIdx == 1) {
			hasDecal.decal = GraphicsHelper.DecalHelper(game.getTexture("laser_bolt_yellow.png"), 0.2f);
		} else if (playerData.playerIdx == 2) {
			hasDecal.decal = GraphicsHelper.DecalHelper(game.getTexture("laser_bolt_magenta.png"), 0.2f);
		} else if (playerData.playerIdx == 3) {
			hasDecal.decal = GraphicsHelper.DecalHelper(game.getTexture("laser_bolt_green.png"), 0.2f);
		} else {
			throw new RuntimeException("Invalid side: " + playerData.playerIdx);
		}

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


	public static AbstractEntity createCannonball(Game game, AbstractEntity shooter, Vector3 start, Vector3 dir) {
		AbstractEntity e = new AbstractEntity(game.ecs, "Cannonball");

		e.addComponent(new PositionComponent());

		PlayerData playerData = (PlayerData)shooter.getComponent(PlayerData.class);
		WeaponSettingsComponent settings = (WeaponSettingsComponent)shooter.getComponent(WeaponSettingsComponent.class);

		HasDecal hasDecal = new HasDecal();
		if (playerData.playerIdx == 0) {
			hasDecal.decal = GraphicsHelper.DecalHelper(game.getTexture("laser_bolt_red.png"), 0.2f);
		} else if (playerData.playerIdx == 1) {
			hasDecal.decal = GraphicsHelper.DecalHelper(game.getTexture("laser_bolt_yellow.png"), 0.2f);
		} else if (playerData.playerIdx == 2) {
			hasDecal.decal = GraphicsHelper.DecalHelper(game.getTexture("laser_bolt_magenta.png"), 0.2f);
		} else if (playerData.playerIdx == 3) {
			hasDecal.decal = GraphicsHelper.DecalHelper(game.getTexture("laser_bolt_green.png"), 0.2f);
		} else {
			throw new RuntimeException("Invalid side: " + playerData.playerIdx);
		}

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


}
