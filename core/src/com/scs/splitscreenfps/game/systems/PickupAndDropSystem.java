package com.scs.splitscreenfps.game.systems;

import java.util.Iterator;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.BillBoardFPS_Main;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.CanBeCarriedComponent;
import com.scs.splitscreenfps.game.components.CanCarryComponent;
import com.scs.splitscreenfps.game.components.PhysicsComponent;
import com.scs.splitscreenfps.game.components.PositionComponent;
import com.scs.splitscreenfps.game.entities.AbstractPlayersAvatar;

public class PickupAndDropSystem extends AbstractSystem {

	private Game game;
	private Vector3 tmpVec = new Vector3();
	
	public PickupAndDropSystem(Game _game, BasicECS ecs) {
		super(ecs, CanCarryComponent.class);

		game = _game;
	}


	@Override
	public void processEntity(AbstractEntity entity) {
		AbstractPlayersAvatar player = (AbstractPlayersAvatar)entity;

		CanCarryComponent canCarry = (CanCarryComponent)entity.getComponent(CanCarryComponent.class);
		if (canCarry.carrying != null) {
			PositionComponent ourPosData = (PositionComponent)player.getComponent(PositionComponent.class);
			PositionComponent itemPosData = (PositionComponent)canCarry.carrying.getComponent(PositionComponent.class);

			Vector3 newpos = new Vector3(ourPosData.position);
			tmpVec.set(player.camera.direction);
			tmpVec.nor();
			tmpVec.scl(.5f);
			newpos.add(tmpVec);

			newpos.y -= .1f;
			itemPosData.position.set(newpos);
		}

		if (player.inputMethod.isPickupDropPressed()) {
			if (canCarry.carrying == null) {
				pickup(player, canCarry);
			} else {
				drop(player, canCarry, false);
			}
		} else if (player.inputMethod.isThrowPressed() && canCarry.carrying != null) {
			drop(player, canCarry, true);
		}
	}


	private void pickup(AbstractPlayersAvatar player, CanCarryComponent canCarry) {
		PositionComponent ourPosData = (PositionComponent)player.getComponent(PositionComponent.class);

		Iterator<AbstractEntity> it = game.physicsSystem.getEntityIterator();
		while (it.hasNext()) {
			AbstractEntity e = it.next();

			if (e.isMarkedForRemoval()) {
				continue;
			}

			CanBeCarriedComponent c = (CanBeCarriedComponent)e.getComponent(CanBeCarriedComponent.class);
			if (c == null) {
				continue;
			}

			PositionComponent posData = (PositionComponent)e.getComponent(PositionComponent.class);
			if (posData != null) {
				float distance = posData.position.dst(ourPosData.position);
				if (distance <= 1) {
					canCarry.carrying = e;

					PhysicsComponent physics = (PhysicsComponent)e.getComponent(PhysicsComponent.class);
					physics.body.setActivationState(Collision.WANTS_DEACTIVATION);
					physics.enable_physics = false;

					e.hideComponent(CanBeCarriedComponent.class);
					//e.hideComponent(PhysicsComponent.class);

					BillBoardFPS_Main.audio.play("sfx/Craft_00.mp3");

					//Settings.p(e + " picked up");
					return;
				}
			}
		}
		//Settings.p("No object found");
	}


	private void drop(AbstractPlayersAvatar player, CanCarryComponent canCarry, boolean throwIt) {
		AbstractEntity item = canCarry.carrying;
		item.restoreComponent(CanBeCarriedComponent.class);
		//item.restoreComponent(PhysicsComponent.class);

		PositionComponent ourPosData = (PositionComponent)player.getComponent(PositionComponent.class);
		Vector3 newpos = new Vector3(ourPosData.position);

		if (Settings.TEST_3RD_PERSON == false) {
			tmpVec.set(player.camera.direction);
		} else {
			tmpVec.set((float)Math.sin(Math.toRadians(ourPosData.angle_y_degrees+90)), 0, (float)Math.cos(Math.toRadians(ourPosData.angle_y_degrees+90)));
		}
		if (throwIt == false) {
			tmpVec.y = 0;
		} else {
			tmpVec.y = 0.2f; // Throw slightly upwards
		}
		tmpVec.nor();
		newpos.add(tmpVec);

		// Set position
		PhysicsComponent physics = (PhysicsComponent)item.getComponent(PhysicsComponent.class);
		physics.body.setActivationState(Collision.ACTIVE_TAG);
		physics.enable_physics = true;
		Matrix4 mat = new Matrix4();
		mat.setTranslation(newpos);
		physics.body.setWorldTransform(mat);
		if (throwIt) {
			physics.getRigidBody().applyCentralImpulse(tmpVec.scl(4));
			physics.body.activate();
			BillBoardFPS_Main.audio.play("sfx/Hit_00.mp3");
		} else {
			physics.getRigidBody().setAngularVelocity(Vector3.Zero);
			physics.getRigidBody().setLinearVelocity(Vector3.Zero);
			BillBoardFPS_Main.audio.play("sfx/Hit_01.mp3");
		}

		canCarry.carrying = null;

		//Settings.p(item + " dropped");
	}
}
