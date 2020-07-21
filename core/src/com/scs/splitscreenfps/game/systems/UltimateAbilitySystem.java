package com.scs.splitscreenfps.game.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.BillBoardFPS_Main;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.PhysicsComponent;
import com.scs.splitscreenfps.game.components.PlayerData;
import com.scs.splitscreenfps.game.components.PositionComponent;
import com.scs.splitscreenfps.game.components.UltimateAbilityComponent;
import com.scs.splitscreenfps.game.entities.AbstractPlayersAvatar;
import com.scs.splitscreenfps.game.entities.BulletEntityFactory;

import ssmith.lang.NumberFunctions;

public class UltimateAbilitySystem extends AbstractSystem {

	private Game game;

	public UltimateAbilitySystem(BasicECS ecs, Game _game) {
		super(ecs, UltimateAbilityComponent.class);

		game = _game;
	}


	@Override
	public void processEntity(AbstractEntity entity) {
		UltimateAbilityComponent ability = (UltimateAbilityComponent)entity.getComponent(UltimateAbilityComponent.class);
		AbstractPlayersAvatar player = (AbstractPlayersAvatar)entity;

		if (ability.in_progress) {
			if (ability.end_time < System.currentTimeMillis()) {
				abilityEnded(player, ability);
			} else {
				continueAbility(player, ability);
			}
			return;
		} 

		if (ability.power < ability.charge_duration) {
			ability.power += Gdx.graphics.getDeltaTime();
			if (ability.power > ability.charge_duration) {
				BillBoardFPS_Main.audio.play("sfx/Replenish.wav");
			}
		}

		PlayerData playerData = (PlayerData)entity.getComponent(PlayerData.class);

		if (Settings.DEBUG_ULTIMATES) {
			ability.power = ability.charge_duration;
		}

		if (ability.power < ability.charge_duration) {
			int level = (int)((ability.power / ability.charge_duration) * 100);
			playerData.ultimateText = "Ultimate: " + level + "%";
			if (player.inputMethod.isUltimatePressed()) {
				BillBoardFPS_Main.audio.play("sfx/type2.mp3");
			} else {
				playerData.ultimateReady = false;
			}
		} else {
			playerData.ultimateText = "Ultimate Ready!";
			playerData.ultimateReady = true;
			if (player.inputMethod.isUltimatePressed()) {
				switch (ability.type) {
				case RocketBarrage:
					startRocketBarrage(player, ability);
					break;
				case CraterStrike:
					startCraterStrike(player);
					break;
				case Minefield:
					startMinefield(player);
					break;
				case TraceyBomb:
					throwTraceyBomb(player);
					break;
				default:
					throw new RuntimeException("Unknown ability: " + ability.type);
				}
				ability.power = 0;
			}
		}


	}


	private void startRocketBarrage(AbstractPlayersAvatar player, UltimateAbilityComponent ability) {
		ability.in_progress = true;
		ability.end_time = System.currentTimeMillis() + 3000;

		// Turn off gravity
		PhysicsComponent pc = (PhysicsComponent)player.getComponent(PhysicsComponent.class);
		pc.body.activate();
		pc.body.setGravity(Vector3.Zero);

		BillBoardFPS_Main.audio.play("speech/havesomerockets.wav");
	}


	private void startCraterStrike(AbstractPlayersAvatar player) {
		BillBoardFPS_Main.audio.play("speech/craterstrike.wav");
		AbstractEntity crater = BulletEntityFactory.createCraterStrike(game, player);
		game.ecs.addEntity(crater);
	}


	private void throwTraceyBomb(AbstractPlayersAvatar player) {
		//todo BillBoardFPS_Main.audio.play("speech/craterstrike.wav");
		AbstractEntity crater = BulletEntityFactory.createTraceyBomb(game, player);
		game.ecs.addEntity(crater);
	}


	private void startMinefield(AbstractPlayersAvatar player) {
		//todo BillBoardFPS_Main.audio.play("speech/craterstrike.wav");

		for (int i=0 ; i<20 ; i++) {
			AbstractEntity mine = BulletEntityFactory.createMine(game, player);
			game.ecs.addEntity(mine);
		}
	}


	private void continueAbility(AbstractPlayersAvatar player, UltimateAbilityComponent ability) {
		switch (ability.type) {
		case RocketBarrage:
			continueRocketBarrage(player, ability);
			break;
		default:
			throw new RuntimeException("Unknown ability: " + ability.type);
		}
	}


	private void continueRocketBarrage(AbstractPlayersAvatar player, UltimateAbilityComponent ability) {
		if (ability.next_shot < System.currentTimeMillis()) {
			ability.next_shot = System.currentTimeMillis() + 150;

			// Fire rocket!
			PositionComponent posData = (PositionComponent)player.getComponent(PositionComponent.class);
			Vector3 startPos = new Vector3();
			startPos.set(posData.position);
			startPos.mulAdd(player.camera.direction, .2f);

			Vector3 dir = new Vector3(player.camera.direction);
			float DIFF = 0.1f;
			dir.x += NumberFunctions.rndFloat(-DIFF, DIFF);
			dir.y += NumberFunctions.rndFloat(-DIFF, DIFF);
			dir.z += NumberFunctions.rndFloat(-DIFF, DIFF);
			dir.nor();

			AbstractEntity r = BulletEntityFactory.createRocket(game, player, startPos, dir);
			game.ecs.addEntity(r);

			PhysicsComponent pc = (PhysicsComponent)player.getComponent(PhysicsComponent.class);
			pc.body.setLinearFactor(Vector3.Zero);
		}
	}


	private void abilityEnded(AbstractPlayersAvatar player, UltimateAbilityComponent ability) {
		ability.in_progress = false;

		switch (ability.type) {
		case RocketBarrage:
			endRocketBarrage(player);
			break;
		default:
			throw new RuntimeException("Unknown ability: " + ability.type);
		}
	}


	private void endRocketBarrage(AbstractPlayersAvatar player) {
		PhysicsComponent pc = (PhysicsComponent)player.getComponent(PhysicsComponent.class);
		pc.body.activate();
		pc.body.setGravity(Game.GRAVITY);
		pc.body.setLinearFactor(new Vector3(1, 1, 1));
	}

}
