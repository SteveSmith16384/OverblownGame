package com.scs.splitscreenfps.game.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.PhysicsComponent;
import com.scs.splitscreenfps.game.components.SecondaryAbilityComponent;
import com.scs.splitscreenfps.game.entities.AbstractPlayersAvatar;

public class SecondaryAbilitySystem extends AbstractSystem {

	private Game game;

	public SecondaryAbilitySystem(BasicECS ecs, Game _game) {
		super(ecs, SecondaryAbilityComponent.class);

		game = _game;
	}


	@Override
	public void processEntity(AbstractEntity entity) {
		SecondaryAbilityComponent ability = (SecondaryAbilityComponent)entity.getComponent(SecondaryAbilityComponent.class);

		long interval = ability.cooldown;
		if (ability.lastShotTime + interval > System.currentTimeMillis()) {
			return;
		}

		AbstractPlayersAvatar player = (AbstractPlayersAvatar)entity;

		if (player.inputMethod.isAbilityPressed()) {
			if (ability.requiresBuildUp) {
				ability.buildUpActivated = true;
				ability.power += Gdx.graphics.getDeltaTime();
				if (ability.power >= ability.max_power) {
					this.performBuildUpAbility(entity, player, ability);
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
				default:
					//throw new RuntimeException("Unknown ability: " + ability.type);
				}
			}
		} else { // Button released?
			if (ability.buildUpActivated) {
				this.performBuildUpAbility(entity, player, ability);
			}

		}
	}

	
	private void performBuildUpAbility(AbstractEntity entity, AbstractPlayersAvatar player, SecondaryAbilityComponent ability) {
		ability.buildUpActivated = false;
		ability.lastShotTime = System.currentTimeMillis();

		switch (ability.type) {
		case PowerPunch:
			performBoost(entity, player, ability.power);
			break;
		default:
			//throw new RuntimeException("Unknown ability: " + ability.type);
		}

		ability.power = 0;
	}

	private void performBoost(AbstractEntity entity, AbstractPlayersAvatar player, float power) {
		PhysicsComponent pc = (PhysicsComponent)entity.getComponent(PhysicsComponent.class);
		pc.body.activate();				
		//pc.body.applyCentralImpulse(player.camera.direction.cpy().scl(30));
		float pow = power*30;
		Settings.p("Performing boost with pow=" + pow);
		pc.body.applyCentralImpulse(player.camera.direction.cpy().scl(pow));

	}


	private void performPowerJump(AbstractEntity entity, AbstractPlayersAvatar player) {
		PhysicsComponent pc = (PhysicsComponent)entity.getComponent(PhysicsComponent.class);
		//pc.body.applyCentralImpulse(new Vector3(0, 30, 0));
		Vector3 dir = new Vector3(player.camera.direction);
		dir.y += .2f;
		dir.nor().scl(30);
		pc.body.applyCentralImpulse(dir);
		Settings.p("Power jump!");

	}


	private void performJetPac(AbstractEntity entity, AbstractPlayersAvatar player) {
		PhysicsComponent pc = (PhysicsComponent)entity.getComponent(PhysicsComponent.class);
		pc.body.applyCentralImpulse(new Vector3(0, 30, 0));
		Settings.p("Power jump!");

	}
}
