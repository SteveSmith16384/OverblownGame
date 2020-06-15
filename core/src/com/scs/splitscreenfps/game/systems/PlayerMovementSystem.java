package com.scs.splitscreenfps.game.systems;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.AnimatedComponent;
import com.scs.splitscreenfps.game.components.PlayerMovementData;
import com.scs.splitscreenfps.game.components.PositionComponent;
import com.scs.splitscreenfps.game.entities.PlayersAvatar_Person;

public class PlayerMovementSystem extends AbstractSystem {

	private static final Vector3 V_DOWN = new Vector3(0, -1, 0);
	
	private Game game;

	public PlayerMovementSystem(Game _game, BasicECS ecs) {
		super(ecs, PlayerMovementData.class);

		game = _game;
	}


	@Override
	public void processEntity(AbstractEntity entity) {
		PlayerMovementData movementData = (PlayerMovementData)entity.getComponent(PlayerMovementData.class);

		// Set model position based on physics object
		PositionComponent pos = (PositionComponent)entity.getComponent(PositionComponent.class);
		Matrix4 mat = movementData.characterController.getWorldTransform();
		mat.getTranslation(pos.position);
		
		if (movementData.offset.x != 0 || movementData.offset.y != 0 || movementData.offset.z != 0) {
			if (movementData.frozenUntil < System.currentTimeMillis()) {
				movementData.characterController.activate(); // Need this!
				movementData.characterController.applyCentralForce(movementData.offset.scl(20));
				//movementData.characterController.setLinearVelocity(movementData.offset); // Overwrites any current force
			}
		}
		
		if (movementData.jumpPressed) {
			// Check they are on ground
			btCollisionObject obj = game.rayTestByDir(pos.position, V_DOWN, PlayersAvatar_Person.PLAYER_HEIGHT+ .2f);
			if (obj != null) {
				movementData.characterController.applyCentralForce(new Vector3(0, 180, 0));
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
