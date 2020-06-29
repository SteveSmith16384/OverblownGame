package com.scs.splitscreenfps.game.systems;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.AnimatedComponent;
import com.scs.splitscreenfps.game.components.PhysicsComponent;
import com.scs.splitscreenfps.game.components.PlayerData;
import com.scs.splitscreenfps.game.components.PlayerMovementData;
import com.scs.splitscreenfps.game.components.PositionComponent;
import com.scs.splitscreenfps.game.entities.PlayersAvatar_Person;

public class PlayerMovementSystem extends AbstractSystem {

	private static final Vector3 V_DOWN = new Vector3(0, -1, 0);
	private static final Vector3 JUMP_FORCE = new Vector3(0, 180, 0);
	private Game game;

	public PlayerMovementSystem(Game _game, BasicECS ecs) {
		super(ecs, PlayerMovementData.class);

		game = _game;
	}


	@Override
	public void processEntity(AbstractEntity entity) {
		PlayerMovementData movementData = (PlayerMovementData)entity.getComponent(PlayerMovementData.class);
		PhysicsComponent physics = (PhysicsComponent)entity.getComponent(PhysicsComponent.class);

		// Set model position based on physics object
		//PositionComponent pos = (PositionComponent)entity.getComponent(PositionComponent.class);
		// scs new Matrix4 mat = physics.body.getWorldTransform();
		// scs new mat.getTranslation(pos.position);

		PlayerData playerData = (PlayerData)entity.getComponent(PlayerData.class);
		if (playerData.health <= 0) {
			return;
		}

		if (movementData.offset.x != 0 || movementData.offset.y != 0 || movementData.offset.z != 0) {
			if (movementData.frozenUntil < System.currentTimeMillis()) {
				physics.body.activate(); // Need this!
				physics.body.applyCentralForce(movementData.offset);
				//movementData.characterController.setLinearVelocity(movementData.offset); // Overwrites any current force
			}
		}

		if (movementData.jumpPressed) {
			// Check they are on ground
			Matrix4 mat = physics.body.getWorldTransform();
			Vector3 pos = new Vector3(); // todo - cache
			mat.getTranslation(pos);
			btCollisionObject obj = game.rayTestByDir(pos, V_DOWN, PlayersAvatar_Person.PLAYER_HEIGHT+ .2f);
			if (obj != null) {
				physics.body.applyCentralForce(JUMP_FORCE);
				//Settings.p("Jump!");
			} else {
				//Settings.p("Not on floor!");
			}
			movementData.jumpPressed = false;
		}

		// Animate
		AnimatedComponent anim = (AnimatedComponent)entity.getComponent(AnimatedComponent.class);
		if (anim != null) {
			if (movementData.offset.len2() > 0) {
				anim.next_animation = anim.walk_anim_name;
			} else {
				anim.next_animation = anim.idle_anim_name;
			}
		}

		movementData.offset.setZero(); // Ready for next loop
	}


}
