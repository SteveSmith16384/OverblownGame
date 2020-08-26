package com.scs.splitscreenfps.game.systems;

import java.util.Iterator;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.CanBeCarriedComponent;
import com.scs.splitscreenfps.game.components.CanCarryComponent;
import com.scs.splitscreenfps.game.components.HasModelComponent;
import com.scs.splitscreenfps.game.components.PhysicsComponent;
import com.scs.splitscreenfps.game.components.PositionComponent;
import com.scs.splitscreenfps.game.entities.AbstractPlayersAvatar;

public class PickupAndDropSystem extends AbstractSystem {

	private Game game;

	public PickupAndDropSystem(Game _game, BasicECS ecs) {
		super(ecs, CanCarryComponent.class);

		game = _game;
	}


	@Override
	public void processEntity(AbstractEntity entity) {
		AbstractPlayersAvatar player = (AbstractPlayersAvatar)entity;

		if (player.inputMethod.isPickupPressed()) {
			CanCarryComponent canCarry = (CanCarryComponent)entity.getComponent(CanCarryComponent.class);

			if (canCarry.carrying == null) {
				pickup(player, canCarry);
			} else {
				drop(player, canCarry);
			}
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

			//PhysicsComponent pc = (PhysicsComponent)e.getComponent(PhysicsComponent.class);
			PositionComponent posData = (PositionComponent)e.getComponent(PositionComponent.class);
			if (posData != null) {
				float distance = posData.position.dst(ourPosData.position);
				if (distance <= 1) {
					canCarry.carrying = e;
					e.hideComponent(HasModelComponent.class);
					e.hideComponent(CanBeCarriedComponent.class);
					// todo - hide physics?
					Settings.p(e + " picked up");
					return;
				}
			}
		}
		Settings.p("No object found");
	}


	private void drop(AbstractPlayersAvatar player, CanCarryComponent canCarry) {
		AbstractEntity item = canCarry.carrying;
		item.restoreComponent(HasModelComponent.class);
		item.restoreComponent(CanBeCarriedComponent.class);

		PositionComponent ourPosData = (PositionComponent)player.getComponent(PositionComponent.class);
		Vector3 newpos = new Vector3(ourPosData.position);
		newpos.y += 2f;
		
		// Set position
		PhysicsComponent md = (PhysicsComponent)item.getComponent(PhysicsComponent.class);
		Matrix4 mat = new Matrix4();
		mat.setTranslation(newpos);
		md.body.setWorldTransform(mat);
		md.body.activate();
		md.getRigidBody().setAngularVelocity(Vector3.Zero);
		md.getRigidBody().setLinearVelocity(Vector3.Zero);
		
		canCarry.carrying = null;

		Settings.p(item + " dropped");


	}
}
