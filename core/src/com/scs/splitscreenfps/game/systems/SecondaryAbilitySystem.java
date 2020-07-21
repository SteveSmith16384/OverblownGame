package com.scs.splitscreenfps.game.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.BillBoardFPS_Main;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.PhysicsComponent;
import com.scs.splitscreenfps.game.components.PlayerData;
import com.scs.splitscreenfps.game.components.PositionComponent;
import com.scs.splitscreenfps.game.components.SecondaryAbilityComponent;
import com.scs.splitscreenfps.game.entities.AbstractPlayersAvatar;
import com.scs.splitscreenfps.game.entities.GraphicsEntityFactory;

import ssmith.lang.NumberFunctions;

public class SecondaryAbilitySystem extends AbstractSystem {

	private Game game;
	private Vector3 tmpVec = new Vector3();
	private Matrix4 tmpMat = new Matrix4();

	public SecondaryAbilitySystem(BasicECS ecs, Game _game) {
		super(ecs, SecondaryAbilityComponent.class);

		game = _game;
	}


	@Override
	public void processEntity(AbstractEntity entity) {
		SecondaryAbilityComponent ability = (SecondaryAbilityComponent)entity.getComponent(SecondaryAbilityComponent.class);

		ability.current_cooldown += Gdx.graphics.getDeltaTime();
		if (ability.current_cooldown >= ability.cooldown_duration) {
			BillBoardFPS_Main.audio.play("sfx/teleport.mp3");
			ability.current_cooldown = 0;
			ability.count_available++;
			if (ability.count_available > ability.max_count) {
				ability.count_available = ability.max_count;
			}
		}

		AbstractPlayersAvatar player = (AbstractPlayersAvatar)entity;
		PlayerData playerData = (PlayerData)entity.getComponent(PlayerData.class);
		playerData.ability1text = ability.type + " (" + ability.count_available + ") Ready!";
		if (ability.count_available > 0) {
			if (player.inputMethod.isAbility1Pressed()) {
				if (ability.button_released) {
					ability.button_released = false;
				if (ability.requiresBuildUp) {
					ability.buildUpActivated = true;
					ability.power += Gdx.graphics.getDeltaTime();
					int pcent = (int)(ability.power * 100 / ability.max_power_duration);
					playerData.ability1text = "Power: " + pcent + "%";
					if (ability.power >= ability.max_power_duration) {
						this.performBuildUpAbility(player, ability);
					}
				} else {
					//Settings.p("Shoot at " + System.currentTimeMillis());

					boolean success = true;

					switch (ability.type) {
					case JumpForwards:
						performPowerJump(entity, player);
						break;
					case JumpUp:
						performJumpUp(entity, player);
						break;
					case JetPac:
						performJetPac(entity, player);
						break;
					case TracerJump:
						success = performTracerJump(player);
						break;
					case StickyMine:
						dropStickyMine(entity, player);
						break;
					default:
						if (Settings.STRICT) {
							throw new RuntimeException("Unknown ability: " + ability.type);
						}
					}

					if (success) {
						ability.count_available--;
					}
				}
				}
			} else { // Button released?
				ability.button_released = true;
				if (ability.buildUpActivated) {
					this.performBuildUpAbility(player, ability);
				}

			}
		}
	}


	private void performBuildUpAbility(AbstractPlayersAvatar player, SecondaryAbilityComponent ability) {
		ability.buildUpActivated = false;
		//ability.lastShotTime = System.currentTimeMillis();

		switch (ability.type) {
		case PowerPunch:
			performPowerPunch(player, ability.power);
			break;
		default:
			//throw new RuntimeException("Unknown ability: " + ability.type);
		}

		ability.power = 0;
	}


