package com.scs.splitscreenfps.game.systems;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.BillBoardFPS_Main;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.AnimatedComponent;
import com.scs.splitscreenfps.game.components.PhysicsComponent;
import com.scs.splitscreenfps.game.components.PlayerData;
import com.scs.splitscreenfps.game.components.PlayerMovementData;
import com.scs.splitscreenfps.game.entities.PlayerAvatar_Person;

public class PlayerMovementSystem extends AbstractSystem {

	private static final Vector3 V_DOWN = new Vector3(0, -1, 0);
	private static final Vector3 JUMP_FORCE = new Vector3(0, 180, 0);

	private Game game;
	private Vector3 tmpVec = new Vector3();

	public PlayerMovementSystem(Game _game, BasicECS ecs) {
		super(ecs, PlayerMovementData.class);

		game = _game;
		
		JUMP_FORCE.y = game.game_config.jump_force;
	}


	@Override
	public void processEntity(AbstractEntity entity) {
		PlayerMovementData movementData = (PlayerMovementData)entity.getComponent(PlayerMovementData.class);
		PhysicsComponent physics = (PhysicsComponent)entity.getComponent(PhysicsComponent.class);

		PlayerData playerData = (PlayerData)entity.getComponent(PlayerData.class);
		if (playerData.health <= 0) {
			return;
		}

		boolean on_floor = false;
		Matrix4 mat = physics.body.getWorldTransform();
		mat.getTranslation(tmpVec);
		ClosestRayResultCallback results = game.rayTestByDir(tmpVec, V_DOWN, PlayerAvatar_Person.PLAYER_HEIGHT + 0.2f); // todo - only check every so often
		if (results != null) {
			btCollisionObject obj = results.getCollisionObject();
			on_floor = (obj != null);
		}
		if (movementData.offset.x != 0 || movementData.offset.y != 0 || movementData.offset.z != 0) {
			if (movementData.frozenUntil < System.currentTimeMillis()) {
				tmpVec.set(movementData.offset);
				float speed = physics.getRigidBody().getLinearVelocity().len();
				//Settings.p("Speed=" + speed);
				if (speed > 0.1f && speed < 2f) {
					float frac = 2 / speed;
					tmpVec.scl(frac);
				}
				physics.body.activate(); // Need this!
				physics.getRigidBody().applyCentralForce(tmpVec);
				if (on_floor) {
					if (movementData.next_footstep_sound < System.currentTimeMillis()) {
						BillBoardFPS_Main.audio.play("sfx/footstep.wav");
						movementData.next_footstep_sound = System.currentTimeMillis() + 350;
					}
				}
			}
		}

		if (movementData.jumpPressed) {
			if (on_floor) {
				physics.getRigidBody().applyCentralForce(JUMP_FORCE);
			}
			movementData.jumpPressed = false;
		}

		// Animate
		AnimatedComponent anim = (AnimatedComponent)entity.getComponent(AnimatedComponent.class);
		if (anim != null) {
			if (movementData.offset.len2() > 0) {
				anim.setNextAnim(anim.walk_anim);//anim.new AnimData(anim.walk_anim_name, true);
			} else {
				anim.setNextAnim(anim.idle_anim);// anim.new AnimData(anim.idle_anim_name, true);
			}
		}

		movementData.offset.setZero(); // Ready for next loop
	}


}
