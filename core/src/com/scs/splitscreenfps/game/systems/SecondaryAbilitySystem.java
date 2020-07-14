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
import com.scs.splitscreenfps.game.components.SecondaryAbilityComponent;
import com.scs.splitscreenfps.game.entities.AbstractPlayersAvatar;
import com.scs.splitscreenfps.game.entities.GraphicsEntityFactory;

import ssmith.lang.NumberFunctions;

public class SecondaryAbilitySystem extends AbstractSystem {

	private Game game;

	public SecondaryAbilitySystem(BasicECS ecs, Game _game) {
		super(ecs, SecondaryAbilityComponent.class);

		game = _game;
	}


	@Override
	public void processEntity(AbstractEntity entity) {
		SecondaryAbilityComponent ability = (SecondaryAbilityComponent)entity.getComponent(SecondaryAbilityComponent.class);

		AbstractPlayersAvatar player = (AbstractPlayersAvatar)entity;
		PlayerData playerData = (PlayerData)entity.getComponent(PlayerData.class);
		playerData.ability2text = ability.type + " Ready!";
		
		long interval = ability.cooldown;
		if (ability.lastShotTime + interval > System.currentTimeMillis()) {
			long cooldown_secs = ((ability.lastShotTime + interval) - System.currentTimeMillis()) / 1000;
			playerData.ability2text = "Cooldown: " + (cooldown_secs+1);
			
			if (cooldown_secs <= 0) {
				BillBoardFPS_Main.audio.play("sfx/teleport.mp3");
			}
			return;
		}

		if (player.inputMethod.isAbilityPressed()) {
			if (ability.requiresBuildUp) {
				ability.buildUpActivated = true;
				ability.power += Gdx.graphics.getDeltaTime();
				int pcent = (int)(ability.power * 100 / ability.max_power);
				playerData.ability2text = "Power: " + pcent + "%";
				if (ability.power >= ability.max_power) {
					this.performBuildUpAbility(player, ability);
				}
			} else {
				//Settings.p("Shoot at " + System.currentTimeMillis());
				ability.lastShotTime = System.currentTimeMillis();

				switch (ability.type) {
				case Jump:
					performPowerJump(entity, player);
					break;
				case JetPac:
					performJetPac(entity, player);
					break;
				case StickyMine:
					dropStickyMine(entity, player);
					break;
				default:
					//throw new RuntimeException("Unknown ability: " + ability.type);
				}
			}
		} else { // Button released?
			if (ability.buildUpActivated) {
				this.performBuildUpAbility(player, ability);
			}

		}
	}

	
	private void performBuildUpAbility(AbstractPlayersAvatar player, SecondaryAbilityComponent ability) {
		ability.buildUpActivated = false;
		ability.lastShotTime = System.currentTimeMillis();

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


	private void dropStickyMine(AbstractEntity entity, AbstractPlayersAvatar player) {
		// todo
	}
	
}
