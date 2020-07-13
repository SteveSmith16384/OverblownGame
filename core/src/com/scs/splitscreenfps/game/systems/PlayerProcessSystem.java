package com.scs.splitscreenfps.game.systems;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractEvent;
import com.scs.basicecs.ISystem;
import com.scs.splitscreenfps.BillBoardFPS_Main;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.EventCollision;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.PhysicsComponent;
import com.scs.splitscreenfps.game.components.PlayerData;
import com.scs.splitscreenfps.game.components.PlayerMovementData;
import com.scs.splitscreenfps.game.components.PositionComponent;
import com.scs.splitscreenfps.game.entities.AbstractPlayersAvatar;
import com.scs.splitscreenfps.game.entities.PlayersAvatar_Person;

public class PlayerProcessSystem implements ISystem {

	private static final float MOVE_SPEED = 15;//20;//25;//1.5f;
	private static final float CAM_SPEED = 3f;
	private static final float LINEAR_VELOCITY_CUTOFF = 4f;

	private Game game;

	private Vector3 tmpVector = new Vector3();
	private Vector2 tmpVec2 = new Vector2();

	public PlayerProcessSystem(Game _game) {
		game = _game;


	}


	@Override
	public void process() {
		for (int i=0 ; i<game.players.length ; i++) {
			if (game.players[i] != null) {
				process(game.players[i]);
			}
		}
	}


	private void process(AbstractPlayersAvatar player) {
		PhysicsComponent ourPhysics = (PhysicsComponent)player.getComponent(PhysicsComponent.class);
		PlayerData ourPlayerData = (PlayerData)player.getComponent(PlayerData.class);

		// Check for collision events to play thud
		List<AbstractEvent> events = game.ecs.getEventsForEntity(EventCollision.class, player);
		for (AbstractEvent evt : events) {
			EventCollision coll = (EventCollision)evt;

			if (coll.force >= LINEAR_VELOCITY_CUTOFF) {
				BillBoardFPS_Main.audio.play("sfx/bump1.wav");

				// Check if we're boomfist or wrecking ball
				AbstractEntity entityHit = (AbstractEntity)coll.entity2;
				if (entityHit instanceof AbstractPlayersAvatar) { // Have we hit another player?
					if (ourPlayerData.performing_power_punch) {
						float force = ourPhysics.body.getLinearVelocity().len();
						if (force > LINEAR_VELOCITY_CUTOFF) { // Did we hit them really hard, i.e. are we Boomfist?
							//Settings.p("Punched!");

							Vector3 dir = ourPhysics.body.getLinearVelocity();
							dir.scl(1);
							PhysicsComponent theirPhysics = (PhysicsComponent)entityHit.getComponent(PhysicsComponent.class);
							theirPhysics.body.activate();
							theirPhysics.body.applyCentralImpulse(dir);
							theirPhysics.body.setDamping(0.2f, 0.2f);
							
							PlayerData theirPlayerData = (PlayerData)entityHit.getComponent(PlayerData.class);
							game.playerDamaged(entityHit, theirPlayerData, 20, player);
							break;
						}
					}
				}
			}
		}

		// Check still powerpunch/damped
		float force = ourPhysics.body.getLinearVelocity().len();
		if (force < LINEAR_VELOCITY_CUTOFF) {
			ourPlayerData.performing_power_punch = false;
			if (ourPlayerData.has_been_punched) {
				//Settings.p("Re-adding damping");
				ourPlayerData.has_been_punched = false;
				ourPhysics.body.setDamping(PlayersAvatar_Person.DAMPING, PlayersAvatar_Person.DAMPING); // todo - not right for Bowling Ball
			}

		}



		checkMovementInput(player);
		player.cameraController.update();

		// Position camera
		if (Game.physics_enabled) {
			PositionComponent posData = (PositionComponent)player.getComponent(PositionComponent.class);
			player.camera.position.set(posData.position.x, posData.position.y + (PlayersAvatar_Person.PLAYER_HEIGHT/2)+Settings.CAM_OFFSET, posData.position.z);

			// Set rotation based on camera
			tmpVec2.set(player.camera.direction.x, player.camera.direction.z);
			posData.angle_y_degrees = -tmpVec2.angle();
		}
	}


	private void checkMovementInput(AbstractPlayersAvatar player) {
		Camera camera = player.camera;

		if (Game.physics_enabled) {
			PlayerMovementData movementData = (PlayerMovementData)player.getComponent(PlayerMovementData.class);

			if (player.inputMethod.getForwards() > Settings.MIN_AXIS) {
				//Settings.p("Fwd:" + this.inputMethod.isForwardsPressed());
				tmpVector.set(camera.direction);
				tmpVector.y = 0;
				movementData.offset.add(tmpVector.nor().scl(player.inputMethod.getForwards() * MOVE_SPEED));
			} else if (player.inputMethod.getBackwards() > Settings.MIN_AXIS) {
				//Settings.p("Back:" + this.inputMethod.isBackwardsPressed());
				tmpVector.set(camera.direction);
				tmpVector.y = 0;
				movementData.offset.add(tmpVector.nor().scl(-MOVE_SPEED * player.inputMethod.getBackwards()));
			}
			if (player.inputMethod.getStrafeLeft() > Settings.MIN_AXIS) {
				tmpVector.set(camera.direction).crs(camera.up);
				tmpVector.y = 0;
				movementData.offset.add(tmpVector.nor().scl(-MOVE_SPEED * player.inputMethod.getStrafeLeft()));
			} else if (player.inputMethod.getStrafeRight() > Settings.MIN_AXIS) {
				tmpVector.set(camera.direction).crs(camera.up);
				tmpVector.y = 0;
				movementData.offset.add(tmpVector.nor().scl(MOVE_SPEED * player.inputMethod.getStrafeRight()));
			}

			if (player.inputMethod.isJumpPressed()) {
				movementData.jumpPressed = true;
			}
		} else {
			if (player.inputMethod.getForwards() > Settings.MIN_AXIS) {
				tmpVector.set(camera.direction);
				tmpVector.scl(Gdx.graphics.getDeltaTime()*CAM_SPEED);
				camera.position.add(tmpVector);
				//camera.update();
			} else if (player.inputMethod.getBackwards() > Settings.MIN_AXIS) {
				tmpVector.set(camera.direction);
				tmpVector.scl(-Gdx.graphics.getDeltaTime()*CAM_SPEED);
				camera.position.add(tmpVector);
				//camera.update();
			}
			if (player.inputMethod.getStrafeLeft() > Settings.MIN_AXIS) {
				tmpVector.set(camera.direction).crs(camera.up);
				tmpVector.y = 0;
				tmpVector.scl(-Gdx.graphics.getDeltaTime()*CAM_SPEED);
				camera.position.add(tmpVector);
			} else if (player.inputMethod.getStrafeRight() > Settings.MIN_AXIS) {
				tmpVector.set(camera.direction).crs(camera.up);
				tmpVector.y = 0;
				tmpVector.scl(Gdx.graphics.getDeltaTime()*CAM_SPEED);
				camera.position.add(tmpVector);
			}
		}
	}

}
