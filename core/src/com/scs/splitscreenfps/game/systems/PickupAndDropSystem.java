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
import com.scs.splitscreenfps.game.components.HasModelComponent;
import com.scs.splitscreenfps.game.components.PhysicsComponent;
import com.scs.splitscreenfps.game.components.PositionComponent;
import com.scs.splitscreenfps.game.entities.AbstractPlayersAvatar;
import com.scs.splitscreenfps.game.entities.EquipmentEntityFactory;

public class PickupAndDropSystem extends AbstractSystem {

	private Game game;

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
			Vector3 dir = new Vector3(); // todo - cache
			dir.set(player.camera.direction);
			dir.nor();
			dir.scl(.5f);
			newpos.add(dir);

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
					physics.disable_physics = true;

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

		Vector3 dir = new Vector3(); // todo - cache
		dir.set(player.camera.direction);
		if (throwIt == false) {// || dir.y < 0) {
			dir.y = 0;
		} else {
			dir.y = 0.2f; // Throw upwards
		}
		dir.nor();
		newpos.add(dir);

		// Set position
		PhysicsComponent physics = (PhysicsComponent)item.getComponent(PhysicsComponent.class);
		physics.body.setActivationState(Collision.ACTIVE_TAG);
		physics.disable_physics = false;
		Matrix4 mat = new Matrix4();
		mat.setTranslation(newpos);
		physics.body.setWorldTransform(mat);
		if (throwIt) {
			physics.getRigidBody().applyCentralImpulse(dir.scl(4));
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