	private void performPowerPunch(AbstractPlayersAvatar player, float power) {
		PhysicsComponent pc = (PhysicsComponent)player.getComponent(PhysicsComponent.class);
		pc.body.activate();
		float pow = 15+(power*30);
		//Settings.p("Performing boost with pow=" + pow);
		pc.body.applyCentralImpulse(player.camera.direction.cpy().scl(pow));
		//pc.body.appl.applyCentralForce(player.camera.direction.cpy().scl(power*3000)); Doesn't do anything?

		PositionComponent posData = (PositionComponent)player.getComponent(PositionComponent.class);
		AbstractEntity e = GraphicsEntityFactory.createBlueExplosion(game, posData.position);
		game.ecs.addEntity(e);

		PlayerData playerData = (PlayerData)player.getComponent(PlayerData.class);
		playerData.performing_power_punch = true;

		BillBoardFPS_Main.audio.play("speech/boom.wav");
	}


	private boolean performTracerJump(AbstractPlayersAvatar player) {
		float dist = 5f;
		PositionComponent posData = (PositionComponent)player.getComponent(PositionComponent.class);
		Vector3 dir = new Vector3(player.camera.direction);
		// set y-dir to be 0
		dir.y = 0;
		dir.nor();
		btCollisionObject obj = game.rayTestByDir(posData.position, dir, dist);
		boolean clear = (obj == null);
		if (clear) {
			// Teleport
			PhysicsComponent pc = (PhysicsComponent)player.getComponent(PhysicsComponent.class);
			pc.body.getWorldTransform(tmpMat);
			tmpMat.getTranslation(tmpVec);
			tmpVec.mulAdd(dir, dist);
			tmpMat.setTranslation(tmpVec);
			pc.body.setWorldTransform(tmpMat);
			pc.body.activate();

			// f/x
			AbstractEntity e = GraphicsEntityFactory.createBlueExplosion(game, posData.position);
			game.ecs.addEntity(e);

			BillBoardFPS_Main.audio.play("sfx/boost-start.ogg");
			Settings.p("Blink!");
			return true;
		} else {
			BillBoardFPS_Main.audio.play("sfx/enemyalert.mp3");
			Settings.p(obj + " is in the way");
			return false;
		}
	}


	private void performPowerJump(AbstractEntity entity, AbstractPlayersAvatar player) {
		PhysicsComponent pc = (PhysicsComponent)entity.getComponent(PhysicsComponent.class);
		pc.body.activate();
		//pc.body.applyCentralImpulse(new Vector3(0, 30, 0));
		Vector3 dir = new Vector3(player.camera.direction);
		dir.y += .2f;
		dir.nor().scl(30);
		pc.body.applyCentralImpulse(dir);
	}


	private void performJetPac(AbstractEntity entity, AbstractPlayersAvatar player) {
		BillBoardFPS_Main.audio.play("sfx/fart" + NumberFunctions.rnd(1, 5) + ".wav");

		PhysicsComponent pc = (PhysicsComponent)entity.getComponent(PhysicsComponent.class);
		pc.body.activate();
		pc.body.applyCentralImpulse(new Vector3(0, 40, 0));

		PositionComponent posData = (PositionComponent)player.getComponent(PositionComponent.class);
		AbstractEntity e = GraphicsEntityFactory.createBlueExplosion(game, posData.position);
		game.ecs.addEntity(e);
	}


	private void performJumpUp(AbstractEntity entity, AbstractPlayersAvatar player) {
		//todo - sfxBillBoardFPS_Main.audio.play("sfx/fart" + NumberFunctions.rnd(1, 5) + ".wav");

		PhysicsComponent pc = (PhysicsComponent)entity.getComponent(PhysicsComponent.class);
		pc.body.activate();
		pc.body.applyCentralImpulse(new Vector3(0, 20, 0));

		PositionComponent posData = (PositionComponent)player.getComponent(PositionComponent.class);
		AbstractEntity e = GraphicsEntityFactory.createBlueExplosion(game, posData.position);
		game.ecs.addEntity(e);
	}


	private void dropStickyMine(AbstractEntity entity, AbstractPlayersAvatar player) {
		// todo
	}

}
