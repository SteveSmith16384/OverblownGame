package com.scs.splitscreenfps.game.systems;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.AnimatedComponent;
import com.scs.splitscreenfps.game.components.PlayerMovementData;
import com.scs.splitscreenfps.game.components.PositionComponent;

public class PlayerMovementSystem extends AbstractSystem {

	private Game game;

	public PlayerMovementSystem(Game _game, BasicECS ecs) {
		super(ecs, PlayerMovementData.class);

		game = _game;
	}


	@Override
	public void processEntity(AbstractEntity entity) {
		PlayerMovementData movementData = (PlayerMovementData)entity.getComponent(PlayerMovementData.class);

		// Set position based on physics object
		PositionComponent pos = (PositionComponent)entity.getComponent(PositionComponent.class);
		Matrix4 mat = movementData.characterController.getWorldTransform();
		mat.getTranslation(pos.position);
		
		//movementData.offset.scl(Gdx.graphics.getDeltaTime());

		if (movementData.offset.x != 0 || movementData.offset.y != 0 || movementData.offset.z != 0) {
			if (movementData.frozenUntil < System.currentTimeMillis()) {
				movementData.characterController.activate(); // Need this!
				movementData.characterController.applyCentralForce(movementData.offset.scl(20));
				//movementData.characterController.setLinearVelocity(movementData.offset); // Overwrites any current force
			}
		}
		
		if (movementData.jumpPressed) {
			movementData.jumpPressed = false;
			movementData.characterController.applyCentralForce(new Vector3(0, 40, 0));
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
