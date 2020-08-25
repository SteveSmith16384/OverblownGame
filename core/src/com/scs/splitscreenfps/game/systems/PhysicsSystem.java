package com.scs.splitscreenfps.game.systems;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.BillBoardFPS_Main;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.PhysicsComponent;
import com.scs.splitscreenfps.game.components.PlayerData;
import com.scs.splitscreenfps.game.components.PositionComponent;
import com.scs.splitscreenfps.game.events.FallenOffEdgeEvent;

public class PhysicsSystem extends AbstractSystem {

	private Game game;
	private Matrix4 tmpMat = new Matrix4();

	public PhysicsSystem(Game _game, BasicECS ecs) {
		super(ecs, PhysicsComponent.class);

		game = _game;
	}

	
	@Override
	public void processEntity(AbstractEntity e) {
		PhysicsComponent pc = (PhysicsComponent)e.getComponent(PhysicsComponent.class);
		if (Settings.STRICT) {
			if (pc.body.isDisposed()) {
				Settings.pe("Body disposed for " + e);
			}
			if (pc.body.getCollisionShape().isDisposed()) {
				Settings.pe("Shapedisposed for " + e);
			}
		}

		PositionComponent posData = (PositionComponent)e.getComponent(PositionComponent.class);

		pc.body.getWorldTransform(tmpMat);
		// Set model position data based on physics data - regardless of whether we draw them
		tmpMat.getTranslation(posData.position);

		float height = posData.position.y;
		if (height < -4) {
			if (pc.removeIfFallen && pc.body.isKinematicObject()) {
				Settings.p("Removed " + e + " since it has fallen off");
				e.remove();
				game.ecs.events.add(new FallenOffEdgeEvent(e));
			} else {
				// Is it a player?
				PlayerData player = (PlayerData)e.getComponent(PlayerData.class);
				if (player != null) {
					if (player.dead == false) {
						game.playerDied(e, player, null);
						BillBoardFPS_Main.audio.play("sfx/deathscream1.wav");
					}
				}
			}
		}
	}


	@Override
	public void addEntity(AbstractEntity e) {
		super.addEntity(e);

		PhysicsComponent pc = (PhysicsComponent)e.getComponent(PhysicsComponent.class);
		if (pc.body instanceof btRigidBody) {
			game.dynamicsWorld.addRigidBody((btRigidBody)pc.body);
		} else {
			game.dynamicsWorld.addCollisionObject(pc.body);
		}

		if (pc.disable_gravity) {
			pc.getRigidBody().setGravity(new Vector3());
		}
		if (pc.force != null) {
			//pc.body.applyCentralForce(pc.force.scl(1));
			pc.getRigidBody().applyCentralImpulse(pc.force);
		}
	}


	@Override
	public void removeEntity(AbstractEntity e) {
		super.removeEntity(e);

		PhysicsComponent pc = (PhysicsComponent)e.getComponent(PhysicsComponent.class);
		if (pc.body instanceof btRigidBody) {
			game.dynamicsWorld.removeRigidBody((btRigidBody)pc.body);
		} else {
			game.dynamicsWorld.removeCollisionObject(pc.body);
		}
		if (pc.body.getCollisionShape().isDisposed() == false) {
			pc.body.getCollisionShape().dispose();
		}
		if (pc.body.isDisposed() == false) {
			pc.body.dispose();
		}
	}


}
