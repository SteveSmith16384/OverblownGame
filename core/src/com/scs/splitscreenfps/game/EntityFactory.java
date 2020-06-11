package com.scs.splitscreenfps.game;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.BillBoardFPS_Main;
import com.scs.splitscreenfps.game.components.AutoMoveComponent;
import com.scs.splitscreenfps.game.components.CollidesComponent;
import com.scs.splitscreenfps.game.components.HasDecal;
import com.scs.splitscreenfps.game.components.IsBulletComponent;
import com.scs.splitscreenfps.game.components.PhysicsComponent;
import com.scs.splitscreenfps.game.components.PlayerData;
import com.scs.splitscreenfps.game.components.PositionComponent;

import ssmith.libgdx.GraphicsHelper;

public class EntityFactory {

	public static AbstractEntity createBullet(BasicECS ecs, AbstractEntity shooter, Vector3 start, Vector3 offset) {
		AbstractEntity e = new AbstractEntity(ecs, "Bullet");

		PositionComponent pos = new PositionComponent(start);
		e.addComponent(pos);

		PlayerData playerData = (PlayerData)shooter.getComponent(PlayerData.class);

		HasDecal hasDecal = new HasDecal();
		if (playerData.side == 0) {
			if (playerData.health > 0) {
				hasDecal.decal = GraphicsHelper.DecalHelper("laser_bolt_red.png", 0.1f);
			} else {
				hasDecal.decal = GraphicsHelper.DecalHelper("laser_bolt_red_desync.png", 0.1f);
			}
		} else if (playerData.side == 1) {
			if (playerData.health > 0) {
			hasDecal.decal = GraphicsHelper.DecalHelper("laser_bolt_blue.png", 0.1f);
			} else {
				hasDecal.decal = GraphicsHelper.DecalHelper("laser_bolt_blue_desync.png", 0.1f);
			}
		} else {
			throw new RuntimeException("Invalid side: " + playerData.side);
		}
		hasDecal.decal.setPosition(pos.position);
		hasDecal.faceCamera = true;
		hasDecal.dontLockYAxis = false;
		e.addComponent(hasDecal);

		//MovementData md = new MovementData();
		//md.must_move_x_and_z = true;
		//e.addComponent(md);

		e.addComponent(new AutoMoveComponent(offset));

		CollidesComponent cc = new CollidesComponent(false, .1f);
		cc.dont_collide_with = shooter;
		e.addComponent(cc);

		e.addComponent(new IsBulletComponent(shooter, playerData.side));
		
		// Add physics
		btBoxShape shape = new btBoxShape(new Vector3(.1f, .1f, .1f));
		btRigidBody body = new btRigidBody(.1f, null, shape);
		body.userData = e;
		body.setFriction(0);
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
		pc.force = offset.scl(1f);
		e.addComponent(pc);
		
		BillBoardFPS_Main.audio.play("sfx/Futuristic Shotgun Single Shot.wav");
		
		return e;
	}

}
